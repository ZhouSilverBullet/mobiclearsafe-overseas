package com.example.adtest.cache;

import android.content.Context;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.example.adtest.bean.AdBean;
import com.example.adtest.bean.CacheGDTBean;
import com.example.adtest.bean.CacheMobiBean;
import com.example.adtest.bean.CacheTTBean;
import com.example.adtest.bean.ConfigItemBean;
import com.example.adtest.config.TTAdManagerHolder;
import com.example.adtest.manager.Constants;
import com.example.adtest.statistical.AdStatistical;
import com.example.adtest.utils.DateUtils;
import com.mobi.adsdk.nativeexpress.MobiAdSize;
import com.mobi.adsdk.nativeexpress.MobiNativeExpressAd;
import com.mobi.adsdk.nativeexpress.MobiNativeExpressAdView;
import com.mobi.adsdk.utils.ADError;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author : liangning
 * date : 2019-12-11  21:16
 */
public class NativeExpressCache {

    private static volatile Map<String, List<CacheTTBean>> mTTMap = new HashMap<>();
    private static volatile Map<String, List<CacheGDTBean>> mGDTMap = new HashMap<>();
    private static volatile Map<String, List<CacheMobiBean>> mMobiMap = new HashMap<>();
    private static final long CacheTime = 1800000;

