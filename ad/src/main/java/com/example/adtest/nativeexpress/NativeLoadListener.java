package com.example.adtest.nativeexpress;

/**
 * author : liangning
 * date : 2019-11-09  18:31
 */
public interface NativeLoadListener {
    void onAdClick(String channel);//广告被点击
    void onLoadFaild(String channel, int faildCode, String faildMsg);//广告加载失败
    void onAdDismissed(String channel);//广告被关闭
    void onAdRenderSuccess(String channel);//广告渲染成功
    void onAdShow(String channel);//广告显示
}
