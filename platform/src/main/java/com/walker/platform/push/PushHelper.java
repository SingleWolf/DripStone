package com.walker.platform.push;

import android.content.Context;
import android.util.Log;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.walker.platform.constant.PlatformConstant;

public class PushHelper {
    private static final String TAG = "PushHelper";

    public static void init(Context context) {

        UMConfigure.init(context, PlatformConstant.UMENG_APP_KEY, "DripStone", UMConfigure.DEVICE_TYPE_PHONE, PlatformConstant.UMENG_APP_SECRET);
        //获取消息推送代理示例
        PushAgent mPushAgent = PushAgent.getInstance(context);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i(TAG, "注册成功：deviceToken：-------->  " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG, "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });
    }
}
