package com.mobi.overseas.clearsafe.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

/**
 * author : liangning
 * date : 2019-11-28  17:31
 */
public class AnimationUtil {

    /**
     * 移动动画  带回弹效果
     *
     * @param X             起始点的 X      0.0f
     * @param toX           终点的 X      0.0f
     * @param Y             起始点的 Y      0.0f
     * @param toY           终点的 Y      0.0f
     * @param AnimationTime 动画执行的时间
     * @param DelayTime     延迟的时间
     * @param RepeatCount   重复次数 0为不重复  <0为无限重复 大于0 重复执行几次
     * @return
     */
    public static Animation getTranslateAnimation(float X, float toX, float Y, float toY, long AnimationTime, long DelayTime,int RepeatCount) {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, X, // 起始点x
                Animation.RELATIVE_TO_SELF, toX, // 终点x
                Animation.RELATIVE_TO_SELF, Y,// 起始点y
                Animation.RELATIVE_TO_SELF, toY);// 终点y
        animation.setInterpolator(new BounceInterpolator());
        animation.setDuration(AnimationTime);
        animation.setRepeatCount(-1);//设置重复次数（缺省为0  表示不重复）
        animation.setRepeatMode(ValueAnimator.REVERSE);// 设置重复模式(RESTART或REVERSE),重复次数大于0或INFINITE生效
        animation.setStartOffset(DelayTime);//设置动画的延迟时间
        return animation;

    }
    /**
     * 移动动画  不带回弹效果
     *
     * @param X
     * @param toX
     * @param Y
     * @param toY
     * @param AnimationTime
     * @return
     */

    public static Animation MoveTranslateAnimation(float X, float toX, float Y, float toY, int AnimationTime) {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, X, // 起始点x
                Animation.RELATIVE_TO_SELF, toX, // 终点x
                Animation.RELATIVE_TO_SELF, Y,// 起始点y
                Animation.RELATIVE_TO_SELF, toY);// 终点y
        // 设置动画插值器 被用来修饰动画效果,定义动画的变化率
        animation.setInterpolator(new DecelerateInterpolator());
        // 设置动画执行时间
        animation.setDuration(AnimationTime);
        return animation;

    }

    /**
     * 移动动画  不带回弹效果 带延迟时间
     *
     * @param X
     * @param toX
     * @param Y
     * @param toY
     * @param AnimationTime
     * @return
     */

    public static Animation MoveTranslateAnimationDelayTime(float X, float toX, float Y, float toY, long AnimationTime, long DelayTime) {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, X, // 起始点x
                Animation.RELATIVE_TO_SELF, toX, // 终点x
                Animation.RELATIVE_TO_SELF, Y,// 起始点y
                Animation.RELATIVE_TO_SELF, toY);// 终点y
        // 设置动画插值器 被用来修饰动画效果,定义动画的变化率
        animation.setInterpolator(new DecelerateInterpolator());
        // 设置动画执行时间
        animation.setDuration(AnimationTime);
        animation.setStartOffset(DelayTime);//设置动画的延迟时间
        return animation;

    }

    /**
     * 淡入淡出动画
     *
     * @param fromAlpha 起始的透明图
     * @param toAlpha   结束时的透明度
     * @return
     */

    public static Animation ViewAlphaAnimation(float fromAlpha, float toAlpha) {
        AlphaAnimation animation = new AlphaAnimation(fromAlpha, toAlpha);
        // 设置动画执行时间
        animation.setDuration(500);
        return animation;
    }

    public static void BounceTopEnter(View view) {
        view.setAlpha(1);
        view.setScaleX(1);
        view.setScaleY(1);
        view.setTranslationX(0);
        view.setTranslationY(0);
        view.setRotation(0);
        view.setRotationY(0);
        view.setRotationX(0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        Interpolator interpolator = null;
        if (interpolator != null) {
            animatorSet.setInterpolator(interpolator);
        }


        DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "translationY", -250 * dm.density, 30, -10, 0));
        animatorSet.start();

    }


    /**
     * 给view设置简单的触摸反馈
     *
     * @param view
     */
    public static void viewSetClickAnimation(final View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.setAlpha(0.6f);
                        break;
                    case MotionEvent.ACTION_UP:
                        view.setAlpha(1f);
                        break;
                }
                return false;
            }
        });
    }


    /**
     * 平移动+淡入
     *
     * @param X
     * @param toX
     * @param Y
     * @param toY
     * @param alphaInitial  淡入 开始
     * @param apphaTo       淡入结束
     * @param AnimationTime
     * @param DelayTime
     * @param interpolator
     * @return
     */
    public static AnimationSet viewAnimationSet(float X, float toX, float Y, float toY, float alphaInitial, float apphaTo, long AnimationTime, long DelayTime, Interpolator interpolator) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, X, // 起始点x
                Animation.RELATIVE_TO_SELF, toX, // 终点x
                Animation.RELATIVE_TO_SELF, Y,// 起始点y
                Animation.RELATIVE_TO_SELF, toY);// 终点y
        animation.setDuration(AnimationTime);
        animation.setRepeatCount(0);//设置重复次数（缺省为0  表示不重复）
        animation.setStartOffset(DelayTime);//设置动画的延迟时间
        animationSet.addAnimation(animation);
        if (null != interpolator) {
            animationSet.setInterpolator(interpolator);
        }

        AlphaAnimation alphaAnimation = new AlphaAnimation(alphaInitial, apphaTo);
        alphaAnimation.setDuration(AnimationTime);
        alphaAnimation.setStartOffset(DelayTime);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;

    }
}
