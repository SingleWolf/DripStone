package com.walker.common.view.titleview;

import android.content.Context;
import android.view.View;

import com.walker.common.R;
import com.walker.common.databinding.TitleViewBinding;
import com.walker.core.base.mvvm.customview.BaseCustomView;
import com.walker.core.util.ToastUtils;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class TitleView extends BaseCustomView<TitleViewBinding, TitleViewViewModel> {
    public TitleView(Context context) {
        super(context);
    }

    @Override
    public int setViewLayoutId() {
        return R.layout.title_view;
    }

    @Override
    public void setDataToView(TitleViewViewModel data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    public void onRootClick(View view) {
        ToastUtils.show(getViewModel().title);
    }
}
