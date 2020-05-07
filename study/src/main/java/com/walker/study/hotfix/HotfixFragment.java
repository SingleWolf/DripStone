package com.walker.study.hotfix;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.walker.core.base.mvc.BaseFragment;
import com.walker.study.R;

/**
 * @Author Walker
 *
 * @Date   2020-04-16 16:33
 *
 * @Summary  热修复
 */
public class HotfixFragment extends BaseFragment {

    public static final String KEY_ID="key_demo_hotfix_fragment";

    public static Fragment instance(){
        return new HotfixFragment();
    }

    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_study_hotfix;
    }
}
