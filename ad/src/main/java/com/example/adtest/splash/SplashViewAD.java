package com.example.adtest.splash;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.example.adtest.bean.AdBean;
import com.example.adtest.bean.ConfigItemBean;
import com.example.adtest.config.TTAdManagerHolder;
import com.example.adtest.manager.AdScenario;
import com.example.adtest.manager.Constants;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.statistical.AdStatistical;

import java.util.List;

/**
 * author : liangning
 * date : 2019-11-28  21:19
 */
public class SplashViewAD {
    private static final String TAG = "SplashViewAD";
    private ViewGroup mBearingView;
    private Context mContext;
    private boolean mDeepLink = true;
    private int width, height;
    private static final int AD_TIME_OUT = 3000;
    private String mPOSID = "";
    private TTAdNative mTTadNative;
    private TTSplashAd mTTSplashAd;//穿山甲开屏广告
    private ScenarioEnum scenario;
    private ConfigItemBean bean;
    private SplashViewADLoadListener mListener;

    private boolean NotSdkCountDown = true;

    public SplashViewAD(Builder builder) {
        this.mContext = builder.mContext;
        this.mDeepLink = builder.mDeepLink;
        this.mPOSID = builder.POSID;
        this.width = builder.width;
        this.height = builder.height;
        this.mBearingView = builder.viewGroup;
        this.scenario = builder.scenarioEnum;
        this.mListener = builder.mListener;
        this.NotSdkCountDown = builder.NotsdkCoundDown;
        if (TextUtils.isEmpty(mPOSID)) {//如果没有直接设置POSID 则根据场景值获取
            mPOSID = AdScenario.getSelfPosId(this.scenario);
        }
//        if (TextUtils.isEmpty(mPOSID)) {
//            mPOSID = Constants.default_native_posid;
//        }
        bean = Constants.getAdItem(mPOSID, mContext);
        if (bean == null) {
            Log.e(TAG, "广告相关数据缺失，请先调用SDKManager.init()");
            if (mListener!=null){
                mListener.LoadError(Constants.TT_KEY,1028001,"未找到广告位ID");
                AdStatistical.trackAD(mContext, Constants.TT_KEY, mPOSID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
            }
            return;
        }
//        loadTTSplash("836888844");
        sortLoad();
    }

    private void sortLoad(){
        List<AdBean> netList = bean.getNetwork();
        int size = netList.size();
        for (int i = 0; i < size; i++) {
            AdBean item = netList.get(i);

            if (item.getSdk().equals(Constants.TT_KEY)) {//是穿山甲广告则初始化
                if (!TTAdManagerHolder.getInitStatus()) {
                    TTAdManagerHolder.singleInit(mContext.getApplicationContext(), item.getParameterBean().getAppid(), item.getParameterBean().getAppname());
                }
                loadTTSplash(item.getParameterBean().getPosid());
            }
//            if (item.getSdk().equals(Constants.GDT_KEY)) {//是广点通广告
//                loadGDTExpressAd(item.getParameterBean().getAppid(), item.getParameterBean().getPosid());
//            }
        }
    }

    /**
     * 加载穿山甲开屏广告
     */
    private void loadTTSplash(String POSID) {
        mTTadNative = TTAdManagerHolder.get().createAdNative(mContext);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(POSID)
                .setSupportDeepLink(mDeepLink)
                .setImageAcceptedSize(width, height)
                .build();
        mTTadNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            public void onError(int i, String s) {
                //加载失败
                if (mListener!=null){
                    mListener.LoadError(Constants.TT_KEY,i,s);
                }
                AdStatistical.trackAD(mContext, Constants.TT_KEY, mPOSID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
            }

            @Override
            public void onTimeout() {
                //加载超时
                if (mListener!=null){
                    mListener.onTimeout(Constants.TT_KEY);
                }
                AdStatistical.trackAD(mContext, Constants.TT_KEY, mPOSID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
            }

            @Override
            public void onSplashAdLoad(TTSplashAd ad) {
                // 请求成功
                if (ad == null) {
                    if (mListener!=null){
                        mListener.LoadError(Constants.TT_KEY,1028001,"未拉取到广告");
                    }
                    return;
                }
                if (mListener!=null){
                    mListener.onSplashAdLoad(Constants.TT_KEY);
                }
                mTTSplashAd = ad;
                addTTSplashToView();
            }
        }, AD_TIME_OUT);
    }

    /**
     * 添加穿山甲开屏广告到view中
     */
    private void addTTSplashToView() {
        if (mTTSplashAd == null) {
            return;
        }
        if (mBearingView == null) {
            return;
        }
        mBearingView.removeAllViews();
        mBearingView.addView(mTTSplashAd.getSplashView());
        if (NotSdkCountDown) {
            mTTSplashAd.setNotAllowSdkCountdown();
        }
        mTTSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, int i) {
                //广告被点击
                if (mListener!=null){
                    mListener.onAdClicked(Constants.TT_KEY);
                }
                AdStatistical.trackAD(mContext, Constants.TT_KEY, mPOSID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
            }

            @Override
            public void onAdShow(View view, int i) {
                //广告展示
                if (mListener!=null){
                    mListener.onAdShow(Constants.TT_KEY);
                }
                AdStatistical.trackAD(mContext, Constants.TT_KEY, mPOSID, Constants.STATUS_CODE_TRUE, Constants.STATUS_CODE_FALSE);
            }

            @Override
            public void onAdSkip() {
                // 广告跳过
                if (mListener != null) {
                    mListener.onAdSkip(Constants.TT_KEY);
                }
            }

            @Override
            public void onAdTimeOver() {
                //广告倒计时结束
                if (mListener != null) {
                    mListener.onAdTimeOver();
                }
            }
        });
        if (mTTSplashAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            mTTSplashAd.setDownloadListener(new TTAppDownloadListener() {
                @Override
                public void onIdle() {

                }

                @Override
                public void onDownloadActive(long l, long l1, String s, String s1) {

                }

                @Override
                public void onDownloadPaused(long l, long l1, String s, String s1) {

                }

                @Override
                public void onDownloadFailed(long l, long l1, String s, String s1) {

                }

                @Override
                public void onDownloadFinished(long l, String s, String s1) {

                }

                @Override
                public void onInstalled(String s, String s1) {

                }
            });
        }
    }

    public static class Builder {
        private Context mContext;
        private String POSID = "";
        private int width, height;
        private boolean mDeepLink = true;
        private ViewGroup viewGroup;
        private ScenarioEnum scenarioEnum;
        private SplashViewADLoadListener mListener;
        private boolean NotsdkCoundDown = true;
        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setPosID(String POSID) {
            this.POSID = POSID;
            return this;
        }

        public Builder setImageAcceptedSize(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setSupportDeepLink(boolean deepLink) {
            this.mDeepLink = deepLink;
            return this;
        }

        public Builder setBearingView(ViewGroup view) {
            this.viewGroup = view;
            return this;
        }

        public Builder setScenario(ScenarioEnum scenario) {
            this.scenarioEnum = scenario;
            return this;
        }

        public Builder setLoadListener(SplashViewADLoadListener listener){
            this.mListener = listener;
            return this;
        }
        public Builder setNotSdkCountDown(boolean notsdkCoundDown) {
            this.NotsdkCoundDown = notsdkCoundDown;
            return this;
        }

        public SplashViewAD build() {
            return new SplashViewAD(this);
        }

    }
}
