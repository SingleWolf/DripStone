package com.walker.common.view.picturetitleview;

import android.content.Context;
import android.view.View;

import com.walker.common.R;
import com.walker.common.databinding.CommonPictureTitleViewBinding;
import com.walker.core.base.mvvm.customview.BaseCustomView;
import com.walker.core.log.LogHelper;

public class PictureTitleView extends BaseCustomView<CommonPictureTitleViewBinding, PictureTitleViewViewModel> {

    public PictureTitleView(Context context) {
        super(context);
    }

    @Override
    protected int setViewLayoutId() {
        return R.layout.common_picture_title_view;
    }

    @Override
    protected void setDataToView(PictureTitleViewViewModel data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    protected void onRootClick(View view) {
        LogHelper.get().d("PictureTitleView", getViewModel().title + " is clicked");
    }
}
