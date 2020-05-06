package com.mobi.clearsafe.ui.clear.control;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;

import com.mobi.clearsafe.utils.UiUtils;

public class ScanAnimatorContainer {
    public static final String TAG = "ScanAnimatorContainer";

    private final View view;
    private ObjectAnimator translationUpToDown;
    private ObjectAnimator translationDownToUp;

    public ScanAnimatorContainer(View view) {
        this.view = view;
    }

    public void startAnimator() {
        view.setVisibility(View.VISIBLE);

        int dp200 = UiUtils.dp2px(view.getContext(), 300);

        translationUpToDown = ObjectAnimator.ofFloat(view, "TranslationY", 0, dp200);
        translationUpToDown.setDuration(1200);
        translationUpToDown.setInterpolator(new FastOutSlowInInterpolator());
        translationUpToDown.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setRotation(180);
                if (translationDownToUp != null) {
                    translationDownToUp.start();
                }
                Log.e(TAG, "translationUpToDown onAnimationEnd");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        translationUpToDown.start();

        translationDownToUp = ObjectAnimator.ofFloat(view, "TranslationY", dp200, 0);
        translationDownToUp.setDuration(1200);
        translationUpToDown.setInterpolator(new FastOutSlowInInterpolator());
        translationDownToUp.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setRotation(0);
                if (translationUpToDown != null) {
                    translationUpToDown.start();
                }
                Log.e(TAG, "translationDownToUp onAnimationEnd");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void stop() {
        translationUpToDown.removeAllListeners();
        translationDownToUp.removeAllListeners();
        translationUpToDown.cancel();
        translationDownToUp.cancel();
        view.setVisibility(View.GONE);
    }

}
