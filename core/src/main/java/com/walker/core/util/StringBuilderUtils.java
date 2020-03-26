package com.walker.core.util;

import java.util.ArrayList;

/**
 * @author Walker
 * @date on 2018/4/17 0017 上午 10:42
 * @email feitianwumu@163.com
 * @desc 字符串工具类
 */
public class StringBuilderUtils {
    /**
     * 拼接字符串
     *
     * @param val_1 拼接目标1
     * @param val_2 拼接目标2
     * @return String
     */
    public static String pliceStr(Object val_1, Object val_2) {
        StringBuilder builder = new StringBuilder();
        builder.append(val_1).append(val_2);
        String result = builder.toString().trim();
        return result;
    }

    /**
     * 拼接字符串
     *
     * @param val_1 拼接目标1
     * @param val_2 拼接目标2
     * @param val_3 拼接目标3
     * @return String
     */
    public static String pliceStr(Object val_1, Object val_2, Object val_3) {
        StringBuilder builder = new StringBuilder();
        builder.append(val_1).append(val_2).append(val_3);
        String result = builder.toString().trim();
        return result;
    }

    /**
     * 拼接字符串
     *
     * @param val_1 拼接目标1
     * @param val_2 拼接目标2
     * @param val_3 拼接目标3
     * @param val_4 拼接目标4
     * @return String
     */
    public static String pliceStr(Object val_1, Object val_2, Object val_3, Object val_4) {
        StringBuilder builder = new StringBuilder();
        builder.append(val_1).append(val_2).append(val_3).append(val_4);
        String result = builder.toString().trim();
        return result;
    }

    /**
     * 拼接字符串
     *
     * @param val_1 拼接目标1
     * @param val_2 拼接目标2
     * @param val_3 拼接目标3
     * @param val_4 拼接目标4
     * @param val_5 拼接目标5
     * @return String
     */
    public static String pliceStr(Object val_1, Object val_2, Object val_3, Object val_4, Object val_5) {
        StringBuilder builder = new StringBuilder();
        builder.append(val_1).append(val_2).append(val_3).append(val_4).append(val_5);
        String result = builder.toString().trim();
        return result;
    }

    /**
     * 拼接字符串
     *
     * @param vallist 拼接集合
     * @return String
     */
    public static String pliceList(ArrayList<Object> valList) {
        StringBuilder builder = new StringBuilder();
        for (Object val : valList) {
            builder.append(val);
        }
        String result = builder.toString().trim();
        valList.clear();
        return result;
    }

    /**
     * 拼接N个字符(基于变长变量)
     *
     * @param values n个字符集合
     * @return 目标字符
     */
    public static String plicePlus(String... values) {
        StringBuilder builder = new StringBuilder();
        for (String val : values) {
            builder.append(val);
        }
        return builder.toString();
    }
}
