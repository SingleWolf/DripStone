package com.walker.study.thread;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.walker.core.base.mvc.BaseFragment;
import com.walker.study.R;

/**
 * @Author Walker
 * @Date 2020-05-15 09:23
 * @Summary 线程与并发相关
 */
public class ThreadFragment extends BaseFragment implements View.OnClickListener {

    public static final String KEY_ID = "key_study_thread_fragment";

    public static Fragment instance() {
        return new ThreadFragment();
    }


    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {
        baseView.findViewById(R.id.btnNotifyWait).setOnClickListener(this);
        baseView.findViewById(R.id.btnStartThread).setOnClickListener(this);
        baseView.findViewById(R.id.btnStopThread).setOnClickListener(this);
        baseView.findViewById(R.id.btnGenDeadLock).setOnClickListener(this);
        baseView.findViewById(R.id.btnSolveDeadLock).setOnClickListener(this);
        baseView.findViewById(R.id.btnOrderRun).setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_study_thread;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnStartThread) {
            ThreadAction.getInstance().start();
        } else if (id == R.id.btnNotifyWait) {
            NotifyAndWait.getInstance().test();
        } else if (id == R.id.btnStopThread) {
            ThreadAction.getInstance().stop();
        } else if (id == R.id.btnGenDeadLock) {
            ThreadAction.getInstance().genDeadLock();
        } else if (id == R.id.btnSolveDeadLock) {
            ThreadAction.getInstance().solveDeadLock();
        } else if (id == R.id.btnOrderRun) {
            ThreadAction.getInstance().orderRun();
        }
    }
}
