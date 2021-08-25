package com.walker.buildsrc.hotfix;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.ApplicationVariant;

import org.gradle.api.Action;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class HotfixPlugin implements Plugin<Project> {

    private String applicationName;

    @Override
    public void apply(Project project) {
        System.out.println("\n\n---------- HotfixPlugin Start ----------\n\n");

        //定义额外属性
        project.getExtensions().create("HotfixExt",HotfixExt.class);

        //解析当前module的gradle文件之后
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                //找到额外属性
                HotfixExt hotfixExt=project.getExtensions().findByType(HotfixExt.class);
                applicationName=hotfixExt.getApplicationName();
                // 找到系统属性
                AppExtension appExtension = project.getExtensions().findByType(AppExtension.class);
                DomainObjectSet<ApplicationVariant> applicationVariants = appExtension.getApplicationVariants();
                for(ApplicationVariant var:applicationVariants){
                   //transformClassesWithDexBuilderForDebug/transformClassesWithDexBuilderForRelease
                    // 在task将class文件编译成dex文件之前插桩代码
                    String variantName = var.getName();
                    String myTaskName = "transformClassesWithDexBuilderFor" + firstCharUpperCase(variantName);
                    Task task = project.getTasks().findByName(myTaskName);
                    task.doFirst(new Action<Task>() {
                        @Override
                        public void execute(Task task) {
                            System.out.println(String.format("\n\n---------- %s Start ----------\n\n",task.getName()));
                            Set<File> files = task.getInputs().getFiles().getFiles();
                            for (File file : files) {
                                String filePath = file.getAbsolutePath();
                                //现在，在这个任务之前，我们打印了所有input的文件名
                                // 发现，这里有jar包，也有class
                                // class,我们要利用字节码插桩的方式，在里面植入一段代码.
                                if (filePath.endsWith(".jar")) {
                                    //解压之后对jar内部的每一个class插桩，然后写回去
                                    // 现在来应对jar包，先解压，然后再执行processClass
                                    try {
                                        processJar(file);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else if (filePath.endsWith(".class")) {
                                    //直接对class插桩，然后写回去
                                    //先写这个
                                    processClass(variantName, file); //对于class的处理完毕
                                }
                            }
                            System.out.println(String.format("\n\n---------- %s End   ----------\n\n",task.getName()));
                        }
                    });
                }
            }
        });

        System.out.println("\n\n---------- HotfixPlugin End   ----------\n\n");
    }

    private void processJar(File file) throws IOException {
        // 先预备一个备份文件
        File bakJar = new File(file.getParent(), file.getName() + ".bak");
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(bakJar));
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = jarFile.entries(); // 准备遍历
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement(); // 迭代器遍历

            jos.putNextEntry(new JarEntry(jarEntry.getName()));
//            InputStream is = jarFile.getInputStream(jarEntry);

            String className = jarEntry.getName();
            if (className.endsWith(".class") && !isApplicationClz(className)
                    && !isAndroidClz(className)) {
//                byte[] byteCode = referHackWhenInit(is);
//                jos.write(byteCode);
                System.out.println(">>> hotfix className:" +className + "\n");
            } else {
                //输出到临时文件
//                jos.write(IOUtils.toByteArray(is));
            }
            jos.closeEntry();
        }
//        jos.close();
//        jarFile.close();
//        file.delete();
//        bakJar.renameTo(file);
    }

    private void processClass(String variantName, File file) {
        String path = file.getAbsolutePath();//拿到完整路径，如下:
        // D:\studydemo\hotfix\HotUpdateDemo\app\build\intermediates\classes\debug\com\example\administrator\myapplication\MainActivity.class
        // 这么一大串，包括三个部分，以debug为分界。
        // D:\studydemo\hotfix\HotUpdateDemo\app\build\intermediates\classes\ 是目录
        // debug\ 是编译变体名
        // com\example\administrator\myapplication\MainActivity.class 类完整路径
        //将他进行分割
        String className = path.split(variantName)[1].substring(1);
        System.out.println(">>> hotfix className:" +className + "\n");
//        System.out.println("className:" + className);//拿到完整类名 com\example\administrator\myapplication\MainActivity.class
        // 由于有些class我们不用执行插桩，包括Application，也包括 androidx和support包
        if (isAndroidClz(className) || isApplicationClz(className)) {
            return;
        }
        // 能走到这里的，都是需要插桩的,那么，在这个任务执行时，我需要:
        // 使用文件流
//        try {
//            FileInputStream fis = new FileInputStream(path);
//            byte[] byteCode = referHackWhenInit(fis);
//            fis.close();
//
//            FileOutputStream fos = new FileOutputStream(path);
//            fos.write(byteCode);
//            fos.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 对指定文件进行插桩,这里用到了ASM
     *
     * @param fis
     * @return
     */
    private byte[] referHackWhenInit(InputStream fis) throws IOException {
        ClassReader cr = new ClassReader(fis);// 通过IO流，将一个class解析出来，解析失败会抛异常
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);//再构建一个writer
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM5, cw) {

            @Override
            public MethodVisitor visitMethod(int access, final String name, String desc,
                                             String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                mv = new MethodVisitor(Opcodes.ASM5, mv) {
                    @Override
                    public void visitInsn(int opcode) {
                        if ("<init>".equals(name) && opcode == Opcodes.RETURN) {
                            super.visitLdcInsn(Type.getType("Lcom/walker/dripstone/hack/HackInitClazz;"));
                        }
                        super.visitInsn(opcode);
                    }
                };
                return mv;
            }
        };
        cr.accept(cv, ClassWriter.COMPUTE_FRAMES);
        return cw.toByteArray();
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
     * 首字母变大写
     *
     * @param input 输入
     * @return 首字母变大写的字符串
     */
    private String firstCharUpperCase(String input) {
        String s = input.substring(0, 1).toUpperCase();
        String last = input.substring(1);
        return new StringBuilder().append(s).append(last).toString();
    }
}
