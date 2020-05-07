package com.walker.dripstone.links;

import android.app.Activity;

import com.walker.core.log.LogHelper;
/**
 * @Author Walker
 *
 * @Date   2020-04-20 21:31
 *
 * @Summary  链接辅助类
 */
public class LinkHelper {
    private static final String TAG = "LinkHelper";
    private static LinkHelper sInstance;

    private ILinker linker;

    private LinkHelper() {
    }

    public static LinkHelper getInstance() {
        if (sInstance == null) {
            synchronized (LinkHelper.class) {
                if (sInstance == null) {
                    sInstance = new LinkHelper();
                }
            }
        }
        return sInstance;
    }

    public void setLinker(String uri) {
        ILinker linkInstance = LinkerFactory.create(uri);
        linker = linkInstance;
        LogHelper.get().i(TAG, uri);
    }

    public void transactLink(Activity activity) {
        if (linker != null && linker.isEnable()) {
            linker.onTransact(activity);
        }
    }
}
