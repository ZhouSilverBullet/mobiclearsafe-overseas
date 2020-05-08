package com.example.adtest.config;

import android.content.Context;

/**
 * 初始化穿上甲广告SDK
 * author : liangning
 * date : 2019-11-07  14:53
 */
public class TTAdManagerHolder {

    private static boolean sInit;//TTADSdk是否初始化

//    public static TTAdManager get(){
//        if (!sInit){
//            throw  new RuntimeException("TTAdSdk is not init, please check.");
//        }
//        return TTAdSdk.getAdManager();
//    }

    /**
     * 初始化
     * @param context
     */
    public static void init(Context context){
        doInit(context);
    }

    public static void reload(Context context,String APPID,String APPNAME){
        sInit = false;
        singleInit(context,APPID,APPNAME);
    }

    public static boolean getInitStatus(){
        return sInit;
    }

    /**
     * 每个广告单独去实例化
     */
    public static void singleInit(Context context,String APPID,String APPNAME){
        if (!sInit){
            //头条广告初始化
//            TTAdSdk.init(context.getApplicationContext(),new TTAdConfig.Builder()
//                    .appId(APPID)
//                    .useTextureView(true)
//                    .appName(APPNAME)
//                    .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
//                    .allowShowNotify(true)//是否允许sdk展示通知栏提示
//                    .allowShowPageWhenScreenLock(true)//是否在锁屏场景支持展示广告落地页
//                    .directDownloadNetworkType(
//                            TTAdConstant.NETWORK_STATE_WIFI,
//                            TTAdConstant.NETWORK_STATE_3G,
//                            TTAdConstant.NETWORK_STATE_4G,
//                            TTAdConstant.NETWORK_STATE_2G,
//                            TTAdConstant.NETWORK_STATE_MOBILE)
//                    .supportMultiProcess(true)//是否支持多进程
//                    .build());
//            //sigmob广告初始化
//            try {
//                WindAds ads = WindAds.sharedAds();
//                ads.setDebugEnable(false);
//                ads.startWithOptions(context, new WindAdOptions(Constants.SIGMOB_APPID, Constants.SIGMOB_APPKEY));
//            }catch (Exception e){
//                e.printStackTrace();
//            }

            sInit = true;
        }
    }

    private static void doInit(Context context){
        if (!sInit){
//            TTAdSdk.init(context,buildConfit());
            sInit = true;
        }
    }
//    private static TTAdConfig buildConfit(){
//        return new TTAdConfig.Builder()
//                .appId(Constants.TT_APPID)
//                .useTextureView(true)
//                .appName(Constants.TT_APPNAME)
//                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
//                .allowShowNotify(true)//是否允许sdk展示通知栏提示
//                .allowShowPageWhenScreenLock(true)//是否在锁屏场景支持展示广告落地页
//                .debug(Constants.DEBUG)
//                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI, TTAdConstant.NETWORK_STATE_3G,TTAdConstant.NETWORK_STATE_4G)
//                .supportMultiProcess(true)//是否支持多进程
//                .build();
//    }
}
