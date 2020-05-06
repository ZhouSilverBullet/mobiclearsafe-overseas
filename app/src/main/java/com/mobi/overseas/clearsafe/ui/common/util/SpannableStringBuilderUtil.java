package com.mobi.overseas.clearsafe.ui.common.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/2 17:10
 * @Dec 略
 */
public class SpannableStringBuilderUtil {
    final static int blueColor = Color.parseColor("#0062ff");
    final static int grayColor = Color.parseColor("#999999");

    final static int redColor = Color.parseColor("#FF3737");
    final static int s66Color = Color.parseColor("#666666");

    public static SpannableStringBuilder getSafeDayStr(int count) {
        //已获得红包：130 金币
        String startStr = "快清理已守护您";
        String centerStr = String.valueOf(count);
        String endStr = "天";

        SpannableStringBuilder ssb = new SpannableStringBuilder(startStr).append(centerStr).append(endStr);
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(grayColor);
        ForegroundColorSpan orangeSpan = new ForegroundColorSpan(blueColor);
        ForegroundColorSpan whiteSpan2 = new ForegroundColorSpan(grayColor);
        ssb.setSpan(whiteSpan, 0, startStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(orangeSpan, startStr.length(), centerStr.length() + startStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(whiteSpan2, centerStr.length() + startStr.length(), endStr.length() + centerStr.length() + startStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ssb;
    }

    public static SpannableStringBuilder getRedStr(String start, String end, int count) {
        //已获得红包：130 金币
        String startStr = TextUtils.isEmpty(start) ? "" : start;
        String centerStr = String.valueOf(count);
        String endStr = TextUtils.isEmpty(end) ? "" : end;

        SpannableStringBuilder ssb = new SpannableStringBuilder(startStr).append(centerStr).append(endStr);
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(s66Color);
        ForegroundColorSpan orangeSpan = new ForegroundColorSpan(redColor);
        ForegroundColorSpan whiteSpan2 = new ForegroundColorSpan(s66Color);
        ssb.setSpan(whiteSpan, 0, startStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(orangeSpan, startStr.length(), centerStr.length() + startStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(whiteSpan2, centerStr.length() + startStr.length(), endStr.length() + centerStr.length() + startStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ssb;
    }

    public static SpannableStringBuilder getRedStr(String start, String end, String count) {
        //已获得红包：130 金币
        String startStr = TextUtils.isEmpty(start) ? "" : start;
        String centerStr = TextUtils.isEmpty(count) ? "" : count;
        String endStr = TextUtils.isEmpty(end) ? "" : end;

        SpannableStringBuilder ssb = new SpannableStringBuilder(startStr).append(centerStr).append(endStr);
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(s66Color);
        ForegroundColorSpan orangeSpan = new ForegroundColorSpan(redColor);
        ForegroundColorSpan whiteSpan2 = new ForegroundColorSpan(s66Color);
        ssb.setSpan(whiteSpan, 0, startStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(orangeSpan, startStr.length(), centerStr.length() + startStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(whiteSpan2, centerStr.length() + startStr.length(), endStr.length() + centerStr.length() + startStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ssb;
    }

    private static String getNotNullStr(String str) {
        return TextUtils.isEmpty(str) ? "" : str;
    }

    public static void handlePowerLast(TextView textView, int m) {
        int n = m / 60;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder("充满还需 ");
        int i2 = ((SpannableStringBuilder) stringBuilder).length();
        ((SpannableStringBuilder) stringBuilder).setSpan(new ForegroundColorSpan(Color.parseColor("#CCffffff")), 0, i2, 33);
        ((SpannableStringBuilder) stringBuilder).append(String.valueOf(n));
        ((SpannableStringBuilder) stringBuilder).setSpan(new StyleSpan(1), i2, ((SpannableStringBuilder) stringBuilder).length(), 33);
        ((SpannableStringBuilder) stringBuilder).setSpan(new AbsoluteSizeSpan(18, true), i2, ((SpannableStringBuilder) stringBuilder).length(), 33);
        n = ((SpannableStringBuilder) stringBuilder).length();
        ((SpannableStringBuilder) stringBuilder).append(" 小时 ");
        ((SpannableStringBuilder) stringBuilder).setSpan(new ForegroundColorSpan(Color.parseColor("#CCffffff")), n, ((SpannableStringBuilder) stringBuilder).length(), 33);
        n = ((SpannableStringBuilder) stringBuilder).length();
        ((SpannableStringBuilder) stringBuilder).append(String.valueOf(m % 60));
        ((SpannableStringBuilder) stringBuilder).setSpan(new StyleSpan(1), n, ((SpannableStringBuilder) stringBuilder).length(), 33);
        ((SpannableStringBuilder) stringBuilder).setSpan(new AbsoluteSizeSpan(18, true), n, ((SpannableStringBuilder) stringBuilder).length(), 33);
        m = ((SpannableStringBuilder) stringBuilder).length();
        ((SpannableStringBuilder) stringBuilder).append(" 分");
        ((SpannableStringBuilder) stringBuilder).setSpan(new ForegroundColorSpan(Color.parseColor("#CCffffff")), m, ((SpannableStringBuilder) stringBuilder).length(), 33);
        textView.setText(stringBuilder);
    }

    public static void handlePowerUse(TextView textView, int m) {
        int n = m / 60;
        SpannableStringBuilder localObject1 = new SpannableStringBuilder("预计可用 ");
        int i2 = ((SpannableStringBuilder) localObject1).length();
        ((SpannableStringBuilder) localObject1).setSpan(new ForegroundColorSpan(Color.parseColor("#CCffffff")), 0, i2, 33);
        ((SpannableStringBuilder) localObject1).append(String.valueOf(n));
        ((SpannableStringBuilder) localObject1).setSpan(new StyleSpan(1), i2, ((SpannableStringBuilder) localObject1).length(), 33);
        ((SpannableStringBuilder) localObject1).setSpan(new AbsoluteSizeSpan(18, true), i2, ((SpannableStringBuilder) localObject1).length(), 33);
        n = ((SpannableStringBuilder) localObject1).length();
        ((SpannableStringBuilder) localObject1).append(" 小时 ");
        ((SpannableStringBuilder) localObject1).setSpan(new ForegroundColorSpan(Color.parseColor("#CCffffff")), n, ((SpannableStringBuilder) localObject1).length(), 33);
        n = ((SpannableStringBuilder) localObject1).length();
        ((SpannableStringBuilder) localObject1).append(String.valueOf(m % 60));
        ((SpannableStringBuilder) localObject1).setSpan(new StyleSpan(1), n, ((SpannableStringBuilder) localObject1).length(), 33);
        ((SpannableStringBuilder) localObject1).setSpan(new AbsoluteSizeSpan(18, true), n, ((SpannableStringBuilder) localObject1).length(), 33);
        m = ((SpannableStringBuilder) localObject1).length();
        ((SpannableStringBuilder) localObject1).append(" 分");
        ((SpannableStringBuilder) localObject1).setSpan(new ForegroundColorSpan(Color.parseColor("#CCffffff")), m, ((SpannableStringBuilder) localObject1).length(), 33);
        textView.setText(localObject1);
    }
}
