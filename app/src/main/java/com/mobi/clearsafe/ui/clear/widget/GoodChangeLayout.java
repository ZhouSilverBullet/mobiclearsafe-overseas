package com.mobi.clearsafe.ui.clear.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adtest.fullscreenvideo.FullScreenVideoAD;
import com.example.adtest.fullscreenvideo.FullScreenVideoListener;
import com.example.adtest.manager.AdScenario;
import com.example.adtest.manager.Constants;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.example.adtest.rewardvideo.RewardVideoLoadListener;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.clear.ad.ADGoodChangeShow;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.utils.UiUtils;
import com.umeng.commonsdk.debug.I;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/23 10:09
 * @Dec 略
 */
public class GoodChangeLayout extends LinearLayout {

    private ValueAnimator valueAnimator;
    private ValueAnimator rightAnimator;
    private ValueAnimator tvAnimator;
    private ValueAnimator tvAnimator2;
    private ValueAnimator rightAnimator2;
    private int currentHeight;
    private TextView tvDec;
    private TextView tvDec2;
    private ImageView ivIcon;
    private int dp220;
    private String mTvValue;
    private String mTvValue2;

    //当第一次就是好的，不弹广告了
    private volatile boolean mIsFirstGood;
    private boolean isInit;
    private boolean mIsGarbageGoodShowAd;
    private ViewGroup mAddRootView;

    public GoodChangeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.view_good_change_layout, this);

        tvDec = findViewById(R.id.tvDec);
        tvDec2 = findViewById(R.id.tvDec2);
        ivIcon = findViewById(R.id.ivIcon);

        currentHeight = getResources().getDisplayMetrics().heightPixels;

        dp220 = UiUtils.dp2px(getContext(), 220);

//        post(() -> {
//
//            initAnimator();
//
//        });

        post(this::initAnimator);


    }

    public void initAnimator() {
        if (isInit) {
            return;
        }
        isInit = true;

        //todo 验证一下
        //把布局设置手机高度
        //好像因为占用了状态栏的情况下，其实高度不包括状态栏的高度
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = currentHeight;
        setLayoutParams(layoutParams);

        ivIcon.setScaleX(0);
        ivIcon.setScaleY(0);

        initRightAnimator();

        initTvAnimator();
        initLayoutAnimator();
    }

    /**
     * 最终执行完成，layout的animator
     */
    private void initLayoutAnimator() {
        valueAnimator = ValueAnimator.ofInt(currentHeight, dp220);
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
//            layout(0, 0, getWidth(), animatedValue);
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = animatedValue;
            setLayoutParams(layoutParams);
            if (animatedValue == dp220) {
                if (goodChangeListener != null) {
                    goodChangeListener.goodChange();
                }
            }
        });
    }

    /**
     * 套着 valueAnimator
     */
    private void initTvAnimator() {
        tvAnimator = ObjectAnimator.ofFloat(tvDec, "translationY", tvDec.getHeight() + 200, tvDec.getHeight());
        tvAnimator.setDuration(300);
        tvAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                tvDec.setText(mTvValue);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (tvAnimator2 != null) {
                    tvAnimator2.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        tvAnimator2 = ObjectAnimator.ofFloat(tvDec2, "translationY", tvDec2.getHeight() + 200, tvDec2.getHeight());
        tvAnimator2.setDuration(300);
        tvAnimator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                tvDec2.setText(mTvValue2);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //延迟弹出可以关闭的广告
                postDelayed(() -> showDelayAd(), 800);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void showDelayAd() {


        if (getContext() instanceof Activity) {
            if (((Activity) getContext()).isFinishing()) {
                return;
            }
        }
//                if (valueAnimator != null) {
//                    valueAnimator.start();
//                }
//                if (rightAnimator2 != null) {
//                    rightAnimator2.start();
//                }
        //关闭广告的情况下，正常使用功能
        //第一次是广告就不进行广告了
        if (Constants.TT_APPID.equals("******")) {
            ToastUtils.showLong("广告关闭状态提示");
            if (valueAnimator != null) {
                valueAnimator.start();
            }
            if (rightAnimator2 != null) {
                rightAnimator2.start();
            }
        } else {
            if (mIsFirstGood) {
                if (valueAnimator != null) {
                    valueAnimator.start();
                }
                if (rightAnimator2 != null) {
                    rightAnimator2.start();
                }
            } else {
                showAd();
            }
        }

    }

    private void showAd() {
        ADGoodChangeShow adGoodChangeShow;
        if (mIsGarbageGoodShowAd) {
            adGoodChangeShow = new ADGoodChangeShow(getContext(),
                    ScenarioEnum.garbage_full, ScenarioEnum.garbage_inter, ScenarioEnum.garbage_splash);
        } else {
            adGoodChangeShow = new ADGoodChangeShow(getContext(),
                    ScenarioEnum.garbage_other_full, ScenarioEnum.garbage_other_inter, ScenarioEnum.garbage_other_splash);
        }
        adGoodChangeShow.splashContainerView(mAddRootView);
        adGoodChangeShow.setISuccessCallback(new ADGoodChangeShow.ISuccessCallback() {
            @Override
            public void onSuccess() {
                if (valueAnimator != null) {
                    valueAnimator.start();
                }
                if (rightAnimator2 != null) {
                    rightAnimator2.start();
                }
            }

            @Override
            public void onLastFailure() {
                if (valueAnimator != null) {
                    valueAnimator.start();
                }
                if (rightAnimator2 != null) {
                    rightAnimator2.start();
                }
            }
        });
        adGoodChangeShow.show();
    }

    /**
     * 套着 tvAnimator
     */
    private void initRightAnimator() {
        rightAnimator = ValueAnimator.ofFloat(0, 1.2f, 1f);
        rightAnimator.setDuration(300);
        rightAnimator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            ivIcon.setScaleX(animatedValue);
            ivIcon.setScaleY(animatedValue);
        });

        rightAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (tvAnimator != null) {
                    tvAnimator.start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        rightAnimator2 = ValueAnimator.ofFloat(1f, 0);
        rightAnimator2.setDuration(300);
        rightAnimator2.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            ivIcon.setScaleX(animatedValue);
            ivIcon.setScaleY(animatedValue);
        });

        rightAnimator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ivIcon.setVisibility(GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public void setTvDec(String value) {
        mTvValue = value;
        tvDec.setText("");
    }

    public void setTvDec2(String value) {
        mTvValue2 = value;
        tvDec2.setText("");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        cancelAnim(valueAnimator);
        cancelAnim(rightAnimator);
        cancelAnim(tvAnimator);
        cancelAnim(tvAnimator2);
        cancelAnim(rightAnimator2);
    }

    private void cancelAnim(ValueAnimator animator) {
        if (animator.isRunning()) {
            animator.cancel();
        }
    }

    public void startAnim() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            return;
        }
        if (rightAnimator != null) {
            rightAnimator.start();
        }
    }

    public void setFirstGood(boolean firstGood) {
        mIsFirstGood = firstGood;
    }

    public void setAdRootView(ViewGroup view) {
        mAddRootView = view;
    }

    private IGoodChangeListener goodChangeListener;

    public void setGoodChangeListener(IGoodChangeListener goodChangeListener) {
        this.goodChangeListener = goodChangeListener;
    }

    public void setGarbageGoodShowAd(boolean isGarbageGoodShowAd) {
        mIsGarbageGoodShowAd = isGarbageGoodShowAd;
    }

    public interface IGoodChangeListener {
        void goodChange();
    }
}
