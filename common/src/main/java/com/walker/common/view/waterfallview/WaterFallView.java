package com.walker.common.view.waterfallview;

import android.content.Context;
import android.view.View;

import com.walker.common.R;
import com.walker.common.databinding.CommonWaterFallViewBinding;
import com.walker.core.base.mvvm.customview.BaseCustomView;

public class WaterFallView extends BaseCustomView<CommonWaterFallViewBinding, WaterFallViewViewModel> {

    public WaterFallView(Context context) {
        super(context);
    }

    @Override
    protected int setViewLayoutId() {
        return R.layout.common_water_fall_view;
    }

    @Override
    protected void setDataToView(WaterFallViewViewModel data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    protected void onRootClick(View view) {

    }
}
