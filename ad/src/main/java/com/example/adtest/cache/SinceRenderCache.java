package com.example.adtest.cache;

import android.content.Context;

import com.example.adtest.bean.SinceCacheGDTBean;
import com.example.adtest.utils.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自渲染缓存
 * author : liangning
 * date : 2020-01-06  17:12
 */
public class SinceRenderCache {

    private static volatile Map<String, List<SinceCacheGDTBean>> mGDTMap = new HashMap<>();
    private static final long CacheTime = 1800000;


    /**
     * 预加载自渲染
     *
     * @param PosIds
     * @param context
     */
    public static void loadGroupSinceRender(String[] PosIds, Context context) {
        
//        int size = PosIds.length;
//        for (int i = 0; i < size; i++) {
//
//        }
        loadGDTSinceRender("1109966989","8030898720311182",context);
    }

    /**
     * 加载广点通自渲染数据
     */
    public static void loadGDTSinceRender(String APPID, final String POSID, Context context) {
        int count = 1;
        if (mGDTMap.get(POSID) == null || mGDTMap.get(POSID).size() <= 0) {
            count = 2;
        } else {
            List<SinceCacheGDTBean> list = mGDTMap.get(POSID);
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (DateUtils.getNowTimeLong() - list.get(i).create_time >= CacheTime) {
                    count = 2;
                }
            }
        }

        final int finalCount = count;
//        NativeUnifiedAD mAdManager = new NativeUnifiedAD(context, APPID, POSID, new NativeADUnifiedListener() {
//            @Override
//            public void onADLoaded(List<NativeUnifiedADData> list) {
//                if (list == null || list.size() <= 0) {
//                    return;
//                }
//
//                if (finalCount >= 2 && list.size() >= 2) {
//                    List<SinceCacheGDTBean> gdtList = new ArrayList<>();
//                    for (int i = 0; i < list.size(); i++) {
//                        SinceCacheGDTBean bean = new SinceCacheGDTBean();
//                        bean.mAdData = list.get(i);
//                        gdtList.add(bean);
//                    }
//                    mGDTMap.put(POSID, gdtList);
//                    return;
//                }
//                if (finalCount < 2 && list.size() < 2) {
//                    List<SinceCacheGDTBean> gdtList = mGDTMap.get(POSID);
//                    if (gdtList == null) {
//                        gdtList = new ArrayList<>();
//                    }
//                    SinceCacheGDTBean bean = new SinceCacheGDTBean();
//                    bean.mAdData = list.get(0);
//                    gdtList.add(bean);
//                    mGDTMap.put(POSID, gdtList);
//                }
//            }
//
//            @Override
//            public void onNoAD(AdError adError) {
//                Log.e("自渲染广告失败","失败"+adError.getErrorCode()+adError.getErrorMsg());
//            }
//        });
////        mAdManager.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO); // 本次拉回的视频广告，从用户的角度看是自动播放的
////        mAdManager.setVideoADContainerRender(VideoOption.VideoADContainerRender.SDK); // 视频播放前，用户看到的广告容器是由SDK渲染的
////        mAdManager.setMaxVideoDuration(10);
//        mAdManager.loadData(count);
    }


//    /**
//     * 获取广点通自渲染缓存数据
//     *
//     * @param POSID 对应的广告平台的POSID
//     * @return
//     */
//    public static NativeUnifiedADData getGDTAdData(String POSID) {
//        List<SinceCacheGDTBean> list = mGDTMap.get(POSID);
//        if (list == null || list.size() <= 0) {
//            return null;
//        }
//        NativeUnifiedADData bean = list.get(0).mAdData;
//        list.remove(0);
//        mGDTMap.put(POSID, list);
//        return bean;
//    }
}
