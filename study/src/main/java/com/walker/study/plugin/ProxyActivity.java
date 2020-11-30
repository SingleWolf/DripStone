package com.walker.study.plugin;

import android.os.Bundle;

import com.walker.core.base.mvc.BaseActivity;
import com.walker.study.R;

/**
 * @Author Walker
 * @Date 2020/11/28 9:28 PM
 * @Summary 占位activity
 */
public class ProxyActivity extends BaseActivity {
    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_study_show;
    }
}
