package com.mobi.overseas.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.adtest.cache.NativeExpressCache;
import com.example.adtest.interaction.InterActionExpressAdView;
import com.example.adtest.interaction.InterActionLoadListener;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.example.adtest.rewardvideo.RewardVideoLoadListener;
import com.mobi.overseas.clearsafe.MainActivity;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.eventbean.CheckTabEvent;
import com.mobi.overseas.clearsafe.eventbean.ScratchRefreshEvent;
import com.mobi.overseas.clearsafe.main.activity.InviteActivity;
import com.mobi.overseas.clearsafe.moneyactivity.ShareActivity;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.ui.common.Bugs;
import com.mobi.overseas.clearsafe.ui.common.ad.RewardVideoLoadListenerImpl;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;

import org.greenrobot.eventbus.EventBus;

/**
 * author : liangning
 * date : 2019-11-01  11:37
 */
public class LoadWebViewActivity extends BaseAppCompatActivity implements LoadWebView.WebViewListener {

    public static final String KEY_URL = "KEY_URL";

    int points = 0;
    int total_points = 0;
    float cash = 0f;
    int activity_id = 1;
    int system_id = 1;
    int show_bottom_btn = 2;//1显示再来一次 2不显示
    int isDouble = 2;// 1显示翻倍 2不显示翻倍
    int pop_type;//弹窗类型
    String posid;
    String pop_up_message;//按钮文案
    String type = "default";//
    Boolean is_ad = false;//判断是信息流还是插屏广告
    Boolean informationAd;//是否展示信息流
    private String[] CacheAds = null;
    private int closeAdType;
    private String scratchClose = "2";//刮刮卡页面是否可关闭  1不可关闭 2可关闭
    private String title;
    private Boolean isLast = false;


