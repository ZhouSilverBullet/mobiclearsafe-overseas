package com.example.adtest.interaction;

/**
 * author : liangning
 * date : 2019-12-13  18:49
 */
public interface InterActionLoadListener {

    void onAdClick(String channel);//广告被点击
    void onLoadFaild(String channel, int faildCode, String faildMsg);//广告加载失败
    void onAdDismissed(String channel);//广告被关闭
    void onAdRenderSuccess(String channel);//广告渲染成功
    void onAdShow(String channel);//广告显示
}
