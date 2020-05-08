package com.example.adtest.fullscreenvideo;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.example.adtest.bean.AdBean;
import com.example.adtest.bean.ConfigItemBean;
import com.example.adtest.config.TTAdManagerHolder;
import com.example.adtest.manager.AdScenario;
import com.example.adtest.manager.Constants;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.utils.SdkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2019-12-24  15:19
 */
public class FullScreenVideoAD {

    private static final String TAG = "FullScreenVideoAD";
    private boolean firstCome = false;//如果800毫秒以后 广告都未加载完 则启用先到先得
    private boolean isShow = false;//是否已经显示过广告
    private Context mContext;
    private FullScreenVideoListener mListener;
    private String POS_ID;
    private ScenarioEnum scenario;
//    private TTAdNative mTTAdNative;
//    private TTFullScreenVideoAd mttFullVideoAd;
    private ConfigItemBean bean;
    private List<String> loadedList = new ArrayList<>();
    private Handler handler = new Handler();
    private Runnable handlerRun = new Runnable() {
        @Override
        public void run() {
            SortShow();
        }
    };

    public FullScreenVideoAD(Builder builder) {
        mContext = builder.mContext;
        this.mListener = builder.mListener;
        this.POS_ID = builder.POSID;
        this.scenario = builder.scenario;
        if (TextUtils.isEmpty(POS_ID)) {//如果没有直接设置POSID 则根据场景值获取
            POS_ID = AdScenario.getSelfPosId(this.scenario);
        }
        if (mListener != null) {
            mListener.onLoadFaild("bean 为null", 0, "");
        }
//        bean = Constants.getAdItem(POS_ID, mContext);
//        if (bean == null) {
//
//            Log.e(TAG, "广告相关数据缺失，请先调用SDKManager.init()");
//            return;
//        }
//        if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
//            loadAdForService();
//        } else {
//            SortLoad();
//        }
    }

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
                loadTTAD(item.getParameterBean().getPosid());
                if (handler != null && handlerRun != null) {
                    handler.removeCallbacks(handlerRun);
                }
                index++;
                Constants.SortParameterMap.put(bean.getPosid(), index);
            }
