package com.walker.demo.trace;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.walker.core.base.mvc.BaseFragment;
import com.walker.core.log.LogHelper;
import com.walker.demo.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TraceMethodFragment extends BaseFragment {
    public static final String KEY_ID = "key_demo_trace_method_fragment";

    private static final String TAG_TEST = "test";

    private ExecutorService mExecutor = Executors.newFixedThreadPool(10);

    public static Fragment instance() {
        return new TraceMethodFragment();
    }

    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {
        MethodEventManager.getInstance().registerMethodObserver(TAG_TEST, new MethodObserver() {
            @Override
            public void onMethodEnter(String tag, String methodName) {
                LogHelper.get().d("MethodEvent", "method " + methodName + " enter at time " + System.currentTimeMillis());
            }

            @Override
            public void onMethodExit(String tag, String methodName) {
                LogHelper.get().d("MethodEvent", "method " + methodName + " exit at time " + System.currentTimeMillis());
            }
        });
        for (int i = 0; i < 10; i++) {
            mExecutor.execute(this::test);
        }

        new TraceTest().tryTest();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_demo_trace_method;
    }

    @TraceMethod(tag = TAG_TEST)
    public void test() {
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
