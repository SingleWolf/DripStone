package com.example.plugintest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DemoActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setLoadPath();
        super.onCreate(savedInstanceState);
        DemoTest.onTest();
        setContentView(R.layout.activity_demo);
        findViewById(R.id.mapClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMapClick();
            }
        });
        findViewById(R.id.nativeInfoClick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNativeInfoClick();
            }
        });
    }

    private void setLoadPath() {
        String loadPath = getIntent().getStringExtra("load_path");
        LoadUtil.setLoadPath(loadPath);
    }

    private void onNativeInfoClick() {
        Toast.makeText(this, new NativeTest().getInfo(), Toast.LENGTH_LONG).show();
    }

    public void onMapClick() {
        Intent intent = new Intent(this, BasicMapActivity.class);
        startActivity(intent);
    }
}
