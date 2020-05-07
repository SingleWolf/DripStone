package com.walker.study.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Walker
 *
 * @Date   2020-05-07 13:39
 *
 * @Summary OnLongClick
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventType(listenerType = View.OnLongClickListener.class ,listenerSetter = "setOnLongClickListener")
public @interface OnLongClick {
    int[] value();
    String[] idStr();
}
