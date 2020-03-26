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
 * @date on 2018/4/2 0002 上午 11:26
 * @email feitianwumu@163.com
 * @desc fragment基类(和AbstractBaseFragmentActivity的继承者搭配使用，支持嵌套fragment)
 */

public abstract class BaseActivityFragment extends Fragment {
    /**
     * 宿主Activity
     * 防止内存不足时fragment调用个体Activity()时报空指针
     */
    private BaseFragmentActivity mHoldActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mHoldActivity = (BaseFragmentActivity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * 获取宿主Activity
     *
     * @return 宿主Activity
     */
    protected BaseFragmentActivity getHoldActivity() {
        return mHoldActivity;
    }

    /**
     * 添加fragment
     *
     * @param fragment 目标fragment
     * @param tag      标签
     */
    protected void addFragment(Fragment fragment, String tag) {
        if (null != fragment) {
            mHoldActivity.addFragment(fragment, tag);
        }
    }

    /**
     * 添加fragment
     *
     * @param fragment 目标fragment
     * @param tag      标签
     * @param isBack   事务被保存到back stack的标识
     */
    protected void addFragment(Fragment fragment, String tag, boolean isBack) {
        if (null != fragment) {
            mHoldActivity.addFragment(fragment, tag, isBack);
        }
    }

    /**
     * 移除fragment
     */
    protected void removeFragment() {
        mHoldActivity.removeFragment();
    }
}
