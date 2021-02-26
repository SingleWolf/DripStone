package com.walker.core.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Walker
 * @date on 2018/3/30 0030 下午 13:28
 * @email feitianwumu@163.com
 * @desc 获取应用crash信息
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static final String FILE_NAME_SUFFIX = ".trace";
    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private OnCrashListener mCrashListener;
    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    public void init(Context context) {
        init(context, null);
    }

    public void init(Context context, OnCrashListener listener) {
        mContext = context.getApplicationContext();
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mCrashListener = listener;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (mCrashListener != null) {
            mCrashListener.onTransact(e);
        }
        try {
            //自行处理：保存本地
            File crashFile = dealException(t, e);
            //other action
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            //如果系统提供了默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
            if (mDefaultCrashHandler != null) {
                mDefaultCrashHandler.uncaughtException(t, e);
            } else {
                Process.killProcess(Process.myPid());
            }
        }
    }

    /*** 导出异常信息到SD卡 ** @param e */
    private File dealException(Thread t, Throwable e) throws Exception {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        File f = new File(mContext.getExternalCacheDir().getAbsoluteFile(), "crash_info");
        if (!f.exists()) {
            f.mkdirs();
        }
        File file = new File(f, time + FILE_NAME_SUFFIX);
        //往文件中写入数据
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        pw.println(time);
        pw.println("Thread: " + t.getName());
        pw.println(getPhoneInfo());
        e.printStackTrace(pw);
        //写入crash堆栈
        pw.close();
        return file;
    }

    private String getPhoneInfo() throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        StringBuilder sb = new StringBuilder();
        //App版本
        sb.append("App Version: ");
        sb.append(pi.versionName);
        sb.append("_");
        sb.append(pi.versionCode + "\n");
        //Android版本号
        sb.append("OS Version: ");
        sb.append(Build.VERSION.RELEASE);
        sb.append("_");
        sb.append(Build.VERSION.SDK_INT + "\n");
        //手机制造商
        sb.append("Vendor: ");
        sb.append(Build.MANUFACTURER + "\n");
        //手机型号
        sb.append("Model: ");
        sb.append(Build.MODEL + "\n");
        //CPU架构
        sb.append("CPU: ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sb.append(Arrays.toString(Build.SUPPORTED_ABIS));
        } else {
            sb.append(Build.CPU_ABI);
        }
        return sb.toString();
    }
}
