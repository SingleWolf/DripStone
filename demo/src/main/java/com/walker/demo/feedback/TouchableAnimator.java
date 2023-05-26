package com.walker.demo.feedback;

import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnFloatAnimator;

public class TouchableAnimator implements OnFloatAnimator {

    @Override
    public android.animation.Animator enterAnim(@NonNull View view, @NonNull WindowManager.LayoutParams layoutParams, @NonNull WindowManager windowManager, @NonNull SidePattern sidePattern) {
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        return null;
    }

    @Override
    public android.animation.Animator exitAnim(@NonNull View view, @NonNull WindowManager.LayoutParams layoutParams, @NonNull WindowManager windowManager, @NonNull SidePattern sidePattern) {
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        return null;
    }
}