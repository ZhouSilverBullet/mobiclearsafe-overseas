package com.mobi.clearsafe.ui.powercontrol.fragment;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.View;
import android.widget.FrameLayout;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.base.BaseFragment;
import com.mobi.clearsafe.ui.powercontrol.widget.BatteryWaveView;
import com.mobi.clearsafe.ui.powercontrol.widget.CleanHighPoweredAppView;
import com.mobi.clearsafe.ui.powercontrol.widget.DropFlashView;

import butterknife.BindView;

import static android.os.BatteryManager.BATTERY_STATUS_CHARGING;
import static android.os.BatteryManager.BATTERY_STATUS_FULL;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/23 01:10
 * @Dec 略
 */
public class PowerControlFragment extends BaseFragment {
    @BindView(R.id.batteryWaveView)
    BatteryWaveView batteryWaveView;
    @BindView(R.id.chaView)
    CleanHighPoweredAppView chaView;
    @BindView(R.id.dfvView)
    DropFlashView dfvView;
    @BindView(R.id.flRoot)
    FrameLayout flRoot;
    private int mLevel;
    private int mStatus;
    /**
     * 是否是充电的状态
     */
    private boolean isPowering;

    @Override
    protected int getFragmentView() {
        return R.layout.fragment_power_control;
    }

    @Override
    protected void initVariables() {
        if (getActivity() != null) {
            mLevel = getActivity().getIntent().getIntExtra("level", 50);
            mStatus = getActivity().getIntent().getIntExtra("status", 0);
        }
    }

    @Override
    protected void initView() {

        batteryWaveView.setCallback(new BatteryWaveView.ICallback() {
            @Override
            public void startBack() {

            }

            @Override
            public void startDraw(int paramInt) {
                if (mStatus == BATTERY_STATUS_CHARGING || mStatus == BATTERY_STATUS_FULL) {
                    isPowering = true;
                    dfvView.setVisibility(View.VISIBLE);
                    dfvView.setCurFlashHeight(paramInt);
                } else {
                    isPowering = false;
                    chaView.setVisibility(View.VISIBLE);
                    chaView.setCurFlashHeight(paramInt);
                }
            }
        });

        batteryWaveView.post(new Runnable() {
            @Override
            public void run() {
                execAnim();
                //动画执行结束
                batteryWaveView.postDelayed(() -> {
                    if (getActivity() == null || getActivity().isFinishing()) {
                        return;
                    }
                    if (!isAdded()) {
                        return;
                    }
                    if (mAnimalFinishCallback != null) {
                        mAnimalFinishCallback.onFinish(isPowering);
                    }
                }, 4000L);
            }
        });
    }

    private void execAnim() {
        float f2 = mLevel - 10;
        float f3 = mLevel + 10;
        float f1 = f2;
        if (f2 < 0.0F) {
            f1 = 0.0F;
        }
        f2 = f3;
        if (f3 > 100.0F) {
            f2 = 100.0F;
        }

        f1 /= 100.0F;
        f2 /= 100.0F;

        ((BatteryWaveView) batteryWaveView).g = f1;
        ((BatteryWaveView) batteryWaveView).f = f2;

        ((BatteryWaveView) batteryWaveView).b = ((int) (((BatteryWaveView) batteryWaveView).mHeight * (1.0F - ((BatteryWaveView) batteryWaveView).g)));
        ((BatteryWaveView) batteryWaveView).e = ((int) (((BatteryWaveView) batteryWaveView).mHeight * (((BatteryWaveView) batteryWaveView).f - ((BatteryWaveView) batteryWaveView).g) - 15.0F));

        if ((batteryWaveView.animatorSet == null) || (!batteryWaveView.animatorSet.isRunning())) {
            batteryWaveView.h = true;
            batteryWaveView.animatorSet = new AnimatorSet();
            ValueAnimator localValueAnimator1 = ValueAnimator.ofInt(new int[]{0, batteryWaveView.d});
            localValueAnimator1.addUpdateListener(batteryWaveView.mAnimatorUpdateListener);
            localValueAnimator1.setDuration(300L);
            localValueAnimator1.setRepeatCount(-1);

            ValueAnimator localValueAnimator2 = ValueAnimator.ofInt(new int[]{0, batteryWaveView.e});
            localValueAnimator2.setDuration(4000L);
            localValueAnimator2.addUpdateListener(batteryWaveView.mAnimatorUpdateListener2);
            batteryWaveView.animatorSet.play(localValueAnimator1).with(localValueAnimator2);
            batteryWaveView.animatorSet.setDuration(4000L);
            batteryWaveView.animatorSet.start();

            batteryWaveView.startBgAnim(flRoot);
        }
    }

    private AnimalFinishCallback mAnimalFinishCallback;

    public void setAnimalFinishCallback(AnimalFinishCallback animalFinishCallback) {
        mAnimalFinishCallback = animalFinishCallback;
    }

    public interface AnimalFinishCallback {
        void onFinish(boolean isPowering);
    }
}
