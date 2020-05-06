package com.example.adtest.nativeexpress;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.example.adtest.bean.AdBean;
import com.example.adtest.bean.ConfigItemBean;
import com.example.adtest.cache.NativeExpressCache;
import com.example.adtest.config.TTAdManagerHolder;
import com.example.adtest.manager.AdScenario;
import com.example.adtest.manager.Constants;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.statistical.AdStatistical;
import com.example.adtest.utils.SdkUtils;
import com.mobi.adsdk.nativeexpress.MobiAdSize;
import com.mobi.adsdk.nativeexpress.MobiNativeExpressAd;
import com.mobi.adsdk.nativeexpress.MobiNativeExpressAdView;
import com.mobi.adsdk.utils.ADError;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2019-11-07  16:33
 */
public class NativeExpressAd {
    private static final String TAG = "NativeExpressAd";
    private static String POS_ID = "";//自有POSID

    private Context mContext;
    private NativeLoadListener mListener;
    private float ADViewWidth;
    private float ADViewHeight;
    private boolean mDeepLink;
    private boolean mHeightAuto;//高度自适应
    private ViewGroup mBearingView;
    private int DEFAULT_COUNT = 1;//加载广告数量  不考虑缓存 默认为1
    private TTAdNative mTTAdNative;
    private TTNativeExpressAd mTTAd;
    private NativeExpressAD nativeExpressAD;
    private NativeExpressADView nativeExpressADView;
    private boolean firstCome = false;//如果800毫秒以后 广告都未加载完 则启用先到先得
    private boolean isShowed = false;//广告是否已经添加到载体view上
    private List<String> loadedList = new ArrayList<>();
    private ConfigItemBean bean;
    private ScenarioEnum scenario;//场景枚举 通过此值获取到对应的 自有posid
    private Handler handler = new Handler();
    private Runnable handlerRun = new Runnable() {
        @Override
        public void run() {
            sortRender(bean);
        }
    };
    private NativeInteractionTypeListener typeListener;
    //mobisdk
    private MobiNativeExpressAd mobiNativeExpressAd;
    private MobiNativeExpressAdView mobiNativeExpressAdView;

    public NativeExpressAd(Builder builder) {
        this.mContext = builder.mContext;
        this.ADViewWidth = builder.ADViewWidth;
        this.ADViewHeight = builder.ADViewHeight;
        this.mDeepLink = builder.mDeepLink;
        this.mHeightAuto = builder.mHeightAuto;
        this.mBearingView = builder.mBearingView;
        this.mListener = builder.mListener;
        this.scenario = builder.scenario;
        this.DEFAULT_COUNT = builder.mCount;
        this.typeListener = builder.typeListener;
        this.POS_ID = builder.PosID;
        if (mBearingView != null) {
            mBearingView.setVisibility(View.GONE);
        }
//        this.POS_ID = "1024018";//默认信息流 测试用
        if (TextUtils.isEmpty(POS_ID)) {//如果没有直接设置POSID 则根据场景值获取
            POS_ID = AdScenario.getSelfPosId(this.scenario);
        }
//        Log.e("使用的信息流广告:",POS_ID);
//        if (TextUtils.isEmpty(POS_ID)) {
//            POS_ID = Constants.default_native_posid;
//        }
        bean = Constants.getAdItem(POS_ID, mContext);
//        if (bean == null) {
//            POS_ID = Constants.default_native_posid;
//            bean = Constants.getAdItem(POS_ID, mContext);
//        }
        if (bean == null) {
            Log.e(TAG, "广告相关数据缺失，请先调用SDKManager.init()");
            if (typeListener != null) {
                typeListener.error();
            }
            return;
        }
        if (bean.getSort_type() == Constants.SORT_TYPE_ORDER) {//如果只按照优先级排序则只取优先级高的
            if (!loadFromCacheForOrder(bean)) {
                loadAdNow(bean);
            }
        } else if (bean.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
            loadAdFromCacheForServer(bean);
        } else {
            loadFromCache(bean);
        }
//        sortLoad(bean);
//        if (handler != null && handlerRun != null) {
//            handler.postDelayed(handlerRun, Constants.bean.getAd_adk_req_timeout());
//        }
//
//        if (bean.getSort_type() == Constants.SORT_TYPE_ORDER) {
//
//        } else if (bean.getSort_type() == Constants.SORT_TYPE_PRICE) {
//            firstCome = true;
//        } else if (bean.getSort_type() == Constants.SORT_TYPE_ORDER__PRICE) {
//            firstCome = true;
//        } else {
//            firstCome = true;
//        }
    }

