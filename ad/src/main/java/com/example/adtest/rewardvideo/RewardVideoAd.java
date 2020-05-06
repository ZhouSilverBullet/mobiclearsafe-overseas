package com.example.adtest.rewardvideo;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.example.adtest.bean.AdBean;
import com.example.adtest.bean.ConfigItemBean;
import com.example.adtest.config.TTAdManagerHolder;
import com.example.adtest.manager.AdScenario;
import com.example.adtest.manager.Constants;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.statistical.AdStatistical;
import com.example.adtest.utils.SdkUtils;
import com.mobi.adsdk.utils.ADError;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;
import com.sigmob.windad.WindAdError;
import com.sigmob.windad.rewardedVideo.WindRewardAdRequest;
import com.sigmob.windad.rewardedVideo.WindRewardInfo;
import com.sigmob.windad.rewardedVideo.WindRewardedVideoAd;
import com.sigmob.windad.rewardedVideo.WindRewardedVideoAdListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 创建激励视频
 * author : liangning
 * date : 2019-11-08  11:13
 */
public class RewardVideoAd {
    private static final String TAG = "RewardVideoAd";
    private static String POS_ID = "";

    private Context mContext;
    private boolean firstCome = false;//如果800毫秒以后 广告都未加载完 则启用先到先得
    private RewardVideoLoadListener mListener;
    private TTAdNative mTTAdNative;
    private TTRewardVideoAd mttRewardVideoAd;
    private boolean mDeepLink;
    private RewardVideoAD rewardVideoAD;//广点通
    // private IncentiveVideoAd rewardVideoAD;//valpub视频广告
    private boolean gdtAdLoaded;//广点通广告是否加载成功
    private boolean isShow = false;//是否已经显示过广告
    private ScenarioEnum scenario;
    private List<String> loadedList = new ArrayList<>();
    private ConfigItemBean bean;
    private WindRewardedVideoAd windRewardedVideoAd;
    private WindRewardAdRequest request;
    private Handler handler = new Handler();
    private Runnable handlerRun = new Runnable() {
        @Override
        public void run() {
            //如果头条权重比广点通小 并且 头条的view不为空
//            if (Constants.TT_SORT < Constants.GDT_SORT && mttRewardVideoAd != null) {
//                showTTVideo();
//            } else if (rewardVideoAD != null ) {
//                showGDTVideo();
//            } else {
//                firstCome = true;
//            }
            SortShow();
        }
    };
    //自有视频
    private com.mobi.adsdk.rewardvideo.RewardVideoAD mobiReward;
    private boolean mobiAdLoaded;


