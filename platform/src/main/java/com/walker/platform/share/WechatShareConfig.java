package com.walker.platform.share;

import android.content.Context;

import com.umeng.socialize.PlatformConfig;
import com.walker.common.share.IShareConfig;

/**
 * Author  : walker
 * Date    : 2022/2/15  3:25 下午
 * Email   : feitianwumu@163.com
 * Summary : 微信分享配置
 */
public class WechatShareConfig implements IShareConfig {
    @Override
    public void onInit(Context context) {
        //狼行天下配置
        PlatformConfig.setWeixin("wx245a89275b18b6fa", "306c4c23661a067527f7ae227bfb3cf0");
        String FileProvider = String.format("%s.fileprovider", context.getApplicationInfo().packageName);
        PlatformConfig.setWXFileProvider(FileProvider);
    }

    @Override
    public int getTag() {
        return 0;
    }
}
