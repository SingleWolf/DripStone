package com.walker.demo.a2b;

import static com.walker.common.media.image.glide.GlideOptions.fitCenterTransform;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

public class PackageUtils {
    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }

        Target target = Glide.with(context)
                        .asBitmap()
                        .load("http://somefakeurl.com/fakeImage.jpeg")
                        .apply(fitCenterTransform())
                        .into(new SimpleTarget<Bitmap>(250, 250) {

                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                // Do something with bitmap here.
                            }

                        });

        return false;
    }
}