    public RewardVideoAd(Builder builder) {
        this.mContext = builder.mContext;
        this.mDeepLink = builder.mDeepLink;
        this.mListener = builder.mListener;
        this.scenario = builder.scenario;
        this.POS_ID = builder.PosID;
        if (TextUtils.isEmpty(POS_ID)) {//如果没有直接设置POSID 则根据场景值获取
            POS_ID = AdScenario.getSelfPosId(this.scenario);
        }
//        if (TextUtils.isEmpty(POS_ID)) {//场景值也获取不到POSID 则取默认值
//            POS_ID = Constants.default_video_posid;
//        }
        bean = Constants.getAdItem(POS_ID, mContext);
//        if (bean == null) {
//            POS_ID = Constants.default_video_posid;
//            bean = Constants.getAdItem(POS_ID, mContext);
//        }
        Log.e(TAG, "使用的POSID:" + POS_ID);
        if (bean == null) {
            if (mListener != null) {
                mListener.onLoadFaild("", -100, "");
            }
            Log.e(TAG, "广告相关数据缺失，请先调用SDKManager.init()");
            return;
        }
        if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
            loadAdForService();
        } else {
            SortLoad();
            if (handler != null && handlerRun != null) {
                handler.postDelayed(handlerRun, Constants.bean.getAd_adk_req_timeout());
            }
            if (bean.getSort_type() == Constants.SORT_TYPE_ORDER) {

            } else if (bean.getSort_type() == Constants.SORT_TYPE_PRICE) {
                firstCome = true;
            } else if (bean.getSort_type() == Constants.SORT_TYPE_ORDER__PRICE) {
                firstCome = true;
            } else if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {

            } else {
                firstCome = true;
            }
        }
    }

    /**
     * 按照服务器固定顺序加载广告
     */
    private void loadAdForService() {
        int index = Constants.SortParameterMap.get(bean.getPosid());
//        Log.e(TAG, "每次存储的值" + index);
        if (bean.getSort_parameter() != null && bean.getSort_parameter().size() > 0) {
            int listIndex = index % bean.getSort_parameter().size();
//            Log.e(TAG, "平台数组" + JSON.toJSONString(bean.getSort_parameter()));
//            Log.e(TAG, "对应的平台下标" + listIndex);
            String plant = bean.getSort_parameter().get(listIndex);
//            Log.e(TAG, "使用的平台为" + plant);
            AdBean item = SdkUtils.getShowAdBean(bean, plant);
            if (plant.equals(Constants.TT_KEY)) {
                if (!TTAdManagerHolder.getInitStatus()) {
                    TTAdManagerHolder.singleInit(mContext.getApplicationContext(), item.getParameterBean().getAppid(), item.getParameterBean().getAppname());
                }
                loadTTRewardViewAd(item.getParameterBean().getPosid());
                if (handler != null && handlerRun != null) {
                    handler.removeCallbacks(handlerRun);
                }
                index++;
                Constants.SortParameterMap.put(bean.getPosid(), index);
            } else if (plant.equals(Constants.GDT_KEY)) {
                loadGDTRewardViewAd(item.getParameterBean().getAppid(), item.getParameterBean().getPosid());
                if (handler != null && handlerRun != null) {
                    handler.removeCallbacks(handlerRun);
                }
                index++;
                Constants.SortParameterMap.put(bean.getPosid(), index);
            } else if (plant.equals(Constants.SIGMOB)) {
                loadSigmob(item.getParameterBean().getPosid());
                if (handler != null && handlerRun != null) {
                    handler.removeCallbacks(handlerRun);
                }
                index++;
                Constants.SortParameterMap.put(bean.getPosid(), index);
            } else if (plant.equals(Constants.MOBI_KEY)) {
                loadMobi(item.getParameterBean().getAppid(), item.getParameterBean().getPosid());
                if (handler != null && handlerRun != null) {
                    handler.removeCallbacks(handlerRun);
                }
                index++;
                Constants.SortParameterMap.put(bean.getPosid(), index);
            }
        } else {
            SortLoad();
            if (handler != null && handlerRun != null) {
                handler.postDelayed(handlerRun, Constants.bean.getAd_adk_req_timeout());
            }
            if (bean.getSort_type() == Constants.SORT_TYPE_ORDER) {

            } else if (bean.getSort_type() == Constants.SORT_TYPE_PRICE) {
                firstCome = true;
            } else if (bean.getSort_type() == Constants.SORT_TYPE_ORDER__PRICE) {
                firstCome = true;
            } else if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {

            } else {
                firstCome = true;
            }
        }
    }

    /**
     * 按顺序加载广告
     */
    private void SortLoad() {
        List<AdBean> netList = bean.getNetwork();
//        Log.e("广告", JSON.toJSONString(bean));
        int size = netList.size();
        for (int i = 0; i < size; i++) {
            AdBean item = netList.get(i);
            Log.e(TAG, item.getSdk());
            if (item.getSdk().equals(Constants.TT_KEY)) {//是穿山甲广告则初始化
                if (!TTAdManagerHolder.getInitStatus()) {
                    TTAdManagerHolder.singleInit(mContext.getApplicationContext(), item.getParameterBean().getAppid(), item.getParameterBean().getAppname());
                }
                loadTTRewardViewAd(item.getParameterBean().getPosid());
            }
            if (item.getSdk().equals(Constants.GDT_KEY)) {//是广点通广告
                loadGDTRewardViewAd(item.getParameterBean().getAppid(), item.getParameterBean().getPosid());
            }
            if (item.getSdk().equals(Constants.SIGMOB)) {//sigmob广告
                loadSigmob(item.getParameterBean().getPosid());
            }
            if (item.getSdk().equals(Constants.MOBI_KEY)) {
                loadMobi(item.getParameterBean().getAppid(), item.getParameterBean().getPosid());
            }
        }
    }


    /**
     * 按顺序判断显示哪个广告
     */
    private void SortShow() {
        List<AdBean> netList = bean.getNetwork();
        int size = netList.size();
        for (int i = 0; i < size; i++) {
            AdBean item = netList.get(i);
            if (item.getSdk().equals(Constants.TT_KEY) && mttRewardVideoAd != null) {
                showTTVideo();
                break;
            }
            if (item.getSdk().equals(Constants.GDT_KEY) && rewardVideoAD != null && gdtAdLoaded) {
                showGDTVideo();
                break;
            }
            if (item.getSdk().equals(Constants.SIGMOB) && windRewardedVideoAd != null && request != null) {
                showSigmob();
                break;
            }
            if (item.getSdk().equals(Constants.MOBI_KEY) && mobiReward != null && mobiAdLoaded) {
                if (showMobiVideo()) {
                    break;
                }
            }
            if (i >= size - 1) {
                firstCome = true;
            }
        }
    }


    //加载穿山甲激励视频广告----------------start----------------

    /**
     * 加载穿山甲激励视频广告
     */
    private void loadTTRewardViewAd(String CODE_ID) {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mContext.getApplicationContext());
        AdSlot adSlot = new AdSlot.Builder()
                .setImageAcceptedSize(1080, 1920)
                .setCodeId(CODE_ID)
                .setSupportDeepLink(mDeepLink)
                .setRewardName("")
                .setRewardAmount(10)
                .setUserID("")
                .setMediaExtra("media-extra")
                .setOrientation(TTAdConstant.VERTICAL)
                .build();
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                AdStatistical.trackAD(mContext, Constants.TT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
                //加载错误
                if (mListener != null) {
                    mListener.onLoadFaild(Constants.TT_KEY, i, s);
                    // mListener.onAdClose(Constants.TT_KEY);
                }
            }

            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                //加载成功
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {
                    @Override
                    public void onAdShow() {
                        //显示广告
                        if (mListener != null) {
                            mListener.onAdShow(Constants.TT_KEY);
                        }
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        if (mListener != null) {
                            mListener.onAdClick(Constants.TT_KEY);
                        }
                        AdStatistical.trackAD(mContext, Constants.TT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
                    }

                    @Override
                    public void onAdClose() {
                        //广告关闭
                        if (mListener != null) {
                            mListener.onAdClose(Constants.TT_KEY);
                        }
                    }

                    @Override
                    public void onVideoComplete() {
                        //播放完成
                        if (mListener != null) {
                            mListener.onVideoComplete(Constants.TT_KEY);
                        }
                    }

                    @Override
                    public void onVideoError() {
                        //播放错误
                        if (mListener != null) {
                            //  mListener.onAdClose(Constants.TT_KEY);
                        }
                    }

                    @Override
                    public void onRewardVerify(boolean b, int i, String s) {
                        //视频播放完成，奖励回调验证
                    }

                    @Override
                    public void onSkippedVideo() {
                        //跳过广告
                    }
                });
                if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
                    showTTVideo();
                } else {
                    recordRenderSuccess(Constants.TT_KEY);
                    if (firstCome) {
                        showTTVideo();
                        firstCome = false;
                    }
                }
            }

            @Override
            public void onRewardVideoCached() {
                //缓存在了本地
            }
        });
    }

    /**
     * 显示视频广告
     */
    private void showTTVideo() {
        if (isShow) return;
        if (mttRewardVideoAd != null) {
            mttRewardVideoAd.showRewardVideoAd((Activity) mContext);
            mttRewardVideoAd = null;
            isShow = true;
            AdStatistical.trackAD(mContext, Constants.TT_KEY, POS_ID, Constants.STATUS_CODE_TRUE, Constants.STATUS_CODE_FALSE);
            destory();
        }
    }
    //加载穿山甲激励视频广告-----------------end------------------

    //加载广点通激励视频广告----------------start-----------------

    /**
     * 加载广点通激励视频广告
     */
    private void loadGDTRewardViewAd(String APP_ID, String POSID) {
        gdtAdLoaded = false;
//        // 1. 初始化激励视频广告
//        rewardVideoAD = new IncentiveVideoAd(mContext, "");
//        // 2. 设置广告监听
//        rewardVideoAD.setIncentiveVideoAdListener(new IncentiveVideoAd.IncentiveVideoAdListener() {
//            @Override
//            public void onAdShow() {//激励视频广告页面展示
//                //广告已经显示
//                if (mListener != null) {
//                    mListener.onAdShow(Constants.GDT_KEY);
//                }
//            }
//
//            @Override
//            public void onVideoComplete() {//激励视频播放完毕
//                //激励视频播放完毕
//                Log.e(TAG,"valpub播放完毕");
//                if (mListener != null) {
//                    mListener.onVideoComplete(Constants.GDT_KEY);
//                }
//            }
//
//            /**
//             * 广告流程出错
//             * 错误码6000 102006 没有匹配到合适的广告。禁止重试，否则可能触发系统策略导致流量收益下降，因此拉不到广告不用理会，由后台决定
//             */
//            @Override
//            public void onError(int i) {
//                Log.e("加载失败", "加载失败");
//                AdStatistical.trackAD(mContext, Constants.GDT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
//                //加载错误
//                if (mListener != null) {
//                    mListener.onLoadFaild(Constants.GDT_KEY, i, String.valueOf(i));
//                   // mListener.onAdClose(Constants.GDT_KEY);
//                }
//            }
//
//            @Override
//            public void onAdLoaded(long l) {//广告加载成功，可在此回调后进行广告展示
//                Log.e("加载成功", "加载成功");
//                //广告加载成功
//                if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
//                    showGDTVideo();
//                } else {
//                    gdtAdLoaded = true;
//                    recordRenderSuccess(Constants.GDT_KEY);
//                    if (firstCome) {
//                        showGDTVideo();
//                        firstCome = false;
//                    }
//                }
//            }
//
//            /**
//             * 激励视频触发激励（观看视频大于一定时长或者视频播放完毕）
//             */
//            @Override
//            public void onReward() {
//
//            }
//
//            @Override
//            public void onVideoClick() {
//                //广告被点击
//                if (mListener != null) {
//                    mListener.onAdClick(Constants.GDT_KEY);
//                }
//                AdStatistical.trackAD(mContext, Constants.GDT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
//            }
//
//            @Override
//            public void onADClose() {
//                //广告被关闭
//                if (mListener != null) {
//                    mListener.onAdClose(Constants.GDT_KEY);
//                }
//            }
//        });
//        // 3. 加载激励视频广告
//        rewardVideoAD.load();
        rewardVideoAD = new RewardVideoAD(mContext, APP_ID, POSID,
                new RewardVideoADListener() {
                    @Override
                    public void onADLoad() {
                        Log.e("加载成功", "加载成功");
                        //广告加载成功
                        if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
                            showGDTVideo();
                        } else {
                            gdtAdLoaded = true;
                            recordRenderSuccess(Constants.GDT_KEY);
                            if (firstCome) {
                                showGDTVideo();
                                firstCome = false;
                            }
                        }
                    }

                    @Override
                    public void onVideoCached() {
                        //广告缓存在本地
                    }

                    @Override
                    public void onADShow() {
                        //广告已经显示
                        if (mListener != null) {
                            mListener.onAdShow(Constants.GDT_KEY);
                        }
                    }

                    @Override
                    public void onADExpose() {
                        //广告曝光
                    }

                    @Override
                    public void onReward() {
                        //激励视频出发奖励
                    }

                    @Override
                    public void onADClick() {
                        //广告被点击
                        if (mListener != null) {
                            mListener.onAdClick(Constants.GDT_KEY);
                        }
                        AdStatistical.trackAD(mContext, Constants.GDT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
                    }

                    @Override
                    public void onVideoComplete() {
                        //激励视频播放完毕
                        if (mListener != null) {
                            mListener.onVideoComplete(Constants.GDT_KEY);
                        }
                    }

                    @Override
                    public void onADClose() {
                        //广告被关闭
                        if (mListener != null) {
                            mListener.onAdClose(Constants.GDT_KEY);
                        }
                    }

                    @Override
                    public void onError(AdError adError) {
                        AdStatistical.trackAD(mContext, Constants.GDT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
                        //加载错误
                        if (mListener != null) {
                            mListener.onLoadFaild(Constants.GDT_KEY, adError.getErrorCode(), adError.getErrorMsg());
                            mListener.onAdClose(Constants.GDT_KEY);
                        }
                    }
                });
        rewardVideoAD.loadAD();
    }

    /**
     * 显示广点通视频
     */
    private void showGDTVideo() {
        Log.e("显示广告", "显示广告");
        if (isShow) return;
        if (rewardVideoAD != null) {
            rewardVideoAD.showAD();
            rewardVideoAD = null;
            isShow = true;
            AdStatistical.trackAD(mContext, Constants.GDT_KEY, POS_ID, Constants.STATUS_CODE_TRUE, Constants.STATUS_CODE_FALSE);
            destory();
        }
    }
    //加载广点通激励视频广告-----------------end------------------

    //自有激励视频 ---------start-----------------------

    private void loadMobi(String APPID, String POSID) {
        mobiAdLoaded = false;
        mobiReward = new com.mobi.adsdk.rewardvideo.RewardVideoAD(mContext, APPID, POSID, new com.mobi.adsdk.rewardvideo.RewardVideoADListener() {
            @Override
            public void onADLoad() {
                //广告加载成功
                if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
                    showMobiVideo();
                } else {
                    mobiAdLoaded = true;
                    recordRenderSuccess(Constants.MOBI_KEY);
                    if (firstCome) {
                        showMobiVideo();
                        firstCome = false;
                    }
                }
            }

            @Override
            public void onVideoCached() {

            }

            @Override
            public void onADShow() {
                if (mListener != null) {
                    mListener.onAdShow(Constants.MOBI_KEY);
                }
            }

            @Override
            public void onADClick() {
                AdStatistical.trackAD(mContext, Constants.MOBI_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
                if (mListener != null) {
                    mListener.onAdClick(Constants.MOBI_KEY);
                }
            }

            @Override
            public void onVideoComplete() {
                if (mListener != null) {
                    mListener.onVideoComplete(Constants.MOBI_KEY);
                }
            }

            @Override
            public void onADClose() {
                if (mListener != null) {
                    mListener.onAdClose(Constants.MOBI_KEY);
                }
            }

            @Override
            public void onError(ADError adError) {
                AdStatistical.trackAD(mContext, Constants.MOBI_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
                if (mListener != null) {
                    mListener.onLoadFaild(Constants.MOBI_KEY, adError.getEcode(), adError.geteMsg());
//                    mListener.onAdClose(Constants.MOBI_KEY);
                }
                ErrorTODO(Constants.MOBI_KEY);
            }
        });
        mobiReward.load();
    }

    private boolean showMobiVideo() {
        if (isShow) return true;
        if (mobiReward != null) {
            mobiReward.showRewardVideo();
            isShow = true;
            AdStatistical.trackAD(mContext, Constants.MOBI_KEY, POS_ID, Constants.STATUS_CODE_TRUE, Constants.STATUS_CODE_FALSE);
            destory();
            return true;
        }
        return false;
    }

    //自有激励视频 ---------end-----------------------

    /**
     * 加载出错之后重新加载 按照优先返回规则 显示
     */
    private void ErrorTODO(String channel) {
        //如果不是按照服务器固定顺序 则判断是否是 order排序第一位
        if (bean.getSort_type() != Constants.SORT_TYPE_SERVICE_ORDER) {
            List<AdBean> list = bean.getNetwork();
            if (list.get(0).getSdk().equals(channel)) {
                //如果是固定顺序第一位 加载失败 则按照优先返回广告顺序
                list.remove(0);
                bean.setNetwork(list);
                firstCome = true;
                SortShow();
            }
        }
    }

    //sigmob激励视频广告----------------start-----------------


    private void loadSigmob(String posId) {
        windRewardedVideoAd = WindRewardedVideoAd.sharedInstance();
        //placementId 必填,USER_ID,OPTIONS可不填，
        request = new WindRewardAdRequest(posId, "", null);
        windRewardedVideoAd.loadAd(request);
        windRewardedVideoAd.setWindRewardedVideoAdListener(new WindRewardedVideoAdListener() {

            //仅sigmob渠道有回调，聚合其他平台无次回调
            @Override
            public void onVideoAdPreLoadSuccess(String placementId) {
                Log.e(TAG, "sigmob激励视频广告数据返回成功");
            }

            //仅sigmob渠道有回调，聚合其他平台无次回调
            @Override
            public void onVideoAdPreLoadFail(String placementId) {
                Log.e(TAG, "sigmob激励视频广告数据返回失败");
            }

            @Override
            public void onVideoAdLoadSuccess(String placementId) {
                Log.e(TAG, "sigmob激励视频广告缓存加载成功");
                if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
                    showSigmob();
                } else {
                    recordRenderSuccess(Constants.SIGMOB);
                    if (firstCome) {
                        showSigmob();
                        firstCome = false;
                    }
                }
            }

            @Override
            public void onVideoAdPlayStart(String placementId) {
                Log.e(TAG, "sigmob激励视频广告播放开始");
                //广告已经显示
                if (mListener != null) {
                    mListener.onAdShow(Constants.SIGMOB);
                }
            }

            @Override
            public void onVideoAdPlayEnd(String s) {
                Log.e(TAG, "sigmob激励视频广告播放结束");
                //激励视频播放完毕
                if (mListener != null) {
                    mListener.onVideoComplete(Constants.SIGMOB);
                }

            }

            @Override
            public void onVideoAdClicked(String placementId) {
                Log.e(TAG, "sigmob激励视频广告CTA点击事件监听");
                //广告被点击
                if (mListener != null) {
                    mListener.onAdClick(Constants.SIGMOB);
                }
                AdStatistical.trackAD(mContext, Constants.SIGMOB, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
            }

            //WindRewardInfo中isComplete方法返回是否完整播放
            @Override
            public void onVideoAdClosed(WindRewardInfo windRewardInfo, String placementId) {
                if (windRewardInfo.isComplete()) {
                    Log.e(TAG, "sigmob激励视频广告完整播放");
                    //广告被关闭
                    if (mListener != null) {
                        mListener.onAdClose(Constants.SIGMOB);
                    }
                } else {
                    Log.e(TAG, "sigmob激励视频广告关闭");
                }
            }

            /**
             * 加载广告错误回调
             * WindAdError 激励视频错误内容
             * placementId 广告位
             */
            @Override
            public void onVideoAdLoadError(WindAdError windAdError, String placementId) {
                Log.e(TAG, "sigmob激励视频加载广告错误" + windAdError.getMessage());
                AdStatistical.trackAD(mContext, Constants.SIGMOB, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
                //加载错误
                if (mListener != null) {
                    mListener.onLoadFaild(Constants.SIGMOB, windAdError.getErrorCode(), windAdError.getMessage());
                    //  mListener.onAdClose(Constants.SIGMOB);
                }
            }


            /**
             * 播放错误回调
             * WindAdError 激励视频错误内容
             * placementId 广告位
             */
            @Override
            public void onVideoAdPlayError(WindAdError windAdError, String placementId) {
                Log.e(TAG, "sigmob激励视频加载广告错误" + windAdError.getMessage());
            }

        });


    }


    //展示sigmob广告
    private void showSigmob() {
        if (isShow) return;
        if (windRewardedVideoAd != null) {
            try {
                //检查广告是否准备完毕
                if (windRewardedVideoAd.isReady(request.getPlacementId())) {
                    //广告播放
                    windRewardedVideoAd.show((Activity) mContext, request);
                    windRewardedVideoAd = null;
                    isShow = true;
                    AdStatistical.trackAD(mContext, Constants.SIGMOB, POS_ID, Constants.STATUS_CODE_TRUE, Constants.STATUS_CODE_FALSE);
                    destory();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    //sigmob激励视频广告----------------end-----------------


    /**
     * 销毁
     */
    public void destory() {
        if (rewardVideoAD != null) {
            rewardVideoAD = null;
        }
        if (mttRewardVideoAd != null) {
            mttRewardVideoAd = null;
        }
        if (windRewardedVideoAd != null) {
            windRewardedVideoAd = null;
        }
        if (mobiReward != null) {
            mobiReward = null;
        }
    }

    /**
     * 判断当前加载完成的广告是否与服务器返回的列表中的第一个一致
     * 当前请求 每渲染成功一个 则像list中添加一个 并判断是否与要加载的数量一致
     *
     * @param channel
     */
    private void recordRenderSuccess(String channel) {
        if (isShow) return;
        if (bean.getNetwork().size() > 0) {
            if (bean.getNetwork().get(0).getSdk().equals(channel) && !isShow) {
                SortShow();
                if (handler != null && handlerRun != null) {
                    handler.removeCallbacks(handlerRun);
                }
                return;
            }
        }
        loadedList.add(channel);
        if (loadedList.size() >= bean.getNetwork().size() && !isShow) {
            SortShow();
            if (handler != null && handlerRun != null) {
                handler.removeCallbacks(handlerRun);
            }
        }
    }

    public static class Builder {
        private Context mContext;
        private boolean mDeepLink;
        private ScenarioEnum scenario;
        private RewardVideoLoadListener mListener;
        private String PosID = "";

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setSupportDeepLink(boolean isDeepLink) {
            this.mDeepLink = isDeepLink;
            return this;
        }

        public Builder setRewardVideoLoadListener(RewardVideoLoadListener listener) {
            this.mListener = listener;
            return this;
        }

        public Builder setScenario(ScenarioEnum scenario) {
            this.scenario = scenario;
            return this;
        }

        public Builder setPosID(String PosID) {
            this.PosID = PosID;
            return this;
        }


        public RewardVideoAd build() {
            return new RewardVideoAd(this);
        }
    }


}
