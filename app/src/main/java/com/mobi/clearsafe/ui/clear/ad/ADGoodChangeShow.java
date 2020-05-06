package com.mobi.clearsafe.ui.clear.ad;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.adtest.fullscreenvideo.FullScreenVideoAD;
import com.example.adtest.fullscreenvideo.FullScreenVideoListener;
import com.example.adtest.interaction.InterActionExpressAdView;
import com.example.adtest.interaction.InterActionLoadListener;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.splash.SplashViewAD;
import com.example.adtest.splash.SplashViewADLoadListener;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.common.ad.FullScreenVideoListenerImpl;
import com.mobi.clearsafe.ui.common.ad.InterActionLoadListenerImpl;
import com.mobi.clearsafe.ui.common.ad.SplashViewADLoadListenerImpl;
import com.mobi.clearsafe.widget.InterActionDialog;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/24 13:56
 * @Dec 略
 */
public class ADGoodChangeShow {

    private final Context mContext;
    private final ScenarioEnum mFull;
    private final ScenarioEnum mInter;
    private final ScenarioEnum mSplash;

    private ViewGroup viewGroup;

    public ADGoodChangeShow(Context context, ScenarioEnum full, ScenarioEnum inter, ScenarioEnum splash) {
        mContext = context;
        mFull = full;
        mInter = inter;
        mSplash = splash;
    }

    public void show() {
        //全屏广告
        showFullScreen(() -> {
            //插屏广告
            showInterAction(() -> {
                //开屏广告
                showSplash(() -> {
                    if (mSuccessCallback != null) {
                        mSuccessCallback.onLastFailure();
                    }
                });
            });
        });
    }

    private void showSplash(IFailureCallback failureCallback) {
        new SplashViewAD.Builder(mContext)
                .setScenario(mSplash)
                .setBearingView(viewGroup)
                .setNotSdkCountDown(false)
                .setImageAcceptedSize(1080, 1920)
                .setLoadListener(new SplashViewADLoadListenerImpl() {
                    @Override
                    public void LoadError(String channel, int errorCode, String errorMsg) {
                        if (failureCallback != null) {
                            failureCallback.onFailure();
                        }
                    }

                    @Override
                    public void onTimeout(String channel) {
                        if (mSuccessCallback != null) {
                            mSuccessCallback.onSuccess();
                        }
                    }

                    @Override
                    public void onAdTimeOver() {
                        if (viewGroup != null) {
                            viewGroup.removeAllViews();
                        }
                        if (mSuccessCallback != null) {
                            mSuccessCallback.onSuccess();
                        }
                    }

                    @Override
                    public void onAdSkip(String channel) {
                        if (viewGroup != null) {
                            viewGroup.removeAllViews();
                        }
                        if (mSuccessCallback != null) {
                            mSuccessCallback.onSuccess();
                        }
                    }
                })
                .build();
    }

    private void showInterAction(IFailureCallback failureCallback) {
        new InterActionExpressAdView.Builder(mContext)
                .setScenarioEnum(mInter)
                .setListener(new InterActionLoadListenerImpl() {

                    @Override
                    public void onLoadFaild(String channel, int faildCode, String faildMsg) {
                        if (failureCallback != null) {
                            failureCallback.onFailure();
                        }
                    }

                    @Override
                    public void onAdDismissed(String channel) {
                        if (mSuccessCallback != null) {
                            mSuccessCallback.onSuccess();
                        }
                    }
                })
                .build();

    }

    private void showFullScreen(IFailureCallback failureCallback) {
        new FullScreenVideoAD.Builder(mContext)
                .setScenario(mFull)
                .setFullScreenVideoListener(new FullScreenVideoListenerImpl() {

                    @Override
                    public void onAdClose(String channel) {
                        if (mSuccessCallback != null) {
                            mSuccessCallback.onSuccess();
                        }
                    }

                    @Override
                    public void onSkippedVideo(String channel) {
                        if (mSuccessCallback != null) {
                            mSuccessCallback.onSuccess();
                        }
                    }

                    @Override
                    public void onLoadFaild(String channel, int faildCode, String faildMsg) {
                        if (failureCallback != null) {
                            failureCallback.onFailure();
                        }
                    }
                })
                .builde();
    }

    public void splashContainerView(ViewGroup addRootView) {
        viewGroup = addRootView;
    }

    private interface IFailureCallback {
        /**
         * 广告失效，或者错误的时候
         */
        void onFailure();

    }

    private ISuccessCallback mSuccessCallback;

    public void setISuccessCallback(ISuccessCallback successCallback) {
        mSuccessCallback = successCallback;
    }

    public interface ISuccessCallback {
        /**
         * 广告失效，或者错误的时候
         */
        void onSuccess();

        /**
         * 串行执行AD后，最终失败了，回调
         * 让外面业务逻辑感知上
         */
        void onLastFailure();
    }
}
