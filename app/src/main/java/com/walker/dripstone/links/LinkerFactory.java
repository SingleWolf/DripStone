package com.walker.dripstone.links;

import android.text.TextUtils;

/**
 * @Author Walker
 * @Date 2020-04-20 21:16
 * @Summary 链接工厂
 */
public class LinkerFactory {
    public static ILinker create(String uri) {
        if(TextUtils.isEmpty(uri)){
            return null;
        }
        WebLinker webLinker = new WebLinker();
        webLinker.setConfig(uri);
        return webLinker;
    }
}
