package com.walker.optimize.group.battery;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.walker.core.log.LogHelper;


public class BatteryUtils {

    private static final String TAG = "BatteryUtils";

    public static void addWhileList(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //是否处于白名单
            if (!pm.isIgnoringBatteryOptimizations(context.getPackageName())) {
                // 直接询问用户是否允许把我们应用加入白名单
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
                //跳转到电量优化管理设置中
//              context.startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));
            }
        }
    }


    public static PowerManager.WakeLock wakeLock(Context context) {
        PowerManager.WakeLock wakeLock = null;
        // 跨进程获取 PowerManager 服务
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        // 判断是否支持 CPU 唤醒
        boolean isWakeLockLevelSupported = powerManager.
                isWakeLockLevelSupported(PowerManager.PARTIAL_WAKE_LOCK);
        Log.e(TAG, "是否支持wakelock: " + isWakeLockLevelSupported);
        // 支持 CPU 唤醒 , 才保持唤醒
        if (isWakeLockLevelSupported) {
            // 创建只唤醒 CPU 的唤醒锁
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "example:WAKE_LOCK");
            // 开始唤醒 CPU
            wakeLock.acquire();
        }
        return wakeLock;
    }

    public static void doWork(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)  //Wi-Fi
                .setRequiresCharging(true) //在设备充电时运行
                .setRequiresBatteryNotLow(true) //电量不足不会运行
                .build();
        OneTimeWorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(UploadWorker.class)
                        .setConstraints(constraints)
                        .build();
        WorkManager
                .getInstance(context)
                .enqueueUniqueWork("upload",
                        ExistingWorkPolicy.KEEP, uploadWorkRequest);

    }


    public static PowerConnectionReceiver register(Context context) {
        IntentFilter ifilter = new IntentFilter();
        //充电状态
        ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        //电量显著变化
        ifilter.addAction(Intent.ACTION_BATTERY_LOW);
        ifilter.addAction(Intent.ACTION_BATTERY_OKAY);
        PowerConnectionReceiver powerConnectionReceiver = new PowerConnectionReceiver();
        context.registerReceiver(powerConnectionReceiver, ifilter);
        return powerConnectionReceiver;
    }


    public static void checkBattery(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // 是否正在充电
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // 什么方式充电？
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        //usb
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        //充电器
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        //获得电量
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level * 100 / (float) scale;

        LogHelper.get().e(TAG, "isCharging: " + isCharging + "  usbCharge: " + usbCharge + "  acCharge:" + acCharge);
        LogHelper.get().e(TAG, "当前电量: " + batteryPct);
    }
}
