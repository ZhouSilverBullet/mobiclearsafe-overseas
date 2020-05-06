package com.mobi.overseas.clearsafe.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.bumptech.glide.Glide;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.example.adtest.rewardvideo.RewardVideoLoadListener;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.WeChatLoginActivity;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.base.adapter.CommonAdapter;
import com.mobi.overseas.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.overseas.clearsafe.eventbean.CheckTabEvent;
import com.mobi.overseas.clearsafe.eventbean.PleasanEvent;
import com.mobi.overseas.clearsafe.eventbean.UserInfoEvent;
import com.mobi.overseas.clearsafe.fragment.bean.SignBean;
import com.mobi.overseas.clearsafe.fragment.bean.SigninBean;
import com.mobi.overseas.clearsafe.fragment.bean.TaskBean;
import com.mobi.overseas.clearsafe.main.activity.InviteActivity;
import com.mobi.overseas.clearsafe.main.activity.StepRaceActivity;
import com.mobi.overseas.clearsafe.me.activity.WithdrawalActivity;
import com.mobi.overseas.clearsafe.me.bean.PointsBean;
import com.mobi.overseas.clearsafe.me.bean.UploadNikeName;
import com.mobi.overseas.clearsafe.moneyactivity.BindPhoneActivity;
import com.mobi.overseas.clearsafe.moneyactivity.InputInviteCodeActivity;
import com.mobi.overseas.clearsafe.moneyactivity.ShareActivity;
import com.mobi.overseas.clearsafe.moneyactivity.bean.ActivityBean;
import com.mobi.overseas.clearsafe.moneyactivity.bean.CardBean;
import com.mobi.overseas.clearsafe.moneyactivity.bean.RewardEntity;
import com.mobi.overseas.clearsafe.moneyactivity.bean.SiginRemind;
import com.mobi.overseas.clearsafe.moneyactivity.bean.SigninDouble;
import com.mobi.overseas.clearsafe.moneyactivity.bean.TaskEntity;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.ui.clear.ad.ADRelativeLayout;
import com.mobi.overseas.clearsafe.ui.common.util.StatusBarPaddingUtil;
import com.mobi.overseas.clearsafe.utils.AnimationUtil;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.CalendarReminderUtils;
import com.mobi.overseas.clearsafe.utils.StatusBarUtil;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.utils.imageloader.ImageLoader;
import com.mobi.overseas.clearsafe.widget.CardDialog;
import com.mobi.overseas.clearsafe.widget.GetGoldNewDialog;
import com.mobi.overseas.clearsafe.widget.LazyLoadFragment;
import com.mobi.overseas.clearsafe.widget.LoadWebViewActivity;
import com.mobi.overseas.clearsafe.widget.RemindDialog;
import com.mobi.overseas.clearsafe.widget.RoundImageView;
import com.mobi.overseas.clearsafe.widget.SignNewDialog;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class activityFragment extends LazyLoadFragment implements View.OnClickListener {

    private RecyclerView rv_sign, rv_sign2, rv_task, new_task;
    private CommonAdapter<SignBean.WeekAll> adapter;
    private CommonAdapter<SignBean.WeekAll> adapter2;
    private CommonAdapter<TaskBean> taskAdapter;
    private CommonAdapter<TaskBean> newTaskAdapter;
    private TextView tv_gold_num, tv_rmb_num;
    private SwipeRefreshLayout swipe_refresh;
    private List<TaskBean> taskList = new ArrayList<>();//普通任务
    private List<TaskBean> newtaskList = new ArrayList<>();//新人任务
    private List<SignBean.WeekAll> signList = new ArrayList<>();//前七天签到天列表
    private List<SignBean.WeekAll> signList2 = new ArrayList<>();//后七天签到天列表

    private SignBean signBean = null;
    private CountDownTimer mTimer;
    private CountDownTimer mTimer2;
    private TextView tv_sign_day_golds;
    private ImageView iv_more_task;

    private LinearLayout layout_reward;
    private RewardEntity rewardEntity;
    private LinearLayout layout_hotact;//新人任务布局

    private boolean isload = false;
    private LinearLayoutManager manager;


    private TextView sign_num;
    private RelativeLayout layout_carry;
    private ViewFlipper notice_view;
    private LinearLayout layout_sign_btn;

    private TextView new_num;
    private TextView receive_card;
    private ViewFlipper card_tip;
    private TextView reward_text;

    private boolean isRefresh = false;//判断登录前后刷新数据
    private boolean can_receive;//收益卡是否能领取


    @Override
    protected int setContentView() {
        return R.layout.fragment_activity;
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("activityFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("activityFragment");

        if (isload) {//用于大转盘页面退出时请求数据
            getTask();
            getPoints();
            isload = false;
        }

    }

    @Override
    protected void lazyLoad() {
        StatusBarPaddingUtil.statusBar(getActivity(), false);

        ButtonStatistical.taskPage();
        getTask();
        getPoints();
        getSignData();
        getRewardCoins();
        if (isRefresh) {
            getSignData();
            isRefresh = false;
        }
        if (signBean != null && signBean.is_today_signin == 0) {
            sign();
        } else {
            if (Const.pBean != null) {
                if (Const.pBean != null && Const.pBean.show_page == 3) {
                    EventBus.getDefault().post(new PleasanEvent(Const.pBean.show_page));
                }
            }
        }
    }


    //得到特权金币
    private void getRewardCoins() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getRewareCoins(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, 20)
                .compose(CommonSchedulers.<BaseResponse<RewardEntity>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<RewardEntity>() {
                    @Override
                    public void onSuccess(RewardEntity demo) {
                        if (demo != null) {
                            rewardEntity = demo;
                            if (demo.getState() == 0) {
//                                layout_reward.setVisibility(View.VISIBLE);
                                if (!TextUtils.isEmpty(demo.getButton_text())) {
                                    reward_text.setText(demo.getButton_text());
                                }
                            } else {
                                layout_reward.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimer2 != null) {
            mTimer2.cancel();
            mTimer2 = null;
        }
    }

    @Override
    protected void firstIn() {

        StatusBarPaddingUtil.topViewPadding(findViewById(R.id.iv_top_bg));
        StatusBarPaddingUtil.topViewPadding(findViewById(R.id.layout_gold));
        StatusBarPaddingUtil.topViewPadding(findViewById(R.id.layout_carry));

        StatusBarPaddingUtil.statusBar(getActivity(), false);

        initView();
    }

    private void initView() {
        EventBus.getDefault().register(this);
        new_num = findViewById(R.id.new_num);
        receive_card = findViewById(R.id.receive_card);
        receive_card.setOnClickListener(this);
        card_tip = findViewById(R.id.card_tip);
        sign_num = findViewById(R.id.sign_num);
        layout_carry = findViewById(R.id.layout_carry);
        layout_carry.setOnClickListener(this);
        notice_view = findViewById(R.id.notice_view);
        layout_sign_btn = findViewById(R.id.layout_sign_btn);
        layout_sign_btn.setOnClickListener(this);
        swipe_refresh = findViewById(R.id.swipe_refresh);
        iv_more_task = findViewById(R.id.iv_more_task);
        reward_text = findViewById(R.id.reward_text);
//        if (UserEntity.getInstance().getConfigEntity().isShow_more_activity()) {
//            iv_more_task.setVisibility(View.VISIBLE);
//        } else {
//            iv_more_task.setVisibility(View.GONE);
//        }
        tv_sign_day_golds = findViewById(R.id.tv_sign_day_golds);

        tv_gold_num = findViewById(R.id.tv_gold_num);
        tv_rmb_num = findViewById(R.id.tv_rmb_num);
        layout_reward = findViewById(R.id.layout_reward);
        layout_hotact = findViewById(R.id.layout_hotact);
        layout_reward.setOnClickListener(this);

        rv_sign = findViewById(R.id.rv_sign);
        rv_sign2 = findViewById(R.id.rv_sign2);
        adapter = new CommonAdapter<SignBean.WeekAll>(getContext(), R.layout.item_signdays_layout, signList) {
            @Override
            public void convert(RecycleViewHolder holder, final SignBean.WeekAll item) {
                TextView tv_nums = holder.getView(R.id.tv_nums);
                LinearLayout ll_fanbei = holder.getView(R.id.ll_fanbei);
                TextView tv_isLing = holder.getView(R.id.tv_isLing);
                ImageView iv_fanbei = holder.getView(R.id.iv_fanbei);
                LinearLayout ll_red = holder.getView(R.id.ll_red);
                TextView tv_red = holder.getView(R.id.tv_red);
                TextView red_nums = holder.getView(R.id.red_nums);
                LinearLayout sign_top = holder.getView(R.id.sign_top);
                TextView fb_tv = holder.getView(R.id.fb_tv);
                if (item.show == 0) {//金色金币
                    ll_red.setVisibility(View.GONE);
                    ll_fanbei.setVisibility(View.GONE);
                    tv_nums.setVisibility(View.VISIBLE);
                    tv_nums.setTextColor(getResources().getColor(R.color.white));
                    tv_nums.setBackgroundResource(R.mipmap.gold_icon2);
                    tv_nums.setText(String.valueOf(item.points));
                    tv_isLing.setVisibility(View.VISIBLE);
                    tv_isLing.setText(getString(R.string.sign_day, String.valueOf(item.number)));
                    tv_isLing.setTextColor(getResources().getColor(R.color.gray_a7cb));
                    tv_isLing.setText(getString(R.string.sign_day, String.valueOf(item.number)));
                    sign_top.setVisibility(View.INVISIBLE);
                } else if (item.show == 1) {//红包
                    tv_nums.setVisibility(View.GONE);
                    tv_isLing.setVisibility(View.GONE);
                    ll_fanbei.setVisibility(View.GONE);
                    tv_red.setVisibility(View.VISIBLE);
                    red_nums.setText(String.valueOf(item.points));
                    tv_red.setText(getString(R.string.sign_day, String.valueOf(item.number)));
                    tv_red.setTextColor(getResources().getColor(R.color.gray_a7cb));
                    sign_top.setVisibility(View.INVISIBLE);
                } else if (item.show == 3) {//翻倍
                    Glide.with(getActivity()).load(R.mipmap.fanbei_icon2).into(iv_fanbei);
                    tv_nums.setVisibility(View.GONE);
                    tv_nums.setVisibility(View.GONE);
                    ll_fanbei.setVisibility(View.VISIBLE);
                    tv_isLing.setVisibility(View.GONE);
                    ll_red.setVisibility(View.GONE);
                    sign_top.setVisibility(View.VISIBLE);
                    fb_tv.setText("+" + item.points);
                    sign_top.startAnimation(AnimationUtil.getTranslateAnimation(0, 0, -0.2f, 0.1f, 2000, 0, -1));
                } else {//灰色 已领
                    sign_top.setVisibility(View.INVISIBLE);
                    ll_red.setVisibility(View.GONE);
                    ll_fanbei.setVisibility(View.GONE);
                    tv_nums.setVisibility(View.VISIBLE);
                    tv_nums.setTextColor(getResources().getColor(R.color.c_C5C5C5));
                    tv_nums.setBackgroundResource(R.mipmap.yiling_icon);
                    tv_nums.setText(String.valueOf(item.points));
                    tv_isLing.setVisibility(View.VISIBLE);
                    if (signBean.week_day == item.number) {
                        tv_isLing.setTextColor(getResources().getColor(R.color.gray_a7aa));
                    } else {
                        tv_isLing.setTextColor(getResources().getColor(R.color.gray_a7aa));
                    }
                    tv_isLing.setText(getString(R.string.received));
                }
                ll_fanbei.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //观看激励视频后，调取签到翻倍
                        if (AppUtil.HWIsShowAd()) {
                            new RewardVideoAd.Builder(getContext())
                                    .setSupportDeepLink(true)
                                    .setScenario(ScenarioEnum.sign_double_video)
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
                                            getSigninDouble(Const.SIGNIG_ACTIVITY_ID);
                                        }

                                        @Override
                                        public void onAdShow(String channel) {

                                        }
                                    }).build();
                        }
                    }
                });
            }
        };
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_sign.setLayoutManager(manager);
        rv_sign.setAdapter(adapter);
        rv_sign.setItemViewCacheSize(7);

