package com.walker.core.base.mvc;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @author Walker
 * @date on 2018/4/2 0002 上午 10:51
 * @email feitianwumu@163.com
 * @desc activity基类（嵌套fragment）
 */

public abstract class BaseFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onTransactBeforeContent();
        setContentView(getContentViewId());
        init(savedInstanceState);
    }

    /**
     * setContentView之前的处理操作
     */
    protected void onTransactBeforeContent() {

    }

    /**
     * 初始化
     *
     * @param savedInstanceState savedInstanceState
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 获取layout的id
     *
     * @return layout的id
     */
    protected abstract int getContentViewId();

    /**
     * 获取activity中替换fragment的布局id
     *
     * @return 替换fragment的布局id
     */
    protected abstract int getFragmentContentId();

    /**
     * 添加fragment
     *
     * @param fragment 目标fragment
     * @param tag      标签
     */
    protected void addFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(getFragmentContentId(), fragment, tag)
                    .addToBackStack(tag)
                    .commitAllowingStateLoss();
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
        if (fragment != null) {
            if (isBack) {
                getSupportFragmentManager().beginTransaction()
                        .replace(getFragmentContentId(), fragment, tag)
                        .addToBackStack(tag)
                        .commitAllowingStateLoss();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(getFragmentContentId(), fragment, tag)
                        .commitAllowingStateLoss();
            }
        }
    }

    /**
     * 移除fragment
     */
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing()) {
            execRelease();
        }
    }

    /**
     * 释放资源
     * 因为onDestroy（）方法执行太迟，顾在onStop（）中如果activity终止了就释放资源
     */
    protected void execRelease() {
    }

}
