package com.example.plugintest;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;

public class BaseActivity extends AppCompatActivity {
    public static final String KEY_HOOK_TAG = "is_hook_activity";
    protected Context mContext;

//    @Override
//    public Resources getResources() {
//        if (getApplication() != null && getApplication().getResources() != null) {
//            return getApplication().getResources();
//        }
//        return super.getResources();

//        Resources resources = LoadUtil.getResources(getApplication());
//        // 如果插件作为一个单独的app，返回 super.getResources()
//        return resources == null ? super.getResources() : resources;
//    }

    // 不会影响到宿主
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources resources = LoadUtil.getResources(getApplication());

        mContext = new ContextThemeWrapper(getBaseContext(), 0);

        Class<? extends Context> clazz = mContext.getClass();
        try {
            Field mResourcesField = clazz.getDeclaredField("mResources");
            mResourcesField.setAccessible(true);
            mResourcesField.set(mContext, resources);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(mContext).inflate(layoutResID, null);
        setContentView(view);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.putExtra(KEY_HOOK_TAG, true);
        super.startActivity(intent);
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        intent.putExtra(KEY_HOOK_TAG, true);
        super.startActivity(intent, options);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra(KEY_HOOK_TAG, true);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        intent.putExtra(KEY_HOOK_TAG, true);
        super.startActivityForResult(intent, requestCode, options);
    }
}
