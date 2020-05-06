package com.mobi.clearsafe.main.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.nativeexpress.NativeExpressAd;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.example.adtest.rewardvideo.RewardVideoLoadListener;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.main.bean.SignRaceSuccessBean;
import com.mobi.clearsafe.main.bean.StepRaceBean;
import com.mobi.clearsafe.main.bean.StepRaceDetailBean;
import com.mobi.clearsafe.main.bean.StepRaceItemBean;
import com.mobi.clearsafe.main.bean.StepRaceRewardBean;
import com.mobi.clearsafe.main.bean.StepRaceUserInfoBean;
import com.mobi.clearsafe.main.dialog.StepFailureDialog;
import com.mobi.clearsafe.main.dialog.StepRacePayDialog;
import com.mobi.clearsafe.main.dialog.StepRaceSignSuccessDialog;
import com.mobi.clearsafe.main.dialog.StepRewardDialog;
import com.mobi.clearsafe.moneyactivity.ShareActivity;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.DateUtils;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.widget.LoadWebViewActivity;
import com.mobi.clearsafe.widget.NoPaddingTextView;
import com.mobi.clearsafe.widget.StepRaceCardDialog;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * author : liangning
 * date : 2019-12-04  15:36
 */
public class StepRaceActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static void IntoStepRace(Context context) {
        Intent intent = new Intent(context, StepRaceActivity.class);
        context.startActivity(intent);
    }

    private LinearLayout ll_back, ll_history, ll_guize;
    private View v_redpoint;//如8000步比赛有未领取奖励 则显示此红点 点击8000步按钮 领取完奖励后 此红点消失
    private TextView tv_leftbtn, tv_rightbtn, tv_allreward, tv_signnum, sign_type, tv_sign_type_tips;
    private StepRaceBean bean;
    private StepRaceItemBean itemBean;
    private int selectIndex = 0;
    private NoPaddingTextView tv_allreward_nper;
    private TextView tv_step1, tv_step2;
    private TextView tv_nextrace_title;//下期比赛显示标题 已报名的比赛在进行中 则显示下期内容
    private TextView tv_next_all_gold, tv_next_all_people;
    private TextView tv_dabiao_num, tv_all_jiangchi;
    private TextView tv_targetstep_nowstep;
    private ImageView iv_topbg;
    private RelativeLayout rl_main_bg;//主区域半圆背景
    private LinearLayout ll_content_bg;//中间主内容区域控件 设置背景
    private LinearLayout ll_racenobegin_sign;//比赛未开始显示的报名人数
    private TextView tv_issign;//比赛未开始 显示的报名状态
    private LinearLayout ll_raceing_sign_jiangchi;//比赛开始时显示的达标人数和总奖池
    private LinearLayout ll_raceing_step;//比赛进行中的步数情况 和达标情况
    private TextView tv_isdabiao;//比赛进行中 达标情况 如未达标 显示进行中 已达标 显示已达标
    private LinearLayout ll_sign_btn;//报名按钮 如已报名当前赛事 并且比赛未开始 则显示分享按钮
    private LinearLayout ll_share;//分享按钮 如报名的当前比赛未开始 则显示分享按钮
    private TextView tv_sign_next;//报名下一期 如果报名的赛事在比赛中 则显示报名下一期
    private LinearLayout ll_nextrace_content;//下期比赛显示报名人数 和奖金池 已报名比赛在进行中，则显示下期内容
    private TextView tv_countdown_time, tv_countdown_time_8000;
    private TextView tv_hdgz;
    private static final int STEP_LEFT = 3000;
    private static final int STEP_RIGHT = 8000;
    private static final String REQUEST_PARAM = "[3000,8000]";
    private RelativeLayout rl_stepracecard;
    private TextView tv_card_num;
    private RelativeLayout fl_ad;

    /**
     * 改变布局状态
     */
    private void checkViewStatus() {
        if (itemBean == null) {
            return;
        }
        //判断今日有无报名
        if (itemBean.user_curr_stages != null) {
            //有报名 显示进行中
            tv_allreward.setText(getResources().getString(R.string.yuqidabiao_jiangjin, itemBean.user_curr_stages.stages_number));
            if (itemBean.detail_curr_stages != null) {
                tv_allreward_nper.setText(String.valueOf(itemBean.detail_curr_stages.expect_points));
            }
            ll_racenobegin_sign.setVisibility(View.GONE);
            ll_raceing_sign_jiangchi.setVisibility(View.VISIBLE);
            ll_raceing_step.setVisibility(View.VISIBLE);
            ll_sign_btn.setVisibility(View.GONE);
            ll_sign_btn.clearAnimation();
            tv_nextrace_title.setVisibility(View.VISIBLE);
            ll_nextrace_content.setVisibility(View.VISIBLE);
            tv_issign.setVisibility(View.GONE);
           // ll_content_bg.setBackgroundResource(R.mipmap.steprace_content_shownext_bg);
            if (itemBean.user_next_stages != null) {
                ll_share.setVisibility(View.VISIBLE);
                tv_sign_next.setVisibility(View.GONE);
            } else {
                ll_share.setVisibility(View.GONE);
                tv_sign_next.setVisibility(View.VISIBLE);
            }
        } else if (itemBean.user_next_stages != null) {
            //已报名下一期 显示距开赛还多久 界面
            tv_allreward.setText(getResources().getString(R.string.nper_allreward, itemBean.user_next_stages.stages_number));
            if (itemBean.detail_next_stages != null) {
                tv_allreward_nper.setText(String.valueOf(itemBean.detail_next_stages.jackpot_points));
            }
            ll_racenobegin_sign.setVisibility(View.VISIBLE);
            ll_share.setVisibility(View.VISIBLE);
            ll_raceing_sign_jiangchi.setVisibility(View.GONE);
            ll_raceing_step.setVisibility(View.GONE);
            ll_sign_btn.setVisibility(View.GONE);
            ll_sign_btn.clearAnimation();
            tv_sign_next.setVisibility(View.GONE);
            tv_nextrace_title.setVisibility(View.GONE);
            ll_nextrace_content.setVisibility(View.GONE);
            tv_issign.setVisibility(View.VISIBLE);
           // ll_content_bg.setBackgroundResource(R.mipmap.steprace_content_bg);
            //显示下一期 显示报名界面
//             {//已报名下一期 显示距开赛还多久 界面
//
//            } else {//没报名 显示 下一期报名界面
//
//            }

        } else {
            //显示下一期 显示报名界面
            if (itemBean.detail_next_stages != null) {
                tv_allreward.setText(getResources().getString(R.string.nper_allreward, itemBean.detail_next_stages.stages_number));
                if (itemBean.detail_next_stages != null) {
                    tv_allreward_nper.setText(String.valueOf(itemBean.detail_next_stages.jackpot_points));
                }
                tv_issign.setVisibility(View.GONE);
                ll_racenobegin_sign.setVisibility(View.VISIBLE);
                ll_share.setVisibility(View.GONE);
                ll_raceing_sign_jiangchi.setVisibility(View.GONE);
                ll_raceing_step.setVisibility(View.GONE);
                ll_sign_btn.setVisibility(View.VISIBLE);
                starAnimal();
                tv_sign_next.setVisibility(View.GONE);
                tv_nextrace_title.setVisibility(View.GONE);
                ll_nextrace_content.setVisibility(View.GONE);
               // ll_content_bg.setBackgroundResource(R.mipmap.steprace_content_bg);
            }

        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steprace_layout);
        ButtonStatistical.toStepChallengeCount();
        initView();
        requestData();
    }

    private void initView() {
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        v_redpoint = findViewById(R.id.v_redpoint);
        tv_leftbtn = findViewById(R.id.tv_leftbtn);
        tv_leftbtn.setOnClickListener(this);
        tv_rightbtn = findViewById(R.id.tv_rightbtn);
        tv_rightbtn.setOnClickListener(this);
        ll_history = findViewById(R.id.ll_history);
        ll_history.setOnClickListener(this);
        ll_guize = findViewById(R.id.ll_guize);
        ll_guize.setOnClickListener(this);
        tv_allreward = findViewById(R.id.tv_allreward);
        tv_allreward_nper = findViewById(R.id.tv_allreward_nper);
        tv_signnum = findViewById(R.id.tv_signnum);
        sign_type = findViewById(R.id.sign_type);
        tv_sign_type_tips = findViewById(R.id.tv_sign_type_tips);
        tv_step1 = findViewById(R.id.tv_step1);
        tv_step2 = findViewById(R.id.tv_step2);
        tv_nextrace_title = findViewById(R.id.tv_nextrace_title);
        tv_next_all_gold = findViewById(R.id.tv_next_all_gold);
        tv_next_all_people = findViewById(R.id.tv_next_all_people);
        tv_dabiao_num = findViewById(R.id.tv_dabiao_num);
        tv_all_jiangchi = findViewById(R.id.tv_all_jiangchi);
        tv_targetstep_nowstep = findViewById(R.id.tv_targetstep_nowstep);
        ll_sign_btn = findViewById(R.id.ll_sign_btn);
        ll_sign_btn.setOnClickListener(this);
        iv_topbg = findViewById(R.id.iv_topbg);
        rl_main_bg = findViewById(R.id.rl_main_bg);
        ll_content_bg = findViewById(R.id.ll_content_bg);
        ll_racenobegin_sign = findViewById(R.id.ll_racenobegin_sign);
        tv_issign = findViewById(R.id.tv_issign);
        ll_raceing_sign_jiangchi = findViewById(R.id.ll_raceing_sign_jiangchi);
        ll_raceing_step = findViewById(R.id.ll_raceing_step);
        tv_isdabiao = findViewById(R.id.tv_isdabiao);
        ll_share = findViewById(R.id.ll_share);
        ll_share.setOnClickListener(this);
        tv_sign_next = findViewById(R.id.tv_sign_next);
        tv_sign_next.setOnClickListener(this);
        ll_nextrace_content = findViewById(R.id.ll_nextrace_content);
        tv_countdown_time = findViewById(R.id.tv_countdown_time);
        tv_countdown_time_8000 = findViewById(R.id.tv_countdown_time_8000);
        tv_hdgz = findViewById(R.id.tv_hdgz);
        tv_hdgz.setOnClickListener(this);
        rl_stepracecard = findViewById(R.id.rl_stepracecard);
        rl_stepracecard.setOnClickListener(this);
        tv_card_num = findViewById(R.id.tv_card_num);
        fl_ad=findViewById(R.id.fl_ad);
        new NativeExpressAd.Builder(this)
                .setAdCount(1)
                .setADViewSize(350, 0)
                .setHeightAuto(true)
                .setSupportDeepLink(true)
                .setBearingView(fl_ad)
                .setScenario(ScenarioEnum.steprace_native)
                .build();

    }

    private void starAnimal() {
        Animation scaleAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.btn_scale);
        LinearInterpolator lins = new LinearInterpolator();
        scaleAnim.setInterpolator(lins);
        ll_sign_btn.startAnimation(scaleAnim);
    }

    /**
     * 刷新页面显示数据
     */
    private void changeView() {
        if (itemBean == null) {
            return;
        }
        if (itemBean.user_curr_stages == null) {//本期没报名 显示 下期赛事信息
            signInfo(itemBean.detail_next_stages);
        } else {//本期报名了
            if (itemBean.detail_curr_stages != null) {//本期赛事详情不为空
                signInfo(itemBean.detail_curr_stages);
            } else if (itemBean.detail_next_stages != null) {
                signInfo(itemBean.detail_next_stages);
            }
        }
        //设置下期赛事显示信息
        if (itemBean.detail_next_stages != null) {
            tv_nextrace_title.setText(getResources().getString(R.string.next_nper_sign_info, itemBean.detail_next_stages.stages_number, String.valueOf(itemBean.detail_next_stages.enrolment_number)));
            tv_next_all_gold.setText(String.valueOf(itemBean.detail_next_stages.jackpot_points));
            tv_next_all_people.setText(String.valueOf(itemBean.detail_next_stages.enrolment_number));
        }

        if (itemBean.user_curr_stages != null) {
            tv_targetstep_nowstep.setText(Html.fromHtml(getResources().getString(R.string.targetstep_nowstep, String.valueOf(itemBean.user_curr_stages.user_step), String.valueOf(itemBean.user_curr_stages.competition_type))));
            if (itemBean.detail_curr_stages != null) {
                if (itemBean.user_curr_stages.user_step >= itemBean.detail_curr_stages.competition_type) {
                    tv_isdabiao.setText(getResources().getString(R.string.yidabiao));
                } else {
                    tv_isdabiao.setText(getResources().getString(R.string.jinxingzhong));
                }
            }

        }
    }

    private void signInfo(StepRaceDetailBean raceBean) {
        if (raceBean == null) {
            return;
        }
        tv_allreward.setText(getResources().getString(R.string.nper_allreward, raceBean.stages_number));
        tv_allreward_nper.setText(String.valueOf(raceBean.jackpot_points));
        tv_signnum.setText(getResources().getString(R.string.signsteprace_num, String.valueOf(raceBean.enrolment_number)));
        tv_dabiao_num.setText(getResources().getString(R.string.dabiao_num, String.valueOf(raceBean.qualified_persons)));
        tv_all_jiangchi.setText(getResources().getString(R.string.all_jiangchi, String.valueOf(raceBean.jackpot_points)));
        if (raceBean.enroll_type == 1) {//报名方式 1支付金币 2看视频报名
            sign_type.setText(String.valueOf(raceBean.enroll_button));
            tv_sign_type_tips.setText(String.valueOf(raceBean.enroll_button2));
//            sign_type.setText(getResources().
//                    getString(R.string.pay_gold_sign,
//                            String.valueOf(raceBean.enroll_points)));
//            tv_sign_type_tips.setText(getResources().getString(R.string.pay_gold_sign_tips, String.valueOf(raceBean.competition_type)));
            tv_step1.setText(getResources().getString(R.string.pay_qiyuejin));
        } else {
            sign_type.setText(String.valueOf(raceBean.enroll_button));
            tv_sign_type_tips.setText(String.valueOf(raceBean.enroll_button2));
//            sign_type.setText(getResources().getString(R.string.watch_free_sign));
//            tv_sign_type_tips.setText(getResources().getString(R.string.watch_free_sign_tips, String.valueOf(raceBean.enroll_success_points)));
            tv_step1.setText(getResources().getString(R.string.watch_video));
        }
        tv_step2.setText(getResources().getString(R.string.finish_step, String.valueOf(raceBean.competition_type)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_leftbtn:
                if (selectIndex != 0) {
                    selectIndex = 0;
                    if (bean != null) {
                        itemBean = bean.step_3000;
                    }
                    ButtonStatistical.challengeFivetabCount();
                    changeTab();
                    changeView();
                    checkViewStatus();
                    CountTimer();
                    tv_countdown_time.setVisibility(View.VISIBLE);
                    tv_countdown_time_8000.setVisibility(View.GONE);
                    if (itemBean != null) {
                        checkReward(itemBean.user_prev_stages);
                    }
                }
                break;
            case R.id.tv_rightbtn:
                if (selectIndex != 1) {
                    selectIndex = 1;
                    if (bean != null) {
                        itemBean = bean.step_8000;
                    }
                    ButtonStatistical.challengeEighttabCount();
                    changeTab();
                    changeView();
                    checkViewStatus();
                    CountTimer();
                    tv_countdown_time.setVisibility(View.GONE);
                    tv_countdown_time_8000.setVisibility(View.VISIBLE);
                    if (itemBean != null) {
                        checkReward(itemBean.user_prev_stages);
                    }
                }
                break;
            case R.id.ll_history:
                int competition_type = STEP_LEFT;
                int activity_id = 1;
                if (itemBean != null) {
                    competition_type = itemBean.detail_next_stages.competition_type;
                    activity_id = itemBean.detail_next_stages.activity_id;
                }
                StepRaceHistoryActivity.IntoStepRaceHistory(StepRaceActivity.this, competition_type, activity_id);
                ButtonStatistical.entryRecordCount();
                break;
            case R.id.ll_guize:
                if (UserEntity.getInstance().getConfigEntity() != null && UserEntity.getInstance().getConfigEntity().getStep_challenge_rules_url() != null) {
                    LoadWebViewActivity.IntoLoadWebView(StepRaceActivity.this, UserEntity.getInstance().getConfigEntity().getStep_challenge_rules_url());
                }
                break;
            case R.id.tv_hdgz:
                if (UserEntity.getInstance().getConfigEntity() != null && UserEntity.getInstance().getConfigEntity().getStep_challenge_rules_url() != null) {
                    LoadWebViewActivity.IntoLoadWebView(StepRaceActivity.this, UserEntity.getInstance().getConfigEntity().getStep_challenge_rules_url());
                }
                break;
            case R.id.ll_sign_btn://报名按钮
                if (itemBean != null && itemBean.detail_next_stages != null) {
                    if (itemBean.detail_next_stages.competition_type == STEP_LEFT) {
                        ButtonStatistical.challengeFiveApplyCount();
                    } else {
                        ButtonStatistical.challengeEightApplyCount();
                    }
                    signRace(itemBean.detail_next_stages);
                }
                break;
            case R.id.tv_sign_next:
                if (itemBean != null && itemBean.detail_next_stages != null) {
                    signRace(itemBean.detail_next_stages);
                }
                break;
            case R.id.ll_share:
                //分享
                getActivity().startActivity(new Intent(getActivity(), ShareActivity.class));
                break;
            case R.id.rl_stepracecard:
                if (bean != null) {
                    StepRaceCardDialog dialog = new StepRaceCardDialog.Builder(StepRaceActivity.this)
                            .setContent(bean.step_card_rule)
                            .build();
                    dialog.show();
                }
                break;
        }

    }

    private void changeTab() {
        if (selectIndex == 0) {//选中5000步
            tv_leftbtn.setTextColor(getResources().getColor(R.color.white));
            tv_leftbtn.setBackgroundResource(R.drawable.shape_corner_18dp_ff7900);
            tv_rightbtn.setTextColor(getResources().getColor(R.color.c_FF7900));
            tv_rightbtn.setBackgroundResource(R.drawable.shape_corner_18dp_tr);
           // rl_main_bg.setBackgroundResource(R.mipmap.steprace_bg);
            iv_topbg.setImageResource(R.mipmap.three_thousand_topbg);
        } else {//选中8000步
            tv_rightbtn.setTextColor(getResources().getColor(R.color.white));
            tv_rightbtn.setBackgroundResource(R.drawable.shape_corner_18dp_ff7900);
            tv_leftbtn.setTextColor(getResources().getColor(R.color.c_FF7900));
            tv_leftbtn.setBackgroundResource(R.drawable.shape_corner_18dp_tr);
         //   rl_main_bg.setBackgroundResource(R.mipmap.steprace_baqian_bg);
            iv_topbg.setImageResource(R.mipmap.baqian_topbg);
        }
    }

    /**
     * 请求数据 主数据 详情数据
     */
    private void requestData() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .requestStepRace(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, Const.SIGNIG_ACTIVITY_ID, REQUEST_PARAM)
                .compose(CommonSchedulers.<BaseResponse<StepRaceBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<StepRaceBean>() {
                    @Override
                    public void onSuccess(StepRaceBean demo) {
                        if (demo != null) {
                            bean = demo;
                            if (demo.step_props_count > 0) {
                                tv_card_num.setText(String.valueOf(demo.step_props_count));
                                tv_card_num.setVisibility(View.VISIBLE);
                            } else {
                                tv_card_num.setVisibility(View.INVISIBLE);
                            }
                            if (selectIndex == 0) {
                                itemBean = bean.step_3000;
                            } else {
                                itemBean = bean.step_8000;
                            }
                            changeView();
                            checkViewStatus();
                            checkReward(itemBean.user_prev_stages);
                            CountTimer();
                            if (demo.step_8000 != null && demo.step_8000.user_prev_stages != null) {
                                if (demo.step_8000.user_prev_stages.state != 2 && demo.step_8000.user_prev_stages.state != 3) {
                                    v_redpoint.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    /**
     * 报名比赛
     *
     * @param racebean
     */
    private void signRace(final StepRaceDetailBean racebean) {
        if (racebean == null) {
            return;
        }
        if (racebean.enroll_type == 1) {//支付金币报名
            StepRacePayDialog dialog = new StepRacePayDialog.Builder(this)
                    .setNper(racebean.stages_number)
                    .setGold(String.valueOf(racebean.enroll_points))
                    .setListener(new StepRacePayDialog.StepRacePayListener() {
                        @Override
                        public void payClick(Dialog dialog) {
                            requestSign(racebean);
                            dialog.dismiss();
                        }
                    }).build();
            dialog.show();
        } else {//看视频报名
            if(AppUtil.HWIsShowAd()){
                new RewardVideoAd.Builder(this)
                        .setSupportDeepLink(true)
                        .setScenario(ScenarioEnum.step_challenge_video)
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
                                requestSign(racebean);
                            }

                            @Override
                            public void onAdShow(String channel) {

                            }
                        }).build();
            }
        }
    }

    /**
     * 请求报名赛事
     *
     * @param racebean
     */
    private void requestSign(final StepRaceDetailBean racebean) {
        if (racebean == null) {
            return;
        }
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .requestSignRace(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, racebean.activity_id, racebean.competition_type)
                .compose(CommonSchedulers.<BaseResponse<SignRaceSuccessBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<SignRaceSuccessBean>() {
                    @Override
                    public void onSuccess(SignRaceSuccessBean demo) {
                        if (demo != null) {
                            UserEntity.getInstance().setCash(demo.cash);
                            UserEntity.getInstance().setPoints(demo.total_points);
                            StepRaceSignSuccessDialog dialog = new StepRaceSignSuccessDialog.Builder(getActivity())
                                    .setNper(racebean.stages_number)
                                    .setGold(String.valueOf(racebean.enroll_success_points))
                                    .setCreatTimt(racebean.create_time)
                                    .setOpeningTime(racebean.opening_time)
                                    .setLottertTime(racebean.lottery_time)
                                    .setListener(new StepRaceSignSuccessDialog.StepRaceSignSuccessListener() {
                                        @Override
                                        public void btnClick(Dialog dialog) {
                                            dialog.dismiss();
//                                            if (racebean.competition_type == STEP_LEFT) {
//                                                ButtonStatistical.challengeFiveReceiveCount();
//                                            } else {
//                                                ButtonStatistical.challengeEightReceiveCount();
//                                            }
                                          //  EventBus.getDefault().post(new CheckTabEvent("mission"));
                                           // AppManager.getInstance().returnToActivity(MainActivity.class);
                                            //关闭此页面 切换
                                        }
                                    }).build();
                            dialog.show();
                            requestData();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        requestData();
                        if (!TextUtils.isEmpty(errorMsg)) {
                            ToastUtils.showShort(errorMsg);
                        }
                    }
                });
    }


    /**
     * 检查用户是否有未领取奖励
     *
     * @param userbean
     */
    private void checkReward(final StepRaceUserInfoBean userbean) {
        if (userbean == null) {
            v_redpoint.setVisibility(View.GONE);
            return;
        }
        if (userbean.state != 2 && userbean.state != 3) {//请求服务器
            OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                    .requestStepRaceLingqu(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, userbean.activity_id, userbean.competition_type, userbean.stages_number)
                    .compose(CommonSchedulers.<BaseResponse<StepRaceRewardBean>>observableIO2Main(this))
                    .subscribe(new BaseObserver<StepRaceRewardBean>() {
                        @Override
                        public void onSuccess(StepRaceRewardBean demo) {
                            if (demo != null) {
                                UserEntity.getInstance().setPoints(demo.total_points);
                                UserEntity.getInstance().setCash(demo.cash);
//                                if (selectIndex == 1) {
                                v_redpoint.setVisibility(View.GONE);
//                                }
                                requestData();
                                if (userbean.is_standard == 1) {//弹已达标弹窗
                                    StepRewardDialog dialog = new StepRewardDialog.Builder(getActivity())
                                            .setGoldString(String.valueOf(demo.points))
                                            .setNper(userbean.stages_number)
                                            .setListener(new StepRewardDialog.StepRewardListener() {
                                                @Override
                                                public void btnClick(Dialog dialog) {
                                                    if (itemBean != null && itemBean.detail_next_stages != null) {
                                                        signRace(itemBean.detail_next_stages);
                                                    }
                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void closeClick(Dialog dialog) {
                                                    dialog.dismiss();
                                                }
                                            }).build();
                                    dialog.show();

                                } else {//弹未达标弹窗
                                    StepFailureDialog failureDialog = new StepFailureDialog.Builder(getActivity())
                                            .setNper(userbean.stages_number)
                                            .setListener(new StepFailureDialog.StepFailureListener() {
                                                @Override
                                                public void closeClick(Dialog dialog) {
                                                    dialog.dismiss();
                                                }

                                                @Override
                                                public void signNextClick(Dialog dialog) {
                                                    if (itemBean != null && itemBean.detail_next_stages != null) {
                                                        signRace(itemBean.detail_next_stages);
                                                    }
                                                    dialog.dismiss();
                                                }
                                            }).build();
                                    failureDialog.show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg, String code) {

                        }
                    });
        }
    }

    private Timer mTimer = null;
    private TimerTask mTask = null;

    private void CountTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                //要做的事
                if (bean != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            countDown(bean.step_3000);
                            countDown(bean.step_8000);
                        }
                    });


                }
            }
        };
        mTimer.schedule(mTask, 0, 1000);
    }

    private void countDown(StepRaceItemBean racebean) {
        if (racebean != null) {
            if (racebean.user_curr_stages != null) {//已报名当前赛事 显示开奖
                if (racebean.user_curr_stages.lottery_time_countdown <= 0) {
                    requestData();
                }
                racebean.user_curr_stages.lottery_time_countdown--;
                racebean.user_curr_stages.opening_time_countdown--;
                String showC = DateUtils.SecondToDate(racebean.user_curr_stages.lottery_time_countdown);
                if (racebean.user_curr_stages.competition_type == STEP_LEFT) {
                    tv_countdown_time.setText(getResources().getString(R.string.open_reward_time, showC));
                } else {
                    tv_countdown_time_8000.setText(getResources().getString(R.string.open_reward_time, showC));
                }
            } else {//未报名 显示下期开赛时间
                if (racebean.detail_next_stages != null) {
                    if (racebean.detail_next_stages.opening_time_countdown <= 0) {
                        requestData();
                    }
                    racebean.detail_next_stages.lottery_time_countdown--;
                    racebean.detail_next_stages.opening_time_countdown--;
                    String showC = DateUtils.SecondToDate(racebean.detail_next_stages.opening_time_countdown);
                    if (racebean.detail_next_stages.competition_type == STEP_LEFT) {
                        tv_countdown_time.setText(getResources().getString(R.string.open_race_time, showC));
                    } else {
                        tv_countdown_time_8000.setText(getResources().getString(R.string.open_race_time, showC));
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }

    }
}
