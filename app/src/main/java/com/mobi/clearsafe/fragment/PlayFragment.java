package com.mobi.clearsafe.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.adtest.cache.NativeExpressCache;
import com.example.adtest.interaction.InterActionExpressAdView;
import com.example.adtest.interaction.InterActionLoadListener;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.example.adtest.rewardvideo.RewardVideoLoadListener;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.eventbean.PleasanEvent;
import com.mobi.clearsafe.eventbean.ScratchRefreshEvent;
import com.mobi.clearsafe.eventbean.UserInfoEvent;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.StatusBarUtil;
import com.mobi.clearsafe.widget.GetGoldNewDialog;
import com.mobi.clearsafe.widget.GoldDialog;
import com.mobi.clearsafe.widget.LazyLoadFragment;
import com.mobi.clearsafe.widget.LoadWebView;
import com.mobi.clearsafe.widget.LoadWebViewActivity;
import com.mobi.clearsafe.wxapi.bean.UserEntity;
import com.umeng.analytics.MobclickAgent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
/**
 * author:zhaijinlu
 * date: 2019/12/5
 * desc:
 */
public class PlayFragment extends LazyLoadFragment implements LoadWebView.WebViewListener {
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
    Boolean informationAd;//是否展示信息流
    @Override
    protected int setContentView() {
        return R.layout.playframgent_layout;
    }
    @Override
    protected void lazyLoad() {
        StatusBarUtil.setStatusBarMode(getActivity(), true, R.color.white);
        ButtonStatistical.dongdongPageCount();
        if (Const.pBean != null) {
            if (Const.pBean != null && Const.pBean.show_page == 4) {
                EventBus.getDefault().post(new PleasanEvent(Const.pBean.show_page));
            }
        }
        if (isRefresh) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //   Log.e("sssssss", UserEntity.getInstance().getConfigEntity().getMove_avtivity_url()+"?token="+UserEntity.getInstance().getToken()+"&user_id"+UserEntity.getInstance().getUserId());
                    lwv.loadUrl(UserEntity.getInstance().getConfigEntity().getMove_avtivity_url() +
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
        lwv = findViewById(R.id.webview);
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
                lwv.loadUrl(UserEntity.getInstance().getConfigEntity().getMove_avtivity_url() +
                        "?token=" + UserEntity.getInstance().getToken()
                        + "&user_id=" + UserEntity.getInstance().getUserId());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PlayFragment");
        if (lwv != null) {
            lwv.onResume();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PlayFragment");
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
                    is_ad = object.getBoolean("is_ad");
                    closeAdType = object.getIntValue("close_ad_type");//  关弹窗广告类型，0-正常 1-插屏 2-视频
                    informationAd = object.getBoolean("information_flow_ad");
                    if (informationAd == null) {
                        informationAd = true;
                    }
                    if (is_ad == null) {
                        is_ad = false;
                    }
                    switch (pop_type) {
                        case 1000://直接领金币
                            if (is_ad) {
                                GoldDialog dialog = new GoldDialog.Builder(getActivity())
                                        .setGold(points)
                                        .setDialogClick(new GoldDialog.GetGoldDialogClick() {
                                            @Override
                                            public void closeBtnClick(Dialog dialog) {
                                                dialog.dismiss();
                                                new InterActionExpressAdView.Builder(getContext())
                                                        .setScenarioEnum(ScenarioEnum.move_water_plaque)
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
                                        })
                                        .build();
                                dialog.show();
                            } else {
                                GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getActivity())
                                        .setGold(points)
                                        .isVisible(false)
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
                                            new RewardVideoAd.Builder(getActivity())
                                                    .setSupportDeepLink(true)
                                                    .setScenario(ScenarioEnum.dongdong_bubble_video)
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
                                                            lwv.loadUrl("javascript:activeDouble()");
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
                                            showAd(closeAdType);
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
                                    .setScenario(ScenarioEnum.dongdong_bubble_video)
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
                                            lwv.loadUrl("javascript:finishVideo()");
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
                                    .setShowAgain(show_bottom_btn == 1 ? true : false)
                                    .setBtnText(pop_up_message)
                                    .setIsShowAd(informationAd)
                                    .setScenario(isDouble == 1 ? ScenarioEnum.turn_table_gold_native : ScenarioEnum.turn_table_pkg_gold_natice)
                                    .setPostID(posid)
                                    .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                        @Override
                                        public void doubleBtnClick(Dialog dialog) {
                                            new RewardVideoAd.Builder(getActivity())
                                                    .setSupportDeepLink(true)
                                                    .setScenario(ScenarioEnum.dongdong_bubble_video)
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
                                                            lwv.loadUrl("javascript:finishVideo()");
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
                                            showAd(closeAdType);
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
                                    .setShowAgain(show_bottom_btn == 1 ? true : false)
                                    .setIsShowAd(informationAd)
                                    .setBtnText(pop_up_message)
                                    .setlableIsVisible(true)
                                    .setScenario(isDouble == 1 ? ScenarioEnum.turn_table_gold_native : ScenarioEnum.turn_table_pkg_gold_natice)
                                    .setPostID(posid)
                                    .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                        @Override
                                        public void doubleBtnClick(Dialog dialog) {
                                            new RewardVideoAd.Builder(getActivity())
                                                    .setSupportDeepLink(true)
                                                    .setScenario(ScenarioEnum.dongdong_bubble_video)
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
                                                            lwv.loadUrl("javascript:sixTimeFun()");
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
                                            showAd(closeAdType);
                                        }
                                        @Override
                                        public void bottomBtnClick(Dialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).build();
                            dialog5.show();
                            break;
                        case 1006://直接领金币
                            if (is_ad) {
                                GoldDialog dialog = new GoldDialog.Builder(getActivity())
                                        .setGold(points)
                                        .setDialogClick(new GoldDialog.GetGoldDialogClick() {
                                            @Override
                                            public void closeBtnClick(Dialog dialog) {
                                                dialog.dismiss();
                                                new InterActionExpressAdView.Builder(getContext())
                                                        .setScenarioEnum(ScenarioEnum.move_water_plaque)
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
                                        })
                                        .setPosId(posid)
                                        .build();
                                dialog.show();
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
            new InterActionExpressAdView.Builder(getActivity())
                    .setScenarioEnum(ScenarioEnum.move_water_plaque)
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
        } else if (closeAdType == 2) {//视频
            new RewardVideoAd.Builder(getActivity())
                    .setSupportDeepLink(true)
                    .setScenario(ScenarioEnum.dongdong_bubble_video)
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
                        }
                        @Override
                        public void onAdShow(String channel) {
                        }
                    }).build();
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
    public void showPlaqueAdvert(String type) {
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
    public void setCanFinish(String s) {
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