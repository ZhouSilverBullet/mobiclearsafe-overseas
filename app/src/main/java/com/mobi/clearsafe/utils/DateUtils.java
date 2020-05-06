package com.mobi.clearsafe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * author:zhaijinlu
 * date: 2019/10/29
 * desc: 时间工具类（时间格式转换方便类）
 */
public class DateUtils {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat();

    /**
     * 返回一定格式的当前时间
     *
     * @param pattern "yyyy-MM-dd HH:mm:ss E"
     * @return
     */
    public static String getCurrentDate(String pattern) {
        SIMPLE_DATE_FORMAT.applyPattern(pattern);
        Date date = new Date(System.currentTimeMillis());
        String dateString = SIMPLE_DATE_FORMAT.format(date);
        return dateString;

    }

    public static long getDateMillis(String dateString, String pattern) {
        long millionSeconds = 0;
        SIMPLE_DATE_FORMAT.applyPattern(pattern);
        try {
            millionSeconds = SIMPLE_DATE_FORMAT.parse(dateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }// 毫秒

        return millionSeconds;
    }

    /**
     * 格式化输入的millis
     *
     * @param millis
     * @param pattern yyyy-MM-dd HH:mm:ss E
     * @return
     */
    public static String dateFormat(long millis, String pattern) {
        Date d = new Date(millis * 1000);
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(d);
    }

    /**
     * 传入时间戳 转换成 指定时间格式
     * @param millis
     * @param pattern
     * @return
     */
    public static String longToTimeStr(long millis,String pattern){
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(new Date(millis));
    }

    /**
     * 将dateString原来old格式转换成new格式
     *
     * @param dateString
     * @param oldPattern yyyy-MM-dd HH:mm:ss E
     * @param newPattern
     * @return oldPattern和dateString形式不一样直接返回dateString
     */
    public static String dateFormat(String dateString, String oldPattern,
                                    String newPattern) {
        long millis = getDateMillis(dateString, oldPattern);
        if (0 == millis) {
            return dateString;
        }
        String date = dateFormat(millis, newPattern);
        return date;
    }

    /**
     * 分钟数 转换为小时+分钟数时间格式 返回map
     * key->hour 小时  key->min 分钟
     *
     * @param minutes
     * @return
     */
    public static HashMap<String, String> Min2Hours(int minutes) {
        int hours = minutes / 60;
        int mins = minutes % 60;
        HashMap<String, String> data = new HashMap<>();
        if (hours < 10) {
            data.put("hour", "0" + hours);
        } else {
            data.put("hour", hours + "");
        }
        if (mins < 10) {
            data.put("min", "0" + mins);
        } else {
            data.put("min", mins + "");
        }

        return data;
    }

    /**
     * 获取当前时间 截止到天
     *
     * @return
     */
    public static String getStringDateDay() {
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(currentTime);
        return dateString;
    }

    /**
     * 获取当前时间 截止到分
     *
     * @return
     */
    public static String getStringDateMin() {
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = format.format(currentTime);
        return dateString;
    }

    /**
     * 截取服务器返回时间
     *
     * @param dateString
     * @return
     */
    public static String getGoTimeString(String dateString) {
        String date = dateString.substring(5, 10);
        return date;
    }

    /**
     * 秒数转 倒计时格式时间
     *
     * @return
     */
    public static String SecondToDate(int second) {

        if (second <= 0) {
            return "00:00:00";
        }
        int hour = 0;//小时
        int min = second / 60;//分钟
        int sec = second % 60;//秒
        if (min >= 60) {
            hour = min / 60;
            min = min % 60;
        }
        String hourS = "";
        String minS = "";
        String SecS = "";
        if (hour >= 10) {
            hourS = String.valueOf(hour);
        } else {
            hourS = "0" + hour;
        }
        if (min >= 10) {
            minS = String.valueOf(min);
        } else {
            minS = "0" + min;
        }
        if (sec >= 10) {
            SecS = String.valueOf(sec);
        } else {
            SecS = "0" + sec;
        }
        return hourS + ":" + minS + ":" + SecS;
    }

}
