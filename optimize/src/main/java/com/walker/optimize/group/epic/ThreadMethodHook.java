package com.walker.optimize.group.epic;

import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;

class ThreadMethodHook extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        Thread t = (Thread) param.thisObject;
        Log.i(EpicHookHelper.TAG, "[ThreadMethodHook] thread:" + t + ", started..");
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        Thread t = (Thread) param.thisObject;
        Log.i(EpicHookHelper.TAG, "[ThreadMethodHook] thread:" + t + ", exit..");
    }
}