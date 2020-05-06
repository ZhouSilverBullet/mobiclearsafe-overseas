package com.mobi.overseas.clearsafe.ui.powercontrol.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mobi.overseas.clearsafe.R;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/21 21:20
 * @Dec 略
 */
public class BatteryView extends RelativeLayout {
    private ProgressBar mProgressBar;
    private ImageView ivFlash;
    private int mProgress;
    private ValueAnimator mValueAnimator;

    public BatteryView(Context paramContext) {
        super(paramContext);
        init();
    }

    public BatteryView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public BatteryView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private void init() {
        View localView = inflate(getContext(), R.layout.battery_view_layout, this);
        this.mProgressBar = ((ProgressBar) localView.findViewById(R.id.battery_level));
        this.ivFlash = ((ImageView) localView.findViewById(R.id.ivFlash));
        this.ivFlash.setVisibility(View.GONE);
        this.mProgressBar.setProgress(50);
    }

    private void cancelAnim() {
        if ((this.mValueAnimator != null) && (this.mValueAnimator.isRunning())) {
            this.mValueAnimator.cancel();
            this.mValueAnimator = null;
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnim();
    }

    public void setChargeState(int state) {
        switch (state) {
            default:
                break;
            case 2:
                this.ivFlash.setVisibility(GONE);
                cancelAnim();
                setProgress(this.mProgress);
                return;
        }
        this.ivFlash.setVisibility(VISIBLE);
        if ((this.mProgress != 100) && ((this.mValueAnimator == null) ||
                (!this.mValueAnimator.isRunning())) && (this.mValueAnimator == null)) {
            this.mValueAnimator = ValueAnimator.ofInt(mProgress, 100);
            this.mValueAnimator.setDuration(2000L);
            this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public final void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator) {
                    int i = (int) paramAnonymousValueAnimator.getAnimatedValue();
                    BatteryView.this.setProgress(i);
                }
            });
            this.mValueAnimator.setRepeatCount(-1);
            this.mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
            this.mValueAnimator.start();
        }
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        this.mProgressBar.setProgress(progress);
    }


}
