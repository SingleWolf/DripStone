package com.walker.demo;

import android.util.Log;

import com.walker.common.aop.AopInvocation;

public class CheckOut implements AopInvocation {
    @Override
    public void onBeforeMethod() {
        Log.i("CheckOut", "onBeforeMethod");
    }

    @Override
    public void onAfterMethod() {
        Log.i("CheckOut", "onAfterMethod");
    }
}
