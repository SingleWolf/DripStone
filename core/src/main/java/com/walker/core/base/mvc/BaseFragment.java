package com.walker.core.base.mvc;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Walker
 * @date on 2018/3/30 0030 下午 15:43
 * @email feitianwumu@163.com
 * @desc fragment的基类
 */
public abstract class BaseFragment extends Fragment {
    /**
     * 宿主Context
     */
    private Context mHoldContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHoldContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View baseView = inflater.inflate(getLayoutId(), container, false);
        return baseView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buildView(view, savedInstanceState);
    }

    /**
     * 创建view
     *
     * @param baseView           view
     * @param savedInstanceState savedInstanceState
     */
    protected abstract void buildView(View baseView, Bundle savedInstanceState);

    /**
     * 获取布局文件id
     *
     * @return 布局文件id
     */
    protected abstract int getLayoutId();

    /**
     * 获取宿主Context
     *
     * @return 宿主Context
     */
    protected Context getHoldContext() {
        return mHoldContext;
    }
}
