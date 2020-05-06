package com.example.adtest.fullscreenvideo;

/**
 * author : liangning
 * date : 2020-04-14  18:50
 */
public interface FullScreenVideoListener {

    void onAdShow(String channel);//广告显示成功

    void onAdClick(String channel);//广告点击

    void onAdClose(String channel);//广告关闭

    void onVideoComplete(String channel);//视频播放完毕

    void onSkippedVideo(String channel);//视频跳过

    void onLoadFaild(String channel,int faildCode, String faildMsg);
}
