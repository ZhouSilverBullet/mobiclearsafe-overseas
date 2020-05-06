package com.mobi.clearsafe.main.fragment;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adtest.interaction.InterActionExpressAdView;
import com.example.adtest.interaction.InterActionLoadListener;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.example.adtest.rewardvideo.RewardVideoLoadListener;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.download.MyDownLoadManager;
import com.mobi.clearsafe.eventbean.CheckTabEvent;
import com.mobi.clearsafe.eventbean.TabTipEvent;
import com.mobi.clearsafe.greendao.TodayStepData;
import com.mobi.clearsafe.greendao.TodayStepSessionUtils;
import com.mobi.clearsafe.main.BubbleClickBean;
import com.mobi.clearsafe.main.GoldBubble;
import com.mobi.clearsafe.main.UserStepInfo;
import com.mobi.clearsafe.main.bean.DownLoadBean;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.DateUtils;
import com.mobi.clearsafe.utils.SPUtil;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.widget.GetGoldNewDialog;
import com.mobi.clearsafe.widget.GoldDialog;
import com.mobi.clearsafe.widget.InterActionDialog;
import com.mobi.clearsafe.widget.LoadWebViewActivity;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/27 14:11
 * @Dec 略
 */
public class HomeFragmentWrap implements View.OnClickListener {

    //记录对应的金币泡泡id
    private String bubble1_id;
    private String bubble2_id;
    private String bubble3_id;
    //记录对应泡泡的弹框类型
    private int bubble1_type;
    private int bubble2_type;
    private int bubble3_type;
    //记录对应泡泡弹框类型的button文案
    private String bubble1_type_message;
    private String bubble2_type_message;
    private String bubble3_type_message;
    //记录泡泡对应的金币数
    private int bubble1_coins;
    private int bubble2_coins;
    private int bubble3_coins;
    //记录对应泡泡关闭弹框时是否出广告
    private int bubble1_closeType;
    private int bubble2_closeType;
    private int bubble3_closeType;

    //泡泡弹框是否出信息流广告
    private boolean bubble1_informationAd;
    private boolean bubble2_informationAd;
    private boolean bubble3_informationAd;

    //记录对应泡泡关闭广告样式
    private int bubble1_h1fg;
    private int bubble2_h1fg;
    private int bubble3_h1fg;

    //泡泡是否跳转
    private int bubble1is_jump;
    private int bubble1jump_type;
    private String bubble1jump_url;
    private DownLoadBean bubble1download;

    private int bubble2is_jump;
    private int bubble2jump_type;
    private String bubble2jump_url;
    private DownLoadBean bubble2download;

    private int bubble3is_jump;
    private int bubble3jump_type;
    private String bubble3jump_url;
    private DownLoadBean bubble3download;

    private RelativeLayout bubble1;
    private TextView bubble1Text;
    private RelativeLayout bubble2;
    private TextView bubble2Text;
    private RelativeLayout bubble3;
    private TextView bubble3Text;
    private RelativeLayout bubble4;
    private TextView bubble4Text;

    private TextView bubble1Content;
    private TextView bubble2Content;
    private TextView bubble3Content;

    private HashMap<String, Boolean> bubbleMap;

    private FragmentActivity activity;
    private UserStepInfo stepInfo;

    public HomeFragmentWrap(RelativeLayout bubble1,
                            TextView bubble1Text,
                            RelativeLayout bubble2,
                            TextView bubble2Text,
                            RelativeLayout bubble3,
                            TextView bubble3Text,
                            RelativeLayout bubble4,
                            TextView bubble4Text,
                            TextView bubble1Content,
                            TextView bubble2Content,
                            TextView bubble3Content) {
        this.bubble1 = bubble1;
        this.bubble2 = bubble2;
        this.bubble3 = bubble3;
        this.bubble4 = bubble4;

        this.bubble1Text = bubble1Text;
        this.bubble2Text = bubble2Text;
        this.bubble3Text = bubble3Text;
        this.bubble4Text = bubble4Text;

        this.bubble1Content = bubble1Content;
        this.bubble2Content = bubble2Content;
        this.bubble3Content = bubble3Content;


        initBubble();
    }

