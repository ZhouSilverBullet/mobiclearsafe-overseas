package com.mobi.overseas.clearsafe.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.adtest.cache.NativeExpressCache;
import com.example.adtest.interaction.InterActionExpressAdView;
import com.example.adtest.interaction.InterActionLoadListener;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.example.adtest.rewardvideo.RewardVideoLoadListener;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.eventbean.PleasanEvent;
import com.mobi.overseas.clearsafe.eventbean.ScratchRefreshEvent;
import com.mobi.overseas.clearsafe.eventbean.ScrathTabEvent;
import com.mobi.overseas.clearsafe.eventbean.UserInfoEvent;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.StatusBarUtil;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.widget.GetGoldNewDialog;
import com.mobi.overseas.clearsafe.widget.GoldScratchDialog;
import com.mobi.overseas.clearsafe.widget.InterActionDialog;
import com.mobi.overseas.clearsafe.widget.LazyLoadFragment;
import com.mobi.overseas.clearsafe.widget.LoadWebView;
import com.mobi.overseas.clearsafe.widget.LoadWebViewActivity;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author:zhaijinlu
 * date: 2019/12/5
 * desc:
 */
public class ScratchFragment extends LazyLoadFragment implements LoadWebView.WebViewListener {
    private LoadWebView lwv;
    private String url;

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
    Boolean is_ad = false;//判断是信息流还是插屏广告
    int closeAdType;
    boolean isRefresh = false;//登录之后刷新数据
    private String type = "";
    public static String scratchClose = "2";//刮刮卡页面是否可关闭  1不可关闭 2可关闭
    private LinearLayout ll_back;
    Boolean informationAd;//是否展示信息流


    @Override
    protected int setContentView() {
        return R.layout.fragment_scratch_layout;
    }

    @Override
    protected void lazyLoad() {
        StatusBarUtil.setStatusBarMode(getActivity(), true, R.color.white);
        ButtonStatistical.bdCount();
        if (Const.pBean != null) {
            if (Const.pBean != null && Const.pBean.show_page == 2) {
                EventBus.getDefault().post(new PleasanEvent(Const.pBean.show_page));
            }
        }
        if (isRefresh) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //   Log.e("sssssss", UserEntity.getInstance().getConfigEntity().getMove_avtivity_url()+"?token="+UserEntity.getInstance().getToken()+"&user_id"+UserEntity.getInstance().getUserId());
                    lwv.loadUrl(UserEntity.getInstance().getConfigEntity().getScratch_card_url() +
                            "?token=" + UserEntity.getInstance().getToken()
                            + "&user_id=" + UserEntity.getInstance().getUserId());
                }
            });
            isRefresh = false;
        }

    }

    @Override
    protected void firstIn() {
        initView();
    }

    private void initView() {
        EventBus.getDefault().register(this);
        ll_back = findViewById(R.id.ll_back);
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
                    } else {
                        ToastUtils.showShort(getResources().getString(R.string.nofinish_scratch));
                    }
                }
            }
        });
        lwv = findViewById(R.id.webview);
