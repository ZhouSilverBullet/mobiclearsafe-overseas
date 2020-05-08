package com.example.adtest.feednative;

import android.content.Context;

/**
 * author : liangning
 * date : 2019-12-25  17:36
 */
public class FeedNativeAD {
    private static final String TAG = "FeedNativeAD";
    private Context mContext;
//    private TTAdNative mTTAdNative;
//    private TTFeedAd mTTFeedAd;


    public FeedNativeAD(Builder builder) {
        this.mContext = builder.mContext;
        loadTTAD("936888755");
    }

    private void loadTTAD(String CodeID){
//        mTTAdNative = TTAdManagerHolder.get().createAdNative(mContext);
//        AdSlot adSlot = new AdSlot.Builder()
//                .setCodeId(CodeID)
//                .setSupportDeepLink(true)
//                .setAdCount(1)
//                .setImageAcceptedSize(640, 320)
//                .build();
//        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
//            @Override
//            public void onError(int i, String s) {
//                Log.e(TAG,"加载失败"+s+i);
//            }
//
//            @Override
//            public void onFeedAdLoad(List<TTFeedAd> list) {
//                if (list==null||list.isEmpty()){
//                    Log.e(TAG,"返回广告数据为空");
//                    return;
//                }
//                mTTFeedAd = list.get(0);
//                mTTFeedAd.setVideoAdListener(new TTFeedAd.VideoAdListener() {
//                    @Override
//                    public void onVideoLoad(TTFeedAd ad) {
//                        if (ad!=null){
//                            Log.e(TAG,"视频加载成功");
//                        }
//                    }
//
//                    @Override
//                    public void onVideoError(int i, int i1) {
//                        Log.e(TAG,"视频播放错误：errorCode="+i+",extraCode="+i1);
//                    }
//
//                    @Override
//                    public void onVideoAdStartPlay(TTFeedAd ttFeedAd) {
//
//                    }
//
//                    @Override
//                    public void onVideoAdPaused(TTFeedAd ttFeedAd) {
//
//                    }
//
//                    @Override
//                    public void onVideoAdContinuePlay(TTFeedAd ttFeedAd) {
//
//                    }
//
//                    @Override
//                    public void onProgressUpdate(long l, long l1) {
//
//                    }
//
//                    @Override
//                    public void onVideoAdComplete(TTFeedAd ttFeedAd) {
//
//                    }
//                });
//            }
//        });
    }


    public static class Builder {
        private Context mContext;

        public Builder(Context context) {
            this.mContext = context;
        }

        public FeedNativeAD build() {
            return new FeedNativeAD(this);
        }
    }

}
