package com.walker.dripstone.lancet;

import android.util.Log;

import com.walker.common.router.IUserCenterRouter;
import com.walker.core.log.LogHelper;
import com.walker.core.router.RouterLoader;
import com.walker.core.util.ToastUtils;
import com.walker.dripstone.GlobalApplication;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.This;
import me.ele.lancet.base.annotations.Insert;
import me.ele.lancet.base.annotations.Proxy;
import me.ele.lancet.base.annotations.TargetClass;

public class LancetHook {

    public static boolean isLoginSuccess = false;

    @Insert("testError")
    @TargetClass("com.walker.optimize.group.lancet.LancetTest")
    public static void testFun(String msg) {
        try {
            Origin.callVoid();
        } catch (Exception e) {
            LogHelper.get().e("LancetHook", "testError : " + e.toString());
            ToastUtils.showCenterLong("error=" + e.toString());
        }
    }

    @Insert("onAopLoginTapped")
    @TargetClass("com.walker.optimize.group.lancet.LancetFragment")
    public void aopLogin() {
        if (isLoginSuccess) {
            Origin.callVoid();
            return;
        }
        IUserCenterRouter userCenterRouter = RouterLoader.load(IUserCenterRouter.class);
        if (userCenterRouter != null && GlobalApplication.Companion.getCurrentActivity() != null) {
            userCenterRouter.onLogin(GlobalApplication.Companion.getCurrentActivity(), new CallbackFunction(This.get()));
        }
    }


    @Proxy("i")
    @TargetClass("android.util.Log")
    public static int interceptLogI(String tag, String msg) {
        msg = msg + "-lancet";
        return (int) Origin.call();
    }


    @TargetClass(value = "androidx.appcompat.app.AppCompatActivity", scope = Scope.LEAF)
    @Insert(value = "onStop", mayCreateSuper = true)
    protected void onStop() {
        Log.i("LancetHook", This.get().getClass().getSimpleName() + " - onStop()");
        Origin.callVoid();
    }

    public static class CallbackFunction implements Function2<Integer, String, Unit> {

        private Object instance;

        public CallbackFunction(Object o) {
            instance = o;
        }

        @Override
        public Unit invoke(Integer resultCode, String s) {
            if (resultCode == 1) {
                isLoginSuccess = true;
                LogHelper.get().i("LancetHook", "登录成功");
//                if (instance != null && instance instanceof LancetFragment) {
//                    ((LancetFragment) instance).onAopLoginTapped();
//                }
            } else {
                LogHelper.get().i("LancetHook", "登录失败");
            }
            return null;
        }
    }

//    @TargetClass("org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager")
//    @Proxy("finalize")
//    protected void finalize() {
//        LogHelper.get().i("LancetHook", "ThreadSafeClientConnManager -> finalize()");
//        Origin.callVoid();
//    }
//
//    @TargetClass("org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager")
//    @Proxy("shutdown")
//    public void shutdown() {
//        LogHelper.get().i("LancetHook", "ThreadSafeClientConnManager -> shutdown()");
//        Origin.callVoid();
//    }
}
