package com.walker.study.annotation;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.walker.common.activity.ShowActivity;
import com.walker.core.base.mvc.BaseFragment;
import com.walker.study.R;
import com.walker.study.retrofit.test.MyRetrofitFragment;

import kotlin.jvm.functions.Function1;

public class InjectFragment extends BaseFragment {

    public static final String KEY_ID = "key_study_inject_fragment";

    private TextView btnReflect;

    public static Fragment instance() {
        return new InjectFragment();
    }


    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {
        btnReflect = baseView.findViewById(R.id.btnReflect);
        btnReflect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InjectActivity.start(getHoldContext(), "Walker", 27, true);
            }
        });

        baseView.findViewById(R.id.btnRetrofit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowActivity.Companion.start(getContext(), "my_retrofit", "手撕Retrofit", new Function1<String, Fragment>() {
                    @Override
                    public Fragment invoke(String s) {
                        return MyRetrofitFragment.instance();
                    }
                });
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_study_inject;
    }
}