    //转盘
    public static void IntoLoadWebView(Context context, String url) {
        // Log.e("tttt",url);
        //ButtonStatistical.bigWheelPage();
        if (url.contains("knife")) {
            ButtonStatistical.IntoFlyKnifeCount();
        } else if (url.contains("toDrink")) {
            ButtonStatistical.drinkWaterCount();
        } else if (url.contains("scratchList")) {
            ButtonStatistical.ScratchCardNumber();
        } else {
            ButtonStatistical.IntoRotaryTableCount();
        }
        Intent intent = new Intent(context, LoadWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    //服务协议
    public static void IntoServiceAgreement(Context context, String url) {
        Intent intent = new Intent(context, LoadWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    //隐私协议
    public static void IntoPrivacyAgreement(Context context, String url) {
        ButtonStatistical.servicePrivacyPage();
        Intent intent = new Intent(context, LoadWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    //赚钱攻略
    public static void IntoStrategy(Context context, String url) {
        ButtonStatistical.earnCoinsPage();
        Intent intent = new Intent(context, LoadWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    //百青藤新闻详情页
    public static void intoBddetails(Context context, String url) {
        Intent intent = new Intent(context, LoadWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private LoadWebView lwv;
    private String url = "";
    private LinearLayout ll_back;
    private TextView tv_title, clickJS;
    private RewardVideoAd ad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadwebview_layout);
        ll_back = findViewById(R.id.ll_back);
        tv_title = findViewById(R.id.tv_title);
        lwv = findViewById(R.id.lwv);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lwv != null && lwv.canGoBack()) {
                    try {
                        if (scratchClose.equals("2")) {
                            lwv.goBack();
                        } else {
                            ToastUtils.showShort(getResources().getString(R.string.nofinish_scratch));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (scratchClose.equals("2")) {
                        finish();
                    } else {
                        ToastUtils.showShort(getResources().getString(R.string.nofinish_scratch));
                    }
                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString(KEY_URL);
        }
        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        if (!url.contains(Const.getBaseUrl())) {
            tv_title.setText("详情");
        }
        lwv.setWebViewListener(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lwv.loadUrl(url);
                if (url.contains("scratchList") || url.contains("list") || url.contains("moving")) {
                    EventBus.getDefault().post(new ScratchRefreshEvent());
                }
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (lwv != null && lwv.canGoBack()) {
                try {
                    if (scratchClose.equals("2")) {
                        lwv.goBack();
                    } else {
                        ToastUtils.showShort(getResources().getString(R.string.nofinish_scratch));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (scratchClose.equals("2")) {
                    finish();
                } else {
                    ToastUtils.showShort(getResources().getString(R.string.nofinish_scratch));
                }
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lwv != null) {
            lwv.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lwv != null) {
            lwv.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lwv != null) {
            lwv.setVisibility(View.GONE);
            lwv.loadUrl("about:blank");
            lwv.stopLoading();
            lwv.setWebChromeClient(null);
            lwv.setWebViewClient(null);
            lwv.destroy();
            lwv = null;
        }
        if (ad != null) {
            ad.destory();
        }
        if (CacheAds != null && CacheAds.length > 0) {
            NativeExpressCache.clearGroupNative(CacheAds);
        }
    }

    @Override
    public void showRewardDialog(final String s) {
        // Log.e("xxxx",s);
        if (s.isEmpty()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject object = JSON.parseObject(s);
                    points = object.getIntValue("points");
                    total_points = object.getIntValue("total_points");
                    activity_id = object.getIntValue("activity_id");
                    system_id = object.getIntValue("system_id");
                    show_bottom_btn = object.getIntValue("show_bottom_btn");
                    isDouble = object.getIntValue("isDouble");
                    cash = (float) object.getDoubleValue("cash");
                    pop_type = object.getIntValue("pop_type");
                    pop_up_message = object.getString("pop_up_message");
                    posid = object.getString("posid");
                    type = object.getString("type");//turn 转盘  water 喝水打卡   flyGameContinue飞刀
                    is_ad = object.getBoolean("is_ad");//true 出插屏广告 false 不出
                    closeAdType = object.getIntValue("close_ad_type");//  关弹窗广告类型，0-正常 1-插屏 2-视频
                    informationAd = object.getBoolean("information_flow_ad");
                    title = object.getString("title");//转盘弹框标题
                    isLast = object.getBoolean("isLast");
                    final int h1fg = object.getIntValue("h1fg");
                    if (is_ad == null) {
                        is_ad = false;
                    }
                    if (informationAd == null) {
                        informationAd = true;
                    }
                    if (TextUtils.isEmpty(pop_up_message)) {
                        pop_up_message = "";
                    }
                    if (TextUtils.isEmpty(type)) {
                        type = "default";
                    }
                    if (TextUtils.isEmpty(title)) {
                        title = "";
                    }
                    switch (pop_type) {
                        case 1000://直接领金币
                            if ("turn".equals(type) || "scratch".equals(type)) {//转盘和刮刮卡
                                if (h1fg != 3000) {
                                    GoldDialog dialog = new GoldDialog.Builder(LoadWebViewActivity.this)
                                            .setGold(points)
                                            .setDialogClick(new GoldDialog.GetGoldDialogClick() {
                                                @Override
                                                public void closeBtnClick(Dialog dialog) {
                                                    dialog.dismiss();
                                                    if (AppUtil.HWIsShowAd()) {
                                                        showPlaque(type, h1fg, isLast);
                                                    } else {
                                                        lwv.loadUrl("javascript:close()");
                                                    }
                                                }
                                            })
                                            .setPosId(posid)
                                            .build();
                                    dialog.show();
                                } else {
                                    GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(LoadWebViewActivity.this)
                                            .setGold(points)
                                            .isVisible(false)
                                            .setIsShowAd(informationAd)
                                            .setShowAgain(show_bottom_btn == 1 ? true : false)
                                            .setBtnText(pop_up_message)
                                            .setTitle(title)
                                            .setScenario(isDouble == 1 ? ScenarioEnum.turn_table_gold_native : ScenarioEnum.turn_table_pkg_gold_natice)
                                            .setPostID(posid)
                                            .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                                @Override
                                                public void doubleBtnClick(Dialog dialog) {

                                                }

                                                @Override
                                                public void closeBtnClick(Dialog dialog) {
                                                    dialog.dismiss();
                                                    if ("flyGameContinue".equals(type)) {
                                                        lwv.loadUrl("javascript:gameBackContinue()");
                                                    } else if ("scratch".equals(type)) {
                                                        lwv.loadUrl("javascript:close()");
                                                    }
                                                    if (isLast) {
                                                        showJumpDialog(type);
                                                    }
                                                }

                                                @Override
                                                public void bottomBtnClick(Dialog dialog) {
                                                    lwv.loadUrl("javascript:rotateFun()");
                                                    dialog.dismiss();
                                                }
                                            }).build();
                                    dialog.show();
                                }
                            } else {
                                if (is_ad) {
                                    GoldDialog dialog = new GoldDialog.Builder(LoadWebViewActivity.this)
                                            .setGold(points)
                                            .setDialogClick(new GoldDialog.GetGoldDialogClick() {
                                                @Override
                                                public void closeBtnClick(Dialog dialog) {
                                                    dialog.dismiss();
                                                    if (AppUtil.HWIsShowAd()) {
                                                        new InterActionExpressAdView.Builder(LoadWebViewActivity.this)
                                                                .setPosID(posid)
                                                                .setListener(new InterActionLoadListener() {
                                                                    @Override
                                                                    public void onAdClick(String channel) {

                                                                    }

                                                                    @Override
                                                                    public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                                                                    }

                                                                    @Override
                                                                    public void onAdDismissed(String channel) {

                                                                    }

                                                                    @Override
                                                                    public void onAdRenderSuccess(String channel) {

                                                                    }

                                                                    @Override
                                                                    public void onAdShow(String channel) {

                                                                    }
                                                                }).build();
                                                    }
                                                }
                                            })
                                            .build();
                                    dialog.show();
                                } else {
                                    GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(LoadWebViewActivity.this)
                                            .setGold(points)
                                            .isVisible(false)
                                            .setIsShowAd(informationAd)
                                            .setShowAgain(show_bottom_btn == 1 ? true : false)
                                            .setBtnText(pop_up_message)
                                            .setTitle(title)
                                            .setScenario(isDouble == 1 ? ScenarioEnum.turn_table_gold_native : ScenarioEnum.turn_table_pkg_gold_natice)
                                            .setPostID(posid)
                                            .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                                @Override
                                                public void doubleBtnClick(Dialog dialog) {

                                                }

                                                @Override
                                                public void closeBtnClick(Dialog dialog) {
                                                    dialog.dismiss();
                                                    if ("flyGameContinue".equals(type)) {
                                                        lwv.loadUrl("javascript:gameBackContinue()");
                                                    } else if ("scratch".equals(type)) {
                                                        lwv.loadUrl("javascript:close()");
                                                    }
                                                }

                                                @Override
                                                public void bottomBtnClick(Dialog dialog) {
                                                    lwv.loadUrl("javascript:rotateFun()");
                                                    dialog.dismiss();
                                                }
                                            }).build();
                                    dialog.show();
                                }
                            }
                            break;
                        case 1001://有翻倍按钮
                            GetGoldNewDialog dialog2 = new GetGoldNewDialog.Builder(LoadWebViewActivity.this)
                                    .setGold(points)
                                    .isVisible(true)
                                    .setIsShowAd(informationAd)
                                    .setShowAgain(show_bottom_btn == 1 ? true : false)
                                    .setBtnText(pop_up_message)
                                    .setTitle(title)
                                    .setScenario(isDouble == 1 ? ScenarioEnum.turn_table_gold_native : ScenarioEnum.turn_table_pkg_gold_natice)
                                    .setPostID(posid)
                                    .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                        @Override
                                        public void doubleBtnClick(Dialog dialog) {
                                            ScenarioEnum scenario = null;
                                            switch (type) {
                                                case "water":
                                                    scenario = ScenarioEnum.drink_water_video;
                                                    break;
                                                case "turn":
                                                    scenario = ScenarioEnum.turn_table_gold_double_video;
                                                    break;
                                                case "move":
                                                    scenario = ScenarioEnum.dongdong_bubble_video;
                                                    break;
                                                case "scratch":
                                                    scenario = ScenarioEnum.scratch_cards_video;
                                                    break;
                                                default:
                                                    scenario = ScenarioEnum.turn_table_gold_double_video;
                                                    break;

                                            }
                                            new RewardVideoAd.Builder(LoadWebViewActivity.this)
                                                    .setSupportDeepLink(true)
                                                    .setScenario(scenario)
                                                    .setRewardVideoLoadListener(new RewardVideoLoadListenerImpl() {

                                                        @Override
                                                        public void onAdClose(String channel) {
                                                            if (lwv == null || isFinishing()) {
                                                                return;
                                                            }
                                                            if ("water".equals(type)) {
                                                                lwv.loadUrl("javascript:drinkDouble()");
                                                            } else if ("turn".equals(type)) {
                                                                lwv.loadUrl("javascript:doubleGold()");
                                                            } else if ("move".equals(type)) {
                                                                lwv.loadUrl("javascript:activeDouble()");
                                                            } else if ("scratch".equals(type)) {
                                                                lwv.loadUrl("javascript:scratchDouble()");
                                                            }
                                                        }

                                                    }).build();
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void closeBtnClick(Dialog dialog) {
                                            dialog.dismiss();
                                            if (AppUtil.HWIsShowAd()) {
                                                if ("turn".equals(type) || "scratch".equals(type)) {//转盘和刮刮卡
                                                    showPlaque(type, h1fg, isLast);
                                                } else {
                                                    showAd(type, closeAdType);
                                                }
                                            } else {
                                                lwv.loadUrl("javascript:close()");
                                            }
                                        }

                                        @Override
                                        public void bottomBtnClick(Dialog dialog) {
                                            lwv.loadUrl("javascript:rotateFun()");
                                            dialog.dismiss();
                                        }
                                    }).build();
                            dialog2.show();
                            break;
                        case 1002://直接看视频然后领取金币
                            new RewardVideoAd.Builder(LoadWebViewActivity.this)
                                    .setSupportDeepLink(true)
                                    .setScenario(ScenarioEnum.turn_table_video)
                                    .setPosID(posid)
                                    .setRewardVideoLoadListener(new RewardVideoLoadListenerImpl() {

                                        @Override
                                        public void onAdClose(String channel) {
                                            if (lwv == null || isFinishing()) {
                                                return;
                                            }
                                            if ("flyGameContinue".equals(type)) {
                                                lwv.loadUrl("javascript:videoOverBack()");
                                                ButtonStatistical.flyKnifeOverCount();
                                            } else if ("water".equals(type)) {
                                                lwv.loadUrl("javascript:finishVideo()");
                                            } else if ("turn".equals(type)) {
                                                lwv.loadUrl("javascript:doubleGold()");
                                            } else if ("move".equals(type)) {
                                                lwv.loadUrl("javascript:finishVideo()");
                                            } else if ("scratch".equals(type)) {
                                                lwv.loadUrl("javascript:finishVideo()");
                                            }
                                        }

                                    }).build();
                            break;
                        case 1003://看视频领取
                        case 1004://翻倍领取
                            GetGoldNewDialog dialog3 = new GetGoldNewDialog.Builder(LoadWebViewActivity.this)
                                    .setGold(points)
                                    .isVisible(true)
                                    .setIsShowAd(informationAd)
                                    .setShowAgain(show_bottom_btn == 1 ? true : false)
                                    .setBtnText(pop_up_message)
                                    .setTitle(title)
                                    .setScenario(isDouble == 1 ? ScenarioEnum.turn_table_gold_native : ScenarioEnum.turn_table_pkg_gold_natice)
                                    .setPostID(posid)
                                    .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                        @Override
                                        public void doubleBtnClick(Dialog dialog) {
                                            ScenarioEnum scenario = null;
                                            switch (type) {
                                                case "water":
                                                    scenario = ScenarioEnum.drink_water_video;
                                                    break;
                                                case "turn":
                                                    scenario = ScenarioEnum.turn_table_gold_double_video;
                                                    break;
                                                case "move":
                                                    scenario = ScenarioEnum.dongdong_bubble_video;
                                                    break;
                                                case "scratch":
                                                    scenario = ScenarioEnum.scratch_cards_video;
                                                    break;
                                                default:
                                                    scenario = ScenarioEnum.turn_table_gold_double_video;
                                                    break;

                                            }
                                            new RewardVideoAd.Builder(LoadWebViewActivity.this)
                                                    .setSupportDeepLink(true)
                                                    .setScenario(scenario)
                                                    .setRewardVideoLoadListener(new RewardVideoLoadListenerImpl() {

                                                        @Override
                                                        public void onAdClose(String channel) {
                                                            if (lwv == null || isFinishing()) {
                                                                return;
                                                            }
                                                            if ("water".equals(type)) {
                                                                lwv.loadUrl("javascript:finishVideo()");
                                                            } else if ("turn".equals(type)) {
                                                                lwv.loadUrl("javascript:doubleGold()");
                                                            } else if ("move".equals(type)) {
                                                                lwv.loadUrl("javascript:finishVideo()");
                                                            } else if ("scratch".equals(type)) {
                                                                lwv.loadUrl("javascript:finishVideo()");
                                                            }

                                                        }

                                                        @Override
                                                        public void onAdShow(String channel) {

                                                        }
                                                    }).build();
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void closeBtnClick(Dialog dialog) {
                                            dialog.dismiss();
                                            if (AppUtil.HWIsShowAd()) {
                                                if ("turn".equals(type) || "scratch".equals(type)) {//转盘和刮刮卡
                                                    showPlaque(type, h1fg, isLast);
                                                } else {
                                                    showAd(type, closeAdType);
                                                }
                                            } else {
                                                lwv.loadUrl("javascript:close()");
                                            }
                                        }

                                        @Override
                                        public void bottomBtnClick(Dialog dialog) {
                                            lwv.loadUrl("javascript:rotateFun()");
                                            dialog.dismiss();
                                        }
                                    }).build();
                            dialog3.show();
                            break;
                        case 1005://最高翻六倍
                            GetGoldNewDialog dialog5 = new GetGoldNewDialog.Builder(LoadWebViewActivity.this)
                                    .setGold(points)
                                    .isVisible(true)
                                    .setlableIsVisible(true)
                                    .setIsShowAd(informationAd)
                                    .setShowAgain(show_bottom_btn == 1 ? true : false)
                                    .setBtnText(pop_up_message)
                                    .setTitle(title)
                                    .setScenario(isDouble == 1 ? ScenarioEnum.turn_table_gold_native : ScenarioEnum.turn_table_pkg_gold_natice)
                                    .setPostID(posid)
                                    .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                        @Override
                                        public void doubleBtnClick(Dialog dialog) {
                                            ScenarioEnum scenario = null;
                                            switch (type) {
                                                case "water":
                                                    scenario = ScenarioEnum.drink_water_video;
                                                    break;
                                                case "turn":
                                                    scenario = ScenarioEnum.turn_table_gold_double_video;
                                                    break;
                                                case "move":
                                                    scenario = ScenarioEnum.dongdong_bubble_video;
                                                    break;
                                                case "scratch":
                                                    scenario = ScenarioEnum.scratch_cards_video;
                                                    break;
                                                default:
                                                    scenario = ScenarioEnum.turn_table_gold_double_video;
                                                    break;

                                            }
                                            new RewardVideoAd.Builder(LoadWebViewActivity.this)
                                                    .setSupportDeepLink(true)
                                                    .setScenario(scenario)
                                                    .setRewardVideoLoadListener(new RewardVideoLoadListenerImpl() {

                                                        @Override
                                                        public void onAdClose(String channel) {
                                                            if (lwv == null || isFinishing()) {
                                                                return;
                                                            }
                                                            ButtonStatistical.sixFbCount();
                                                            if ("water".equals(type)) {
                                                                lwv.loadUrl("javascript:sixTimeFun()");
                                                            } else if ("turn".equals(type)) {
                                                                lwv.loadUrl("javascript:doubleGold()");
                                                            } else if ("move".equals(type)) {
                                                                lwv.loadUrl("javascript:sixTimeFun()");
                                                            } else if ("scratch".equals(type)) {
                                                                lwv.loadUrl("javascript:sixTimeFun()");
                                                            }
                                                            //  TurnTableDouble(finalSystem_id, finalActivity_id);
                                                        }

                                                    }).build();
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void closeBtnClick(Dialog dialog) {
                                            dialog.dismiss();
                                            if (AppUtil.HWIsShowAd()) {
                                                if ("turn".equals(type) || "scratch".equals(type)) {//转盘和刮刮卡
                                                    showPlaque(type, h1fg, isLast);
                                                } else {
                                                    showAd(type, closeAdType);
                                                }
                                            } else {
                                                lwv.loadUrl("javascript:close()");
                                            }
                                        }

                                        @Override
                                        public void bottomBtnClick(Dialog dialog) {
                                            lwv.loadUrl("javascript:rotateFun()");
                                            dialog.dismiss();
                                        }
                                    }).build();
                            dialog5.show();
                            break;
                        case 1006://直接领金币 与1000的区别： 弹框显示总共获得多少金币与1000
                            if ("turn".equals(type) || "scratch".equals(type)) {//转盘和刮刮卡
                                if (h1fg != 3000) {
                                    GoldDialog dialog = new GoldDialog.Builder(LoadWebViewActivity.this)
                                            .setGold(points)
                                            .setDialogClick(new GoldDialog.GetGoldDialogClick() {
                                                @Override
                                                public void closeBtnClick(Dialog dialog) {
                                                    dialog.dismiss();
                                                    if (AppUtil.HWIsShowAd()) {
                                                        showPlaque(type, h1fg, isLast);
                                                    } else {
                                                        lwv.loadUrl("javascript:close()");
                                                    }
                                                }
                                            })
                                            .setPosId(posid)
                                            .build();
                                    dialog.show();
                                } else {
                                    GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(LoadWebViewActivity.this)
                                            .setGold(points)
                                            .isVisible(false)
                                            .setFanbeiTitle(true)
                                            .setIsShowAd(informationAd)
                                            .setShowAgain(show_bottom_btn == 1 ? true : false)
                                            .setBtnText(pop_up_message)
                                            .setTitle(title)
                                            .setScenario(isDouble == 1 ? ScenarioEnum.turn_table_gold_native : ScenarioEnum.turn_table_pkg_gold_natice)
                                            .setPostID(posid)
                                            .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                                @Override
                                                public void doubleBtnClick(Dialog dialog) {

                                                }

                                                @Override
                                                public void closeBtnClick(Dialog dialog) {
                                                    dialog.dismiss();
                                                    if (AppUtil.HWIsShowAd()) {
                                                        showPlaque(type, h1fg, isLast);
                                                    } else {
                                                        lwv.loadUrl("javascript:close()");
                                                    }
                                                }

                                                @Override
                                                public void bottomBtnClick(Dialog dialog) {
                                                    lwv.loadUrl("javascript:rotateFun()");
                                                    dialog.dismiss();
                                                }
                                            }).build();
                                    dialog.show();
                                }
                            } else {
                                if (is_ad) {
                                    GoldDialog dialog = new GoldDialog.Builder(LoadWebViewActivity.this)
                                            .setGold(points)
                                            .setDialogClick(new GoldDialog.GetGoldDialogClick() {
                                                @Override
                                                public void closeBtnClick(Dialog dialog) {
                                                    dialog.dismiss();
                                                    if (AppUtil.HWIsShowAd()) {
                                                        new InterActionExpressAdView.Builder(LoadWebViewActivity.this)
                                                                .setPosID(posid)
                                                                .setListener(new InterActionLoadListener() {
                                                                    @Override
                                                                    public void onAdClick(String channel) {

                                                                    }

                                                                    @Override
                                                                    public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                                                                    }

                                                                    @Override
                                                                    public void onAdDismissed(String channel) {

                                                                    }

                                                                    @Override
                                                                    public void onAdRenderSuccess(String channel) {

                                                                    }

                                                                    @Override
                                                                    public void onAdShow(String channel) {

                                                                    }
                                                                }).build();
                                                    }
                                                }
                                            })
                                            .setPosId(posid)
                                            .build();
                                    dialog.show();
                                } else {
                                    GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(LoadWebViewActivity.this)
                                            .setGold(points)
                                            .isVisible(false)
                                            .setFanbeiTitle(true)
                                            .setIsShowAd(informationAd)
                                            .setShowAgain(show_bottom_btn == 1 ? true : false)
                                            .setBtnText(pop_up_message)
                                            .setTitle(title)
                                            .setScenario(isDouble == 1 ? ScenarioEnum.turn_table_gold_native : ScenarioEnum.turn_table_pkg_gold_natice)
                                            .setPostID(posid)
                                            .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                                @Override
                                                public void doubleBtnClick(Dialog dialog) {

                                                }

                                                @Override
                                                public void closeBtnClick(Dialog dialog) {
                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void bottomBtnClick(Dialog dialog) {
                                                    lwv.loadUrl("javascript:rotateFun()");
                                                    dialog.dismiss();
                                                }
                                            }).build();
                                    dialog.show();
                                }
                            }

                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showJumpDialog(String type) {
        int types = 0;
        if (type.equals("turn")) {
            types = 1;
        } else if (type.equals("scratch")) {
            types = 2;
        }
        BackFlowDialog dialog = new BackFlowDialog.Builder(this)
                .setType(types)
                .setDialogClick(new BackFlowDialog.DialogClick() {
                    @Override
                    public void moreBtnClick(Dialog dialog) {
                        dialog.dismiss();
                        startActivity(new Intent(LoadWebViewActivity.this, ShareActivity.class));
                    }

                    @Override
                    public void closeBtnClick(Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build();
        dialog.show();

    }


    //弹框关闭时展示广告
    private void showAd(final String type, int closeAdType) {
        if (closeAdType == 1) {//插屏
            ScenarioEnum scenario = null;
            switch (type) {
                case "turn":
                    scenario = ScenarioEnum.game_plaque;
                    break;
                case "move":
                case "water":
                    scenario = ScenarioEnum.move_water_plaque;
                    break;
                case "scratch":
                    scenario = ScenarioEnum.scratch_cards_plaque;
                    break;
            }
            new InterActionExpressAdView.Builder(this)
                    .setScenarioEnum(scenario)
                    .setListener(new InterActionLoadListener() {
                        @Override
                        public void onAdClick(String channel) {

                        }

                        @Override
                        public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                        }

                        @Override
                        public void onAdDismissed(String channel) {
                            if ("scratch".equals(type)) {
                                lwv.loadUrl("javascript:close()");
                            }
                        }

                        @Override
                        public void onAdRenderSuccess(String channel) {

                        }

                        @Override
                        public void onAdShow(String channel) {

                        }
                    }).build();
        } else if (closeAdType == 2) {//视频
            ScenarioEnum scenario = null;
            switch (type) {
                case "water":
                    scenario = ScenarioEnum.drink_water_video;
                    break;
                case "turn":
                    scenario = ScenarioEnum.turn_table_gold_double_video;
                    break;
                case "move":
                    scenario = ScenarioEnum.dongdong_bubble_video;
                    break;
                case "scratch":
                    scenario = ScenarioEnum.scratch_cards_video;
                    break;
            }
            new RewardVideoAd.Builder(LoadWebViewActivity.this)
                    .setSupportDeepLink(true)
                    .setScenario(scenario)
                    .setRewardVideoLoadListener(new RewardVideoLoadListenerImpl() {

                        @Override
                        public void onAdClose(String channel) {
                            if (lwv == null || isFinishing()) {
                                return;
                            }
                            if ("scratch".equals(type)) {
                                lwv.loadUrl("javascript:close()");
                            }
                        }

                    }).build();

        } else {
            if ("scratch".equals(type)) {
                lwv.loadUrl("javascript:close()");
            }
        }
    }

    @Override
    public void showAd(String s) {
        Log.e("xxxx", "H5要求显示广告" + s);
        JSONObject object = JSON.parseObject(s);
        final String type = object.getString("type");
        final String posid = object.getString("posid");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new RewardVideoAd.Builder(LoadWebViewActivity.this)
                        .setSupportDeepLink(true)
                        .setScenario(ScenarioEnum.turn_table_video)
                        .setPosID(posid)
                        .setRewardVideoLoadListener(new RewardVideoLoadListenerImpl() {

                            @Override
                            public void onAdClose(String channel) {
                                //   Log.e("xxx", "调到了");
                                if (lwv == null || isFinishing()) {
                                    return;
                                }
                                if ("luckyDraw".equals(type)) {
                                    lwv.loadUrl("javascript:finishVideo()");
                                } else {
                                    lwv.loadUrl("javascript:videoFinished()");
                                }
                            }

                        }).build();
            }
        });

    }

    @Override
    public void openNewWebview(String url) {
        LoadWebViewActivity.IntoLoadWebView(LoadWebViewActivity.this, url);
    }

    @Override
    public void setTitle(final String s) {
        if (tv_title != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_title.setText(s);
                }
            });

        }
    }

    @Override
    public void addStep(int step) {//关闭当前页面
        finish();
    }

    @Override
    public void addGold(int gold) {
//        ToastUtils.showShort(String.valueOf(gold));
    }

    @Override
    public void openOtherApp(String intentStr) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(intentStr);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void closeThisActicity(int s) {
        finish();
    }


    @Override
    public void setAllGold(int gold) {
//        ToastUtils.showShort(String.valueOf(gold));
    }

    @Override
    public void setAllStep(int step) {
//        ToastUtils.showShort(String.valueOf(step));
    }

    @Override
    public void showPlaqueAdvert(final String type) {//展示开屏广告
        if (type.isEmpty()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = JSON.parseObject(type);
                final String type = object.getString("type");
                String posid = object.getString("posid");
                int h1fg = object.getIntValue("h1fg");
                Boolean islast = object.getBoolean("isLast");
                if (islast == null) {
                    islast = false;
                }
                showPlaque(type, h1fg, islast);
            }
        });


    }

    @Override
    public void InitializeAD(String s) {
        if (!TextUtils.isEmpty(s)) {
            final String[] adGroup = s.split(",");
            CacheAds = adGroup;
            if (adGroup != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NativeExpressCache.loadGroupNative(adGroup, LoadWebViewActivity.this);
                    }
                });
            }
        }
    }

    @Override
    public void setCanFinish(String s) {
        scratchClose = s;
    }

    @Override
    public void openNewActivity(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(s)) {
                    // Log.e("xxxx",s);
                    JSONObject json = JSON.parseObject(s);
                    String jump_url = json.getString("jump_url");
                    int judge_type = json.getIntValue("judge_type");
                    String params = json.getString("params");
                    if (judge_type == 1) {//原生
                        AppUtil.startActivityFromAction(LoadWebViewActivity.this, jump_url, params);
                    } else if (judge_type == 2) {//h5
                        String jump_url_new = jump_url
                                + "?token=" + UserEntity.getInstance().getToken()
                                + "&user_id=" + UserEntity.getInstance().getUserId()
                                + "&version=" + AppUtil.packageName(MyApplication.getContext());
                        LoadWebViewActivity.IntoLoadWebView(LoadWebViewActivity.this, jump_url_new);
                    } else if (judge_type == 3) {
                        Intent intent = new Intent(LoadWebViewActivity.this, MainActivity.class);
                        LoadWebViewActivity.this.startActivity(intent);
                        EventBus.getDefault().post(new CheckTabEvent(jump_url));
                    }
                }
            }
        });
    }

    private void showPlaque(final String type, final int h1fg, boolean isLast) {
        ScenarioEnum scenario = null;
        switch (type) {
            case "water":
                scenario = ScenarioEnum.drink_water_native;
                break;
            case "turn":
                scenario = ScenarioEnum.turn_table_gold_native;
                break;
            case "move":
                scenario = ScenarioEnum.dongdong_bubble_native;
                break;
            case "scratch":
                scenario = ScenarioEnum.scratch_cards_native;
                break;
            default:
                scenario = ScenarioEnum.drink_water_native;
        }
        switch (h1fg) {
            case 3000:
                if ("knife".equals(type)) {
                    lwv.loadUrl("javascript:gameBackContinue()");
                    ButtonStatistical.flyKnifeOverCount();
                } else {
                    lwv.loadUrl("javascript:close()");
                }
                break;
            case 3001:
                ScenarioEnum scen = null;
                switch (type) {
                    case "turn":
                        scen = ScenarioEnum.game_plaque;
                        break;
                    case "scratch":
                        scen = ScenarioEnum.scratch_cards_plaque;
                        break;
                }
                new InterActionExpressAdView.Builder(LoadWebViewActivity.this)
                        .setScenarioEnum(scen)
                        .setWasDelayed(false)
                        .setListener(new InterActionLoadListener() {
                            @Override
                            public void onAdClick(String channel) {

                            }

                            @Override
                            public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                            }

                            @Override
                            public void onAdDismissed(String channel) {
                                if ("knife".equals(type)) {
                                    lwv.loadUrl("javascript:gameBackContinue()");
                                    ButtonStatistical.flyKnifeOverCount();
                                } else {
                                    lwv.loadUrl("javascript:close()");
                                }
                            }

                            @Override
                            public void onAdRenderSuccess(String channel) {

                            }

                            @Override
                            public void onAdShow(String channel) {

                            }
                        }).build();
                break;
            case 3002:
                ScenarioEnum scens = null;
                switch (type) {
                    case "turn":
                        scens = ScenarioEnum.game_plaque;
                        break;
                    case "scratch":
                        scens = ScenarioEnum.scratch_cards_plaque;
                        break;
                }
                new InterActionExpressAdView.Builder(LoadWebViewActivity.this)
                        .setScenarioEnum(scens)
                        .setWasDelayed(true)
                        .setListener(new InterActionLoadListener() {
                            @Override
                            public void onAdClick(String channel) {

                            }

                            @Override
                            public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                            }

                            @Override
                            public void onAdDismissed(String channel) {
                                if ("knife".equals(type)) {
                                    lwv.loadUrl("javascript:gameBackContinue()");
                                    ButtonStatistical.flyKnifeOverCount();
                                } else {
                                    lwv.loadUrl("javascript:close()");
                                }
                            }

                            @Override
                            public void onAdRenderSuccess(String channel) {

                            }

                            @Override
                            public void onAdShow(String channel) {

                            }
                        }).build();
                break;
            case 3003:
                InterActionDialog actionDialog = new InterActionDialog.Builder(LoadWebViewActivity.this)
                        .setH1fg(false)
                        .setListener(new InterActionDialog.InterActionDialogListener() {
                            @Override
                            public void close() {
                                if ("knife".equals(type)) {
                                    lwv.loadUrl("javascript:gameBackContinue()");
                                    ButtonStatistical.flyKnifeOverCount();
                                } else {
                                    lwv.loadUrl("javascript:close()");
                                }
                            }
                        })
                        .setEnum(scenario)
                        .build();
                actionDialog.show();
                break;
            case 3004:
                InterActionDialog actionDialog2 = new InterActionDialog.Builder(LoadWebViewActivity.this)
                        .setH1fg(true)
                        .setListener(new InterActionDialog.InterActionDialogListener() {
                            @Override
                            public void close() {
                                if ("knife".equals(type)) {
                                    lwv.loadUrl("javascript:gameBackContinue()");
                                    ButtonStatistical.flyKnifeOverCount();
                                } else {
                                    lwv.loadUrl("javascript:close()");
                                }
                            }
                        })
                        .setEnum(scenario)
                        .build();
                actionDialog2.show();
                break;
            case 3005:
                ScenarioEnum scenariovideo = null;
                switch (type) {
                    case "water":
                        scenariovideo = ScenarioEnum.drink_water_video;
                        break;
                    case "turn":
                        scenariovideo = ScenarioEnum.turn_table_gold_double_video;
                        break;
                    case "move":
                        scenariovideo = ScenarioEnum.dongdong_bubble_video;
                        break;
                    case "scratch":
                        scenariovideo = ScenarioEnum.scratch_cards_video;
                        break;
                    default:
                        scenariovideo = ScenarioEnum.drink_water_video;
                        break;
                }
                new RewardVideoAd.Builder(getActivity())
                        .setSupportDeepLink(true)
                        .setScenario(scenariovideo)
                        .setRewardVideoLoadListener(new RewardVideoLoadListenerImpl() {

                            @Bugs("lwv NullPointException")
                            @Override
                            public void onAdClose(String channel) {
                                if (lwv == null || isFinishing()) {
                                    return;
                                }
                                if ("knife".equals(type)) {
                                    lwv.loadUrl("javascript:gameBackContinue()");
                                    ButtonStatistical.flyKnifeOverCount();
                                } else {
                                    lwv.loadUrl("javascript:close()");
                                }
                            }

                        }).build();
                break;
        }
        if (isLast) {
            showJumpDialog(type);
        }
    }


}
