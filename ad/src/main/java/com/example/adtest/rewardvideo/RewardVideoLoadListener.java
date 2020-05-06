package com.example.adtest.rewardvideo;

/**
 * author : liangning
 * date : 2019-11-10  14:38
 */
public interface RewardVideoLoadListener {
    void onAdClick(String channel);
    void onVideoComplete(String channel);//广告播放完成
    void onLoadFaild(String channel, int faildCode, String faildMsg);
    void onAdClose(String channel);//广告看完之后关闭
    void onAdShow(String channel);//广告显示成功
}
