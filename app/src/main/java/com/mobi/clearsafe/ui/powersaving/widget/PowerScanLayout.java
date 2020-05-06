package com.mobi.clearsafe.ui.powersaving.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.util.TypefaceUtil;
import com.mobi.clearsafe.utils.UiUtils;
import com.mobi.clearsafe.widget.NoPaddingTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/15 14:34
 * @Dec 略
 */
public class PowerScanLayout extends LinearLayout {
    @BindView(R.id.ivScannerBg)
    ImageView ivScannerBg;
    @BindView(R.id.ivScanner)
    ImageView ivScanner;
    @BindView(R.id.clLayout)
    CircleLayout clLayout;
    @BindView(R.id.tvProgressNum)
    NoPaddingTextView tvProgressNum;
    @BindView(R.id.llProgress)
    LinearLayout llProgress;
    @BindView(R.id.tvDec)
    TextView tvDec;
    @BindView(R.id.ivIcon1)
    ImageView ivIcon1;
    @BindView(R.id.ivIcon6)
    ImageView ivIcon6;
    @BindView(R.id.pb)
    ProgressBar pb;

    public PowerScanLayout(Context context) {
        this(context, null);
    }

    public PowerScanLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PowerScanLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.power_scan_layout, this, true);
        ButterKnife.bind(this);

        Typeface mtypeface = TypefaceUtil.getTypeFace(getContext(), "hyt.ttf");
        tvProgressNum.setTypeface(mtypeface);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        ivScannerBg.setScaleX(0);
        ivScannerBg.setScaleY(0);

        ivScanner.setScaleX(0);
        ivScanner.setScaleY(0);


        post(new Runnable() {
            @Override
            public void run() {
                ivScannerBgAnim();
            }
        });
    }

    private void ivScannerBgAnim() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivScannerBg, "ScaleX", 0f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivScannerBg, "ScaleY", 0f, 1.2f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ivScannerAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void ivScannerAnim() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivScanner, "ScaleX", 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivScanner, "ScaleY", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(500);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                ivScannerAnim();
                ivScannerRotationAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();

    }

    private void ivScannerRotationAnim() {

        ivScanner.setPivotX(UiUtils.dp2px(getContext(), 5));
        ivScanner.setPivotY(UiUtils.dp2px(getContext(), 5));

        ObjectAnimator rotation = ObjectAnimator.ofFloat(ivScanner, View.ROTATION, 0f, 360f);
        rotation.setRepeatMode(ValueAnimator.RESTART);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setRepeatCount(10000);
        rotation.setDuration(500);
        rotation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                ivScannerAnim();
//                ivScannerRotationAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        rotation.start();
    }


    public void setTwoDrawable(Drawable drawable1, Drawable drawable2) {
        ivIcon1.setImageDrawable(drawable1);
        ivIcon6.setImageDrawable(drawable2);
    }

    public void setProgress(int progress) {
        tvProgressNum.setText(String.valueOf(progress));
        pb.setProgress(progress);
    }
}