//            else if (plant.equals(Constants.GDT_KEY)) {
//                loadGDTRewardViewAd(item.getParameterBean().getAppid(), item.getParameterBean().getPosid());
//                if (handler != null && handlerRun != null) {
//                    handler.removeCallbacks(handlerRun);
//                }
//                index++;
//                Constants.SortParameterMap.put(bean.getPosid(), index);
//            } else if (plant.equals(Constants.SIGMOB)) {
//                loadSigmob(item.getParameterBean().getPosid());
//                if (handler != null && handlerRun != null) {
//                    handler.removeCallbacks(handlerRun);
//                }
//                index++;
//                Constants.SortParameterMap.put(bean.getPosid(), index);
//            } else if (plant.equals(Constants.MOBI_KEY)) {
//                loadMobi(item.getParameterBean().getAppid(), item.getParameterBean().getPosid());
//                if (handler != null && handlerRun != null) {
//                    handler.removeCallbacks(handlerRun);
//                }
//                index++;
//                Constants.SortParameterMap.put(bean.getPosid(), index);
//            }
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
     * 按顺序判断显示哪个广告
     */
    private void SortShow() {
        List<AdBean> netList = bean.getNetwork();
        int size = netList.size();
//        for (int i = 0; i < size; i++) {
//            AdBean item = netList.get(i);
//            if (item.getSdk().equals(Constants.TT_KEY) && mttFullVideoAd != null) {
//                if (showTTAD()) {
//                    break;
//                }
//            }
////            if (item.getSdk().equals(Constants.GDT_KEY) && rewardVideoAD != null && gdtAdLoaded) {
////                if (showGDTVideo()) {
////                    break;
////                }
////            }
////            if (item.getSdk().equals(Constants.SIGMOB) && windRewardedVideoAd != null && request != null) {
////                if (showSigmob()) {
////                    break;
////                }
////            }
////            if (item.getSdk().equals(Constants.MOBI_KEY) && mobiReward != null && mobiAdLoaded) {
////                if (showMobiVideo()) {
////                    break;
////                }
////            }
//            if (i >= size - 1) {
//                firstCome = true;
//            }
//        }
    }


    /**
     * 按顺序加载
     */
    private void SortLoad() {
        List<AdBean> netList = bean.getNetwork();
        int size = netList.size();
        for (int i = 0; i < size; i++) {
            AdBean item = netList.get(i);
            if (item.getSdk().equals(Constants.TT_KEY)) {
                if (!TTAdManagerHolder.getInitStatus()) {
                    TTAdManagerHolder.singleInit(mContext.getApplicationContext(), item.getParameterBean().getAppid(), item.getParameterBean().getAppname());
                }
                loadTTAD(item.getParameterBean().getPosid());
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

    /**
     * 加载穿山甲全屏视频
     */
    private void loadTTAD(String CodeId) {
        if (!TTAdManagerHolder.getInitStatus()) {
            TTAdManagerHolder.singleInit(mContext.getApplicationContext(), Constants.TT_APPID, Constants.TT_APPNAME);
        }
//        mTTAdNative = TTAdManagerHolder.get().createAdNative(mContext.getApplicationContext());
//        AdSlot adSlot = new AdSlot.Builder()
//                .setCodeId(CodeId)
//                .setSupportDeepLink(true)
//                .setImageAcceptedSize(1080, 1920)
//                .setOrientation(TTAdConstant.VERTICAL)
//                .build();
//        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
//            @Override
//            public void onError(int i, String s) {
//                if (mListener != null) {
//                    mListener.onLoadFaild(Constants.TT_KEY, i, s);
//                }
//            }
//
//            @Override
//            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ad) {
//                mttFullVideoAd = ad;
//                mttFullVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
//                    @Override
//                    public void onAdShow() {
//                        if (mListener != null) {
//                            mListener.onAdShow(Constants.TT_KEY);
//                        }
//                    }
//
//                    @Override
//                    public void onAdVideoBarClick() {
//                        if (mListener != null) {
//                            mListener.onAdClick(Constants.TT_KEY);
//                        }
//                        AdStatistical.trackAD(mContext, Constants.TT_KEY, POS_ID,
//                                Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
//                    }
//
//                    @Override
//                    public void onAdClose() {
//                        //广告关闭
//                        if (mListener != null) {
//                            mListener.onAdClose(Constants.TT_KEY);
//                        }
//                    }
//
//                    @Override
//                    public void onVideoComplete() {
//                        //播放完成
//                        if (mListener != null) {
//                            mListener.onVideoComplete(Constants.TT_KEY);
//                        }
//                    }
//
//                    @Override
//                    public void onSkippedVideo() {
//                        if (mListener != null) {
//                            mListener.onSkippedVideo(Constants.TT_KEY);
//                        }
//                    }
//                });
//                if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
//                    showTTAD();
//                } else {
//                    recordRenderSuccess(Constants.TT_KEY);
//                    if (firstCome) {
//                        showTTAD();
//                        firstCome = false;
//                    }
//                }
//            }
//
//            @Override
//            public void onFullScreenVideoCached() {
//                Log.e(TAG, "广告已缓存");
//            }
//        });
    }

    /**
     * 显示穿山甲广告
     */
    private boolean showTTAD() {
        if (isShow) return true;
//        if (mttFullVideoAd != null) {
//            mttFullVideoAd.showFullScreenVideoAd((Activity) mContext);
//            mttFullVideoAd = null;
//            isShow = true;
//            AdStatistical.trackAD(mContext, Constants.TT_KEY, POS_ID, Constants.STATUS_CODE_TRUE, Constants.STATUS_CODE_FALSE);
//            destory();
//            return true;
//        }
        return false;
    }

    private void destory() {
//        if (mttFullVideoAd != null) {
//            mttFullVideoAd = null;
//        }
    }

    public static class Builder {

        private Context mContext;
        private FullScreenVideoListener mListener;
        private String POSID;
        private ScenarioEnum scenario;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setFullScreenVideoListener(FullScreenVideoListener listener) {
            this.mListener = listener;
            return this;
        }

        public Builder setPosID(String POSID) {
            this.POSID = POSID;
            return this;
        }

        public Builder setScenario(ScenarioEnum scenario) {
            this.scenario = scenario;
            return this;
        }

        public FullScreenVideoAD builde() {
            return new FullScreenVideoAD(this);
        }

    }


}
