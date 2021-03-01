package com.walker.crash;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DemoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }

    public void onJavaCrash(View v) {
        CrashReport.testJavaCrash();

    }

    public void onNativeCrash(View v) {
        CrashReport.testNativeCrash();
    }
}
