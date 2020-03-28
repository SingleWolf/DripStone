package com.walker.common.view.titleview;

import android.content.Context;
import android.view.View;

import com.walker.common.R;
import com.walker.common.databinding.CommonTitleViewBinding;
import com.walker.core.base.mvvm.customview.BaseCustomView;

public class TitleView extends BaseCustomView<CommonTitleViewBinding, TitleViewViewModel> {
    public TitleView(Context context) {
        super(context);
    }

    @Override
    public int setViewLayoutId() {
        return R.layout.common_title_view;
    }

    @Override
    public void setDataToView(TitleViewViewModel data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    public void onRootClick(View view) {
    }
}