    /**
     * 按照服务器返回的固定顺序 sort_type 为4时 的加载方式
     *
     * @param bean
     */
    private void loadAdFromCacheForServer(ConfigItemBean bean) {
        int index = Constants.SortParameterMap.get(bean.getPosid());
        if (bean.getSort_parameter() != null && bean.getSort_parameter().size() > 0) {
            int listIndext = index % bean.getSort_parameter().size();//累加的值 对集合item数量取模
            String plant = bean.getSort_parameter().get(listIndext);//需要显示的广告平台
            AdBean item = SdkUtils.getShowAdBean(bean, plant);
            if (plant.equals(Constants.TT_KEY)) {
                if (item != null) {
                    if (!TTAdManagerHolder.getInitStatus()) {
                        TTAdManagerHolder.singleInit(mContext.getApplicationContext(), item.getParameterBean().getAppid(), item.getParameterBean().getAppname());
                    }
                    mTTAd = NativeExpressCache.getTTNative(item.getParameterBean().getPosid());
                    NativeExpressCache.loadTTExpressAd(item.getParameterBean().getPosid(), mContext, POS_ID);
                    if (mTTAd != null) {
                        bindTTAdListener(mTTAd);
                        mTTAd.render();
                        renderTTAD();
                        if (handler != null && handlerRun != null) {
                            handler.removeCallbacks(handlerRun);
                        }
                        index++;
                        Constants.SortParameterMap.put(bean.getPosid(), index);
                    }
                }
            } else if (plant.equals(Constants.GDT_KEY)) {
                if (item != null) {
                    nativeExpressADView = NativeExpressCache.getGDTNative(item.getParameterBean().getPosid());
                    NativeExpressCache.loadGDTExpressAd(item.getParameterBean().getAppid(), item.getParameterBean().getPosid(), mContext, POS_ID);
                    if (nativeExpressADView != null) {
                        if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                            //如果是视频广告 可添加视频播放监听
                            nativeExpressADView.setMediaListener(listener);
                        }
                        nativeExpressADView.render();
                        renderGDTAD();
                        if (handler != null && handlerRun != null) {
                            handler.removeCallbacks(handlerRun);
                        }
                        index++;
                        Constants.SortParameterMap.put(bean.getPosid(), index);
                    }
                }
            }
        } else {
            loadAdNow(bean);
        }
    }

    /**
     * 无可用预加载广告 需现加载
     *
     * @param bean
     */
    private void loadAdNow(ConfigItemBean bean) {
        sortLoad(bean);
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

    /**
     * 只读取network中第一个
     * 返回值为 true 有可使用广告  false无可使用广告 需去现加载
     */
    private boolean loadFromCacheForOrder(ConfigItemBean bean) {
        List<AdBean> list = bean.getNetwork();
        if (list != null && list.size() >= 1) {
            AdBean item = list.get(0);
            if (item.getSdk().equals(Constants.TT_KEY)) {
                if (!TTAdManagerHolder.getInitStatus()) {
                    TTAdManagerHolder.singleInit(mContext.getApplicationContext(), item.getParameterBean().getAppid(), item.getParameterBean().getAppname());
                }
                mTTAd = NativeExpressCache.getTTNative(item.getParameterBean().getPosid());
                NativeExpressCache.loadTTExpressAd(item.getParameterBean().getPosid(), mContext, POS_ID);
                if (mTTAd != null) {
                    bindTTAdListener(mTTAd);
                    mTTAd.render();
                    recordRenderSuccess(Constants.TT_KEY);
                    return true;
                }
            }
            if (item.getSdk().equals(Constants.GDT_KEY)) {
                nativeExpressADView = NativeExpressCache.getGDTNative(item.getParameterBean().getPosid());
                NativeExpressCache.loadGDTExpressAd(item.getParameterBean().getAppid(), item.getParameterBean().getPosid(), mContext, POS_ID);
                if (nativeExpressADView != null) {
                    if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                        //如果是视频广告 可添加视频播放监听
                        nativeExpressADView.setMediaListener(listener);
                    }
                    nativeExpressADView.render();
                    recordRenderSuccess(Constants.GDT_KEY);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 按顺序从缓存中读取广告数据
     *
     * @param bean
     */
    private void loadFromCache(ConfigItemBean bean) {
        List<AdBean> netList = bean.getNetwork();
        int size = netList.size();
        for (int i = 0; i < size; i++) {
            AdBean item = netList.get(i);
            if (item.getSdk().equals(Constants.TT_KEY)) {
                if (!TTAdManagerHolder.getInitStatus()) {
                    TTAdManagerHolder.singleInit(mContext.getApplicationContext(), item.getParameterBean().getAppid(), item.getParameterBean().getAppname());
                }
                mTTAd = NativeExpressCache.getTTNative(item.getParameterBean().getPosid());
                NativeExpressCache.loadTTExpressAd(item.getParameterBean().getPosid(), mContext, POS_ID);
                if (mTTAd != null) {
                    bindTTAdListener(mTTAd);
                    mTTAd.render();
                    recordRenderSuccess(Constants.TT_KEY);
                }
            }
            if (item.getSdk().equals(Constants.GDT_KEY)) {
                nativeExpressADView = NativeExpressCache.getGDTNative(item.getParameterBean().getPosid());
                NativeExpressCache.loadGDTExpressAd(item.getParameterBean().getAppid(), item.getParameterBean().getPosid(), mContext, POS_ID);
                if (nativeExpressADView != null) {
                    if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                        //如果是视频广告 可添加视频播放监听
                        nativeExpressADView.setMediaListener(listener);
                    }
                    nativeExpressADView.render();
                    recordRenderSuccess(Constants.GDT_KEY);
                }
            }
            if (item.getSdk().equals(Constants.MOBI_KEY)) {
                mobiNativeExpressAdView = NativeExpressCache.getMobiNative(item.getParameterBean().getPosid());
                NativeExpressCache.loadMobiExpress(item.getParameterBean().getAppid(), item.getParameterBean().getPosid(), mContext, POS_ID);
                if (mobiNativeExpressAdView != null) {
                    mobiNativeExpressAdView.render();
                    recordRenderSuccess(Constants.MOBI_KEY);
                }
            }
        }
    }

    /**
     * 按顺序加载广告
     */
    private void sortLoad(ConfigItemBean bean) {
        List<AdBean> netList = bean.getNetwork();
        int size = netList.size();
        for (int i = 0; i < size; i++) {
            AdBean item = netList.get(i);

            if (item.getSdk().equals(Constants.TT_KEY)) {//是穿山甲广告则初始化
                if (!TTAdManagerHolder.getInitStatus()) {
                    TTAdManagerHolder.singleInit(mContext.getApplicationContext(), item.getParameterBean().getAppid(), item.getParameterBean().getAppname());
                }
                loadTTExpressAd(item.getParameterBean().getPosid());
            }
            if (item.getSdk().equals(Constants.GDT_KEY)) {//是广点通广告
                loadGDTExpressAd(item.getParameterBean().getAppid(), item.getParameterBean().getPosid());
            }
            if (item.getSdk().equals(Constants.MOBI_KEY)) {
                loadMobiExpressAd(item.getParameterBean().getAppid(), item.getParameterBean().getPosid());
            }
        }
    }

    /**
     * 按顺序渲染
     */
    private void sortRender(ConfigItemBean bean) {
        List<AdBean> netList = bean.getNetwork();
        int size = netList.size();
        for (int i = 0; i < size; i++) {
            AdBean item = netList.get(i);
            if (item.getSdk().equals(Constants.TT_KEY) && mTTAd != null) {
                renderTTAD();
                break;
            }
            if (item.getSdk().equals(Constants.GDT_KEY) && nativeExpressADView != null) {
                renderGDTAD();
                break;
            }
            if (item.getSdk().equals(Constants.MOBI_KEY) && mobiNativeExpressAdView != null) {
                renderMobi();
                break;
            }
            if (i >= size - 1) {
                firstCome = true;
            }
        }
    }

    //加载穿山甲------------------------------------------------------

    /**
     * 加载穿山甲广告
     */
    private void loadTTExpressAd(String CodeID) {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(mContext);
        if (mHeightAuto) {
            ADViewHeight = 0;
        }
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(CodeID)
                .setSupportDeepLink(mDeepLink)
                .setAdCount(DEFAULT_COUNT)
                .setExpressViewAcceptedSize(ADViewWidth, ADViewHeight)
                .setImageAcceptedSize(640, 320)
                .build();
        mTTAdNative.loadNativeExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            //加载失败
            @Override
            public void onError(int i, String s) {
                AdStatistical.trackAD(mContext, Constants.TT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
//                mBearingView.removeAllViews();
                if (mListener != null) {
                    mListener.onLoadFaild(Constants.TT_KEY, i, s);
                }
            }

            //加载成功
            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                if (list == null || list.size() <= 0) {
                    return;
                }
                mTTAd = list.get(0);
                bindTTAdListener(mTTAd);
                mTTAd.render();
                recordRenderSuccess(Constants.TT_KEY);
                renderTTAD();
            }
        });
    }

    private void bindTTAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
            @Override
            public void onAdDismiss() {
                if (mListener != null) {
                    mListener.onAdDismissed(Constants.TT_KEY);
                }
            }

            @Override
            public void onAdClicked(View view, int i) {
                //广告被点击
                if (mListener != null) {
                    mListener.onAdClick(Constants.TT_KEY);
                }
                AdStatistical.trackAD(mContext, Constants.TT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
            }

            @Override
            public void onAdShow(View view, int i) {
                //广告显示
                if (mListener != null) {
                    mListener.onAdShow(Constants.TT_KEY);
                }
            }

            @Override
            public void onRenderFail(View view, String s, int i) {
                //广告渲染失败
            }

            @Override
            public void onRenderSuccess(View view, float v, float v1) {
                //广告渲染成功
                if (firstCome) {
                    renderTTAD();
                    firstCome = false;
                }
                if (mListener != null) {
                    mListener.onAdRenderSuccess(Constants.TT_KEY);
                }
            }
        });
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        //设置下载监听
//        Log.e("绑定下载事件","监听下载");
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
//                Log.e("下载完成",l+"-"+s+"-"+s1);
            }

            @Override
            public void onInstalled(String s, String s1) {
//                Log.e("安装完成",s+"-"+s1);
            }
        });
    }

    /**
     * 填充广告View到载体
     */
    private void renderTTAD() {
        if (mBearingView == null) {
            return;
        }
        if (isShowed) {
            return;
        }
        if (mBearingView.getChildCount() > 0) {
            mBearingView.removeAllViews();
        }
        if (mBearingView.getVisibility() != View.VISIBLE) {
            mBearingView.setVisibility(View.VISIBLE);
        }

        if (mTTAd != null) {
            if (typeListener != null) {
                typeListener.InteractionType(mTTAd.getInteractionType());
            }
            mBearingView.addView(mTTAd.getExpressAdView());
            isShowed = true;
            AdStatistical.trackAD(mContext, Constants.TT_KEY, POS_ID, Constants.STATUS_CODE_TRUE, Constants.STATUS_CODE_FALSE);
        }
    }
    //加载穿山甲结束---------------------------------------------------

    //加载广点通------------------------------------------------------

    /**
     * 加载广点通广告
     */
    private void loadGDTExpressAd(String APPID, final String POSID) {
        nativeExpressAD = new NativeExpressAD(mContext, getADSize(), APPID, POSID,
                new NativeExpressAD.NativeExpressADListener() {
                    @Override
                    public void onADLoaded(List<NativeExpressADView> list) {
                        //加载广告成功
                        if (nativeExpressADView != null) {
                            nativeExpressADView.destroy();
                        }
                        if (list != null && list.size() > 0) {
                            nativeExpressADView = list.get(0);
                        }
                        if (nativeExpressADView != null) {
                            if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                                //如果是视频广告 可添加视频播放监听
                                nativeExpressADView.setMediaListener(listener);
                            }
                        }
                        nativeExpressADView.render();
                        recordRenderSuccess(Constants.GDT_KEY);
                    }

                    @Override
                    public void onRenderFail(NativeExpressADView nativeExpressADView) {
                        //广告渲染失败
                    }

                    @Override
                    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                        //广告渲染成功
                        if (firstCome) {
                            renderGDTAD();
                            firstCome = false;
                        }
                        if (mListener != null) {
                            mListener.onAdRenderSuccess(Constants.GDT_KEY);
                        }
                    }

                    @Override
                    public void onADExposure(NativeExpressADView nativeExpressADView) {
                        //广告曝光
                        if (mListener != null) {
                            mListener.onAdShow(Constants.GDT_KEY);
                        }
                    }

                    @Override
                    public void onADClicked(NativeExpressADView nativeExpressADView) {
                        //广告被点击
                        if (mListener != null) {
                            mListener.onAdClick(Constants.GDT_KEY);
                        }
                        AdStatistical.trackAD(mContext, Constants.GDT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
                    }

                    @Override
                    public void onADClosed(NativeExpressADView nativeExpressADView) {
                        //广告关闭
                        if (mBearingView != null && mBearingView.getChildCount() > 0) {
                            mBearingView.removeAllViews();
                            mBearingView.setVisibility(View.GONE);
                        }
                        if (mListener != null) {
                            mListener.onAdDismissed(Constants.GDT_KEY);
                        }
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
                        AdStatistical.trackAD(mContext, Constants.GDT_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
                        //加载失败
                        if (mListener != null) {
                            mListener.onLoadFaild(Constants.GDT_KEY, adError.getErrorCode(), adError.getErrorMsg());
                        }
                    }
                });
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS)//设置什么网络情况下可以自动播放视频
                .setAutoPlayMuted(true)//设置自动播放视频时是否静音
                .build());
