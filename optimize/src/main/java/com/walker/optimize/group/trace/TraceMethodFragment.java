package com.walker.optimize.group.trace;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.walker.core.base.mvc.BaseFragment;
import com.walker.core.log.LogHelper;
import com.walker.optimize.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TraceMethodFragment extends BaseFragment {
    public static final String KEY_ID = "key_optimize_trace_method_fragment";

    private ExecutorService mExecutor = Executors.newFixedThreadPool(10);

    public static Fragment instance() {
        return new TraceMethodFragment();
    }

    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {
        baseView.findViewById(R.id.tv_test).setOnClickListener(v -> onTestTapped());
    }

    private void onTestTapped() {
        LogHelper.get().d("TraceMethodFragment", "onTestTapped");
        mExecutor.execute(() -> {
            new TraceTest().tryTest();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_optimize_trace_method;
    }
}