    /**
     * 缓存
     * 加载分组的信息流广告
     */
    public static void loadGroupNative(String[] PosIDs, Context context) {
        int size = PosIDs.length;
        for (int i = 0; i < size; i++) {
            ConfigItemBean bean = Constants.getAdItem(PosIDs[i], context);
            if (bean != null) {
                List<AdBean> list = bean.getNetwork();
                if (list != null && list.size() >= 1) {
                    if (bean.getSort_type() == Constants.SORT_TYPE_ORDER) {//如果按顺序显示 则只加载默认第一个
                        AdBean item = list.get(0);
                        if (item.getSdk().equals(Constants.TT_KEY)) {
                            List<CacheTTBean> ttList = mTTMap.get(item.getParameterBean().getPosid());
                            if (ttList == null || ttList.size() <= 0) {
                                if (!TTAdManagerHolder.getInitStatus()) {
                                    TTAdManagerHolder.singleInit(context.getApplicationContext(), item.getParameterBean().getAppid(), item.getParameterBean().getAppname());
                                }
                                loadTTExpressAd(item.getParameterBean().getPosid(), context, bean.getPosid());
                            }
                        }
                        if (item.getSdk().equals(Constants.GDT_KEY)) {
                            List<CacheGDTBean> gdtList = mGDTMap.get(item.getParameterBean().getPosid());
                            if (gdtList == null || gdtList.size() <= 0) {
                                loadGDTExpressAd(item.getParameterBean().getAppid(), item.getParameterBean().getPosid(), context, bean.getPosid());
                            }
                        }
                        if (item.getSdk().equals(Constants.MOBI_KEY)) {
                            List<CacheMobiBean> mobiList = mMobiMap.get(item.getParameterBean().getPosid());
                            if (mobiList == null || mobiList.size() <= 0) {
                                loadMobiExpress(item.getParameterBean().getAppid(), item.getParameterBean().getPosid(), context, bean.getPosid());
                            }
                        }
                    } else {
                        int listsize = list.size();
                        for (int j = 0; j < listsize; j++) {
                            AdBean item = list.get(j);
                            if (item.getSdk().equals(Constants.TT_KEY)) {
                                List<CacheTTBean> ttList = mTTMap.get(item.getParameterBean().getPosid());
                                if (ttList == null || ttList.size() <= 0) {
                                    if (!TTAdManagerHolder.getInitStatus()) {
                                        TTAdManagerHolder.singleInit(context.getApplicationContext(), item.getParameterBean().getAppid(), item.getParameterBean().getAppname());
                                    }
                                    loadTTExpressAd(item.getParameterBean().getPosid(), context, bean.getPosid());
                                }
                            }
                            if (item.getSdk().equals(Constants.GDT_KEY)) {
                                List<CacheGDTBean> gdtList = mGDTMap.get(item.getParameterBean().getPosid());
                                if (gdtList == null || gdtList.size() <= 0) {
                                    loadGDTExpressAd(item.getParameterBean().getAppid(), item.getParameterBean().getPosid(), context, bean.getPosid());
                                }
                            }
                            if (item.getSdk().equals(Constants.MOBI_KEY)) {
                                List<CacheMobiBean> mobiList = mMobiMap.get(item.getParameterBean().getPosid());
                                if (mobiList == null || mobiList.size() <= 0) {
                                    loadMobiExpress(item.getParameterBean().getAppid(), item.getParameterBean().getPosid(), context, bean.getPosid());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据分组清除缓存的信息流广告
     *
     * @param PosID
     */
    public static void clearGroupNative(String[] PosID) {
        int size = PosID.length;
        for (int i = 0; i < size; i++) {
            ConfigItemBean bean = Constants.getAdItemNoContext(PosID[i]);
            if (bean != null) {
                List<AdBean> list = bean.getNetwork();
                if (list != null) {
                    int listsize = list.size();
                    for (int j = 0; j < listsize; j++) {
                        AdBean item = list.get(j);
                        if (item.getSdk().equals(Constants.TT_KEY)) {
                            mTTMap.remove(item.getParameterBean().getPosid());
                        }
                        if (item.getSdk().equals(Constants.GDT_KEY)) {
                            mGDTMap.remove(item.getParameterBean().getPosid());
                        }
                        if (item.getSdk().equals(Constants.MOBI_KEY)) {
                            mMobiMap.remove(item.getParameterBean().getPosid());
                        }
                    }
                }
            }
        }
    }


    /**
     * 得到穿山甲 广告缓存列表
     *
     * @param POSID 广告平台生成的POSID
     * @return
     */
    public static TTNativeExpressAd getTTNative(String POSID) {

        List<CacheTTBean> mList = mTTMap.get(POSID);
        if (mList == null || mList.size() <= 0) {
            return null;
        }
        TTNativeExpressAd ad = mList.get(0).mNative;
        mList.remove(0);
        return ad;
    }


    /**
     * 设置穿山甲缓存数据
     *
     * @param TTPosID
     * @param list
     */
    public static void setTTNativeList(String TTPosID, List<CacheTTBean> list) {
        mTTMap.put(TTPosID, list);
    }

    /**
     * 得到广点通广告缓存列表
     *
     * @param GDTPosID
     * @return
     */
    public static NativeExpressADView getGDTNative(String GDTPosID) {
        List<CacheGDTBean> mList = mGDTMap.get(GDTPosID);
        if (mList == null || mList.size() <= 0) {
            return null;
        }
        return mList.get(0).mNative;
    }

    /**
     * 设置广点通广告缓存列表
     *
     * @param GDTPosID
     * @param list
     */
    public static void setGDTNativeList(String GDTPosID, List<CacheGDTBean> list) {
        mGDTMap.put(GDTPosID, list);
    }

    /**
     * 得到mobi广告缓存列表
     *
     * @param MobiPosID
     * @return
     */
    public static MobiNativeExpressAdView getMobiNative(String MobiPosID) {
        List<CacheMobiBean> mList = mMobiMap.get(MobiPosID);
        if (mList == null || mList.size() <= 0) {
            return null;
        }
        MobiNativeExpressAdView mobiNativeExpressAdView = mList.get(0).mobiNativeExpressAdView;
        mList.remove(0);
        return mobiNativeExpressAdView;
    }


    /**
     * 加载穿山甲广告
     * 缓存穿山甲广告 信息流广告每次从缓存中读取一条  则需要跳用此方法添加一条缓存
     *
     * @param CodeID 广告平台POSID
     * @param POS_ID 自有平台POSID
     */
    public static void loadTTExpressAd(final String CodeID,
                                       final Context mContext,
                                       final String POS_ID) {
        try {
            int count = 1;
            if (mTTMap.get(CodeID) == null || mTTMap.get(CodeID).size() == 0) {
                count = 2;
            } else {
                List<CacheTTBean> list = mTTMap.get(CodeID);
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    if (DateUtils.getNowTimeLong() - list.get(i).create_time >= CacheTime) {
                        count = 2;
                    }
                }
            }

            TTAdNative mTTAdNative = TTAdManagerHolder.get().createAdNative(mContext);

            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(CodeID)
                    .setSupportDeepLink(true)
                    .setAdCount(count)
                    .setExpressViewAcceptedSize(338, 0)
                    .setImageAcceptedSize(640, 320)
                    .build();
            final int finalCount = count;
            mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
                //加载失败
                @Override
                public void onError(int i, String s) {
                    // Log.e("ADLoad穿山甲", "广告加载失败" + i + s);
                    AdStatistical.trackAD(mContext, Constants.TT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
//                mBearingView.removeAllViews();
//                if (mListener != null) {
//                    mListener.onLoadFaild(Constants.TT_KEY, i, s);
//                }
                }

                //加载成功
                @Override
                public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                    if (list == null || list.size() <= 0) {
                        return;
                    }
                    //    Log.e("ADLoad穿山甲", "广告加载成功");
                    if (finalCount >= 2 && list.size() >= 2) {
                        List<TTNativeExpressAd> mList = list;
                        List<CacheTTBean> ttList = new ArrayList<>();
                        for (int i = 0; i < mList.size(); i++) {
                            CacheTTBean bean = new CacheTTBean();
                            bean.mNative = mList.get(i);
                            ttList.add(bean);
                        }
                        mTTMap.put(CodeID, ttList);
                        return;
                    }
                    if (finalCount == 1 && list.size() == 1) {
                        List<CacheTTBean> mList = mTTMap.get(CodeID);
                        if (mList == null) {
                            mList = new ArrayList<>();
                        }
                        CacheTTBean bean = new CacheTTBean();
                        bean.mNative = list.get(0);
                        mList.add(0, bean);
                        mTTMap.put(CodeID, mList);
                    }
//                mTTAd = list.get(0);
//                bindTTAdListener(mTTAd);
//                mTTAd.render();
//                recordRenderSuccess(Constants.TT_KEY);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 加载广点通广告
     *
     * @param APPID       广告平台APPID
     * @param POSID       广告平台POSID
     * @param SELF_POS_ID 自有平台POSID
     */
    public static void loadGDTExpressAd(String APPID, final String POSID, final Context mContext, final String SELF_POS_ID) {
        int count = 1;
        if (mGDTMap.get(POSID) == null || mGDTMap.get(POSID).size() == 0) {
            count = 2;
        } else {
            List<CacheGDTBean> list = mGDTMap.get(POSID);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (DateUtils.getNowTimeLong() - list.get(i).create_time >= CacheTime) {
                    count = 2;
                }
            }
        }

        final int finalCount = count;
        NativeExpressAD nativeExpressAD = new NativeExpressAD(mContext, getADSize(), APPID, POSID,
                new NativeExpressAD.NativeExpressADListener() {
                    @Override
                    public void onADLoaded(List<NativeExpressADView> list) {
                        //加载广告成功
//                        if (nativeExpressADView != null) {
//                            nativeExpressADView.destroy();
//                        }
                        if (list == null || list.size() <= 0) {
                            return;
                        }
                        //      Log.e("ADLoad广点通", "广告加载成功");
                        if (finalCount >= 2 && list.size() >= 2) {
                            List<NativeExpressADView> mList = list;
                            List<CacheGDTBean> gdtList = new ArrayList<>();
                            for (int i = 0; i < mList.size(); i++) {
                                CacheGDTBean bean = new CacheGDTBean();
                                bean.mNative = mList.get(i);
                                gdtList.add(bean);
                            }
                            mGDTMap.put(POSID, gdtList);
                            return;
                        }
                        if (finalCount == 1 && list.size() == 1) {
                            List<CacheGDTBean> mList = mGDTMap.get(POSID);
                            if (mList == null) {
                                mList = new ArrayList<>();
                            }
                            CacheGDTBean bean = new CacheGDTBean();
                            bean.mNative = list.get(0);
                            mList.add(0, bean);
                            mGDTMap.put(POSID, mList);
                        }
//                        if (list != null && list.size() > 0) {
//                            nativeExpressADView = list.get(0);
//                        }
//                        if (nativeExpressADView != null) {
//                            if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
//                                //如果是视频广告 可添加视频播放监听
//                                nativeExpressADView.setMediaListener(listener);
//                            }
//                        }
//                        nativeExpressADView.render();
//                        recordRenderSuccess(Constants.GDT_KEY);
                    }

                    @Override
                    public void onRenderFail(NativeExpressADView nativeExpressADView) {
                        //广告渲染失败
                    }

                    @Override
                    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                        //广告渲染成功
//                        if (firstCome) {
//                            renderGDTAD();
//                            firstCome = false;
//                        }
//                        if (mListener != null) {
//                            mListener.onAdRenderSuccess(Constants.GDT_KEY);
//                        }
                    }

                    @Override
                    public void onADExposure(NativeExpressADView nativeExpressADView) {
                        //广告曝光
//                        if (mListener != null) {
//                            mListener.onAdShow(Constants.GDT_KEY);
//                        }
                    }

                    @Override
                    public void onADClicked(NativeExpressADView nativeExpressADView) {
                        //广告被点击
//                        if (mListener != null) {
//                            mListener.onAdClick(Constants.GDT_KEY);
//                        }
                        AdStatistical.trackAD(mContext, Constants.GDT_KEY, SELF_POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
                    }

                    @Override
                    public void onADClosed(NativeExpressADView nativeExpressADView) {
                        //广告关闭
//                        if (mBearingView != null && mBearingView.getChildCount() > 0) {
//                            mBearingView.removeAllViews();
//                            mBearingView.setVisibility(View.GONE);
//                        }
//                        if (mListener != null) {
//                            mListener.onAdDismissed(Constants.GDT_KEY);
//                        }
                    }

                    @Override
                    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

                    }

                    @Override
                    public void onNoAD(AdError adError) {
                        //  Log.e("ADLoad广点通", "广告加载失败" + adError.getErrorCode() + adError.getErrorMsg());
                        AdStatistical.trackAD(mContext, Constants.GDT_KEY, SELF_POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
                        //加载失败
//                        if (mListener != null) {
//                            mListener.onLoadFaild(Constants.GDT_KEY, adError.getErrorCode(), adError.getErrorMsg());
//                        }
                    }
                });
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS)//设置什么网络情况下可以自动播放视频
                .setAutoPlayMuted(true)//设置自动播放视频时是否静音
                .build());
//        nativeExpressAD.setMaxVideoDuration(15);//设置视频最大时长
        nativeExpressAD.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO);
        nativeExpressAD.loadAD(count);
    }

    private static ADSize getADSize() {
        return new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT);
    }

    public static void loadMobiExpress(String APPID, final String POSID, final Context mContext,
                                       final String SELF_POS_ID) {
        int count = 1;
        if (mMobiMap.get(POSID) == null || mMobiMap.get(POSID).size() <= 0) {
            count = 2;
        } else {
            List<CacheMobiBean> list = mMobiMap.get(POSID);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (DateUtils.getNowTimeLong() - list.get(i).create_time >= CacheTime) {
                    count = 2;
                }
            }
        }
        MobiNativeExpressAd mobiNativeExpressAd = new MobiNativeExpressAd(mContext, APPID, POSID, getMobiADSize(),
                new MobiNativeExpressAd.MobiNativeExpressAdListener() {
                    @Override
                    public void onADLoaded(List<MobiNativeExpressAdView> list) {
                        if (list == null || list.size() <= 0) {
                            return;
                        }
                        if (list.size() >= 2) {
                            List<MobiNativeExpressAdView> mList = list;
                            List<CacheMobiBean> mobiList = new ArrayList<>();
                            for (int i = 0; i < mList.size(); i++) {
                                CacheMobiBean bean = new CacheMobiBean();
                                bean.mobiNativeExpressAdView = mList.get(i);
                                mobiList.add(bean);
                            }
                            mMobiMap.put(POSID, mobiList);
                        } else {
                            List<CacheMobiBean> mList = mMobiMap.get(POSID);
                            if (mList == null) {
                                mList = new ArrayList<>();
                            }
                            CacheMobiBean bean = new CacheMobiBean();
                            bean.mobiNativeExpressAdView = list.get(0);
                            mList.add(0, bean);
                            mMobiMap.put(POSID, mList);
                        }


                    }

                    @Override
                    public void onRenderFail(MobiNativeExpressAdView mobiNativeExpressAdView) {

                    }

                    @Override
                    public void onRenderSuccess(MobiNativeExpressAdView mobiNativeExpressAdView) {

                    }

                    @Override
                    public void onADExposure(MobiNativeExpressAdView mobiNativeExpressAdView) {

                    }

                    @Override
                    public void onADClicked(MobiNativeExpressAdView mobiNativeExpressAdView) {
                        AdStatistical.trackAD(mContext, Constants.MOBI_KEY, SELF_POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
                    }

                    @Override
                    public void onADClosed(MobiNativeExpressAdView mobiNativeExpressAdView) {

                    }

                    @Override
                    public void onNoAD(ADError adError) {
                        AdStatistical.trackAD(mContext, Constants.MOBI_KEY, SELF_POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
                    }
                });
        mobiNativeExpressAd.loadAD(count);
    }

    private static MobiAdSize getMobiADSize() {
        return new MobiAdSize(MobiAdSize.FULL_WIDTH, MobiAdSize.AUTO_HEIGHT);
    }
}
