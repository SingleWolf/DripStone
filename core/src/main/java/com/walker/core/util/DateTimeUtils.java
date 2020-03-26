package com.walker.core.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Walker
 * @date on 2018/4/17 0017 上午 10:53
 * @email feitianwumu@163.com
 * @desc 时间日期辅助类
 */
public class DateTimeUtils {
    private static final String TAG = "DateTimeUtils";

    private static final String FORMAT_1 = "yyyyMMddHHmmss";
    private static final String FORMAT_2 = "yyyy/MM/dd HH:mm:ss";
    private static final String FORMAT_3 = "yyyy-MM-dd HH:mm:ss";
    private static final String FORMAT_4 = "yyyy年MM月dd日 HH时mm分ss秒";
    private static final String FORMAT_5 = "yyyy/MM/dd";
    private static final String FORMAT_6 = "yyyy-MM-dd";
    private static final String FORMAT_7 = "yyyy年MM月dd日";
    private static final String FORMAT_8 = "HH:mm:ss";
    private static final String FORMAT_9 = "HH时mm分ss秒";
    private static final String FORMAT_10 = "yyyy";
    private static final String FORMAT_11 = "MM";
    private static final String FORMAT_12 = "dd";
    private static final String FORMAT_13 = "HH";

    public enum DateFormat {
        NORMAL, ALL_1, ALL_2, ALL_3, DAY_1, DAY_2, DAY_3, MIN_1, MIN_2, ONLY_YEAR, ONLY_MONTH, ONLY_DAY, ONLY_HOUR
    }

    /**
     * 时间格式
     *
     * @param format DateFormat
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getDateFormat(DateFormat format) {
        switch (format) {
            case NORMAL:
                return new SimpleDateFormat(FORMAT_1);
            case ALL_1:
                return new SimpleDateFormat(FORMAT_2);
            case ALL_2:
                return new SimpleDateFormat(FORMAT_3);
            case ALL_3:
                return new SimpleDateFormat(FORMAT_4);
            case DAY_1:
                return new SimpleDateFormat(FORMAT_5);
            case DAY_2:
                return new SimpleDateFormat(FORMAT_6);
            case DAY_3:
                return new SimpleDateFormat(FORMAT_7);
            case MIN_1:
                return new SimpleDateFormat(FORMAT_8);
            case MIN_2:
                return new SimpleDateFormat(FORMAT_9);
            case ONLY_YEAR:
                return new SimpleDateFormat(FORMAT_10);
            case ONLY_MONTH:
                return new SimpleDateFormat(FORMAT_11);
            case ONLY_DAY:
                return new SimpleDateFormat(FORMAT_12);
            case ONLY_HOUR:
                return new SimpleDateFormat(FORMAT_13);
        }
        return new SimpleDateFormat(FORMAT_1);
    }

    /**
     * 获取通用的时间（精确到秒）
     *
     * @return 通用的时间
     */
    public static String getNormalDate() {
        return getDate2String(DateFormat.NORMAL);
    }

    /**
     * 获取明文时间
     *
     * @param format 目标格式
     * @return 字符串
     */
    public static String getDate2String(DateFormat format) {
        String time = "";
        try {
            SimpleDateFormat sf = getDateFormat(format);
            Date date = new Date();
            time = sf.format(date);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return time;
    }

    /**
     * 获取当前时间点
     *
     * @return 当前时间的long型值
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取制定点的时间戳
     *
     * @param source 指定时间
     * @param format 时间格式
     * @return long型时间戳
     */
    public static long getTimeStamp(String source, DateFormat format) {
        long timestamp;
        SimpleDateFormat df = getDateFormat(format);
        try {
            Date date = df.parse(source);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            timestamp = cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            timestamp = 0;
        }
        return timestamp;
    }

    /**
     * 获取制定点的时间戳
     *
     * @param source 指定时间
     * @return long型时间戳
     */
    public static long getTimeStamp(String source) {
        return getTimeStamp(source, DateFormat.NORMAL);
    }

    /**
     * 判断是否是相同一天
     *
     * @param timestamp1 比对时间戳1
     * @param timestamp2 比对时间戳2
     * @return 布尔结果
     */
    public static boolean isSameDay(final long timestamp1, final long timestamp2) {
        final Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date(timestamp1));

        final Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date(timestamp2));

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }
}