//        lwv.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        lwv.setLongClickable(true);
        lwv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        lwv.setWebViewListener(this);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //  Log.e("sssssss", UserEntity.getInstance().getConfigEntity().getMove_avtivity_url()+"?token="+UserEntity.getInstance().getToken()+"&user_id"+UserEntity.getInstance().getUserId());
                lwv.loadUrl(UserEntity.getInstance().getConfigEntity().getScratch_card_url() +
                        "?token=" + UserEntity.getInstance().getToken()
                        + "&user_id=" + UserEntity.getInstance().getUserId());
            }
        });
        if (getView() != null) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                        // 监听到返回按钮点击事件
                        if (scratchClose.equals("2")) {
                            if (lwv.canGoBack()) {
                                lwv.goBack();
                                return true;
                            }
                        } else {
                            ToastUtils.showShort(getResources().getString(R.string.nofinish_scratch));
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ScratchFragment");
        if (lwv != null) {
            lwv.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ScratchFragment");
        if (lwv != null) {
            lwv.onPause();
        }
    }

    @Override
    public void onDestroy() {
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

    }

    @Override
    public void showRewardDialog(final String s) {
        Log.e("ssssss", s);
        if (s.isEmpty()) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
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
                    is_ad = object.getBoolean("is_ad");
                    closeAdType = object.getIntValue("close_ad_type");//  关弹窗广告类型，0-正常 1-插屏 2-视频
                    informationAd = object.getBoolean("information_flow_ad");
                    final int h1fg = object.getIntValue("h1fg");
                    if (informationAd == null) {
                        informationAd = true;
                    }
                    if (is_ad == null) {
                        is_ad = false;
                    }
                    if (TextUtils.isEmpty(pop_up_message)) {
                        pop_up_message = "";
                    }
                    if (TextUtils.isEmpty(type)) {
                        type = "default";
                    }
                    switch (pop_type) {
                        case 1000://直接领金币
                            if (h1fg != 3000) {
                                if (type.equals("scratch")) {
                                    //刮刮卡在关闭广告的时候 需要给H5回调
                                    GoldScratchDialog dialog = new GoldScratchDialog.Builder(getContext())
                                            .setGold(points)
                                            .setDialogClick(new GoldScratchDialog.ScratchDialogListener() {
                                                @Override
                                                public void countDownFinish(Dialog dialog) {
                                                    dialog.dismiss();
                                                    showPlaque(type, h1fg);
                                                }
                                            }).build();
                                    dialog.show();
                                }
                            } else {
                                GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getActivity())
                                        .setGold(points)
                                        .setIsShowAd(informationAd)
                                        .isVisible(false)
                                        .setShowAgain(show_bottom_btn == 1 ? true : false)
                                        .setBtnText(pop_up_message)
                                        .setScenario(isDouble == 1 ? ScenarioEnum.turn_table_gold_native : ScenarioEnum.turn_table_pkg_gold_natice)
                                        .setPostID(posid)
                                        .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                            @Override
                                            public void doubleBtnClick(Dialog dialog) {

                                            }

                                            @Override
                                            public void closeBtnClick(Dialog dialog) {
                                                dialog.dismiss();
                                                if ("scratch".equals(type)) {
                                                    lwv.loadUrl("javascript:close()");
                                                }
                                            }

                                            @Override
                                            public void bottomBtnClick(Dialog dialog) {
                                                dialog.dismiss();
                                            }
                                        }).build();
                                dialog.show();
                            }
                            break;
                        case 1001://有翻倍按钮
                            GetGoldNewDialog dialog2 = new GetGoldNewDialog.Builder(getActivity())
                                    .setGold(points)
                                    .isVisible(true)
                                    .setIsShowAd(informationAd)
                                    .setShowAgain(show_bottom_btn == 1 ? true : false)
                                    .setBtnText(pop_up_message)
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
                                            new RewardVideoAd.Builder(getActivity())
                                                    .setSupportDeepLink(true)
                                                    .setScenario(scenario)
                                                    .setRewardVideoLoadListener(new RewardVideoLoadListener() {
                                                        @Override
                                                        public void onAdClick(String channel) {

                                                        }

                                                        @Override
                                                        public void onVideoComplete(String channel) {

                                                        }

                                                        @Override
                                                        public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                                                        }

                                                        @Override
                                                        public void onAdClose(String channel) {
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

                                                        @Override
                                                        public void onAdShow(String channel) {

                                                        }
                                                    }).build();
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void closeBtnClick(Dialog dialog) {
                                            dialog.dismiss();
//                                            showAd(closeAdType);
                                            showPlaque(type, h1fg);
                                        }

                                        @Override
                                        public void bottomBtnClick(Dialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).build();
                            dialog2.show();
                            break;
                        case 1002://直接看视频然后领取金币
                            new RewardVideoAd.Builder(getActivity())
                                    .setSupportDeepLink(true)
                                    .setScenario(ScenarioEnum.scratch_cards_video)
                                    .setPosID(posid)
                                    .setRewardVideoLoadListener(new RewardVideoLoadListener() {
                                        @Override
                                        public void onAdClick(String channel) {

                                        }

                                        @Override
                                        public void onVideoComplete(String channel) {

                                        }

                                        @Override
                                        public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                                        }

                                        @Override
                                        public void onAdClose(String channel) {
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

                                        @Override
                                        public void onAdShow(String channel) {

                                        }
                                    }).build();
                            break;
                        case 1003://看视频领取
                        case 1004://翻倍领取
                            GetGoldNewDialog dialog4 = new GetGoldNewDialog.Builder(getActivity())
                                    .setGold(points)
                                    .isVisible(true)
                                    .setIsShowAd(informationAd)
                                    .setShowAgain(show_bottom_btn == 1 ? true : false)
                                    .setBtnText(pop_up_message)
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

                                            new RewardVideoAd.Builder(getActivity())
                                                    .setSupportDeepLink(true)
                                                    .setScenario(scenario)
                                                    .setRewardVideoLoadListener(new RewardVideoLoadListener() {
                                                        @Override
                                                        public void onAdClick(String channel) {

                                                        }

                                                        @Override
                                                        public void onVideoComplete(String channel) {

                                                        }

                                                        @Override
                                                        public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                                                        }

                                                        @Override
                                                        public void onAdClose(String channel) {
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
//                                            showAd(closeAdType);
                                            showPlaque(type, h1fg);
                                        }

                                        @Override
                                        public void bottomBtnClick(Dialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).build();
                            dialog4.show();
                            break;
                        case 1005://翻六倍
                            GetGoldNewDialog dialog5 = new GetGoldNewDialog.Builder(getActivity())
                                    .setGold(points)
                                    .isVisible(true)
                                    .setIsShowAd(informationAd)
                                    .setShowAgain(show_bottom_btn == 1 ? true : false)
                                    .setBtnText(pop_up_message)
                                    .setlableIsVisible(true)
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
                                            new RewardVideoAd.Builder(getActivity())
                                                    .setSupportDeepLink(true)
                                                    .setScenario(scenario)
                                                    .setRewardVideoLoadListener(new RewardVideoLoadListener() {
                                                        @Override
                                                        public void onAdClick(String channel) {

                                                        }

                                                        @Override
                                                        public void onVideoComplete(String channel) {

                                                        }

                                                        @Override
                                                        public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                                                        }

                                                        @Override
                                                        public void onAdClose(String channel) {
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
//                                            showAd(closeAdType);
                                            showPlaque(type, h1fg);
                                        }

                                        @Override
                                        public void bottomBtnClick(Dialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).build();
                            dialog5.show();
                            break;
                        case 1006://直接领金币
                            if (h1fg != 3000) {
                                if (type.equals("scratch")) {
                                    //刮刮卡在关闭广告的时候 需要给H5回调
                                    GoldScratchDialog dialog = new GoldScratchDialog.Builder(getContext())
                                            .setGold(points)
                                            .setDialogClick(new GoldScratchDialog.ScratchDialogListener() {
                                                @Override
                                                public void countDownFinish(Dialog dialog) {
                                                    dialog.dismiss();
                                                    showPlaque(type, h1fg);
                                                }
                                            }).build();
                                    dialog.show();
                                }
                            } else {
                                GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getActivity())
                                        .setGold(points)
                                        .isVisible(false)
                                        .setFanbeiTitle(true)
                                        .setIsShowAd(informationAd)
                                        .setShowAgain(show_bottom_btn == 1 ? true : false)
                                        .setBtnText(pop_up_message)
                                        .setScenario(isDouble == 1 ? ScenarioEnum.turn_table_gold_native : ScenarioEnum.turn_table_pkg_gold_natice)
                                        .setPostID(posid)
                                        .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                            @Override
                                            public void doubleBtnClick(Dialog dialog) {

                                            }

                                            @Override
                                            public void closeBtnClick(Dialog dialog) {
                                                dialog.dismiss();
                                                if ("scratch".equals(type)) {
                                                    lwv.loadUrl("javascript:close()");
                                                }
                                            }

                                            @Override
                                            public void bottomBtnClick(Dialog dialog) {
                                                dialog.dismiss();
                                            }
                                        }).build();
                                dialog.show();
                            }
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //弹框关闭时展示广告
    private void showAd(int closeAdType) {
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
            new InterActionExpressAdView.Builder(getActivity())
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
            new RewardVideoAd.Builder(getActivity())
                    .setSupportDeepLink(true)
                    .setScenario(scenario)
                    .setRewardVideoLoadListener(new RewardVideoLoadListener() {
                        @Override
                        public void onAdClick(String channel) {

                        }

                        @Override
                        public void onVideoComplete(String channel) {

                        }

                        @Override
                        public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                        }

                        @Override
                        public void onAdClose(String channel) {
                            if ("scratch".equals(type)) {
                                lwv.loadUrl("javascript:close()");
                            }
                        }

                        @Override
                        public void onAdShow(String channel) {

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

    }

    @Override
    public void openNewWebview(String url) {
    }

    @Override
    public void setTitle(final String s) {
//        if (tv_title != null) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tv_title.setText(s);
//                }
//            });
//
//        }
    }

    @Override
    public void addStep(int step) {
    }

    @Override
    public void addGold(int gold) {

    }

    @Override
    public void openOtherApp(String intentStr) {
        Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(intentStr);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void closeThisActicity(int s) {

    }




    @Override
    public void setAllGold(int gold) {
    }

    @Override
    public void setAllStep(int step) {
    }

    @Override
    public void showPlaqueAdvert(final String type) {
        Log.e("ssss", type);
        if (TextUtils.isEmpty(type)) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = JSON.parseObject(type);
                final String type = object.getString("type");
                String posid = object.getString("posid");
                int h1fg = object.getIntValue("h1fg");
                showPlaque(type, h1fg);
            }
        });
    }

    private void showPlaque(final String type, final int h1fg) {
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
                lwv.loadUrl("javascript:close()");
                break;
            case 3001:
                new InterActionExpressAdView.Builder(getContext())
                        .setScenarioEnum(ScenarioEnum.scratch_cards_plaque)
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
                                } else if ("scratch".equals(type)) {
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
                new InterActionExpressAdView.Builder(getContext())
                        .setScenarioEnum(ScenarioEnum.scratch_cards_plaque)
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
                                } else if ("scratch".equals(type)) {
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
                InterActionDialog actionDialog = new InterActionDialog.Builder(getContext())
                        .setH1fg(false)
                        .setListener(new InterActionDialog.InterActionDialogListener() {
                            @Override
                            public void close() {
                                if ("knife".equals(type)) {
                                    lwv.loadUrl("javascript:gameBackContinue()");
                                    ButtonStatistical.flyKnifeOverCount();
                                } else if ("scratch".equals(type)) {
                                    lwv.loadUrl("javascript:close()");
                                }
                            }
                        })
                        .setEnum(scenario)
                        .build();
                actionDialog.show();
                break;
            case 3004:
                InterActionDialog actionDialog2 = new InterActionDialog.Builder(getContext())
                        .setH1fg(true)
                        .setListener(new InterActionDialog.InterActionDialogListener() {
                            @Override
                            public void close() {
                                if ("knife".equals(type)) {
                                    lwv.loadUrl("javascript:gameBackContinue()");
                                    ButtonStatistical.flyKnifeOverCount();
                                } else if ("scratch".equals(type)) {
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
                        .setRewardVideoLoadListener(new RewardVideoLoadListener() {
                            @Override
                            public void onAdClick(String channel) {

                            }

                            @Override
                            public void onVideoComplete(String channel) {

                            }

                            @Override
                            public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                            }

                            @Override
                            public void onAdClose(String channel) {
                                if ("knife".equals(type)) {
                                    lwv.loadUrl("javascript:gameBackContinue()");
                                    ButtonStatistical.flyKnifeOverCount();
                                } else if ("scratch".equals(type)) {
                                    lwv.loadUrl("javascript:close()");
                                }
                            }

                            @Override
                            public void onAdShow(String channel) {

                            }
                        }).build();
                break;
        }
    }

    @Override
    public void InitializeAD(String s) {
        if (!TextUtils.isEmpty(s)) {
            final String[] adGroup = s.split(",");
            if (adGroup != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NativeExpressCache.loadGroupNative(adGroup, getContext());
                    }
                });
            }
        }
    }

    @Override
    public void setCanFinish(final String s) {
        scratchClose = s;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (s.equals("1")) {
                    if (lwv != null) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) lwv.getLayoutParams();
                        layoutParams.topMargin = AppUtil.dpToPx(0);
                        lwv.setLayoutParams(layoutParams);
                    }
                } else {
                    if (lwv != null) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) lwv.getLayoutParams();
                        layoutParams.topMargin = (AppUtil.dpToPx(-44));
                        lwv.setLayoutParams(layoutParams);
                    }
                }
            }
        });

        EventBus.getDefault().post(new ScrathTabEvent(s));
    }

    @Override
    public void openNewActivity(final String s) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(s)) {
                    JSONObject json = JSON.parseObject(s);
                    String jump_url = json.getString("jump_url");
                    int judge_type = json.getIntValue("judge_type");
                    String params = json.getString("params");
                    if (judge_type == 1) {//原生
                        AppUtil.startActivityFromAction(getContext(), jump_url, params);
                    } else if (judge_type == 2) {//h5
                        String jump_url_new = jump_url
                                + "?token=" + UserEntity.getInstance().getToken()
                                + "&user_id=" + UserEntity.getInstance().getUserId();
                        LoadWebViewActivity.IntoLoadWebView(getContext(), jump_url_new);
                    }
                }
            }
        });
    }

    //登录、退出登录重新加载
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(UserInfoEvent info) {
        isRefresh = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(ScratchRefreshEvent info) {
        isRefresh = true;
    }

}
