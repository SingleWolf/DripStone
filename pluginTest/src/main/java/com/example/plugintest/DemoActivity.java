package com.example.plugintest;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DemoActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setLoadPath();
        super.onCreate(savedInstanceState);
        DemoTest.onTest();
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_demo, null);
        setContentView(view);
    }

    private void setLoadPath() {
        String loadPath = getIntent().getStringExtra("load_path");
        LoadUtil.setLoadPath(loadPath);
    }
}
