package com.example.adtest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.adtest.R;
import com.example.adtest.cache.SinceRenderCache;

/**
 * 全屏信息流自渲染
 * author : liangning
 * date : 2020-01-06  18:06
 */
public class FeedNative2FullScreenActivity extends Activity {

    public static void IntoFeedActivity(Context context) {
        Intent intent = new Intent(context, FeedNative2FullScreenActivity.class);
        context.startActivity(intent);
    }

    private String POSID = "";//自有POSID
//    private NativeUnifiedADData mAdData;
    private AQuery mAQuery;
    private TextView tv_download;
//    private NativeAdContainer mContainer;
    private RelativeLayout rl_ad_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_feed_layout);
        Intent intent = getIntent();
        if (intent != null) {
            POSID = intent.getStringExtra("posid");
        }
        if (TextUtils.isEmpty(POSID)) {
            //设置成默认ID
        }
//        mAdData = SinceRenderCache.getGDTAdData("8030898720311182");
        SinceRenderCache.loadGDTSinceRender("1109966989","8030898720311182",this);
//        if (mAdData == null) {
//            finish();
//            return;
//        }
//        initView();
//        initAd(mAdData);
    }

    private void initView() {
        tv_download = findViewById(R.id.tv_download);
        rl_ad_view = findViewById(R.id.rl_ad_view);
        mAQuery = new AQuery(findViewById(R.id.native_ad_container));
//        mContainer = findViewById(R.id.native_ad_container);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (mAdData != null) {
//            mAdData.resume();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mAdData != null) {
//            mAdData.destroy();
//        }
    }

//    private void initAd(final NativeUnifiedADData ad) {
//        //渲染布局
//        renderUI(ad);
//        List<View> clickableViews = new ArrayList<>();
//        clickableViews.add(tv_download);
//        clickableViews.add(rl_ad_view);
//        ad.bindAdToView(this, mContainer, null, clickableViews);
//        ad.setNativeAdEventListener(new NativeADEventListener() {
//            @Override
//            public void onADExposed() {
//
//            }
//
//            @Override
//            public void onADClicked() {
//                Log.e("FeedActivity", "广告被点击");
//            }
//
//            @Override
//            public void onADError(AdError adError) {
//                Log.e("FeedActivity", "广告失败" + adError.getErrorMsg() + adError.getErrorCode());
//            }
//
//            @Override
//            public void onADStatusChanged() {
//                updateAdAction(tv_download, ad);
//            }
//        });
//        updateAdAction(tv_download, ad);
//    }

//    private void updateAdAction(TextView tv, NativeUnifiedADData ad) {
//        if (!ad.isAppAd()) {
//            tv.setText("浏览");
//            return;
//        }
//        switch (ad.getAppStatus()) {
//            case 0:
//                tv.setText("下载");
//                break;
//            case 1:
//                tv.setText("启动");
//                break;
//            case 2:
//                tv.setText("更新");
//                break;
//            case 4:
//                tv.setText(ad.getProgress() + "%");
//                break;
//            case 8:
//                tv.setText("安装");
//                break;
//            case 16:
//                tv.setText("下载失败，重新下载");
//                break;
//            default:
//                tv.setText("浏览");
//                break;
//        }
//    }


//    private void renderUI(NativeUnifiedADData ad) {
//        int patternType = ad.getAdPatternType();
//        if (patternType == AdPatternType.NATIVE_2IMAGE_2TEXT
//                || patternType == AdPatternType.NATIVE_VIDEO) {
//            mAQuery.id(R.id.iv_icon).image(ad.getIconUrl(), false, true);
//            mAQuery.id(R.id.tv_title).text(ad.getTitle());
//            mAQuery.id(R.id.tv_content).text(ad.getDesc());
//            mAQuery.id(R.id.iv_big).image(ad.getImgUrl(), false, true, 0, 0,
//                    new BitmapAjaxCallback() {
//                        @Override
//                        protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
//                            if (iv.getVisibility() == View.VISIBLE) {
//                                iv.setImageBitmap(bm);
//                            }
//                        }
//                    });
//        }
//    }

}
