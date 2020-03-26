package com.walker.core.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @date on 2018/4/11 0011 下午 17:35
 * @author Walker
 * @email feitianwumu@163.com
 * @desc  活动管理器
 */
public class ActivityCollector {
    /**
     * 所有开启活动的集合
     */
    private static List<Activity> sActivityList = new ArrayList<Activity>();

    /**
     * 获取活动集合
     *
     * @return List<Activity>
     */
    public static List<Activity> listActivity() {
        return sActivityList;
    }

    /**
     * 添加新开启的活动
     *
     * @param activity 目标活动
     */
    public static void addActivity(Activity activity) {
        sActivityList.add(activity);
    }

    /**
     * 移出活动
     *
     * @param activity 目标活动
     */
    public static void removeActivity(Activity activity) {
        sActivityList.remove(activity);
    }

    /**
     * 终止所有活动
     */
    public static void finishAll() {
        for (Activity activity : sActivityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 判断活动栈是否为空
     *
     * @return boolean
     */
    public static boolean isEmpty() {
        return sActivityList.isEmpty();
    }
}