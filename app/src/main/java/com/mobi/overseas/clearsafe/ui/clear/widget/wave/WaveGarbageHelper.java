package com.mobi.overseas.clearsafe.ui.clear.widget.wave;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.mobi.overseas.clearsafe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/26 15:31
 * @Dec 略
 */
public class WaveGarbageHelper {
    public static final String TAG = "WaveGarbageHelper";

    private WaveView mWaveView;
    private long waterLevelRatioDuration;
    private float waterLevelRatio;

    private AnimatorSet mAnimatorSet;
    private Runnable delayRunnable;

    public WaveGarbageHelper(WaveView waveView) {
        this(waveView, 8000, 0.3f);
    }

    public WaveGarbageHelper(WaveView waveView, long waterLevelRatioDuration, float waterLevelRatio) {
        mWaveView = waveView;
        this.waterLevelRatioDuration = waterLevelRatioDuration;
        this.waterLevelRatio = waterLevelRatio;
        initAnimation();
    }


    public void start() {
        mWaveView.setShowWave(true);
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();

        // horizontal animation.
        // wave waves infinitely.
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveShiftAnim);

        // vertical animation.
        // water level increases from 0 to center of WaveView
        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(
                mWaveView, "waterLevelRatio", 0f, waterLevelRatio);
        waterLevelAnim.setDuration(waterLevelRatioDuration);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        animators.add(waterLevelAnim);

        // amplitude animation.
        // wave grows big then grows small, repeatedly
        ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(
                mWaveView, "amplitudeRatio", 0.0001f, 0.05f);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(5000);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        animators.add(amplitudeAnim);


        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);

        delayRunnable = new Runnable() {
            @Override
            public void run() {
                transparentAnim();
            }
        };

//        mWaveView.postDelayed(delayRunnable, 5000);
    }

    public void transparentAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mWaveView.getFrontWaveColor(), 0x00FFFFFF);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            mWaveView.setWaveColor(0x00ffffff, animatedValue);
            if (0x00ffffff == animatedValue) {
                Log.e(TAG, "animatedValue: " + animatedValue);
                mWaveView.setVisibility(View.GONE);
            }
        });


        valueAnimator.start();
    }

    public void cancel() {
        if (mAnimatorSet != null) {
//            mAnimatorSet.cancel();
            mAnimatorSet.end();
        }
        //清除
        if (mWaveView != null) {
            mWaveView.removeCallbacks(delayRunnable);
        }
    }
}
