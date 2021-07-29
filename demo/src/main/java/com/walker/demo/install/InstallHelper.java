package com.walker.demo.install;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.walker.core.efficiency.activityresult.ActivityResultHelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * android安装应用(适用于各个版本)
 */
public class InstallHelper {

    private static volatile InstallHelper sInstance;

    public static InstallHelper get() {
        if (sInstance == null) {
            synchronized (InstallHelper.class) {
                if (sInstance == null) {
                    sInstance = new InstallHelper();
                }
            }
        }
        return sInstance;
    }

    private InstallHelper() {
    }

    public void onInstall(FragmentActivity activity, String apkPath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startInstallO(activity, apkPath);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) startInstallN(activity, apkPath);
        else startInstall(activity, apkPath);
    }

    /**
     * android1.x-6.x
     */
    private void startInstall(FragmentActivity activity, String apkPath) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(install);
    }

    /**
     * android7.x
     */
    private void startInstallN(FragmentActivity activity, String apkPath) {
        //参数1 上下文, 参数2 在AndroidManifest中的android:authorities值, 参数3  共享的文件
        Uri apkUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".install_file_provider", new File(apkPath));
        Intent install = new Intent(Intent.ACTION_VIEW);
        //由于没有在Activity环境下启动Activity,设置下面的标签
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        activity.startActivity(install);
    }

    /**
     * android8.x
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallO(FragmentActivity activity, String apkPath) {
        boolean isGranted = activity.getPackageManager().canRequestPackageInstalls();
        if (isGranted) startInstallN(activity, apkPath);//安装应用的逻辑(写自己的就可以)
        else new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("安装应用需要打开未知来源权限，请去设置中开启权限")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int w) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                        ActivityResultHelper.init(activity).startActivityForResult(intent, (resultCode, data) -> {
                            if (resultCode == Activity.RESULT_OK) {
                                onInstall(activity, apkPath);
                            }
                        });
                    }
                }).show();
    }

    /**
     * 执行具体的静默安装逻辑，需要手机ROOT。
     *
     * @param apkPath 要安装的apk文件的路径
     * @return 安装成功返回true，安装失败返回false。
     */
    public boolean onSilentInstall(String apkPath) {
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String command = "pm install -r " + apkPath + "\n";
            dataOutputStream.write(command.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("TAG", "install msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 判断手机是否拥有Root权限。
     *
     * @return 有root权限返回true，否则返回false。
     */
    public boolean isRoot() {
        boolean bool = false;
        try {
            bool = (!new File("/system/bin/su").exists()) || (!new File("/system/xbin/su").exists());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }
}
