package com.lgp.webmanager.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: a247292980
 * Date: 2017/08/14
 * <p>
 * 日期工具类
 **/
public class DateUtil {
    // 1分钟
    private final static long MINUTE = 60 * 1000;
    // 1小时
    private final static long HOUR = 60 * MINUTE;
    // 1天
    private final static long DAY = 24 * HOUR;
    // 月
    private final static long MONTH = 31 * DAY;
    // 年
    private final static long YEAR = 12 * MONTH;

    public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmssSSS";

    public static String getDateSequence() {
        return new SimpleDateFormat(YYYYMMDDHHMMSS).format(new Date());
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }


    public static String getTimeFormatText(Long date) {
        if (date == null) {
            return null;
        }
        long diff = System.currentTimeMillis() - date;
        long r = 0;
        if (diff > YEAR) {
            r = (diff / YEAR);
            return r + "年前";
        }
        if (diff > MONTH) {
            r = (diff / MONTH);
            return r + "个月前";
        }
        if (diff > DAY) {
            r = (diff / DAY);
            return r + "天前";
        }
        if (diff > HOUR) {
            r = (diff / HOUR);
            return r + "个小时前";
        }
        if (diff > MINUTE) {
            r = (diff / MINUTE);
            return r + "分钟前";
        }
        return "刚刚";
    }

    /**
     * 将时间戳转换成当天0点
     */
    public static long getDayBegin(long timestamp) {
        String format = "yyyy-MM-DD";
        String toDayString = new SimpleDateFormat(format).format(new Date(timestamp));
        Date toDay = null;
        try {
            toDay = org.apache.commons.lang3.time.DateUtils.parseDate(toDayString, new String[]{format});

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return toDay.getTime();
    }

    /**
     * 获取一个月之前的时间戳
     */
    public static long getLastMonthTime() {
        return getDayBegin(getCurrentTime()) - 86400000L * 30;
    }

}