//        adapter2 = new CommonAdapter<SignBean.WeekAll>(getContext(), R.layout.item_signdays_layout, signList2) {
//            @Override
//            public void convert(RecycleViewHolder holder, final SignBean.WeekAll item) {
//                TextView tv_nums = holder.getView(R.id.tv_nums);
//                LinearLayout ll_fanbei = holder.getView(R.id.ll_fanbei);
//                TextView tv_isLing = holder.getView(R.id.tv_isLing);
//                ImageView iv_fanbei = holder.getView(R.id.iv_fanbei);
//                LinearLayout ll_red=holder.getView(R.id.ll_red);
//                TextView tv_red=holder.getView(R.id.tv_red);
//                TextView red_nums=holder.getView(R.id.red_nums);
//                TextView sign_top=holder.getView(R.id.sign_top);
//                if (item.show == 0) {//金色金币
//                    sign_top.setVisibility(View.INVISIBLE);
//                    ll_red.setVisibility(View.GONE);
//                    ll_fanbei.setVisibility(View.GONE);
//                    tv_nums.setVisibility(View.VISIBLE);
//                    tv_nums.setTextColor(getResources().getColor(R.color.white));
//                    tv_nums.setBackgroundResource(R.mipmap.gold_icon);
//                    tv_nums.setText(String.valueOf(item.points));
//                    tv_isLing.setVisibility(View.VISIBLE);
//                    tv_isLing.setText(getString(R.string.sign_day, String.valueOf(item.number)));
//                    tv_isLing.setTextColor(getResources().getColor(R.color.gray_a7cb));
//                    tv_isLing.setText(getString(R.string.sign_day, String.valueOf(item.number)));
//                } else if (item.show == 1) {//红包
//                    sign_top.setVisibility(View.INVISIBLE);
//                    tv_nums.setVisibility(View.GONE);
//                    tv_isLing.setVisibility(View.GONE);
//                    ll_fanbei.setVisibility(View.GONE);
//                    tv_red.setVisibility(View.VISIBLE);
//                    red_nums.setText(String.valueOf(item.points));
//                    tv_red.setText(getString(R.string.sign_day, String.valueOf(item.number)));
//                    tv_red.setTextColor(getResources().getColor(R.color.gray_a7cb));
//                } else if (item.show == 3) {//翻倍
//                    Glide.with(getActivity()).load(R.mipmap.fanbei_icon).into(iv_fanbei);
//                    tv_nums.setVisibility(View.GONE);
//                    tv_nums.setVisibility(View.GONE);
//                    ll_fanbei.setVisibility(View.VISIBLE);
//                    tv_isLing.setVisibility(View.GONE);
//                    ll_red.setVisibility(View.GONE);
//                    sign_top.setVisibility(View.VISIBLE);
//                    sign_top.setText(String.valueOf(item.points));
//                } else {//灰色 已领
//                    sign_top.setVisibility(View.INVISIBLE);
//                    ll_red.setVisibility(View.GONE);
//                    ll_fanbei.setVisibility(View.GONE);
//                    tv_nums.setVisibility(View.VISIBLE);
//                    tv_nums.setTextColor(getResources().getColor(R.color.c_C5C5C5));
//                    tv_nums.setBackgroundResource(R.mipmap.yiling_icon);
//                    tv_nums.setText(String.valueOf(item.points));
//                    tv_isLing.setVisibility(View.VISIBLE);
//                    if (signBean.week_day == item.number) {
//                        tv_isLing.setTextColor(getResources().getColor(R.color.gray_a7aa));
//                    }else {
//                        tv_isLing.setTextColor(getResources().getColor(R.color.gray_a7aa));
//                    }
//                    tv_isLing.setText(getString(R.string.received));
//                }
//                ll_fanbei.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //观看激励视频后，调取签到翻倍
//                        new RewardVideoAd.Builder(getContext())
//                                .setSupportDeepLink(true)
//                                .setScenario(ScenarioEnum.sign_double_video)
//                                .setRewardVideoLoadListener(new RewardVideoLoadListener() {
//                                    @Override
//                                    public void onAdClick(String channel) {
//
//                                    }
//
//                                    @Override
//                                    public void onVideoComplete(String channel) {
//
//                                    }
//
//                                    @Override
//                                    public void onLoadFaild(String channel, int faildCode, String faildMsg) {
//
//                                    }
//
//                                    @Override
//                                    public void onAdClose(String channel) {
//                                        getSigninDouble(Const.SIGNIG_ACTIVITY_ID);
//                                    }
//
//                                    @Override
//                                    public void onAdShow(String channel) {
//
//                                    }
//                                }).build();
//                    }
//                });
//            }
//        };
//        manager = new LinearLayoutManager(getContext());
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        rv_sign2.setLayoutManager(manager);
//        rv_sign2.setAdapter(adapter2);
//        rv_sign2.setItemViewCacheSize(7);


        new_task = findViewById(R.id.new_task);
        newTaskAdapter = new CommonAdapter<TaskBean>(getContext(), R.layout.new_task_item, newtaskList) {
            @Override
            public void convert(final RecycleViewHolder holder, final TaskBean taskBean) {
                LinearLayout layout_view = holder.getView(R.id.layout_view);
                RoundImageView task_icon = holder.getView(R.id.task_icon);
                TextView tv_content = holder.getView(R.id.tv_content);
                LinearLayout layout_btn = holder.getView(R.id.layout_btn);
                TextView btn_content = holder.getView(R.id.btn_content);
                ImageView gold_icon = holder.getView(R.id.gold_icon);
                TextView tv_point = holder.getView(R.id.tv_point);
                final int left_times = taskBean.total_times - taskBean.times;//已完成次数
//                if (taskBean.total_times > 1) {
//                    String text = taskBean.name + "(" + left_times + "/" + taskBean.total_times + ")";
//                    SpannableStringBuilder style = new SpannableStringBuilder(text);
//                    style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c_FFAD2B)), text.indexOf("(") + 1, text.indexOf("/"), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                    holder.setText(R.id.tv_content, style);
//                } else {
//                    holder.setText(R.id.tv_content, taskBean.name);
//                }
                tv_content.setText(taskBean.name);
                ImageLoader.loadCircleImage(getContext(), task_icon, taskBean.title_picture);

                if (taskBean.activity_state == 1) {
                    btn_content.setVisibility(View.VISIBLE);
                    gold_icon.setVisibility(View.GONE);
                    tv_point.setVisibility(View.GONE);
                    btn_content.setText("已完成");
                    btn_content.setTextColor(getResources().getColor(R.color.c_B1B1B1));
                } else if (taskBean.activity_state == 4) {
                    btn_content.setVisibility(View.VISIBLE);
                    gold_icon.setVisibility(View.GONE);
                    tv_point.setVisibility(View.GONE);
                    btn_content.setText("立即领取");
                    btn_content.setTextColor(getResources().getColor(R.color.white));
                } else {
                    layout_btn.setBackgroundResource(R.drawable.finishi_bg_shape);
                    btn_content.setVisibility(View.GONE);
                    gold_icon.setVisibility(View.VISIBLE);
                    tv_point.setVisibility(View.VISIBLE);
                    tv_point.setText("+" + taskBean.points);
                }
                layout_btn.clearAnimation();
                switch (taskBean.activity_state) {
                    case 1://已完成
                        layout_btn.setBackgroundResource(R.drawable.finishi_bg_shape);
                        break;
                    case 4://立即领取
                        layout_btn.setBackgroundResource(R.drawable.new_task_btn_shape);
                        Animation scaleAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.btn_scale);
                        LinearInterpolator lins = new LinearInterpolator();
                        scaleAnim.setInterpolator(lins);
                        layout_btn.startAnimation(scaleAnim);
                        break;
                }
                layout_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (taskBean.activity_state == 4) {//领取金币点击按钮
                            ButtonStatistical.receiveBtn();
                            showDialog(taskBean.pop_type, taskBean.activity_id, taskBean.pop_up_message, taskBean.points, taskBean.class_id);
                        } else {
                            if (taskBean.activity_id == 3 && taskBean.class_id == 3) {//绑定手机号
                                if (UserEntity.getInstance().getUserInfo() == null) {
                                    ToastUtils.showShort("请先微信登录");
                                    WeChatLoginActivity.IntoSettings(getActivity());
                                    return;
                                }
                                if (taskBean.activity_state == 2) {
                                    ButtonStatistical.bindPhoneBtn();
                                    BindPhoneActivity.IntoBindPhone(getActivity());
                                }
                            } else if (taskBean.activity_id == 4 && taskBean.class_id == 3) {//绑定微信
                                if (taskBean.activity_state == 2) {
                                    ButtonStatistical.bindWechatBtn();
                                    WeChatLoginActivity.IntoSettings(getActivity());
                                }
                            } else if (taskBean.activity_id == 5 && taskBean.class_id == 3) {//激励视频
                                if (taskBean.activity_state == 5 && mTimer == null) {//有限次数内方可进入视频
                                    ButtonStatistical.creativeVideoBtn();
                                    new RewardVideoAd.Builder(getContext())
                                            .setSupportDeepLink(true)
                                            .setScenario(ScenarioEnum.task_video)
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
                                                    getGold(taskBean.activity_id);
                                                }

                                                @Override
                                                public void onAdShow(String channel) {

                                                }
                                            }).build();

                                }
                            } else if (taskBean.activity_id == 24 && taskBean.class_id == 3) {//分组看视频
                                if (taskBean.activity_state == 5 && mTimer2 == null) {//有限次数内方可进入视频
                                    ButtonStatistical.timeThreeVideo();
                                    new RewardVideoAd.Builder(getContext())
                                            .setSupportDeepLink(true)
                                            .setScenario(ScenarioEnum.task_video)
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
                                                    updateVidioCount();
                                                }

                                                @Override
                                                public void onAdShow(String channel) {

                                                }
                                            }).build();

                                }
                            } else if (taskBean.activity_id == 25 && taskBean.class_id == 3) {//转盘
                                if (left_times < taskBean.total_times) {//有限次数内方可进入转盘
                                    ButtonStatistical.bigWheelBtn();
                                    ButtonStatistical.goRotaryTableCount();
                                    isload = true;
                                    LoadWebViewActivity.IntoLoadWebView(getContext(), taskBean.jump_address + "?token=" + UserEntity.getInstance().getToken() + "&user_id=" + UserEntity.getInstance().getUserId() + "&version=" + AppUtil.packageName(MyApplication.getContext()));
                                }
                            } else if (taskBean.activity_id == 13 && taskBean.class_id == 3) {//趣味游戏
                                isload = true;
                                ButtonStatistical.polymerization();
                                LoadWebViewActivity.IntoLoadWebView(getContext(), taskBean.jump_address + "?token=" + UserEntity.getInstance().getToken() + "&user_id=" + UserEntity.getInstance().getUserId() + "&version=" + AppUtil.packageName(MyApplication.getContext()));
                            } else if (taskBean.activity_id == 15 && taskBean.class_id == 3) {//提现
                                if (taskBean.activity_state == 3) {
                                    if (UserEntity.getInstance().getUserInfo() == null) {
                                        WeChatLoginActivity.IntoSettings(getActivity());
                                        return;
                                    }
                                    ButtonStatistical.toWithdrawl();
                                    Intent intent = WithdrawalActivity.IntoWithdrawal(getActivity());
                                    startActivity(intent);
                                    isload = true;
                                }
                            } else if (taskBean.activity_id == 1 && taskBean.class_id == 16) {//达标赛
                                StepRaceActivity.IntoStepRace(getActivity());
                                isload = true;
                            } else if (taskBean.activity_id == 17 && taskBean.class_id == 3) {//每日分享
                                ButtonStatistical.everydayGoShare();
                                getActivity().startActivity(new Intent(getActivity(), ShareActivity.class));
                                //  getShareContent();
                            } else if (taskBean.activity_id == 36 && taskBean.class_id == 3) {//邀请好友
                                ButtonStatistical.inviteTofinish();
                                getActivity().startActivity(new Intent(getActivity(), ShareActivity.class));
                                isload = true;
                            } else if (taskBean.activity_id == 18 && taskBean.class_id == 3) {//填写邀请码
                                if (UserEntity.getInstance().getUserInfo() == null) {
                                    ToastUtils.showShort("请先微信登录");
                                    WeChatLoginActivity.IntoSettings(getActivity());
                                    return;
                                }
                                getActivity().startActivity(new Intent(getActivity(), InputInviteCodeActivity.class));
                                isload = true;
                            } else if (taskBean.activity_id == 1 && taskBean.class_id == 19) {//日历提醒
                                if (taskBean.activity_state == 7) {
                                    ButtonStatistical.openRemindCount();
                                    getPermissions();
                                }
                            } else if (taskBean.activity_id == 7 && taskBean.class_id == 3) {//去喝水
                                ButtonStatistical.toFinishDrinkWater();
                                LoadWebViewActivity.IntoLoadWebView(getContext(), taskBean.jump_address + "?token=" + UserEntity.getInstance().getToken() + "&user_id=" + UserEntity.getInstance().getUserId() + "&version=" + AppUtil.packageName(MyApplication.getContext()));
                            } else if (taskBean.activity_id == 31 && taskBean.class_id == 3) {
                                ButtonStatistical.tosports();
                                EventBus.getDefault().post(new CheckTabEvent("activity"));
                            }
                        }
                    }
                });
            }
        };
        LinearLayoutManager linearLayoutManagernew = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        new_task.setLayoutManager(linearLayoutManagernew);
        new_task.setAdapter(newTaskAdapter);


        rv_task = findViewById(R.id.rv_task);
        taskAdapter = new CommonAdapter<TaskBean>(getContext(), R.layout.item_task_layout, taskList) {
            @Override
            public void convert(final RecycleViewHolder holder, final TaskBean taskBean) {
                setData(holder, taskBean);
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv_task.setLayoutManager(linearLayoutManager);
        rv_task.setAdapter(taskAdapter);
        swipe_refresh.setEnabled(false);
//        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getPoints();
//                getTask();
//                getSignData();
//            }
//        });

    }

    private void setData(final RecycleViewHolder holder, final TaskBean taskBean) {
        //第三个位置的广告位
        View llRoot = holder.getView(R.id.llRoot);
        ADRelativeLayout adRelativeLayout = holder.getView(R.id.adRLayout);
        if (taskBean.getType() == TaskBean.TYPE_AD) {
            llRoot.setVisibility(View.GONE);
            adRelativeLayout.setVisibility(View.VISIBLE);
            adRelativeLayout.setAdrScenario(ScenarioEnum.task_native1);
            adRelativeLayout.showAdLayout();
            return;
        }
        adRelativeLayout.setAdrScenario(null);
        adRelativeLayout.setVisibility(View.GONE);
        adRelativeLayout.showAdLayout();

        llRoot.setVisibility(View.VISIBLE);

//        ImageView iv_bg = holder.getView(R.id.iv_bg);
//        ImageLoader.loadImage(getContext(), iv_bg, taskBean.bg_url);
        final int left_times = taskBean.total_times - taskBean.times;//已完成次数
        if (taskBean.total_times > 1) {
            String text = taskBean.name + "(" + left_times + "/" + taskBean.total_times + ")";
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c_FFAD2B)), text.indexOf("(") + 1, text.indexOf("/"), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.setText(R.id.tv_title, style);
        } else {
            holder.setText(R.id.tv_title, taskBean.name);
        }
        holder.setText(R.id.tv_tips, taskBean.introduction);
        ImageView iv_task_gold_icon = holder.getView(R.id.iv_task_gold_icon);
        if (taskBean.activity_id == 36 && taskBean.class_id == 3) {
            holder.setText(R.id.tv_gold, "+" + taskBean.points + "现金");
            Glide.with(getContext()).load(R.mipmap.invite_red_icon).into(iv_task_gold_icon);
        } else {
            holder.setText(R.id.tv_gold, "+" + taskBean.points);
            Glide.with(getContext()).load(R.mipmap.act_gold_icon).into(iv_task_gold_icon);
        }
        ImageView iv_icon = holder.getView(R.id.iv_icon);
        ImageLoader.loadCircleImage(getContext(), iv_icon, taskBean.title_picture);
        TextView tv_btn = holder.getView(R.id.tv_btn);
