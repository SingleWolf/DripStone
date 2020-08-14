package com.walker.ui.group.photoview;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.walker.core.base.mvc.BaseFragment;
import com.walker.ui.R;

public class PhotoViewFragment extends BaseFragment {

    public static final String KEY_ID = "key_ui_photo_view_fragment";

    public static Fragment getInstance(){
        Fragment fragment=new PhotoViewFragment();
        return fragment;
    }

    @Override
    protected void buildView(View baseView, Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ui_photo_view;
    }
}
