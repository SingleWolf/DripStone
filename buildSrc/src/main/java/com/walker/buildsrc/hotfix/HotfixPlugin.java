package com.walker.buildsrc.hotfix;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.AppPlugin;
import com.android.build.gradle.api.ApplicationVariant;
import com.android.utils.FileUtils;

import org.antlr.v4.misc.Utils;
import org.apache.commons.compress.utils.IOUtils;
import org.gradle.api.Action;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskOutputs;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class HotfixPlugin implements Plugin<Project> {

    private String applicationName;
    private ArrayList<String> hookClassPrefix;

    @Override
    public void apply(Project project) {
        System.out.println("\n\n---------- HotfixPlugin Start ----------\n\n");
        if (!project.getPlugins().hasPlugin(AppPlugin.class)) {
            throw new GradleException("无法在非android application插件中使用热修复插件");
        }
        //定义额外属性
        project.getExtensions().create("HotfixExt", HotfixExt.class);

        //解析当前module的gradle文件之后
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                //找到额外属性
                HotfixExt hotfixExt = project.getExtensions().findByType(HotfixExt.class);
                if (!hotfixExt.isOpen()) {
                    return;
                }
                applicationName = hotfixExt.getApplicationName();
                hookClassPrefix = hotfixExt.getFixPrefix();
                // 找到系统属性
                AppExtension appExtension = project.getExtensions().findByType(AppExtension.class);
                DomainObjectSet<ApplicationVariant> applicationVariants = appExtension.getApplicationVariants();
                for (ApplicationVariant var : applicationVariants) {
                    String variantName = var.getName();
                    //当前用户是debug模式，并且没有配置debug运行执行热修复
                    if (variantName.contains("debug") && !hotfixExt.isOnDebug()) {
                        return;
                    }
                    //配置热修复插件生成补丁的一系列任务
                    configTasks(project, var, hotfixExt);
                }
            }
        });
    }

    void configTasks(final Project project, final ApplicationVariant variant,
                     final HotfixExt hotfixExt) {
        //获得: debug/release
        String variantName = variant.getName();
        //首字母大写
        String capitalizeName = Utils.capitalize(variantName);
        System.out.println(String.format("\n\n---------- configTasks[%s] start   ----------\n\n", capitalizeName));

        //热修复的输出目录
        File outputDir;
        //如果没有指名输出目录，默认输出到 build/patch/debug(release) 下
        if (!HotfixUtils.isEmpty(hotfixExt.getOutputPath())) {
            outputDir = new File(hotfixExt.getOutputPath(), variantName);
        } else {
            outputDir = new File(project.getBuildDir(), "patch/" + variantName);
        }
        outputDir.mkdirs();

        //获得android的混淆任务
        final Task proguardTask =
                project.getTasks().findByName("transformClassesAndResourcesWithProguardFor" + capitalizeName);
        /**
         * 备份本次的mapping文件
         */
        final File mappingBak = new File(outputDir, "mapping.txt");
        //如果没开启混淆，则为null，不需要备份mapping
        if (proguardTask != null) {
            // dolast：在这个任务之后再干一些事情
            // 在混淆后备份mapping
            proguardTask.doLast(new Action<Task>() {
                @Override
                public void execute(Task task) {
                    //混淆任务输出的所有文件
                    TaskOutputs outputs = proguardTask.getOutputs();
                    Set<File> files = outputs.getFiles().getFiles();
                    for (File file : files) {
                        //把mapping文件备份
                        if (file.getName().endsWith("mapping.txt")) {
                            try {
                                FileUtils.copyFile(file, mappingBak);
                                project.getLogger().error("mapping: " + mappingBak.getCanonicalPath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            });
        }
        //将上次混淆的mapping应用到本次,如果没有上次的混淆文件就没操作
        HotfixUtils.applyMapping(proguardTask, mappingBak);
        /**
         * 在混淆后 记录类的hash值，并生成补丁包
         */
        final File hexFile = new File(outputDir, "hex.txt");

        // 需要打包补丁的类的jar包
        final File patchClassFile = new File(outputDir, "patchClass.jar");
        // 用dx打包后的jar包:补丁包
        final File patchFile = new File(outputDir, "patch.jar");

        //打包dex任务
        final Task dexTask = project.getTasks().findByName("transformClassesWithDexBuilderFor" + capitalizeName);
        if (dexTask == null) {
            System.out.println("\n\n---------- dexTask[transformClassesWithDexBuilderFor] is null   ----------\n\n");
            return;
        }
        //dofirst：在任务之前干一些事情
        // 在把class打包dex之前，插桩并记录每个class的md5 hash值
        dexTask.doFirst(new Action<Task>() {
            @Override
            public void execute(Task task) {
                /**
                 *  插桩 记录md5并对比
                 */
                PatchGenerator patchGenerator = new PatchGenerator(project, patchFile,
                        patchClassFile, hexFile);
                //记录类的md5
                Map<String, String> newHexs = new HashMap<>();
                //任务的输入，dex打包任务要输入什么？ 自然是所有的class与jar包了！
                Set<File> files = dexTask.getInputs().getFiles().getFiles();
                for (File file : files) {
                    String filePath = file.getAbsolutePath();
                    if (filePath.endsWith(".jar")) {
                        processJar(file, newHexs, patchGenerator);
                    } else if (filePath.endsWith(".class")) {
                        processClass(variant.getDirName(), file, newHexs,
                                patchGenerator);
                    }

                }
                //类的md5集合 写入到文件
                HotfixUtils.writeHex(newHexs, hexFile);
                try {
                    //生成补丁
                    patchGenerator.generate();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(String.format("\n\n----------  patchGenerator error = %s  ----------\n\n", e.toString()));
                }
            }
        });

        Task task = project.getTasks().create("patch" + capitalizeName);
        task.setGroup("patch");
        task.dependsOn(dexTask);

        System.out.println(String.format("\n\n---------- configTasks[%s] end   ----------\n\n", capitalizeName));

    }

    /**
     * @param file
     * @param hexs
     */
    void processClass(String dirName, File file,
                      Map<String, String> hexs,
                      PatchGenerator patchGenerator) {
        String filePath = file.getAbsolutePath();
        //注意这里的filePath包含了目录+包名+类名，所以去掉目录
        String className = filePath.split(dirName)[1].substring(1);
        if (!isFixClz(className)) {
            return;
        }
        try {
            FileInputStream is = new FileInputStream(filePath);
            //执行插桩
            byte[] byteCode = referHackWhenInit(is);
            String hex = HotfixUtils.hex(byteCode);
            is.close();

            FileOutputStream os = new FileOutputStream(filePath);
            os.write(byteCode);
            os.close();

            hexs.put(className, hex);
            //对比缓存的md5，不一致则放入补丁
            patchGenerator.checkClass(className, hex, byteCode);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("\n\n----------  processClass error = %s  ----------\n\n", e.toString()));
        }
    }


    void processJar(File file, Map<String, String> hexs,
                    PatchGenerator patchGenerator) {
        try {
            File bakJar = new File(file.getParent(), file.getName() + ".bak");
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(bakJar));

            JarFile jarFile = new JarFile(file);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();

                jarOutputStream.putNextEntry(new JarEntry(jarEntry.getName()));
                InputStream is = jarFile.getInputStream(jarEntry);

                String className = jarEntry.getName();
                if (className.endsWith(".class") && isFixClz(className) && !className.startsWith("com/enjoy/patch")) {
                    byte[] byteCode = referHackWhenInit(is);
                    String hex = HotfixUtils.hex(byteCode);
                    hexs.put(className, hex);
                    //对比缓存的md5，不一致则放入补丁
                    patchGenerator.checkClass(className, hex, byteCode);
                    jarOutputStream.write(byteCode);
                } else {
                    //输出到临时文件
                    jarOutputStream.write(IOUtils.toByteArray(is));
                }
                is.close();
                jarOutputStream.closeEntry();
            }
            jarOutputStream.close();
            jarFile.close();
            file.delete();
            bakJar.renameTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(String.format("\n\n----------  processJar error = %s  ----------\n\n", e.toString()));
        }
    }

    /**
     * 是否是AndroidSdk的第三方支持包
     *
     * @return
     */
    private boolean isAndroidClz(String ori) {
        return ori.startsWith("android") || ori.startsWith("androidx");
    }

    private boolean isApplicationClz(String ori) {
        return ori.equals(applicationName);
    }

    /**
     * 是否为目标class
     *
     * @return
     */
    private boolean isFixClz(String ori) {
        boolean result = false;
        if (hookClassPrefix.isEmpty()) {
            result = !isAndroidClz(ori);
        } else {
            for (String prefix : hookClassPrefix) {
                System.out.println("\n\n---------- HotfixPlugin prefix=" + prefix + "\n");
                result = ori.contains(prefix);
                if (result) {
                    break;
                }
            }
        }
        if (result) {
            result = !isApplicationClz(ori);
        }
        return result;
    }


    private byte[] referHackWhenInit(InputStream inputStream) throws IOException {
        ClassReader cr = new ClassReader(inputStream);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM6, cw) {
            @Override
            public MethodVisitor visitMethod(int access, final String name, String desc,
                                             String signature, String[] exceptions) {

                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                mv = new MethodVisitor(api, mv) {
                    @Override
                    public void visitInsn(int opcode) {
                        //在构造方法中插入AntilazyLoad引用
                        if ("<init>".equals(name) && opcode == Opcodes.RETURN) {
                            super.visitLdcInsn(Type.getType("Lcom/enjoy/patch/hack/AntilazyLoad;"));
                        }
                        super.visitInsn(opcode);
                    }
                };
                return mv;
            }

        };
        cr.accept(cv, ClassReader.EXPAND_FRAMES);
        return cw.toByteArray();
    }
}