//        nativeExpressAD.setMaxVideoDuration(15);//设置视频最大时长
        nativeExpressAD.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO);
        nativeExpressAD.loadAD(DEFAULT_COUNT);
    }

    private NativeExpressMediaListener listener = new NativeExpressMediaListener() {
        @Override
        public void onVideoInit(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoLoading(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoCached(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {

        }

        @Override
        public void onVideoStart(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoPause(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoComplete(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {

        }

        @Override
        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {

        }
    };

    private ADSize getADSize() {
        if (mHeightAuto || ADViewHeight <= 0) {
            return new ADSize((int) ADViewWidth, ADSize.AUTO_HEIGHT);
        }
        return new ADSize((int) ADViewWidth, (int) ADViewHeight);
    }

    private void renderGDTAD() {
        if (mBearingView == null) {
            return;
        }
        if (isShowed) {
            return;
        }
        if (mBearingView.getChildCount() > 0) {
            mBearingView.removeAllViews();
        }
        if (mBearingView.getVisibility() != View.VISIBLE) {
            mBearingView.setVisibility(View.VISIBLE);
        }
        if (nativeExpressADView != null) {

            try {
                mBearingView.addView(nativeExpressADView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            isShowed = true;
            AdStatistical.trackAD(mContext, Constants.GDT_KEY, POS_ID, Constants.STATUS_CODE_TRUE, Constants.STATUS_CODE_FALSE);
//            nativeExpressADView.render();
        }
    }

    //加载广点通结束----------------------------------------------------
    //加载Mobi广告------------------------------------------------------
    private void loadMobiExpressAd(String APPID, String POSID) {
        mobiNativeExpressAd = new MobiNativeExpressAd(mContext, APPID, POSID, getMobiADSize(),
                new MobiNativeExpressAd.MobiNativeExpressAdListener() {
                    @Override
                    public void onADLoaded(List<MobiNativeExpressAdView> list) {
                        if (mobiNativeExpressAdView != null) {
                            mobiNativeExpressAdView.destory();
                        }
                        if (list != null && list.size() > 0) {
                            mobiNativeExpressAdView = list.get(0);
                        }
                        if (mobiNativeExpressAdView != null) {
                            mobiNativeExpressAdView.render();
                            recordRenderSuccess(Constants.MOBI_KEY);
                        }
                    }

                    @Override
                    public void onRenderFail(MobiNativeExpressAdView mobiNativeExpressAdView) {
                        Log.e(TAG, "onRenderFail");
                    }

                    @Override
                    public void onRenderSuccess(MobiNativeExpressAdView mobiNativeExpressAdView) {
                        if (firstCome) {
                            renderMobi();
                            firstCome = false;
                        }
                        if (mListener != null) {
                            mListener.onAdRenderSuccess(Constants.MOBI_KEY);
                        }
                    }

                    @Override
                    public void onADExposure(MobiNativeExpressAdView mobiNativeExpressAdView) {
                        if (mListener != null) {
                            mListener.onAdShow(Constants.MOBI_KEY);
                        }
                    }

                    @Override
                    public void onADClicked(MobiNativeExpressAdView mobiNativeExpressAdView) {
                        if (mListener != null) {
                            mListener.onAdClick(Constants.MOBI_KEY);
                        }
                        AdStatistical.trackAD(mContext, Constants.MOBI_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_TRUE);
                    }

                    @Override
                    public void onADClosed(MobiNativeExpressAdView mobiNativeExpressAdView) {
                        if (mBearingView != null && mBearingView.getChildCount() > 0) {
                            mBearingView.removeAllViews();
                            mBearingView.setVisibility(View.GONE);
                        }
                        if (mListener != null) {
                            mListener.onAdDismissed(Constants.MOBI_KEY);
                        }
                    }

                    @Override
                    public void onNoAD(ADError adError) {
                        AdStatistical.trackAD(mContext, Constants.MOBI_KEY, POS_ID, Constants.STATUS_CODE_FALSE, Constants.STATUS_CODE_FALSE);
                        //加载失败
                        if (mListener != null) {
                            mListener.onLoadFaild(Constants.MOBI_KEY, adError.getEcode(), adError.geteMsg());
                        }
                        ErrorTODO(Constants.MOBI_KEY);
                    }
                });
        mobiNativeExpressAd.loadAD(DEFAULT_COUNT);
    }

    private MobiAdSize getMobiADSize() {
//        if (mHeightAuto || ADViewHeight <= 0) {
//            return new MobiAdSize((int) ADViewWidth, MobiAdSize.AUTO_HEIGHT);
//        }
        return new MobiAdSize(MobiAdSize.FULL_WIDTH, MobiAdSize.AUTO_HEIGHT);
    }

    private void renderMobi() {
        if (mBearingView == null) {
            return;
        }
        if (isShowed) {
            return;
        }
        if (mBearingView.getChildCount() > 0) {
            mBearingView.removeAllViews();
        }
        if (mBearingView.getVisibility() != View.VISIBLE) {
            mBearingView.setVisibility(View.VISIBLE);
        }
        if (mobiNativeExpressAdView != null) {
            try {
                mBearingView.addView(mobiNativeExpressAdView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            isShowed = true;
            AdStatistical.trackAD(mContext, Constants.MOBI_KEY, POS_ID, Constants.STATUS_CODE_TRUE, Constants.STATUS_CODE_FALSE);
        }
    }

    //加载Mobi广告结束------------------------------------------------------

    /**
     * 加载出错之后重新加载 按照优先返回规则 显示
     */
    private void ErrorTODO(String channel) {
        //如果不是按照服务器固定顺序 则判断是否是 order排序第一位
        if (bean != null && bean.getSort_type() != Constants.SORT_TYPE_SERVICE_ORDER) {
            List<AdBean> list = bean.getNetwork();
            if (list != null && list.size() > 0 && list.get(0).getSdk().equals(channel)) {
                //如果是固定顺序第一位 加载失败 则按照优先返回广告顺序
                list.remove(0);
                bean.setNetwork(list);
                firstCome = true;
                sortLoad(bean);
            }
        }
    }

    public void destory() {
        if (mBearingView != null) {
            mBearingView.removeAllViews();
            mBearingView = null;
        }
        if (mTTAd != null) {
            mTTAd.destroy();
            mTTAd = null;
        }
        if (mTTAdNative != null) {
            mTTAdNative = null;
        }
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
            nativeExpressADView = null;
        }
        if (nativeExpressAD != null) {
            nativeExpressAD = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (handlerRun != null) {
            handlerRun = null;
        }
        if (mobiNativeExpressAd != null) {
            mobiNativeExpressAd = null;
        }
        if (mobiNativeExpressAdView != null) {
            mobiNativeExpressAdView.destory();
            mobiNativeExpressAdView = null;
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
                sortRender(bean);
                if (handler != null && handlerRun != null) {
                    handler.removeCallbacks(handlerRun);
                }
                return;
            }
        }
        loadedList.add(channel);
        if (loadedList.size() >= bean.getNetwork().size() && !isShowed) {
            sortRender(bean);
            if (handler != null && handlerRun != null) {
                handler.removeCallbacks(handlerRun);
            }
        }
    }

    public static class Builder {
        private Context mContext;
        private float ADViewWidth;
        private float ADViewHeight;
        private boolean mDeepLink = true;
        private boolean mHeightAuto;//高度自适应
        private ViewGroup mBearingView;
        private int mCount = 1;//加载广告的数量
        private NativeLoadListener mListener;
        private ScenarioEnum scenario;//场景值
        private String PosID = "";
        private NativeInteractionTypeListener typeListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        /**
         * 设置广告View宽高
         *
         * @param width
         * @param height
         * @return
         */
        public Builder setADViewSize(float width, float height) {
            this.ADViewWidth = width;
            this.ADViewHeight = height;
            return this;
        }

        public Builder setSupportDeepLink(boolean isDeepLink) {
            this.mDeepLink = isDeepLink;
            return this;
        }

        /**
         * 设置加载广告的view
         *
         * @param bearingView
         * @return
         */
        public Builder setBearingView(ViewGroup bearingView) {
            this.mBearingView = bearingView;
            return this;
        }

        /**
         * 设置广告加载数量
         *
         * @param count
         * @return
         */
        public Builder setAdCount(int count) {
            this.mCount = count;
            return this;
        }

        public Builder setHeightAuto(boolean isAuto) {
            this.mHeightAuto = isAuto;
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

        public Builder setNativeLoadListener(NativeLoadListener listener) {
            this.mListener = listener;
            return this;
        }

        public Builder setInteractionTypeListener(NativeInteractionTypeListener listener) {
            this.typeListener = listener;
            return this;
        }

        public NativeExpressAd build() {
            return new NativeExpressAd(this);
        }

    }

}
