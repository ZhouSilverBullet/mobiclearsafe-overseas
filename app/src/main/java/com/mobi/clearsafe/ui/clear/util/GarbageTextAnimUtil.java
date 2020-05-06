package com.mobi.clearsafe.ui.clear.util;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/8 14:04
 * @Dec 略
 */
public class GarbageTextAnimUtil {
    /**
     * @param tv         TextView
     * @param startValue
     * @param endValue   最终显示的值
     *                   todo 这个应该还存在造大量动画对象问题
     */
    public static void addTextViewAddAnim(TextView tv, float startValue, float endValue) {
//        if (startValue > endValue) {
//            float tmp = startValue;
//            startValue = endValue;
//            endValue = tmp;
//        }

        //取消后面的动画
        if (tv != null) {
            if (tv.getTag() instanceof ValueAnimator) {
                ((ValueAnimator) tv.getTag()).removeAllUpdateListeners();
                ((ValueAnimator) tv.getTag()).cancel();
            }
        }

        //kb -> MB -> GB 会有小数点变化
        if (endValue < 10f && startValue >= 10f) {
            startValue = endValue;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startValue, endValue);
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            float animatedValue = (float) (((int) (value * 100)) / 100.0);
            if (animatedValue < 0) {
                animatedValue = 0;
            }
            if (tv != null) {
                tv.setText(String.valueOf(animatedValue));
            }
        });
        valueAnimator.start();

        if (tv != null) {
            tv.setTag(valueAnimator);
        }

    }

    /**
     * @param tv         TextView
     * @param startValue
     * @param endValue   最终显示的值
     *
     */
    public static void addTextViewAddAnim(TextView tv, int startValue, int endValue, long duration) {

        //取消后面的动画
        if (tv != null) {
            if (tv.getTag() instanceof ValueAnimator) {
                ((ValueAnimator) tv.getTag()).removeAllUpdateListeners();
                ((ValueAnimator) tv.getTag()).cancel();
            }
        }

        ValueAnimator valueAnimator = ValueAnimator.ofInt(startValue, endValue);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            if (tv != null) {
                tv.setText(String.valueOf(value));
            }
        });
        valueAnimator.start();

        if (tv != null) {
            tv.setTag(valueAnimator);
        }

    }
}
