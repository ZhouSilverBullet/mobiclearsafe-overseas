package com.mobi.clearsafe.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 数据计算工具类
 * author : liangning
 * date : 2019-11-02  11:10
 */
public class DataUtils {

    /**
     * 金币数量 转换计算为rmb
     *
     * @param gold
     * @return
     */
    public static float Gold2Money(int gold) {
        float money = 0.0f;
        float fmoney = gold / 10000f;
        BigDecimal bd = new BigDecimal(fmoney);
        BigDecimal bd2 = bd.setScale(2, BigDecimal.ROUND_DOWN);
        money = bd2.floatValue();
        return money;
    }

    /**
     * 获得小数点后n位的方法 四舍五入
     *
     * @param value float值
     * @param digit 位数
     * @return
     */
    public static float getDotFloat(float value, int digit) {
        int d = (int) Math.pow(10, digit);
        return (float) (Math.round(value * d)) / d;
    }

    /**
     * 分转成人民币 元 并去掉无用的0
     *
     * @param fen
     * @return
     */
    public static String Fen2Yuan(int fen) {
        float yuan = getDotFloat(fen / 100f, 1);
        NumberFormat nf = NumberFormat.getInstance();
        String yuanS = nf.format(yuan);
        return yuanS;
    }


    /**
     * 将包名中的.转换成_ 并生成 apk文件名
     *
     * @return
     */
    public static String PkgPointTo_(String pkgName) {
        return pkgName.replace(".", "_")+".apk";

    }


}
