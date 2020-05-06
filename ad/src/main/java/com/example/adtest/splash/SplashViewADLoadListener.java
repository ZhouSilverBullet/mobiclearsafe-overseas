package com.example.adtest.splash;

/**
 * author : liangning
 * date : 2019-11-28  21:54
 */
public interface SplashViewADLoadListener {

    void LoadError(String channel, int errorCode, String errorMsg);//广告加载错误
    void onTimeout(String channel);//广告加载超时
    void onAdClicked(String channel);//广告被点击
    void onAdShow(String channel);//广告显示
    void onSplashAdLoad(String channel);//广告加载成功
    default void onAdSkip(String channel) { //广告跳过

    }
    default void onAdTimeOver() { //广告倒计时结束

    }
}
