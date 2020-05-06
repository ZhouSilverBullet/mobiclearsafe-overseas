package com.example.adtest.interaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.example.adtest.WasDelayedDialog;
import com.example.adtest.bean.AdBean;
import com.example.adtest.bean.ConfigItemBean;
import com.example.adtest.config.TTAdManagerHolder;
import com.example.adtest.manager.AdScenario;
import com.example.adtest.manager.Constants;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.statistical.AdStatistical;
import com.example.adtest.utils.SdkUtils;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2019-12-13  16:06
 */
public class InterActionExpressAdView {
    private static final String TAG = "InterActionExpressAdView";
    private TTAdNative mTTAdNative;
    private TTNativeExpressAd mTTAd;
    private Context mContext;
    private UnifiedInterstitialAD iad;
    private boolean firstCome;
    private ConfigItemBean bean;
    private String SelfPosID = "";
    private ScenarioEnum scenario;//场景值
    private float ADViewWidth = 300;
    private List<String> loadedList = new ArrayList<>();
    private boolean isShowed = false;
    private InterActionLoadListener mListener;
    private Handler handler = new Handler();
    private Runnable handlerRun = new Runnable() {
        @Override
        public void run() {
            sortShow(bean);
        }
    };
    private boolean wasDelayed;


    @SuppressLint("LongLogTag")
    public InterActionExpressAdView(Builder builder) {

        this.SelfPosID = builder.SelfPosID;
        this.scenario = builder.scenario;
        this.ADViewWidth = builder.ADViewWidth;
        this.mContext = builder.mContext;
        this.mListener = builder.mListener;
        this.wasDelayed = builder.wasDelayed;

        if (TextUtils.isEmpty(SelfPosID)) {//如果没有直接设置POSID 则根据场景值获取
            SelfPosID = AdScenario.getSelfPosId(this.scenario);
        }
//        if (TextUtils.isEmpty(SelfPosID)) {
//            SelfPosID = Constants.default_inter_posid;
//        }
        bean = Constants.getAdItem(SelfPosID, mContext);
//        if (bean == null) {
//            SelfPosID = Constants.default_inter_posid;
//            bean = Constants.getAdItem(SelfPosID, mContext);
//        }
        if (bean == null) {
            if (mListener != null) {
                mListener.onLoadFaild("bean == null 广告相关数据缺失", 0, "");
            }
            Log.e(TAG, "广告相关数据缺失，请先调用SDKManager.init()");
            return;
        }
        if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
            loadForService();
        } else {
            sortload();
            if (handler != null && handlerRun != null) {
                handler.postDelayed(handlerRun, Constants.bean.getAd_adk_req_timeout());
            }
            if (bean.getSort_type() == Constants.SORT_TYPE_ORDER) {

            } else if (bean.getSort_type() == Constants.SORT_TYPE_PRICE) {
                firstCome = true;
            } else if (bean.getSort_type() == Constants.SORT_TYPE_ORDER__PRICE) {
                firstCome = true;
            } else {
                firstCome = true;
            }
        }
    }

    private void loadForService() {
        int index = Constants.SortParameterMap.get(bean.getPosid());
        if (bean.getSort_parameter() != null && bean.getSort_parameter().size() > 0) {
            int listIndex = index % bean.getSort_parameter().size();
            String plant = bean.getSort_parameter().get(listIndex);
            AdBean item = SdkUtils.getShowAdBean(bean, plant);
            if (plant.equals(Constants.TT_KEY)) {
                if (item.getSdk().equals(Constants.TT_KEY)) {//是穿山甲广告则初始化
                    if (!TTAdManagerHolder.getInitStatus()) {
                        TTAdManagerHolder.singleInit(mContext.getApplicationContext(), item.getParameterBean().getAppid(), item.getParameterBean().getAppname());
                    }
                    loadTT(item.getParameterBean().getPosid());
                }
            } else if (plant.equals(Constants.GDT_KEY)) {
                loadGDT(item.getParameterBean().getAppid(), item.getParameterBean().getPosid());
            }
        } else {
            sortload();
            if (handler != null && handlerRun != null) {
                handler.postDelayed(handlerRun, Constants.bean.getAd_adk_req_timeout());
            }
            if (bean.getSort_type() == Constants.SORT_TYPE_ORDER) {

            } else if (bean.getSort_type() == Constants.SORT_TYPE_PRICE) {
                firstCome = true;
            } else if (bean.getSort_type() == Constants.SORT_TYPE_ORDER__PRICE) {
                firstCome = true;
            } else {
                firstCome = true;
            }
        }

    }

    private void sortload() {
        List<AdBean> netList = bean.getNetwork();
        int size = netList.size();
        for (int i = 0; i < size; i++) {
            AdBean item = netList.get(i);

            if (item.getSdk().equals(Constants.TT_KEY)) {//是穿山甲广告则初始化
                if (!TTAdManagerHolder.getInitStatus()) {
                    TTAdManagerHolder.singleInit(mContext.getApplicationContext(), item.getParameterBean().getAppid(), item.getParameterBean().getAppname());
                }
                loadTT(item.getParameterBean().getPosid());
            }
            if (item.getSdk().equals(Constants.GDT_KEY)) {//是广点通广告
                loadGDT(item.getParameterBean().getAppid(), item.getParameterBean().getPosid());
            }
        }
    }

    /**
     * 加载穿山甲插屏广告
     *
     * @param CodeID
     */
    private void loadTT(String CodeID) {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mContext);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(CodeID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(640, 320)
                .setExpressViewAcceptedSize(ADViewWidth, 0)
                .setAdCount(1)
                .build();
        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String s) {
                if (mListener != null) {
                    mListener.onLoadFaild(Constants.TT_KEY, i, s);
                }
                AdStatistical.trackAD(mContext, Constants.TT_KEY, SelfPosID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                if (list == null || list.size() <= 0) {
                    return;
                }
                mTTAd = list.get(0);
                bindAdListener(mTTAd);
                recordRenderSuccess(Constants.TT_KEY);
                mTTAd.render();
            }
        });

    }

    /**
     * 绑定穿山甲插屏广告监听事件
     *
     * @param ad
     */
    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
            @Override
            public void onAdDismiss() {
                //广告关闭
                if (mListener != null) {
                    mListener.onAdDismissed(Constants.TT_KEY);
                }
            }

            @Override
            public void onAdClicked(View view, int i) {
                //广告被点击
                if (mListener != null) {
                    mListener.onAdClick(Constants.TT_KEY);
//                    if(mTTAd!=null){
//                        mTTAd.destroy();
//                    }
                }
                AdStatistical.trackAD(mContext, Constants.TT_KEY, SelfPosID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
            }

            @Override
            public void onAdShow(View view, int i) {
                //广告显示
                if (mListener != null) {
                    mListener.onAdShow(Constants.TT_KEY);
                }
                AdStatistical.trackAD(mContext, Constants.TT_KEY, SelfPosID, Constants.STATUS_CODE_TRUE, Constants.STATUS_CODE_FALSE);
            }

            @Override
            public void onRenderFail(View view, String s, int i) {
                //广告渲染失败
            }

            @Override
            public void onRenderSuccess(View view, float v, float v1) {
                //广告渲染成功
                //mTTAd.showInteractionExpressAd((Activity) mContext);
                if (mListener != null) {
                    mListener.onAdRenderSuccess(Constants.TT_KEY);
                }
                if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
                    showTTAD();
                } else {
                    if (firstCome) {
                        showTTAD();
                        firstCome = false;
                    }
                }
            }
        });
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
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

    /**
     * 显示穿山甲插屏广告
     */
    private void showTTAD() {
        if (isShowed) {
            return;
        }
        if (mTTAd != null) {
            mTTAd.showInteractionExpressAd((Activity) mContext);
            if (wasDelayed) {
                new WasDelayedDialog.Builder(mContext)
                        .setView(mTTAd.getExpressAdView())
                        .build();
            }
            isShowed = true;
        }
    }

    private void loadGDT(String APPID, String POSID) {
        if (iad != null) {
            iad.close();
            iad.destroy();
            iad = null;
        }
        if (iad == null) {
            iad = new UnifiedInterstitialAD((Activity) mContext, APPID, POSID, new UnifiedInterstitialADListener() {
                @Override
                public void onADReceive() {
                    //广告加载成功
                    if (firstCome) {
                        showGDTAD();
                        firstCome = false;
                    }
                    recordRenderSuccess(Constants.GDT_KEY);
                }

                @Override
                public void onVideoCached() {

                }

                @Override
                public void onNoAD(AdError adError) {
                    //没有广告
                    if (mListener != null) {
                        mListener.onLoadFaild(Constants.GDT_KEY, adError.getErrorCode(), adError.getErrorMsg());
                    }
                    AdStatistical.trackAD(mContext, Constants.GDT_KEY, SelfPosID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
                }

                @Override
                public void onADOpened() {
                    //广告展开时回调
                    if (mListener != null) {
                        mListener.onAdShow(Constants.GDT_KEY);
                    }
                }

                @Override
                public void onADExposure() {
                    //广告曝光时回调
                    if (mListener != null) {
                        mListener.onAdRenderSuccess(Constants.GDT_KEY);
                    }
                    AdStatistical.trackAD(mContext, Constants.GDT_KEY, SelfPosID, Constants.STATUS_CODE_TRUE, Constants.STATUS_CODE_FALSE);
                }

                @Override
                public void onADClicked() {
                    //广告点击时回调
                    if (mListener != null) {
                        mListener.onAdClick(Constants.GDT_KEY);
                    }
                    AdStatistical.trackAD(mContext, Constants.GDT_KEY, SelfPosID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
                }

                @Override
                public void onADLeftApplication() {
                    //广告点击离开应用时回调
                }

                @Override
                public void onADClosed() {
                    //广告关闭时回调
                    if (mListener != null) {
                        mListener.onAdDismissed(Constants.GDT_KEY);
                    }
                }
            });
            setVideoOption();
            iad.loadAD();
        }
    }

    private void setVideoOption() {
        VideoOption.Builder builder = new VideoOption.Builder();
        VideoOption option = builder.setAutoPlayMuted(true)
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS).build();
        iad.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO);
        iad.setVideoOption(option);
//        iad.setMaxVideoDuration(10);

    }

    /**
     * 显示广点通插屏广告
     */
    private void showGDTAD() {
        if (isShowed) {
            return;
        }
        if (iad != null) {
            iad.show();
            isShowed = true;
        }
    }

    /**
     * 按照服务器返回优先级显示插屏广告
     */
    private void sortShow(ConfigItemBean bean) {
        List<AdBean> netList = bean.getNetwork();
        int size = netList.size();
        for (int i = 0; i < size; i++) {
            AdBean item = netList.get(i);
            if (item.getSdk().equals(Constants.TT_KEY) && mTTAd != null) {
                showTTAD();
                break;
            }
            if (item.getSdk().equals(Constants.GDT_KEY) && iad != null) {
                showGDTAD();
                break;
            }
            if (i >= size - 1) {
                firstCome = true;
            }
        }
    }


    /**
     * 判断当前加载完成的广告是否与服务器返回的列表中的第一个一致
     * 当前请求 每渲染成功一个 则像list中添加一个 并判断是否与要加载的数量一致
     *
     * @param channel
     */
    private void recordRenderSuccess(String channel) {
        if (isShowed) return;
        if (bean.getNetwork().size() > 0) {
            if (bean.getNetwork().get(0).getSdk().equals(channel) && !isShowed) {
                sortShow(bean);
                if (handler != null && handlerRun != null) {
                    handler.removeCallbacks(handlerRun);
                }
                return;
            }
        }
        loadedList.add(channel);
        if (loadedList.size() >= bean.getNetwork().size() && !isShowed) {
            sortShow(bean);
            if (handler != null && handlerRun != null) {
                handler.removeCallbacks(handlerRun);
            }
        }
    }

    /**
     * 销毁View
     */
    public void destory() {
        if (iad != null) {
            iad.close();
            iad.destroy();
            iad = null;
        }
        if (mTTAd != null) {
            mTTAd.destroy();
            mTTAd = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (handlerRun != null) {
            handlerRun = null;
        }
    }

    public static class Builder {
        private Context mContext;
        private ScenarioEnum scenario;
        private String SelfPosID = "";
        private float ADViewWidth = 300;
        private InterActionLoadListener mListener;
        private boolean wasDelayed;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setScenarioEnum(ScenarioEnum scenario) {
            this.scenario = scenario;
            return this;
        }

        public Builder setWasDelayed(boolean wasDelayed) {
            this.wasDelayed = wasDelayed;
            return this;
        }


        public Builder setPosID(String posid) {
            this.SelfPosID = posid;
            return this;
        }

        public Builder setAdViewWidth(float width) {
            this.ADViewWidth = width;
            return this;
        }

        public Builder setListener(InterActionLoadListener listener) {
            this.mListener = listener;
            return this;
        }

        public InterActionExpressAdView build() {
            return new InterActionExpressAdView(this);
        }

    }


}
