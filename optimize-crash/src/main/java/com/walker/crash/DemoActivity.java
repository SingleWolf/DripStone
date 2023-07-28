package com.walker.crash;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initData(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData(intent);
    }

    private void initData(Intent intent) {
        String jsonData = intent.getStringExtra("data");
        if (!TextUtils.isEmpty(jsonData)) {
            Toast.makeText(this, jsonData, Toast.LENGTH_LONG).show();
        }
    }

    public void onJavaCrash(View v) {
        CrashReport.testJavaCrash();
//        Toast.makeText(this, "应用安装：" + isAvilible(this), Toast.LENGTH_LONG).show();
    }

    public void onNativeCrash(View v) {
        CrashReport.testNativeCrash();
    }

    public void onNativeAirBag(View v) {
        NativeAirbagHelper.openNativeAirbag(11,"","");
    }

    public void onTestNativeAirBag_1(View v) {
        NativeAirbagHelper.testNativeCrash_1();
    }

    public void onTestNativeAirBag_2(View v) {
        NativeAirbagHelper.testNativeCrash_2();
    }

    public static boolean isAvilible(Context context) {
        List<PackageInfo> pinfo = context.getPackageManager().getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.walker.dripstone")) {
                    return true;
                }
            }
        }
        return false;
    }

}