    private void initBubble() {
        bubble1.setOnClickListener(this);
        bubble2.setOnClickListener(this);
        bubble3.setOnClickListener(this);
        bubble4.setOnClickListener(this);

        doRepeatAnim(bubble1, 1400);
        doRepeatAnim(bubble2, 1500);
        doRepeatAnim(bubble3, 1600);
        doRepeatAnim(bubble4, 1700);
    }

    public void setActivity(FragmentActivity activity) {
        this.activity = activity;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public void handleBubble() {

        bubbleMap = (HashMap<String, Boolean>) SPUtil.getHashMapData(MyApplication.getContext(), "coins");
        if (bubbleMap.size() == 0) {//首次安装软件默认都为false 隐藏状态
            bubbleMap.put("1", false);
            bubbleMap.put("2", false);
            bubbleMap.put("3", false);
            SPUtil.putHashMapData(MyApplication.getContext(), "coins", bubbleMap);
        }

//        getUserStepInfo();
        getBubble();
    }


    private void doRepeatAnim(View view, long time) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -10, 10, -10);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setDuration(time);
        animator.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bubble1:
                ButtonStatistical.coinsBubbleBtn();
                bubble1.setVisibility(View.GONE);
                bubbleMap.put("1", false);
                SPUtil.putHashMapData(MyApplication.getContext(), "coins", bubbleMap);//控件点击隐藏，更新map数据
                bubble1Text.setText("");//置空控件，方便赋值
                bubbleClick(bubble1_id, bubble1_type, bubble1_type_message, bubble1_coins, bubble1_closeType, bubble1_informationAd, bubble1_h1fg, bubble1is_jump, bubble1jump_type, bubble1jump_url, bubble1download);
                break;
            case R.id.bubble2:
                ButtonStatistical.coinsBubbleBtn();
                bubble2.setVisibility(View.GONE);
                bubbleMap.put("2", false);
                SPUtil.putHashMapData(MyApplication.getContext(), "coins", bubbleMap);
                bubble2Text.setText("");
                bubbleClick(bubble2_id, bubble2_type, bubble2_type_message, bubble2_coins, bubble2_closeType, bubble2_informationAd, bubble2_h1fg, bubble2is_jump, bubble2jump_type, bubble2jump_url, bubble2download);
                break;
            case R.id.bubble3:
                ButtonStatistical.coinsBubbleBtn();
                bubble3.setVisibility(View.GONE);
                bubbleMap.put("3", false);
                SPUtil.putHashMapData(MyApplication.getContext(), "coins", bubbleMap);
                bubble3Text.setText("");
                bubbleClick(bubble3_id, bubble3_type, bubble3_type_message, bubble3_coins, bubble3_closeType, bubble3_informationAd, bubble3_h1fg, bubble3is_jump, bubble3jump_type, bubble3jump_url, bubble3download);
                break;
            case R.id.bubble4:
                ButtonStatistical.exchangeStepBubbleCount();
                //todo 步数换金币就不用了
//                exchangeStep();
                break;
        }
    }

    private void getBubble() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getBubble(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<List<GoldBubble>>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<List<GoldBubble>>() {
                    @Override
                    public void onSuccess(List<GoldBubble> demo) {
//                        refreshLayout.setRefreshing(false);//关闭刷新
                        bubbleMap = (HashMap<String, Boolean>) SPUtil.getHashMapData(MyApplication.getContext(), "coins");
                        int isVisiableCount = 0;
                        for (String count : bubbleMap.keySet()) {
                            if (bubbleMap.get(count) == true) {
                                isVisiableCount++;
                            }
                        }
                        //如果数据条数大于当前控件显示个数，需要增加显示几个，则遍历几次，每次遍历都会增加显示一个
                        if (demo.size() - isVisiableCount == 1) {
                            getBubbleVisiable(1);
                        } else if (demo.size() - isVisiableCount == 2) {
                            getBubbleVisiable(2);
                        } else if (demo.size() - isVisiableCount == 3) {
                            getBubbleVisiable(3);
                        }

                        //根据map的value 控制控件显隐
                        bubble1.setVisibility(bubbleMap.get("1") ? View.VISIBLE : View.GONE);
                        bubble2.setVisibility(bubbleMap.get("2") ? View.VISIBLE : View.GONE);
                        bubble3.setVisibility(bubbleMap.get("3") ? View.VISIBLE : View.GONE);

                        bubble1Text.setText("");
                        bubble2Text.setText("");
                        bubble3Text.setText("");
                        //赋值 控件显示且内容为空
                        for (int i = 0; i < demo.size(); i++) {
                            if (bubbleMap.get("1") && TextUtils.isEmpty(bubble1Text.getText())) {
                                bubble1Text.setText(demo.get(i).getType() == 1 ? String.valueOf(demo.get(i).getPoints()) : "?");
                                if (!TextUtils.isEmpty(demo.get(i).getName())) {
                                    bubble1Content.setText(demo.get(i).getName());
                                } else {
                                    bubble1Content.setText("");
                                }
                                bubble1_id = demo.get(i).getId();
                                bubble1_type = demo.get(i).getPop_type();
                                bubble1_type_message = demo.get(i).getPop_up_message();
                                bubble1_closeType = demo.get(i).getClose_ad_type();
                                bubble1_coins = demo.get(i).getPoints();
                                bubble1_informationAd = demo.get(i).isInformation_flow_ad();
                                bubble1_h1fg = demo.get(i).getH1fg();
                                bubble1is_jump = demo.get(i).getIs_jump();
                                bubble1jump_type = demo.get(i).getJump_type();
                                bubble1jump_url = demo.get(i).getJump_url();
                                bubble1download = demo.get(i).getDownload();
                            } else if (bubbleMap.get("2") && TextUtils.isEmpty(bubble2Text.getText())) {
                                bubble2Text.setText(demo.get(i).getType() == 1 ? String.valueOf(demo.get(i).getPoints()) : "?");
                                if (!TextUtils.isEmpty(demo.get(i).getName())) {
                                    bubble2Content.setText(demo.get(i).getName());
                                } else {
                                    bubble2Content.setText("");
                                }
                                bubble2_id = demo.get(i).getId();
                                bubble2_type = demo.get(i).getPop_type();
                                bubble2_type_message = demo.get(i).getPop_up_message();
                                bubble2_closeType = demo.get(i).getClose_ad_type();
                                bubble2_coins = demo.get(i).getPoints();
                                bubble2_informationAd = demo.get(i).isInformation_flow_ad();
                                bubble2_h1fg = demo.get(i).getH1fg();
                                bubble2is_jump = demo.get(i).getIs_jump();
                                bubble2jump_type = demo.get(i).getJump_type();
                                bubble2jump_url = demo.get(i).getJump_url();
                                bubble2download = demo.get(i).getDownload();
                            } else if (bubbleMap.get("3") && TextUtils.isEmpty(bubble3Text.getText())) {
                                bubble3Text.setText(demo.get(i).getType() == 1 ? String.valueOf(demo.get(i).getPoints()) : "?");
                                if (!TextUtils.isEmpty(demo.get(i).getName())) {
                                    bubble3Content.setText(demo.get(i).getName());
                                } else {
                                    bubble3Content.setText("");
                                }
                                bubble3_id = demo.get(i).getId();
                                bubble3_type = demo.get(i).getPop_type();
                                bubble3_type_message = demo.get(i).getPop_up_message();
                                bubble3_closeType = demo.get(i).getClose_ad_type();
                                bubble3_coins = demo.get(i).getPoints();
                                bubble3_informationAd = demo.get(i).isInformation_flow_ad();
                                bubble3_h1fg = demo.get(i).getH1fg();
                                bubble3is_jump = demo.get(i).getIs_jump();
                                bubble3jump_type = demo.get(i).getJump_type();
                                bubble3jump_url = demo.get(i).getJump_url();
                                bubble3download = demo.get(i).getDownload();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
//                        refreshLayout.setRefreshing(false);//关闭刷新
                        //根据map的value 控制控件显隐
                        bubble1.setVisibility(bubbleMap.get("1") ? View.VISIBLE : View.GONE);
                        bubble2.setVisibility(bubbleMap.get("2") ? View.VISIBLE : View.GONE);
                        bubble3.setVisibility(bubbleMap.get("3") ? View.VISIBLE : View.GONE);
                    }
                });
    }

    /**
     * 获取用户当前步数
     */
    private void getUserStepInfo() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getUserStep(UserEntity.getInstance().getUserId(), 0)
                .compose(CommonSchedulers.<BaseResponse<UserStepInfo>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<UserStepInfo>() {
                    @Override
                    public void onSuccess(UserStepInfo demo) {
//                        refreshLayout.setRefreshing(false);//关闭刷新
                        stepInfo = demo;
                        UserEntity.getInstance().setTarget_step(demo.getTarget_step());
                        UserEntity.getInstance().setCash(demo.getCash());
                        UserEntity.getInstance().setPoints(demo.getTotal_coins());
                        EventBus.getDefault().post(new TabTipEvent(demo.getSign_points()));
                        if (demo != null) {
                            //数据库中存入微信步数
                            List<TodayStepData> data = TodayStepSessionUtils.query(getActivity(), DateUtils.getStringDateDay());
                            if (data != null && data.size() > 0) {
                                TodayStepData stepData = data.get(0);
                                stepData.setWeChat_data(String.valueOf(demo.getStep()));
                                stepData.setDate(DateUtils.getStringDateDay());
                                stepData.setTime(String.valueOf(System.currentTimeMillis()));
                                TodayStepSessionUtils.insertDbBean(getActivity(), stepData);
                            } else {
                                String userId = UserEntity.getInstance().getUserId();
                                TodayStepData stepData = new TodayStepData();
                                if (!TextUtils.isEmpty(userId)) {
                                    stepData.setUserId(userId);
                                }
                                stepData.setWeChat_data(String.valueOf(demo.getStep()));
                                stepData.setDate(DateUtils.getStringDateDay());
                                stepData.setTime(String.valueOf(System.currentTimeMillis()));
                                TodayStepSessionUtils.insertDbBean(getActivity(), stepData);
                            }
                            upDateView(demo);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
//                        refreshLayout.setRefreshing(false);//关闭刷新
                        bubble1.setVisibility(bubbleMap.get("1") ? View.VISIBLE : View.GONE);
                        bubble2.setVisibility(bubbleMap.get("2") ? View.VISIBLE : View.GONE);
                        bubble3.setVisibility(bubbleMap.get("3") ? View.VISIBLE : View.GONE);
                    }

                });
    }

    /**
     * 刷新UI
     */
    private void upDateView(UserStepInfo demo) {
        if (demo.isIs_step_bubble()) {
            bubble4.setVisibility(View.VISIBLE);
            bubble4Text.setText(demo.getBubble().getExchange_coins() + "");
        } else {
            bubble4.setVisibility(View.GONE);
        }
        List<GoldBubble> coins_bubble_list = demo.getCoins_bubble_list();
        bubbleMap = (HashMap<String, Boolean>) SPUtil.getHashMapData(MyApplication.getContext(), "coins");
        int isVisiableCount = 0;
        for (String count : bubbleMap.keySet()) {
            if (bubbleMap.get(count) == true) {
                isVisiableCount++;
            }
        }
        //如果数据条数大于当前控件显示个数，需要增加显示几个，则遍历几次，每次遍历都会增加显示一个
        if (coins_bubble_list.size() - isVisiableCount == 1) {
            getBubbleVisiable(1);
        } else if (coins_bubble_list.size() - isVisiableCount == 2) {
            getBubbleVisiable(2);
        } else if (coins_bubble_list.size() - isVisiableCount == 3) {
            getBubbleVisiable(3);
        }

        //根据map的value 控制控件显隐
        bubble1.setVisibility(bubbleMap.get("1") ? View.VISIBLE : View.GONE);
        bubble2.setVisibility(bubbleMap.get("2") ? View.VISIBLE : View.GONE);
        bubble3.setVisibility(bubbleMap.get("3") ? View.VISIBLE : View.GONE);
        bubble1Text.setText("");
        bubble2Text.setText("");
        bubble3Text.setText("");
        //赋值 控件显示且内容为空
        for (int i = 0; i < coins_bubble_list.size(); i++) {
            if (bubbleMap.get("1") && TextUtils.isEmpty(bubble1Text.getText())) {
                bubble1Text.setText(coins_bubble_list.get(i).getType() == 1 ? String.valueOf(coins_bubble_list.get(i).getPoints()) : "?");
                if (!TextUtils.isEmpty(coins_bubble_list.get(i).getName())) {
                    bubble1Content.setText(coins_bubble_list.get(i).getName());
                } else {
                    bubble1Content.setText("");
                }
                bubble1_id = coins_bubble_list.get(i).getId();
                bubble1_type = coins_bubble_list.get(i).getPop_type();
                bubble1_type_message = coins_bubble_list.get(i).getPop_up_message();
                bubble1_closeType = coins_bubble_list.get(i).getClose_ad_type();
                bubble1_coins = coins_bubble_list.get(i).getPoints();
                bubble1_informationAd = coins_bubble_list.get(i).isInformation_flow_ad();
                bubble1_h1fg = coins_bubble_list.get(i).getH1fg();
                bubble1is_jump = coins_bubble_list.get(i).getIs_jump();
                bubble1jump_type = coins_bubble_list.get(i).getJump_type();
                bubble1jump_url = coins_bubble_list.get(i).getJump_url();
                bubble1download = coins_bubble_list.get(i).getDownload();
            } else if (bubbleMap.get("2") && TextUtils.isEmpty(bubble2Text.getText())) {
                bubble2Text.setText(coins_bubble_list.get(i).getType() == 1 ? String.valueOf(coins_bubble_list.get(i).getPoints()) : "?");
                if (!TextUtils.isEmpty(coins_bubble_list.get(i).getName())) {
                    bubble2Content.setText(coins_bubble_list.get(i).getName());
                } else {
                    bubble2Content.setText("");
                }
                bubble2_id = coins_bubble_list.get(i).getId();
                bubble2_type = coins_bubble_list.get(i).getPop_type();
                bubble2_type_message = coins_bubble_list.get(i).getPop_up_message();
                bubble2_closeType = coins_bubble_list.get(i).getClose_ad_type();
                bubble2_coins = coins_bubble_list.get(i).getPoints();
                bubble2_informationAd = coins_bubble_list.get(i).isInformation_flow_ad();
                bubble2_h1fg = coins_bubble_list.get(i).getH1fg();
                bubble2is_jump = coins_bubble_list.get(i).getIs_jump();
                bubble2jump_type = coins_bubble_list.get(i).getJump_type();
                bubble2jump_url = coins_bubble_list.get(i).getJump_url();
                bubble2download = coins_bubble_list.get(i).getDownload();
            } else if (bubbleMap.get("3") && TextUtils.isEmpty(bubble3Text.getText())) {
                bubble3Text.setText(coins_bubble_list.get(i).getType() == 1 ? String.valueOf(coins_bubble_list.get(i).getPoints()) : "?");
                if (!TextUtils.isEmpty(coins_bubble_list.get(i).getName())) {
                    bubble3Content.setText(coins_bubble_list.get(i).getName());
                } else {
                    bubble3Content.setText("");
                }
                bubble3_id = coins_bubble_list.get(i).getId();
                bubble3_type = coins_bubble_list.get(i).getPop_type();
                bubble3_type_message = coins_bubble_list.get(i).getPop_up_message();
                bubble3_closeType = coins_bubble_list.get(i).getClose_ad_type();
                bubble3_coins = coins_bubble_list.get(i).getPoints();
                bubble3_informationAd = coins_bubble_list.get(i).isInformation_flow_ad();
                bubble3_h1fg = coins_bubble_list.get(i).getH1fg();
                bubble3is_jump = coins_bubble_list.get(i).getIs_jump();
                bubble3jump_type = coins_bubble_list.get(i).getJump_type();
                bubble3jump_url = coins_bubble_list.get(i).getJump_url();
                bubble3download = coins_bubble_list.get(i).getDownload();
            }
        }
    }

    public void getBubbleVisiable(int size) {
        for (int i = 0; i < size; i++) {
            if (!bubbleMap.get("1")) {
                bubbleMap.put("1", true);
            } else if (!bubbleMap.get("2")) {
                bubbleMap.put("2", true);
            } else if (!bubbleMap.get("3")) {
                bubbleMap.put("3", true);
            }
            SPUtil.putHashMapData(MyApplication.getContext(), "coins", bubbleMap);
        }
    }


    public void bubbleClick(final String id, final int pop_type, final String btn_text, final int coins, final int closeAdType, final boolean informationAd, final int h1fg, int is_jump, int jump_type, String jump_url, DownLoadBean downLoadBean) {
        ButtonStatistical.coinsBubbleBtnNumer();
        switch (pop_type) {
            case 1000:
                if (is_jump == 1) {
                    jump(jump_type, jump_url, "", downLoadBean);
                }
                getBubbleClickCoins(id, pop_type, btn_text);
                break;
            case 1001:
                if (is_jump == 1) {
                    jump(jump_type, jump_url, "", downLoadBean);
                }
                OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                        .bubbleClick(UserEntity.getInstance().getUserId(), id)
                        .compose(CommonSchedulers.<BaseResponse<BubbleClickBean>>observableIO2Main(getActivity()))
                        .subscribe(new BaseObserver<BubbleClickBean>() {
                            @Override
                            public void onSuccess(BubbleClickBean demo) {
                                UserEntity.getInstance().setPoints(demo.getTotal());
                                UserEntity.getInstance().setCash(demo.getCash());
                                getBubble();
//                                getUserStepInfo();
                                GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getActivity())
                                        .isVisible(true)
                                        .setShowAgain(false)
                                        .setBtnText(btn_text)
                                        .setIsShowAd(demo.isInformation_flow_ad())
                                        .setGold(demo.getPoints())
                                        .setScenario(ScenarioEnum.gold_bubble_native)
                                        .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                            @Override
                                            public void doubleBtnClick(Dialog dialog) {
                                                //金币翻倍跳转广告
                                                dialog.dismiss();
                                                ButtonStatistical.doubleBtnNumer();
                                                new RewardVideoAd.Builder(getActivity())
                                                        .setSupportDeepLink(true)
                                                        .setScenario(ScenarioEnum.gold_bubble_double_video)
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
                                                                getDoubleBubbleConins();
                                                            }

                                                            @Override
                                                            public void onAdShow(String channel) {

                                                            }
                                                        }).build();
                                            }

                                            @Override
                                            public void closeBtnClick(Dialog dialog) {
                                                dialog.dismiss();
                                                //关闭时展示广告
                                                showPlaque(h1fg);
                                                //   showAd(closeAdType);
                                            }

                                            @Override
                                            public void bottomBtnClick(Dialog dialog) {

                                            }
                                        }).build();
                                dialog.show();
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg, String code) {
                                ToastUtils.showShort(errorMsg);
                            }
                        });
                break;
            case 1002:
                new RewardVideoAd.Builder(getActivity())
                        .setSupportDeepLink(true)
                        .setScenario(ScenarioEnum.gold_bubble_double_video)
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
                                getBubbleClickCoins(id, pop_type, btn_text);
                            }

                            @Override
                            public void onAdShow(String channel) {

                            }
                        }).build();
                break;
            case 1003:
            case 1004:
                if (is_jump == 1) {
                    jump(jump_type, jump_url, "", downLoadBean);
                }
                GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getActivity())
                        .isVisible(true)
                        .setShowAgain(false)
                        .setIsShowAd(informationAd)
                        .setBtnText(btn_text)
                        .setGold(coins)
                        .setScenario(ScenarioEnum.gold_bubble_native)
                        .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                            @Override
                            public void doubleBtnClick(Dialog dialog) {
                                dialog.dismiss();
                                new RewardVideoAd.Builder(getActivity())
                                        .setSupportDeepLink(true)
                                        .setScenario(ScenarioEnum.gold_bubble_double_video)
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
                                                getBubbleClickCoins(id, pop_type, btn_text);
                                            }

                                            @Override
                                            public void onAdShow(String channel) {

                                            }
                                        }).build();
                            }

                            @Override
                            public void closeBtnClick(Dialog dialog) {
                                getBubble();
//                                getUserStepInfo();
                                dialog.dismiss();
                                //关闭弹窗时展示广告
                                showPlaque(h1fg);
                                //   showAd(closeAdType);
                            }

                            @Override
                            public void bottomBtnClick(Dialog dialog) {

                            }
                        }).build();
                dialog.show();
                break;
            case 1005:
                if (is_jump == 1) {
                    jump(jump_type, jump_url, "", downLoadBean);
                }
                OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                        .bubbleClick(UserEntity.getInstance().getUserId(), id)
                        .compose(CommonSchedulers.<BaseResponse<BubbleClickBean>>observableIO2Main(getActivity()))
                        .subscribe(new BaseObserver<BubbleClickBean>() {
                            @Override
                            public void onSuccess(BubbleClickBean demo) {
                                UserEntity.getInstance().setPoints(demo.getTotal());
                                UserEntity.getInstance().setCash(demo.getCash());
                                getBubble();
//                                getUserStepInfo();
                                //金币泡泡弹出获得金币数量弹窗
                                GetGoldNewDialog dialogs = new GetGoldNewDialog.Builder(getActivity())
                                        .isVisible(true)
                                        .setShowAgain(false)
                                        .setBtnText(btn_text)
                                        .setlableIsVisible(true)
                                        .setIsShowAd(demo.isInformation_flow_ad())
                                        .setGold(demo.getPoints())
                                        .setScenario(ScenarioEnum.gold_bubble_native)
                                        .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                            @Override
                                            public void doubleBtnClick(Dialog dialog) {
                                                //金币翻倍跳转广告
                                                dialog.dismiss();
                                                new RewardVideoAd.Builder(getActivity())
                                                        .setSupportDeepLink(true)
                                                        .setScenario(ScenarioEnum.gold_bubble_double_video)
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
                                                                getDoubleRandom();
                                                            }

                                                            @Override
                                                            public void onAdShow(String channel) {

                                                            }
                                                        }).build();
                                            }

                                            @Override
                                            public void closeBtnClick(Dialog dialog) {
                                                dialog.dismiss();
                                                showPlaque(h1fg);
                                                //  showAd(closeAdType);
                                            }

                                            @Override
                                            public void bottomBtnClick(Dialog dialog) {

                                            }
                                        }).build();
                                dialogs.show();
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg, String code) {
                                ToastUtils.showShort(errorMsg);
                            }
                        });
                break;
        }
    }

    //泡泡跳转
    private void jump(int jump_type, String jump_url, String parm, DownLoadBean downLoadBean) {
        if (jump_type == 1) {//原生
            AppUtil.startActivityFromAction(getActivity(), jump_url, parm);
        } else if (jump_type == 2) {//H5
            if (jump_url.contains("turn3") && !jump_url.contains("scratchList")) {
                ButtonStatistical.bubbleToTurn();
            } else if (jump_url.contains("scratchList")) {
                ButtonStatistical.bubbleToScratch();
            }
            String url = jump_url
                    + "?token=" + UserEntity.getInstance().getToken()
                    + "&user_id=" + UserEntity.getInstance().getUserId()
                    + "&version=" + AppUtil.packageName(MyApplication.getContext());
            LoadWebViewActivity.IntoLoadWebView(getActivity(), url);
        } else if (jump_type == 3) {
            EventBus.getDefault().post(new CheckTabEvent(jump_url));
        } else if (jump_type == 4) {//下载
            if (AppUtil.getNetworkTypeName().equals("WIFI")) {
                if (downLoadBean != null) {
                    if (AppUtil.checkAppInstalled(getActivity(), downLoadBean.pkgName)) {
                        //APP已安装
                    } else {
                        //APP未安装
                        if (AppUtil.checkDownLoad(downLoadBean.pkgName, getActivity())) {
                            //已下载
                            AppUtil.installAPP(getActivity(), AppUtil.getFilePath(downLoadBean.pkgName, getActivity()));
                        } else {
                            //未下载
                            MyDownLoadManager.downLoadApk(getActivity(), downLoadBean.downloadUrl, downLoadBean.pkgName, downLoadBean.appName);
                        }
                    }
                }
            }
        }

    }

    //直接领取
    private void getBubbleClickCoins(String id, final int pop_type, final String btn_text) {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .bubbleClick(UserEntity.getInstance().getUserId(), id)
                .compose(CommonSchedulers.<BaseResponse<BubbleClickBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<BubbleClickBean>() {
                    @Override
                    public void onSuccess(BubbleClickBean demo) {
                        UserEntity.getInstance().setPoints(demo.getTotal());
                        UserEntity.getInstance().setCash(demo.getCash());
                        getBubble();
//                        getUserStepInfo();
                        if (demo.getH1fg() != 3000) {
                            showPlaqueDialog(demo);
                        } else {
                            showGoldDialog(demo, pop_type, demo.isInformation_flow_ad());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        ToastUtils.showShort(errorMsg);
                    }
                });
    }

    //信息流弹框
    private void showGoldDialog(BubbleClickBean demo, int pop_type, boolean informationAd) {
        //金币泡泡弹出获得金币数量弹窗
        GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getActivity())
                .isVisible(false)
                .setShowAgain(false)
                .setlableIsVisible(false)
                .setFanbeiTitle(pop_type == 1005 ? true : false)
                .setIsShowAd(informationAd)
                .setGold(demo.getPoints())
                .setScenario(ScenarioEnum.gold_bubble_native)
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

                    }
                }).build();
        dialog.show();
    }

    //插屏弹框
    private void showPlaqueDialog(final BubbleClickBean demo) {
        GoldDialog dialog = new GoldDialog.Builder(getActivity())
                .setGold(demo.getPoints())
                .setDialogClick(new GoldDialog.GetGoldDialogClick() {
                    @Override
                    public void closeBtnClick(Dialog dialog) {
                        dialog.dismiss();
                        showPlaque(demo.getH1fg());
                    }
                })
                .build();
        dialog.show();

    }

    //展示插屏广告
    private void showPlaque(int h1fg) {
        switch (h1fg) {
            case 3001:
                new InterActionExpressAdView.Builder(getActivity())
                        .setScenarioEnum(ScenarioEnum.default_plaque)
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
                new InterActionExpressAdView.Builder(getActivity())
                        .setScenarioEnum(ScenarioEnum.default_plaque)
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
                InterActionDialog actionDialog = new InterActionDialog.Builder(getActivity())
                        .setH1fg(false)
                        .setEnum(ScenarioEnum.gold_bubble_native)
                        .build();
                actionDialog.show();
                break;
            case 3004:
                InterActionDialog actionDialog2 = new InterActionDialog.Builder(getActivity())
                        .setH1fg(true)
                        .setEnum(ScenarioEnum.gold_bubble_native)
                        .build();
                actionDialog2.show();
                break;
            case 3005:
                new RewardVideoAd.Builder(getActivity())
                        .setSupportDeepLink(true)
                        .setScenario(ScenarioEnum.gold_bubble_double_video)
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
                break;
        }
    }

    //金币泡泡翻倍
    private void getDoubleBubbleConins() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .doubleBubbleConins(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<BubbleClickBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<BubbleClickBean>() {
                    @Override
                    public void onSuccess(BubbleClickBean demo) {
                        UserEntity.getInstance().setPoints(demo.getTotal());
                        UserEntity.getInstance().setCash(demo.getCash());
                        if (demo.getH1fg() != 3000) {
                            showPlaqueDialog(demo);
                        } else {
                            showGoldDialog(demo, 1000, demo.isInformation_flow_ad());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    //翻六倍接口
    private void getDoubleRandom() {
        ButtonStatistical.sixFbCount();
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getDoubleRandom(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<BubbleClickBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<BubbleClickBean>() {
                    @Override
                    public void onSuccess(BubbleClickBean demo) {
                        UserEntity.getInstance().setPoints(demo.getTotal_points());
                        UserEntity.getInstance().setCash(demo.getCash());
                        if (demo.getH1fg() != 3000) {
                            showPlaqueDialog(demo);
                        } else {
                            showGoldDialog(demo, 1005, demo.isInformation_flow_ad());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }
}
