package com.walker.study.plugin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.walker.common.BaseApplication;
import com.walker.core.base.mvc.BaseFragment;
import com.walker.core.log.LogHelper;
import com.walker.core.ui.loadsir.LoadingCallback;
import com.walker.core.util.ToastUtils;
import com.walker.study.R;

import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author Walker
 * @Date 2020/11/29 5:56 PM
 * @Summary 插件化
 */
public class HookPluginFragment extends BaseFragment {

    public static final String KEY_ID = "key_study_hook_plugin_fragment";
    private static boolean sPluginLoaded = false;
    private LoadService mLoadService;

    public static Fragment instance() {
        return new HookPluginFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!sPluginLoaded) {
            HookUtil.setProxyConfig(context.getPackageName(), ProxyActivity.class.getName());
            HookUtil.hookAMS();
            HookUtil.hookHandler();
        }
    }

    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {
        mLoadService = LoadSir.getDefault().register(baseView);
        mLoadService.showSuccess();
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
        Observable.just(sPluginLoaded).map(new Function<Boolean, Boolean>() {
            @Override
            public Boolean apply(@NonNull Boolean isLoaded) throws Exception {
                if (!isLoaded) {
                    loadingPlugin();
                    Thread.sleep(3 * 1000);
                }
                return true;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mLoadService.showCallback(LoadingCallback.class);
                    }

                    @Override
                    public void onNext(@NonNull Boolean isStart) {
                        if (isStart) {
                            mLoadService.showSuccess();
                            startPlugin();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        LogHelper.get().e("PluginTest", e.getMessage());
                        ToastUtils.showCenter(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void startPlugin() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.example.plugintest",
                    "com.example.plugintest.DemoActivity"));
            intent.putExtra(HookUtil.KEY_HOOK_TAG, true);
            intent.putExtra("load_path", BaseApplication.pluginLoadPath);
            getHoldContext().startActivity(intent);
            LogHelper.get().i("PluginTest", "###  startPlugin ###");
        } catch (Exception e) {
            LogHelper.get().e("PluginTest", e.getMessage());
        }
    }

    private void loadingPlugin() {
        if (!sPluginLoaded) {
            LoadUtil.loadPlugin(BaseApplication.context, BaseApplication.pluginLoadPath);
            sPluginLoaded = true;
            LogHelper.get().d("PluginTest", "load plugin successful !");
        }
    }
}
