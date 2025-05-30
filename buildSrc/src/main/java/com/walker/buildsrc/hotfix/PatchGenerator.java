package com.walker.buildsrc.hotfix;

import com.android.build.gradle.AppExtension;

import org.apache.tools.ant.taskdefs.condition.Os;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.process.ExecSpec;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * @author Lance
 * @date 2019-09-23
 */
public class PatchGenerator {

    private File patchFile;
    private String buildToolsVersion;
    private Project project;
    private File jarFile;
    private Map<String, String> oldHexs;
    private JarOutputStream jarOutputStream;

    public PatchGenerator(Project project, File patchFile, File jarFile,
                          File hexFile) {
        this.project = project;
        AppExtension android = project.getExtensions().getByType(AppExtension.class);
        buildToolsVersion = android.getBuildToolsVersion();
        this.patchFile = patchFile;
        this.jarFile = jarFile;
        if (hexFile.exists()) {
            oldHexs = HotfixUtils.readHex(hexFile);
        }

    }

    public void checkClass(String className, String hex, byte[] byteCode) {
        if (HotfixUtils.isEmpty(oldHexs)) {
            return;
        }
        String oldHex = oldHexs.get(className);
        // 缓存不存在并且不相等，需要进入补丁包
        if (oldHex == null || !oldHex.equals(hex)) {
            JarOutputStream output = getOutput();
            try {
                output.putNextEntry(new JarEntry(className));
                output.write(byteCode);
                output.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private JarOutputStream getOutput() {
        if (jarOutputStream == null) {
            try {
                jarOutputStream = new JarOutputStream(new FileOutputStream(jarFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jarOutputStream;
    }

    public void generate() throws Exception {
        if (!jarFile.exists()) {
            return;
        }
        achiveJavaVersion();
        JarOutputStream output = getOutput();
        output.close();
        Properties properties = new Properties();
        File localProps = project.getRootProject().file("local.properties");
        //因为dx命令在 sdk中，获得sdk目录
        String sdkDir;
        if (localProps.exists()) {
            properties.load(new FileInputStream(localProps));
            sdkDir = properties.getProperty("sdk.dir");
        } else {
            sdkDir = System.getenv("ANDROID_HOME");
        }
        //windows使用 dx.bat命令,linux/mac使用 dx命令
        String cmdExt = Os.isFamily(Os.FAMILY_WINDOWS) ? ".bat" : "";
        // 执行：dx --dex --output=output.jar input.jar
        final String dxPath = sdkDir + "/build-tools/" + buildToolsVersion +
                "/dx" + cmdExt;
        final String patch = " --output=" + patchFile.getAbsolutePath();
//        project.exec(new Action<ExecSpec>() {
//            @Override
//            public void execute(ExecSpec execSpec) {
//                execSpec.commandLine(dxPath, " --dex", patch, jarFile.getAbsolutePath());
//            }
//        });
//        jarFile.delete();

        final String cmd = dxPath + " --dex " + patch + " " + jarFile.getAbsolutePath();
        System.out.println(String.format("\n<------- CMD = %s ------->\n", cmd));
        Process process =
                Runtime.getRuntime().exec(cmd);
        process.waitFor();
        // 命令执行失败
        if (process.exitValue() != 0) {
            throw new IOException("generate patch error:" + cmd);
        }
        jarFile.delete();
        project.getLogger().error("\npatch generated in : " + patchFile);
    }

    private void achiveJavaVersion() {
        try {
            Process process = Runtime.getRuntime().exec("java -version");
            process.waitFor();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream is = process.getErrorStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            System.out.println(new String(bos.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
