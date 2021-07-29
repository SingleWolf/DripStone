package com.walker.common.view;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.walker.common.media.image.ImageLoadHelper;

public class CommonBindingAdapters {

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        ImageLoadHelper.INSTANCE.loadUrl(view, url, null);
    }

    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, Boolean value) {
        view.setVisibility(value ? View.VISIBLE : View.GONE);
    }
}
