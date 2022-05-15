package com.walker.study.hotfix;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.walker.study.util.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HotfixHelper {

    private static File initHack(Context context) {

        File hackDir = context.getDir("hack", Context.MODE_PRIVATE);
        File hackFile = new File(hackDir, "hack.jar");
        if (!hackFile.exists()) {
            BufferedInputStream is = null;
            BufferedOutputStream os = null;
            try {
                is = new BufferedInputStream(context.getAssets().open("hack" +
                        ".jar"));
                os = new BufferedOutputStream(new FileOutputStream(hackFile));
                byte[] buffer = new byte[4096];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Utils.close(is);
                Utils.close(os);
            }
        }
        return hackFile;

    }

    public static void installPatch(Application application, File patch) {
        File hackFile = initHack(application);
        ClassLoader classLoader = application.getClassLoader();
        List<File> files = new ArrayList<>();
        if (patch.exists()) {
            files.add(patch);
        }
        files.add(hackFile);
        File dexOptDir = application.getCacheDir();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                ClassLoaderInjector.inject(application, classLoader, files);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            try {
                //23 6.0及以上
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    InstallUtil.V23.install(classLoader, files, dexOptDir);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    InstallUtil.V19.install(classLoader, files, dexOptDir); //4.4以上
                } else {  // >= 14
                    InstallUtil.V14.install(classLoader, files, dexOptDir);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
