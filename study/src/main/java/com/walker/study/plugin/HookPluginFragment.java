package com.walker.study.plugin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.walker.common.BaseApplication;
import com.walker.core.base.mvc.BaseFragment;
import com.walker.core.log.LogHelper;
import com.walker.study.R;

import java.lang.reflect.Method;

/**
 * @Author Walker
 * @Date 2020/11/29 5:56 PM
 * @Summary 插件化
 */
public class HookPluginFragment extends BaseFragment {

    public static final String KEY_ID = "key_study_hook_plugin_fragment";

    public static Fragment instance() {
        return new HookPluginFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        HookUtil.setProxyConfig(context.getPackageName(), ProxyActivity.class.getName());
        HookUtil.hookAMS();
        HookUtil.hookHandler();
    }

    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {
        baseView.findViewById(R.id.btnHookActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartHook();
            }
        });
        baseView.findViewById(R.id.btnStartPlugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartPlugin();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_study_hook_plugin;
    }

    private void onStartHook() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(getHoldContext().getPackageName(),
                    HookActivity.class.getName()));
            intent.putExtra(HookUtil.KEY_HOOK_TAG, true);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("PluginTest", e.toString());
        }
    }

    private void onStartPlugin() {
        try {
            Class<?> clazz = Class.forName("com.example.plugintest.DemoTest");
            Method print = clazz.getMethod("onTest");
            print.invoke(null);
        } catch (Exception e) {
            Log.e("PluginTest", e.toString());
        }
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.example.plugintest",
                    "com.example.plugintest.DemoActivity"));
            intent.putExtra(HookUtil.KEY_HOOK_TAG, true);
            intent.putExtra("load_path", BaseApplication.pluginLoadPath);
            getHoldContext().startActivity(intent);
        } catch (Exception e) {
            LogHelper.get().e("PluginTest", e.getMessage());
        }
    }
}
