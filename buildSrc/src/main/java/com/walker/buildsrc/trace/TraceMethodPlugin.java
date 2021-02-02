package com.walker.buildsrc.trace;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.LibraryExtension;
import com.android.build.gradle.api.ApplicationVariant;
import com.android.build.gradle.api.LibraryVariant;

import org.apache.commons.io.IOUtils;
import org.gradle.api.Action;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.internal.DefaultDomainObjectSet;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class TraceMethodPlugin implements Plugin<Project> {

    private String traceClazzPrefix;

    private boolean enable;

    @Override
    public void apply(Project project) {
        System.out.println("\n\n---------- TraceMethodPlugin Start ----------\n\n");
        //定义额外属性
        project.getExtensions().create("TraceMethodExt", TraceMethodExt.class);
        //解析当前module的gradle文件之后
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                //找到额外属性
                TraceMethodExt traceMethodExt = project.getExtensions().findByType(TraceMethodExt.class);
                traceClazzPrefix = traceMethodExt.getTracePrefix();
                enable = traceMethodExt.isEnable();
                if (enable == false) {
                    return;
                }
                // 找到系统属性
                AppExtension appExtension = project.getExtensions().findByType(AppExtension.class);
                if (appExtension == null) {
                    LibraryExtension libraryExtension = project.getExtensions().findByType(LibraryExtension.class);
                    if (libraryExtension == null) {
                        System.out.println("\n\n---------- TraceMethodPlugin not found extension ----------\n\n");
                        return;
                    } else {
                        handleLibraryExtension(project, libraryExtension);
                    }
                } else {
                    handleAppExtension(project, appExtension);
                }
            }
        });
    }

    private void handleLibraryExtension(Project project, LibraryExtension libraryExtension) {
        System.out.println("\n\n---------- TraceMethodPlugin from library ----------\n\n");
        DefaultDomainObjectSet<com.android.build.gradle.api.LibraryVariant> libraryExtensions = libraryExtension.getLibraryVariants();
        for (LibraryVariant var : libraryExtensions) {
            String variantName = var.getName();
            String myTaskName = "createFullJar" + firstCharUpperCase(variantName);
            Task task = project.getTasks().findByName(myTaskName);
            if (task == null) {
                System.out.println(String.format("\n\n---------- TraceMethodPlugin %s is null ----------\n\n", myTaskName));
                return;
            }
            task.doFirst(new Action<Task>() {
                @Override
                public void execute(Task task) {
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
                }
            });
        }
    }

    private void handleAppExtension(Project project, AppExtension appExtension) {
        System.out.println("\n\n---------- TraceMethodPlugin from app ----------\n\n");
        DomainObjectSet<ApplicationVariant> applicationVariants = appExtension.getApplicationVariants();
        for (ApplicationVariant var : applicationVariants) {
            //transformClassesWithDexBuilderForDebug/transformClassesWithDexBuilderForRelease
            // 在task将class文件编译成dex文件之前插桩代码
            String variantName = var.getName();
            String myTaskName = "transformClassesWithDexBuilderFor" + firstCharUpperCase(variantName);
            Task task = project.getTasks().findByName(myTaskName);
            task.doFirst(new Action<Task>() {
                @Override
                public void execute(Task task) {
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
                }
            });
        }
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
            InputStream is = jarFile.getInputStream(jarEntry);

            String className = jarEntry.getName();
            if (className.endsWith(".class")&&isTraceClz(className)) {
                byte[] sourceBytes = IOUtils.toByteArray(is);
                byte[] byteCode = modifyClass(sourceBytes);
                jos.write(byteCode);
            } else {
                //输出到临时文件
                jos.write(IOUtils.toByteArray(is));
            }
            jos.closeEntry();
        }
        jos.close();
        jarFile.close();
        file.delete();
        bakJar.renameTo(file);
    }

    private void processClass(String variantName, File file) {
        //拿到完整路径，如下:
        String path = file.getAbsolutePath();
        // D:\studydemo\hotfix\HotUpdateDemo\app\build\intermediates\classes\debug\com\example\administrator\myapplication\MainActivity.class
        // 这么一大串，包括三个部分，以debug为分界。
        // D:\studydemo\hotfix\HotUpdateDemo\app\build\intermediates\classes\ 是目录
        // debug\ 是编译变体名
        // com\example\administrator\myapplication\MainActivity.class 类完整路径
        //将他进行分割
        String className = path.split(variantName)[1].substring(1);
        // 仅对需要的class进行插桩
        if (!isTraceClz(className)) {
            return;
        }
        // 能走到这里的，都是需要插桩的,那么，在这个任务执行时，我需要:
        // 使用文件流
        try {
            FileInputStream fis = new FileInputStream(path);
            byte[] sourceBytes = IOUtils.toByteArray(fis);
            byte[] byteCode = modifyClass(sourceBytes);
            fis.close();

            FileOutputStream fos = new FileOutputStream(path);
            fos.write(byteCode);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否为目标class
     *
     * @return
     */
    private boolean isTraceClz(String ori) {
        return ori.contains(traceClazzPrefix);
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

    private byte[] modifyClass(byte[] classBytes) {
        ClassReader classReader = new ClassReader(classBytes);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new TraceMethodClassVisitor(classWriter);
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
}
