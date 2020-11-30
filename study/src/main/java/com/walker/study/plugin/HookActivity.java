package com.walker.study.plugin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @Author Walker
 * @Date 2020/11/30 10:27 AM
 * @Summary HookActivity
 */
public class HookActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView content = new TextView(this);
        content.setText("I'm is HookActivity\n\n这是HookActivity");
        setContentView(content);
    }
}
