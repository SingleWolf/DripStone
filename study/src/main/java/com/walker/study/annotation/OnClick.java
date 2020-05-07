package com.walker.study.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Walker
 * @Date 2020-05-06 21:50
 * @Summary OnClick
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventType(listenerType = View.OnClickListener.class ,listenerSetter = "setOnClickListener")
public @interface OnClick {
    int[] value();
    String[] idStr();
}