//        View view_line = holder.getView(R.id.view_line);
//        if (taskList.size() - 1 == holder.getAdapterPosition()) {
//            view_line.setVisibility(View.GONE);
//        }
        holder.setText(R.id.tv_btn, taskBean.getState());
        TextView btn = holder.getView(R.id.tv_btn);
        btn.clearAnimation();
        switch (taskBean.activity_state) {
            case 1://已完成
                btn.setBackgroundResource(R.drawable.finishi_bg_shape);
                break;
            case 2://去绑定
            case 3://去赚钱
                btn.setBackgroundResource(R.mipmap.go_finish_bg);
                break;
            case 4://立即领取
                btn.setBackgroundResource(R.mipmap.lingqu_btn_bg);
                Animation scaleAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.btn_scale);
                LinearInterpolator lins = new LinearInterpolator();
                scaleAnim.setInterpolator(lins);
                btn.startAnimation(scaleAnim);
                break;
            case 5://去看看
                btn.setBackgroundResource(R.mipmap.go_finish_bg);
                break;
            case 6://倒计时
                if (taskBean.activity_id == 5 && taskBean.class_id == 3) {//激励视频
                    if (taskBean.countdown > 0) {
                        btn.setBackgroundResource(R.drawable.finishi_bg_shape);
                        if (mTimer == null) {
                            mTimer = new CountDownTimer((long) taskBean.countdown * 1000, 1000) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    int remainTime = (int) (millisUntilFinished / 1000L);
                                    int h = 0;
                                    int d = 0;
                                    int s = 0;
                                    int temp = remainTime % 3600;
                                    if (remainTime > 3600) {
                                        h = remainTime / 3600;
                                        if (temp != 0) {
                                            if (temp > 60) {
                                                d = temp / 60;
                                                if (temp % 60 != 0) {
                                                    s = temp % 60;
                                                }
                                            } else {
                                                s = temp;
                                            }
                                        }
                                    } else {
                                        d = remainTime / 60;
                                        if (remainTime % 60 != 0) {
                                            s = remainTime % 60;
                                        }
                                    }
                                    holder.setText(R.id.tv_btn, String.format("%02d", d) + ":" + String.format("%02d", s));

                                }

                                @Override
                                public void onFinish() {
                                    mTimer = null;
                                    getTask();
                                }
                            };
                            mTimer.start();
                        }
                    }
                } else if (taskBean.activity_id == 24 && taskBean.class_id == 3) {//分组视频
                    if (taskBean.countdown > 0) {
                        btn.setBackgroundResource(R.drawable.finishi_bg_shape);
                        if (mTimer2 == null) {
                            mTimer2 = new CountDownTimer((long) taskBean.countdown * 1000, 1000) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    int remainTime = (int) (millisUntilFinished / 1000L);
                                    int h = 0;
                                    int d = 0;
                                    int s = 0;
                                    int temp = remainTime % 3600;
                                    if (remainTime > 3600) {
                                        h = remainTime / 3600;
                                        if (temp != 0) {
                                            if (temp > 60) {
                                                d = temp / 60;
                                                if (temp % 60 != 0) {
                                                    s = temp % 60;
                                                }
                                            } else {
                                                s = temp;
                                            }
                                        }
                                    } else {
                                        d = remainTime / 60;
                                        if (remainTime % 60 != 0) {
                                            s = remainTime % 60;
                                        }
                                    }
                                    holder.setText(R.id.tv_btn, String.format("%02d", d) + ":" + String.format("%02d", s));

                                }

                                @Override
                                public void onFinish() {
                                    mTimer2 = null;
                                    getTask();
                                }
                            };
                            mTimer2.start();
                        }
                    }
                }

                break;
            case 7://去看看
                btn.setBackgroundResource(R.mipmap.go_finish_bg);
                break;
        }
        tv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskBean.activity_state == 4) {//领取金币点击按钮
                    ButtonStatistical.receiveBtn();
                    showDialog(taskBean.pop_type, taskBean.activity_id, taskBean.pop_up_message, taskBean.points, taskBean.class_id);
                } else {
                    if (taskBean.activity_id == 3 && taskBean.class_id == 3) {//绑定手机号
                        if (UserEntity.getInstance().getUserInfo() == null) {
                            ToastUtils.showShort("请先微信登录");
                            WeChatLoginActivity.IntoSettings(getActivity());
                            return;
                        }
                        if (taskBean.activity_state == 2) {
                            ButtonStatistical.bindPhoneBtn();
                            BindPhoneActivity.IntoBindPhone(getActivity());
                        }
                    } else if (taskBean.activity_id == 4 && taskBean.class_id == 3) {//绑定微信
                        if (taskBean.activity_state == 2) {
                            ButtonStatistical.bindWechatBtn();
                            WeChatLoginActivity.IntoSettings(getActivity());
                        }
                    } else if (taskBean.activity_id == 5 && taskBean.class_id == 3) {//激励视频
                        if (taskBean.activity_state == 5 && mTimer == null) {//有限次数内方可进入视频
                            ButtonStatistical.creativeVideoBtn();
                            new RewardVideoAd.Builder(getContext())
                                    .setSupportDeepLink(true)
                                    .setScenario(ScenarioEnum.task_video)
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
                                            getGold(taskBean.activity_id);
                                        }

                                        @Override
                                        public void onAdShow(String channel) {

                                        }
                                    }).build();

                        }
                    } else if (taskBean.activity_id == 24 && taskBean.class_id == 3) {//分组看视频
                        if (taskBean.activity_state == 5 && mTimer2 == null) {//有限次数内方可进入视频
                            ButtonStatistical.timeThreeVideo();
                            new RewardVideoAd.Builder(getContext())
                                    .setSupportDeepLink(true)
                                    .setScenario(ScenarioEnum.task_video)
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
                                            updateVidioCount();
                                        }

                                        @Override
                                        public void onAdShow(String channel) {

                                        }
                                    }).build();

                        }
                    } else if (taskBean.activity_id == 25 && taskBean.class_id == 3) {//转盘
                        if (left_times < taskBean.total_times) {//有限次数内方可进入转盘
                            ButtonStatistical.bigWheelBtn();
                            ButtonStatistical.goRotaryTableCount();
                            isload = true;
                            LoadWebViewActivity.IntoLoadWebView(getContext(), taskBean.jump_address + "?token=" + UserEntity.getInstance().getToken() + "&user_id=" + UserEntity.getInstance().getUserId() + "&version=" + AppUtil.packageName(MyApplication.getContext()));
                        }
                    } else if (taskBean.activity_id == 13 && taskBean.class_id == 3) {//趣味游戏
                        isload = true;
                        ButtonStatistical.polymerization();
                        LoadWebViewActivity.IntoLoadWebView(getContext(), taskBean.jump_address + "?token=" + UserEntity.getInstance().getToken() + "&user_id=" + UserEntity.getInstance().getUserId() + "&version=" + AppUtil.packageName(MyApplication.getContext()));
                    } else if (taskBean.activity_id == 15 && taskBean.class_id == 3) {//提现
                        if (taskBean.activity_state == 3) {
                            if (UserEntity.getInstance().getUserInfo() == null) {
                                WeChatLoginActivity.IntoSettings(getActivity());
                                return;
                            }
                            ButtonStatistical.toWithdrawl();
                            Intent intent = WithdrawalActivity.IntoWithdrawal(getActivity());
                            startActivity(intent);
                            isload = true;
                        }
                    } else if (taskBean.activity_id == 1 && taskBean.class_id == 16) {//达标赛
                        StepRaceActivity.IntoStepRace(getActivity());
                        isload = true;
                    } else if (taskBean.activity_id == 17 && taskBean.class_id == 3) {//每日分享
                        ButtonStatistical.everydayGoShare();
                        getActivity().startActivity(new Intent(getActivity(), ShareActivity.class));
                        //  getShareContent();
                    } else if (taskBean.activity_id == 36 && taskBean.class_id == 3) {//邀请好友
                        ButtonStatistical.inviteTofinish();
                        getActivity().startActivity(new Intent(getActivity(), ShareActivity.class));
                        isload = true;
                    } else if (taskBean.activity_id == 16 && taskBean.class_id == 3) {//邀请好友 有金币奖励
                        ButtonStatistical.inviteTofinish();
                        getActivity().startActivity(new Intent(getActivity(), ShareActivity.class));
                        isload = true;
                    } else if (taskBean.activity_id == 18 && taskBean.class_id == 3) {//填写邀请码
                        if (UserEntity.getInstance().getUserInfo() == null) {
                            ToastUtils.showShort("请先微信登录");
                            WeChatLoginActivity.IntoSettings(getActivity());
                            return;
                        }
                        getActivity().startActivity(new Intent(getActivity(), InputInviteCodeActivity.class));
                        isload = true;
                    } else if (taskBean.activity_id == 1 && taskBean.class_id == 19) {//日历提醒
                        if (taskBean.activity_state == 7) {
                            ButtonStatistical.openRemindCount();
                            getPermissions();
                        }
                    } else if (taskBean.activity_id == 7 && taskBean.class_id == 3) {//去喝水
                        ButtonStatistical.toFinishDrinkWater();
                        LoadWebViewActivity.IntoLoadWebView(getContext(), taskBean.jump_address + "?token=" + UserEntity.getInstance().getToken() + "&user_id=" + UserEntity.getInstance().getUserId() + "&version=" + AppUtil.packageName(MyApplication.getContext()));
                    } else if (taskBean.activity_id == 31 && taskBean.class_id == 3) {
                        ButtonStatistical.tosports();
                        EventBus.getDefault().post(new CheckTabEvent("activity"));
                    } else if(taskBean.activity_id == 1 && taskBean.class_id == 42) {
                        LoadWebViewActivity.IntoLoadWebView(getContext(), taskBean.jump_address + "?token=" + UserEntity.getInstance().getToken() + "&user_id=" + UserEntity.getInstance().getUserId() + "&version=" + AppUtil.packageName(MyApplication.getContext()));
                    } else if(taskBean.activity_id  == 1 && taskBean.class_id == 44) {
                        LoadWebViewActivity.IntoLoadWebView(getContext(), taskBean.jump_address + "?token=" + UserEntity.getInstance().getToken() + "&user_id=" + UserEntity.getInstance().getUserId() + "&version=" + AppUtil.packageName(MyApplication.getContext()));
                    }
                }
            }
        });
    }

    //看视频增加次数
    private void updateVidioCount() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .lookVideo(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<UploadNikeName>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<UploadNikeName>() {
                    @Override
                    public void onSuccess(UploadNikeName demo) {
                        getTask();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }


    /**
     * 动态申请权限
     */
    private void getPermissions() {
        XXPermissions.with(getActivity())
                .permission(Permission.READ_CALENDAR,
                        Permission.WRITE_CALENDAR
                )
                .request(new OnPermission() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        //已经取得的权限
                        Log.e("已经取得的权限", granted.toString());
                        if (isAll) {
                            RemindDialog dialog = new RemindDialog.Builder(getActivity())
                                    .setDialogClick(new RemindDialog.RemindDialogClick() {
                                        @Override
                                        public void openBtnClick(Dialog dialog) {
                                            getSiginRemind();
                                            dialog.dismiss();
                                        }
                                    }).build();
                            dialog.show();
                            //删除日历
                            //  CalendarReminderUtils.deleteCalendarEvent(getActivity(),"全民走路");

                        }

                    }

                    @Override
                    public void noPermission(List<String> denied, final boolean quick) {
                        //拒绝的权限
                        Log.e("拒绝的权限", denied.toString());

                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getSiginRemind() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getSigninRemind(UserEntity.getInstance().getUserId(), 1, 1)
                .compose(CommonSchedulers.<BaseResponse<List<SiginRemind>>>observableIO2Main(getContext()))
                .subscribe(new BaseObserver<List<SiginRemind>>() {
                    @Override
                    public void onSuccess(List<SiginRemind> demo) {
                        if (demo.size() > 0) {
                            getTask();
                            for (SiginRemind remind : demo) {
                                CalendarReminderUtils.addCalendarEvent(getActivity(), remind.getTitle(), remind.getNotes(), remind.getStart_time(), remind.getEnd_time(), remind.getAlarm_str());
                            }
                            ToastUtils.showShort(getResources().getString(R.string.open_success));
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    //判断弹框样式
    private void showDialog(int pop_type, final int activity_id, String btn_text, int coins, final int class_id) {
        switch (pop_type) {
            case 1000:
                getZPcoins(activity_id, class_id);
                break;
            case 1001:
                break;
            case 1002:
                ScenarioEnum scenario = null;
                if (activity_id == 20 && class_id == 3) {//特权金币
                    scenario = ScenarioEnum.reward_video;
                } else {//绑定微信
                    scenario = ScenarioEnum.task_video;
                }
                new RewardVideoAd.Builder(getContext())
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
                                if (activity_id == 20 && class_id == 3) {//特权金币
                                    getGold(activity_id);
                                } else {
                                    getZPcoins(activity_id, class_id);
                                }
                            }

                            @Override
                            public void onAdShow(String channel) {

                            }
                        }).build();
                break;
            case 1003:
            case 1004:
                GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getContext())
                        .isVisible(true)
                        .setShowAgain(false)
                        .setBtnText(btn_text)
                        .setGold(coins)
                        .setScenario(ScenarioEnum.task_video_native)
                        .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                            @Override
                            public void doubleBtnClick(Dialog dialog) {
                                dialog.dismiss();
                                new RewardVideoAd.Builder(getContext())
                                        .setSupportDeepLink(true)
                                        .setScenario(ScenarioEnum.task_video)
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
                                                getZPcoins(activity_id, class_id);
                                            }

                                            @Override
                                            public void onAdShow(String channel) {

                                            }
                                        }).build();
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
                break;
        }
    }

    //领取金币接口
    private void getZPcoins(final int activity_id, final int class_id) {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getZPcoins(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, activity_id, class_id)
                .compose(CommonSchedulers.<BaseResponse<ActivityBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<ActivityBean>() {
                    @Override
                    public void onSuccess(final ActivityBean demo) {
                        UserEntity.getInstance().setPoints(demo.getTotal_points());
                        UserEntity.getInstance().setCash(demo.getCash());
                        ScenarioEnum scenario = null;
                        if (activity_id == 3 && class_id == 3) {//绑定手机号
                            scenario = ScenarioEnum.bind_phone_native;
                        } else if (activity_id == 4 && class_id == 3) {//绑定微信
                            scenario = ScenarioEnum.bind_wx_native;
                        } else if (activity_id == 5 && class_id == 3) {//激励视频
                            scenario = ScenarioEnum.task_video_native;
                        } else if (activity_id == 24 && class_id == 3) {//分组视频
                            ButtonStatistical.timeThreeVideoReceive();
                            scenario = ScenarioEnum.task_video_native;
                        } else if (activity_id == 25 && class_id == 3) {//转盘
                            ButtonStatistical.rotaryTableOnceReceive();
                            scenario = ScenarioEnum.turn_table_gold_native;
                        } else if (activity_id == 13 && class_id == 3) {//飞刀
                            ButtonStatistical.flyKnifeOnceReceive();
                            scenario = ScenarioEnum.fly_knife_native;
                        } else if (activity_id == 15 && class_id == 3) {//提现
                            ButtonStatistical.toWithdrawlOnceReceive();
                            scenario = ScenarioEnum.task_reward_native;
                        } else if (activity_id == 1 && class_id == 16) {//达标赛
                            scenario = ScenarioEnum.task_reward_native;
                        } else if (activity_id == 17 && class_id == 3) {//每日分享
                            ButtonStatistical.everydayShareOnceReceive();
                            scenario = ScenarioEnum.task_reward_native;
                        } else if (activity_id == 16 && class_id == 3) {//邀请好友
                            ButtonStatistical.inviteOnceReceive();
                            scenario = ScenarioEnum.task_reward_native;
                        } else if (activity_id == 18 && class_id == 3) {//填写邀请码
                            scenario = ScenarioEnum.task_reward_native;
                        } else if (activity_id == 7 && class_id == 3) {//签到日历
                            scenario = ScenarioEnum.task_reward_native;
                        }
                        GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getContext())
                                .isVisible(false)
                                .setGold(demo.getPoints())
                                .setIsShowAd(true)
                                .setScenario(scenario)
                                .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                    @Override
                                    public void doubleBtnClick(Dialog dialog) {
                                        //金币翻倍跳转广告
                                    }

                                    @Override
                                    public void closeBtnClick(Dialog dialog) {
                                        dialog.dismiss();
                                        tv_gold_num.setText(String.valueOf(demo.getTotal_points()));
                                        tv_rmb_num.setText(getString(R.string.can_carry, String.valueOf(demo.getCash())));
                                        getTask();

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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_reward://特权金币
                ButtonStatistical.rewardClickCount();
                if (rewardEntity != null) {
                    showDialog(rewardEntity.getPop_type(), 20, rewardEntity.getPop_up_message(), rewardEntity.getPoints(), 3);
                }
                break;
            case R.id.layout_carry:
                if (UserEntity.getInstance().getUserInfo() == null) {
                    WeChatLoginActivity.IntoSettings(getActivity());
                    return;
                }
                Intent intent = WithdrawalActivity.IntoWithdrawal(getActivity());
                startActivity(intent);
                break;
            case R.id.layout_sign_btn:
                if (rv_sign2 != null) {
                    rv_sign2.setVisibility(rv_sign2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
                break;
            case R.id.receive_card://领取收益卡
//                CardDialog cardDialog=new CardDialog.Builder(getContext())
//                        .setTitleContent("打的费")
//                        .setDesContent("收益卡")
//                        .setType(1)
//                        .setButtonClick(new CardDialog.onButtonClick() {
//                            @Override
//                            public void onConfirmClick(Dialog dialog) {
//                                dialog.dismiss();
//                            }//                        })
//                        .build();
//                cardDialog.show();
                if (UserEntity.getInstance().getUserInfo() == null) {
                    WeChatLoginActivity.IntoSettings(getActivity());
                    return;
                }
                if (can_receive) {
                    receiveCard();
                } else {
                    ToastUtils.showShort(getResources().getString(R.string.string_receive_card_tip));
                }
                break;
        }
    }

    //领取金币收益卡
    private void receiveCard() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .incomeCard(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<CardBean>>observableIO2Main(getContext()))
                .subscribe(new BaseObserver<CardBean>() {
                    @Override
                    public void onSuccess(CardBean demo) {
                        if (demo != null) {
                            ButtonStatistical.newuserEarncard();
                            CardDialog cardDialog = new CardDialog.Builder(getContext())
                                    .setTitleContent(demo.getTitle())
                                    .setDesContent(demo.getContent())
                                    .setImageUrl(demo.getImage())
                                    .setBtnText("领取金币加速卡")
                                    .setType(102)
                                    .setButtonClick(new CardDialog.onButtonClick() {
                                        @Override
                                        public void onConfirmClick(Dialog dialog) {
                                            dialog.dismiss();
                                            layout_hotact.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            cardDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        ToastUtils.showShort(errorMsg);
                    }
                });
    }

    /**
     * 请求签到接口
     */
    private void sign() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .Signin(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, Const.SIGNIG_ACTIVITY_ID)
                .compose(CommonSchedulers.<BaseResponse<SigninBean>>observableIO2Main(getContext()))
                .subscribe(new BaseObserver<SigninBean>() {
                    @Override
                    public void onSuccess(final SigninBean demo) {
                        if (demo.getIs_today_signin() == 0) {
                            //签到成功弹窗
                            SignNewDialog dialog = new SignNewDialog.Builder(getContext())
                                    .setGold(demo.points)
                                    .setmSignDay(demo.getContinuity_signin_number())
                                    .setDialogClick(new SignNewDialog.DialogClick() {
                                        @Override
                                        public void doubleBtnClick(Dialog dialog) {
                                            //签到翻倍
                                            new RewardVideoAd.Builder(getContext())
                                                    .setSupportDeepLink(true)
                                                    .setScenario(ScenarioEnum.sign_double_video)
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
                                                            getSigninDouble(Const.SIGNIG_ACTIVITY_ID);
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
                                            UserEntity.getInstance().setPoints(demo.getTotal_points());
                                            UserEntity.getInstance().setCash(demo.getCash());
                                            tv_gold_num.setText(String.valueOf(demo.getTotal_points()));
                                            tv_rmb_num.setText(getString(R.string.can_carry, String.valueOf(demo.getCash())));
                                            getSignData();
                                        }
                                    }).build();
                            dialog.show();

                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }

                });
    }

    /**
     * 获取签到列表数据
     */
    private void getSignData() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getSignData(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, Const.SIGNIG_ACTIVITY_ID)
                .compose(CommonSchedulers.<BaseResponse<SignBean>>observableIO2Main(getContext()))
                .subscribe(new BaseObserver<SignBean>() {
                    @Override
                    public void onSuccess(final SignBean demo) {
                        swipe_refresh.setRefreshing(false);
                        if (demo != null) {
                            signBean = demo;
                            signList.clear();
                            signList2.clear();
                            signList = demo.week_all;
                            adapter.replaceList(signList);


//                            for (int i=0;i<demo.week_all.size();i++) {
//                                if(i<7){
//                                    signList.add(demo.week_all.get(i));
//                                }else {
//                                    signList2.add(demo.week_all.get(i));
//                                }
//                            }
//                            adapter.notifyDataSetChanged();
//                            adapter2.notifyDataSetChanged();
//                            if(demo.continuity_signin_number>7){
//                                rv_sign2.setVisibility(View.VISIBLE);
//                            }else {
//                                rv_sign2.setVisibility(View.GONE);
//                            }
                            sign_num.setText(getResources().getString(R.string.string_signinfo, String.valueOf(demo.continuity_signin_number)));
                            tv_sign_day_golds.setText(Html.fromHtml(getResources().getString(R.string.sign_day_tomorrow_golds, String.valueOf(demo.tomorrow_points))));
                            if (demo.is_today_signin == 0) {//未签到则请求签到接口
                                sign();
                            }
                            if (demo.today_welfare_list.size() > 0) {
                                notice_view.removeAllViews();
                                for (SignBean.WelfareList bean : demo.today_welfare_list) {
                                    View view = LayoutInflater.from(getContext()).inflate(R.layout.welfare_notice_view, null);
                                    TextView tv1 = view.findViewById(R.id.tv1);
                                    TextView tv2 = view.findViewById(R.id.tv2);
                                    tv1.setText(bean.name);
                                    tv2.setText(bean.butten_text + ">");
                                    notice_view.addView(view);
                                }
                                notice_view.startFlipping();
                            }
                            notice_view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ButtonStatistical.missionskip();
                                    int postion = notice_view.getDisplayedChild();
                                    int jump_type = demo.today_welfare_list.get(postion).jump_type;
                                    if (jump_type == 1) {//原生
                                        AppUtil.startActivityFromAction(getContext(), demo.today_welfare_list.get(postion).jump_url, demo.today_welfare_list.get(postion).params);
                                    } else if (jump_type == 2) {//H5
                                        String jump_url = demo.today_welfare_list.get(postion).jump_url
                                                + "?token=" + UserEntity.getInstance().getToken()
                                                + "&user_id=" + UserEntity.getInstance().getUserId()
                                                + "&version=" + AppUtil.packageName(MyApplication.getContext());
                                        LoadWebViewActivity.IntoLoadWebView(getContext(), jump_url);
                                    } else if (jump_type == 3) {
                                        EventBus.getDefault().post(new CheckTabEvent(demo.today_welfare_list.get(postion).jump_url));
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        swipe_refresh.setRefreshing(false);
                    }

                });
    }

    /**
     * 获取任务列表
     */
    private void getTask() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getTaskList(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID)
                .compose(CommonSchedulers.<BaseResponse<TaskEntity>>observableIO2Main(getContext()))
                .subscribe(new BaseObserver<TaskEntity>() {
                    @Override
                    public void onSuccess(TaskEntity demo) {
                        swipe_refresh.setRefreshing(false);
                        new_num.setText(Html.fromHtml(getResources().getString(R.string.string_tasknum, String.valueOf(demo.getNew_user_done_count()), String.valueOf(demo.getNew_user_total_count()))));
                        can_receive = demo.isCan_receive();
                        taskList = demo.getNormal_list();
                        newtaskList = demo.getNew_user_list();
                        if (taskList != null && taskList.size() > 2) {
                            TaskBean taskBean = new TaskBean();
                            taskBean.setType(TaskBean.TYPE_AD);
                            taskList.add(2, taskBean);
                        }
                        taskAdapter.replaceList(taskList);
                        newTaskAdapter.replaceList(newtaskList);
                        if (demo.isIs_receive_income_card()) {
                            layout_hotact.setVisibility(View.GONE);
                        } else {
//                            layout_hotact.setVisibility(View.VISIBLE);
                        }
//                        if(demo.getBroadcast_list()!=null&&demo.getBroadcast_list().size()>0){
//                            final List<HotNoticeBean.HotNotice> list = demo.getBroadcast_list();
//                            for (int i=0;i<list.size();i++) {
//                                View view = LayoutInflater.from(getContext()).inflate(R.layout.hot_notice_view,null);
//                                SpannableString spannableString = AppUtil.matcherSearchText(getResources().getColor(R.color.c_FF492B),list.get(i).getTitle(), String.valueOf(list.get(i).getRed_sign()));
//                                TextView tv1 =  view.findViewById(R.id.view1);
//                                tv1.setText(spannableString);
//                                card_tip.addView(view);
//                            }
//                            card_tip.startFlipping();
//                        }


                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        swipe_refresh.setRefreshing(false);
                    }

                });
    }

    /**
     * 获取用户当前金币数
     */
    private void getPoints() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getPoints(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<PointsBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<PointsBean>() {
                    @Override
                    public void onSuccess(PointsBean demo) {
                        if (demo != null) {
                            UserEntity.getInstance().setPoints(demo.getPoints());
                            UserEntity.getInstance().setCash(demo.getCash());
                            tv_gold_num.setText(String.valueOf(demo.getPoints()));
                            tv_rmb_num.setText(getString(R.string.can_carry, String.valueOf(demo.getCash())));
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }

                });
    }


    /**
     * 完成任务领取金币
     *
     * @param activityID
     */
    private void getGold(final int activityID) {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .receiveGold(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, activityID)
                .compose(CommonSchedulers.<BaseResponse<ActivityBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<ActivityBean>() {
                    @Override
                    public void onSuccess(final ActivityBean demo) {
                        GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getContext())
                                .isVisible(false)
                                .setShowAgain(false)
                                .setGold(demo.getPoints())
                                .setIsShowAd(true)
                                .setScenario(ScenarioEnum.task_video_native)
                                .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                    @Override
                                    public void doubleBtnClick(Dialog dialog) {
                                        //金币翻倍跳转广告
                                    }

                                    @Override
                                    public void closeBtnClick(Dialog dialog) {
                                        dialog.dismiss();
                                        UserEntity.getInstance().setPoints(demo.getTotal_points());
                                        UserEntity.getInstance().setCash(demo.getCash());
                                        tv_gold_num.setText(String.valueOf(demo.getTotal_points()));
                                        tv_rmb_num.setText(getString(R.string.can_carry, String.valueOf(demo.getCash())));
                                        getTask();
                                        getRewardCoins();

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
    }


    /**
     * 签到翻倍
     *
     * @param activityID
     */
    private void getSigninDouble(int activityID) {
        ButtonStatistical.signVideoBtn();
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .signinDouble(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, activityID + "")
                .compose(CommonSchedulers.<BaseResponse<SigninDouble>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<SigninDouble>() {
                    @Override
                    public void onSuccess(final SigninDouble demo) {
                        GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getContext())
                                .isVisible(false)
                                .setGold(demo.getPoints())
                                .setIsShowAd(true)
                                .setScenario(ScenarioEnum.sign_double_native)
                                .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                    @Override
                                    public void doubleBtnClick(Dialog dialog) {
                                        //金币翻倍跳转广告
                                    }

                                    @Override
                                    public void closeBtnClick(Dialog dialog) {
                                        dialog.dismiss();
                                        //刷新数据
                                        UserEntity.getInstance().setPoints(demo.getTotal_points());
                                        UserEntity.getInstance().setCash(demo.getCash());
                                        tv_gold_num.setText(String.valueOf(demo.getTotal_points()));
                                        tv_rmb_num.setText(getString(R.string.can_carry, String.valueOf(demo.getCash())));
                                        getSignData();
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
    }


    //登录之后会收到通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(UserInfoEvent info) {
        if (info != null) {//绑定微信成功、绑定手机号成功，刷新任务列表
            isRefresh = true;
            getTask();
            getRewardCoins();
        }

    }

}
