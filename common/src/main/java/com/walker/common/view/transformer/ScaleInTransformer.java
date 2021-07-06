package com.walker.common.view.transformer;


import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class ScaleInTransformer extends BasePageTransformer {
    private static final float DEFAULT_MIN_SCALE = 0.65f;
    private float mMinScale = DEFAULT_MIN_SCALE;
    private int mScreenW;

    public ScaleInTransformer() {

    }

    public ScaleInTransformer(float minScale) {
        this(minScale, NonPageTransformer.INSTANCE);
    }

    public ScaleInTransformer(ViewPager.PageTransformer pageTransformer) {
        this(DEFAULT_MIN_SCALE, pageTransformer);
    }


    public ScaleInTransformer(float minScale, ViewPager.PageTransformer pageTransformer) {
        mMinScale = minScale;
        mPageTransformer = pageTransformer;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void pageTransform(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        view.setPivotY(pageHeight / 2);
        view.setPivotX(pageWidth / 2);
        if (position < -0.6f) { // [-Infinity,-0.6)
            // This page is way off-screen to the left.
            view.setScaleX(mMinScale);
            view.setScaleY(mMinScale);

        } else if (position <= 0.4f) { // (-0.6,0.4]
            float scaleFactor = (0.6f + position) * (1 - mMinScale) + mMinScale;
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else if (position <= 1.4f) { // (0.4,1.4]
            // float scaleFactor = (1.4f - position) * (1 - mMinScale) + mMinScale;
            float scaleFactor = (mMinScale - 1) * position - 0.4f * mMinScale + 1.4f;
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        } else {//(1.4,Infinity]
            view.setScaleX(mMinScale);
            view.setScaleY(mMinScale);
        }
    }
}
