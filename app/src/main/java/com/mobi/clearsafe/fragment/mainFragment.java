package com.mobi.clearsafe.fragment;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.adtest.interaction.InterActionExpressAdView;
import com.example.adtest.interaction.InterActionLoadListener;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.example.adtest.rewardvideo.RewardVideoLoadListener;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.WeChatLoginActivity;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.base.adapter.CommonAdapter;
import com.mobi.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.clearsafe.download.MyDownLoadManager;
import com.mobi.clearsafe.eventbean.CheckTabEvent;
import com.mobi.clearsafe.eventbean.PleasanEvent;
import com.mobi.clearsafe.eventbean.RequestStep;
import com.mobi.clearsafe.eventbean.StepEvent;
import com.mobi.clearsafe.eventbean.TabTipEvent;
import com.mobi.clearsafe.eventbean.UserInfoEvent;
import com.mobi.clearsafe.eventbean.ViewpagerEvent;
import com.mobi.clearsafe.greendao.TodayStepData;
import com.mobi.clearsafe.greendao.TodayStepSessionUtils;
import com.mobi.clearsafe.main.BannerBean;
import com.mobi.clearsafe.main.BubbleClickBean;
import com.mobi.clearsafe.main.GoldBubble;
import com.mobi.clearsafe.main.RewardBean;
import com.mobi.clearsafe.main.StepBubble;
import com.mobi.clearsafe.main.StepExchangeCoins;
import com.mobi.clearsafe.main.UserStepInfo;
import com.mobi.clearsafe.main.bean.CardState;
import com.mobi.clearsafe.main.bean.DownLoadBean;
import com.mobi.clearsafe.main.bean.RedCoinsBean;
import com.mobi.clearsafe.me.activity.MyCardActivity;
import com.mobi.clearsafe.me.activity.StepHistoryActivity;
import com.mobi.clearsafe.me.activity.WithdrawalActivity;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.DateUtils;
import com.mobi.clearsafe.utils.SPUtil;
import com.mobi.clearsafe.utils.StatusBarUtil;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.utils.imageloader.CornerTransform;
import com.mobi.clearsafe.utils.imageloader.Utils;
import com.mobi.clearsafe.widget.AcceleratorCardDialog;
import com.mobi.clearsafe.widget.CustomDashboard;
import com.mobi.clearsafe.widget.GetGoldNewDialog;
import com.mobi.clearsafe.widget.GetRedDialog;
import com.mobi.clearsafe.widget.GoldDialog;
import com.mobi.clearsafe.widget.InterActionDialog;
import com.mobi.clearsafe.widget.LazyLoadFragment;
import com.mobi.clearsafe.widget.LoadWebViewActivity;
import com.mobi.clearsafe.widget.PlaqueDialog;
import com.mobi.clearsafe.widget.StepCeilingNewDialog;
import com.mobi.clearsafe.wxapi.WeixinHandler;
import com.mobi.clearsafe.wxapi.bean.UserEntity;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class mainFragment extends LazyLoadFragment implements View.OnClickListener {

    private TextView tv_today_step;
    private TextView get_money;
    private LinearLayout rl_sync_wx;
    private TextView tv_exchange_money;
    private TextView tv_mileage;
    private TextView tv_hour;
    private TextView tv_second;
    private TextView tv_kll;
    private Banner activit_banner;
    private ArrayList<BannerBean.SlideShow> bannerList;
    private SwipeRefreshLayout refreshLayout;
    private RelativeLayout bubble4;
    private TextView bubble1, bubble2, bubble3, bubble4_text;
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

    private UserStepInfo stepInfo;


    private TextView tv_strategy;
    private LinearLayout layout_data;

    private LinearLayout activity_one, activity_two;
    private ImageView image_one, image_two;
    private TextView title1, title2, des1, des2;

    private LinearLayout  layout_card;
    private LinearLayout layout_sign;
    private ImageView coin_image;
    private TextView tv_card;
    private CardState cardState;


    private CustomDashboard circle_progress;//圆形进度条
    private TextView step_string;


    private int current_Step;//暂存步数
    public HashMap<String, Boolean> bubbleMap;// key存四个位置，value存显隐状态 true显示 false隐藏

    private boolean isSyncWechat = false;//是否同步微信步数
    private boolean isShowedPlaque = false;//是否显示过插屏弹窗

    private RecyclerView rv_activity;
    private CommonAdapter<BannerBean.HotActivityList> adapter;
    private List<BannerBean.HotActivityList> hotActivityLists = new ArrayList<>();
    private BannerBean hotdata;
    private ImageView iv_float,iv_gold;
    private TextView sign_state,tv;

    @Override
    protected int setContentView() {
        return R.layout.fragment_main;
    }

    @Override
    protected void lazyLoad() {
        StatusBarUtil.setStatusBarMode(getActivity(), true, R.color.white);
        ButtonStatistical.homePageCount();
        if (!TextUtils.isEmpty(Const.getBaseUrl()) && !TextUtils.isEmpty(UserEntity.getInstance().getToken())) {
            getUserStepInfo();
           // getBubble();
            getCardState();
            if (hotdata == null) {
                getBanner();
            }
        }
        if (Const.pBean != null && Const.pBean.show_page == 1) {
            EventBus.getDefault().post(new PleasanEvent(Const.pBean.show_page));
        }
    }

    //获取收益卡状态
    private void getCardState() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getCardState(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<CardState>>observableIO2Main(getContext()))
                .subscribe(new BaseObserver<CardState>() {
                    @Override
                    public void onSuccess(CardState demo) {
                        if (demo != null) {
                            cardState = demo;
                            if (demo.getState() == 1) {
                                layout_card.setVisibility(View.VISIBLE);
                                Glide.with(getActivity()).load(R.mipmap.main_coin_icon).into(coin_image);
                                tv_card.setText(getResources().getString(R.string.string_card_state1));
                            } else if (demo.getState() == 2) {
                                layout_card.setVisibility(View.VISIBLE);
                                Glide.with(getActivity()).load(R.mipmap.main_coin_icon).into(coin_image);
                                tv_card.setText(getResources().getString(R.string.string_card_state2));
                            } else {
                                layout_card.setVisibility(View.GONE);
                            }
                            if(demo.getSignin_last_points()>0){
                                sign_state.setText(String.valueOf(demo.getSignin_last_points()));
                                iv_gold.setVisibility(View.VISIBLE);
                                tv.setVisibility(View.VISIBLE);
                            }else {
                                sign_state.setText(getResources().getString(R.string.qiandao));
                                iv_gold.setVisibility(View.GONE);
                                tv.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("mainFragment");
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void firstIn() {
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("mainFragment");
        //同步微信步数调用
        if (isSyncWechat) {
            getUserStepInfo();
            isSyncWechat = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        EventBus.getDefault().register(this);
        step_string=findViewById(R.id.step_string);
        iv_gold=findViewById(R.id.iv_gold);
        tv=findViewById(R.id.tv);
        tv_today_step = findViewById(R.id.tv_today_step);
        refreshLayout = findViewById(R.id.swipe_refresh);
        get_money = findViewById(R.id.tv_get_money);
        tv_strategy = findViewById(R.id.tv_strategy);
        tv_strategy.setOnClickListener(this);
        get_money.setOnClickListener(this);
        rl_sync_wx = findViewById(R.id.sync_wechet_step);
        rl_sync_wx.setOnClickListener(this);
        tv_exchange_money = findViewById(R.id.tv_exchange_money);
        Animation scaleAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.btn_scale);
        LinearInterpolator lins = new LinearInterpolator();
        scaleAnim.setInterpolator(lins);
        tv_exchange_money.startAnimation(scaleAnim);
        tv_mileage = findViewById(R.id.tv_mileage);
        tv_hour = findViewById(R.id.tv_hour);
        tv_second = findViewById(R.id.tv_second);
        tv_kll = findViewById(R.id.tv_kll);
        circle_progress = findViewById(R.id.circle_progress);
        activity_one = findViewById(R.id.activity_one);
        activity_two = findViewById(R.id.activity_two);
        image_one = findViewById(R.id.image_one);
        image_two = findViewById(R.id.image_two);
        title1 = findViewById(R.id.tv_title);
        title2 = findViewById(R.id.tv_title2);
        des1 = findViewById(R.id.tv_des);
        des2 = findViewById(R.id.tv_des2);
        activity_one.setOnClickListener(this);
        activity_two.setOnClickListener(this);
        layout_sign = findViewById(R.id.layout_sign);
        layout_card = findViewById(R.id.layout_card);
        tv_card = findViewById(R.id.tv_card);
        coin_image = findViewById(R.id.coin_image);
        layout_sign.setOnClickListener(this);
        layout_card.setOnClickListener(this);
        sign_state=findViewById(R.id.sign_state);
        bubble1 = findViewById(R.id.bubble1);
        bubble2 = findViewById(R.id.bubble2);
        bubble3 = findViewById(R.id.bubble3);
        bubble4 = findViewById(R.id.bubble4);
        bubble1.setOnClickListener(this);
        bubble2.setOnClickListener(this);
        bubble3.setOnClickListener(this);
        bubble4.setOnClickListener(this);
        doRepeatAnim(bubble1, 1400);
        doRepeatAnim(bubble2, 1500);
        doRepeatAnim(bubble3, 1600);
        doRepeatAnim(bubble4, 1700);
        bubbleMap = (HashMap<String, Boolean>) SPUtil.getHashMapData(MyApplication.getContext(), "coins");
        if (bubbleMap.size() == 0) {//首次安装软件默认都为false 隐藏状态
            bubbleMap.put("1", false);
            bubbleMap.put("2", false);
            bubbleMap.put("3", false);
            SPUtil.putHashMapData(MyApplication.getContext(), "coins", bubbleMap);
        }
        bubble4_text = findViewById(R.id.bubble4_text);
        layout_data = findViewById(R.id.layout_data);
        layout_data.setOnClickListener(this);
        activit_banner = findViewById(R.id.activity_banner);
        activit_banner.setImageLoader(new MyLoader());
        activit_banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                ButtonStatistical.clickBannerBtn();
                int type = bannerList.get(position).judge_type;
                if (type == 1) {//原生
                    AppUtil.startActivityFromAction(getContext(), bannerList.get(position).jump_url, bannerList.get(position).params);
                } else if (type == 2) {//H5
                    if (bannerList.get(position).jump_url.contains("knife")) {
                        ButtonStatistical.bannerIntoFlyKnifeCount();
                    } else {
                        ButtonStatistical.bannerIntoRotaryTableCount();
                    }
                    String jump_url = bannerList.get(position).jump_url
                            + "?token=" + UserEntity.getInstance().getToken()
                            + "&user_id=" + UserEntity.getInstance().getUserId()
                            +"&version="+AppUtil.packageName(MyApplication.getContext());
                    LoadWebViewActivity.IntoLoadWebView(getContext(), jump_url);
                } else if (type == 3) {
                    ButtonStatistical.bannerIntoTaskPageCount();
                    EventBus.getDefault().post(new CheckTabEvent(bannerList.get(position).jump_url));
                }
            }
        });
        activit_banner.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), Utils.dp2px(getActivity(), 8));
            }
        });
        activit_banner.setClipToOutline(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBanner();
                getUserStepInfo();
             //   getBubble();
                getCardState();
            }
        });
        rv_activity = findViewById(R.id.rv_activity);
        adapter = new CommonAdapter<BannerBean.HotActivityList>(getContext(), R.layout.item_hotact_layout, hotActivityLists) {
            @Override
            public void convert(RecycleViewHolder holder, final BannerBean.HotActivityList item) {
                final ImageView iv_bg = holder.getView(R.id.iv_bg);
                com.mobi.clearsafe.utils.imageloader.ImageLoader.loadImage(getContext(), iv_bg, item.url);
                iv_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int jump_type = item.judge_type;
                        if (jump_type == 1) {//原生
                            AppUtil.startActivityFromAction(getContext(), item.jump_url, item.params);
                        } else if (jump_type == 2) {//H5
                            String jump_url = item.jump_url
                                    + "?token=" + UserEntity.getInstance().getToken()
                                    + "&user_id=" + UserEntity.getInstance().getUserId()
                                    +"&version="+AppUtil.packageName(MyApplication.getContext());
                            LoadWebViewActivity.IntoLoadWebView(getContext(), jump_url);
                        } else if (jump_type == 3) {
                            EventBus.getDefault().post(new CheckTabEvent(item.jump_url));
                        }
                    }
                });
            }
        };
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_activity.setLayoutManager(layoutManager);
        rv_activity.setAdapter(adapter);
        iv_float = findViewById(R.id.iv_float);
    }

    private void doRepeatAnim(View view, long time) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -10, 10, -10);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.setDuration(time);
        animator.start();
    }

    private void getBanner() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getBanner(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<BannerBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<BannerBean>() {
                    @Override
                    public void onSuccess(final BannerBean demo) {
                        refreshLayout.setRefreshing(false);//关闭刷新
                        if (demo != null) {
                            hotdata = demo;
                            if (demo.slide_show.size() > 0) {
                                bannerList = (ArrayList<BannerBean.SlideShow>) demo.slide_show;
                                ArrayList<String> urlList = new ArrayList<>();
                                for (BannerBean.SlideShow bannerBean : bannerList) {
                                    urlList.add(bannerBean.url);
                                }
                                activit_banner.setImages(urlList);
                                activit_banner.start();
                            }
                            if (!isShowedPlaque && demo.insert_screen.size() > 0) {
                                if (demo.insert_screen.get(0).is_ad) {
//                                    InterActionExpressAdView build = new InterActionExpressAdView.Builder(getActivity())
//                                            .setScenarioEnum(ScenarioEnum.low_plaque)
//                                            .setWasDelayed(false)
//                                            .setListener(new InterActionLoadListener() {
//                                                @Override
//                                                public void onAdClick(String channel) {
//
//                                                }
//
//                                                @Override
//                                                public void onLoadFaild(String channel, int faildCode, String faildMsg) {
//
//                                                }
//
//                                                @Override
//                                                public void onAdDismissed(String channel) {
//
//                                                }
//
//                                                @Override
//                                                public void onAdRenderSuccess(String channel) {
//
//                                                }
//
//                                                @Override
//                                                public void onAdShow(String channel) {
//
//                                                }
//                                            }).build();
                                } else {
                                    PlaqueDialog dialog = new PlaqueDialog.Builder(getContext())
                                            .setList(demo.insert_screen)
                                            .build();
                                    dialog.show();
                                }
                                isShowedPlaque = true;
                            }
                            if (demo.hot_activity_list != null && demo.hot_activity_list.size() > 0) {
                                if (demo.hot_activity_list.get(0) != null) {
                                    com.mobi.clearsafe.utils.imageloader.ImageLoader.loadImage(getContext(), image_two, demo.hot_activity_list.get(0).url);
                                    title2.setText(demo.hot_activity_list.get(0).name);
                                    des2.setText(demo.hot_activity_list.get(0).content);
                                }
                                if (demo.hot_activity_list.get(1) != null) {
                                    com.mobi.clearsafe.utils.imageloader.ImageLoader.loadImage(getContext(), image_one, demo.hot_activity_list.get(1).url);
                                    title1.setText(demo.hot_activity_list.get(1).name);
                                    des1.setText(demo.hot_activity_list.get(1).content);
                                }
                                hotActivityLists = demo.hot_activity_list;
                                //  adapter.replaceList(hotActivityLists);
                            }
                            if (demo.red_envelope != null) {
                                if (!TextUtils.isEmpty(demo.red_envelope.url) && !TextUtils.isEmpty(demo.red_envelope.jump_url)) {
                                    iv_float.setVisibility(View.VISIBLE);
                                    com.mobi.clearsafe.utils.imageloader.ImageLoader.loadImage(getContext(), iv_float, demo.red_envelope.url);
                                    iv_float.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int jump_type = demo.red_envelope.judge_type;
                                            if (jump_type == 1) {//原生
                                                AppUtil.startActivityFromAction(getContext(),
                                                        demo.red_envelope.jump_url,
                                                        demo.red_envelope.params);
                                            } else if (jump_type == 2) {//H5
                                                String jump_url = demo.red_envelope.jump_url
                                                        + "?token=" + UserEntity.getInstance().getToken()
                                                        + "&user_id=" + UserEntity.getInstance().getUserId()
                                                        +"&version="+AppUtil.packageName(MyApplication.getContext());
                                                LoadWebViewActivity.IntoLoadWebView(getContext(), jump_url);
                                            } else if (jump_type == 3) {
                                                EventBus.getDefault().post(new CheckTabEvent(demo.red_envelope.jump_url));
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        refreshLayout.setRefreshing(false);//关闭刷新
                    }

                });
    }

    private class MyLoader extends ImageLoader {

        CornerTransform transformation = new CornerTransform(getContext(), Utils.dp2px(getContext(), 8));

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path)
                    .transform(transformation)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imageView);
        }
    }

    /**
     * 获取用户当前步数
     */
    private void getUserStepInfo() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getUserStep(UserEntity.getInstance().getUserId(), current_Step)
                .compose(CommonSchedulers.<BaseResponse<UserStepInfo>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<UserStepInfo>() {
                    @Override
                    public void onSuccess(UserStepInfo demo) {
                        refreshLayout.setRefreshing(false);//关闭刷新
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
                        refreshLayout.setRefreshing(false);//关闭刷新
                        bubble1.setVisibility(bubbleMap.get("1") ? View.VISIBLE : View.GONE);
                        bubble2.setVisibility(bubbleMap.get("2") ? View.VISIBLE : View.GONE);
                        bubble3.setVisibility(bubbleMap.get("3") ? View.VISIBLE : View.GONE);
                    }

                });
    }

    private void getBubble() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getBubble(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<List<GoldBubble>>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<List<GoldBubble>>() {
                    @Override
                    public void onSuccess(List<GoldBubble> demo) {
                        refreshLayout.setRefreshing(false);//关闭刷新
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

                        bubble1.setText("");
                        bubble2.setText("");
                        bubble3.setText("");
                        //赋值 控件显示且内容为空
                        for (int i = 0; i < demo.size(); i++) {
                            if (bubbleMap.get("1") && TextUtils.isEmpty(bubble1.getText())) {
                                bubble1.setText(demo.get(i).getType() == 1 ? String.valueOf(demo.get(i).getPoints()) : "?");
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
                            } else if (bubbleMap.get("2") && TextUtils.isEmpty(bubble2.getText())) {
                                bubble2.setText(demo.get(i).getType() == 1 ? String.valueOf(demo.get(i).getPoints()) : "?");
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
                            } else if (bubbleMap.get("3") && TextUtils.isEmpty(bubble3.getText())) {
                                bubble3.setText(demo.get(i).getType() == 1 ? String.valueOf(demo.get(i).getPoints()) : "?");
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
                        refreshLayout.setRefreshing(false);//关闭刷新
                        //根据map的value 控制控件显隐
                        bubble1.setVisibility(bubbleMap.get("1") ? View.VISIBLE : View.GONE);
                        bubble2.setVisibility(bubbleMap.get("2") ? View.VISIBLE : View.GONE);
                        bubble3.setVisibility(bubbleMap.get("3") ? View.VISIBLE : View.GONE);
                    }
                });
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


    /**
     * 刷新UI
     */
    private void upDateView(UserStepInfo demo) {
        tv_today_step.setText(demo.getStep() + "");
        tv_mileage.setText(demo.getKilometer() + "");
        int hour = demo.getMinute() / 60;
        int minutes = demo.getMinute() % 60;
        tv_hour.setText(hour + "");
        tv_second.setText(minutes + "");
        tv_kll.setText(demo.getCalories() + "");
        circle_progress.setTemp(demo.getStep());
        if (demo.getButton_state() == 1) {
            get_money.setText(getResources().getString(R.string.continue_hard));
            get_money.setBackgroundResource(R.mipmap.btn_blue_defbg);
            get_money.clearAnimation();
        } else if (demo.getButton_state() == 4) {
            get_money.setText(getResources().getString(R.string.string_receive_step));
            get_money.setBackgroundResource(R.mipmap.btn_blue_bg);
            Animation scaleAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.btn_scale);
            LinearInterpolator lins = new LinearInterpolator();
            scaleAnim.setInterpolator(lins);
            get_money.startAnimation(scaleAnim);
        } else {
            get_money.setText(getResources().getString(R.string.today_finish));
            get_money.setBackgroundResource(R.mipmap.btn_blue_defbg);
            get_money.clearAnimation();
        }
        if (demo.isIs_step_bubble()) {
            bubble4.setVisibility(View.VISIBLE);
            bubble4_text.setText(demo.getBubble().getExchange_coins() + "");
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
        bubble1.setText("");
        bubble2.setText("");
        bubble3.setText("");
        //赋值 控件显示且内容为空
        for (int i = 0; i < coins_bubble_list.size(); i++) {
            if (bubbleMap.get("1") && TextUtils.isEmpty(bubble1.getText())) {
                bubble1.setText(coins_bubble_list.get(i).getType() == 1 ? String.valueOf(coins_bubble_list.get(i).getPoints()) : "?");
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
            } else if (bubbleMap.get("2") && TextUtils.isEmpty(bubble2.getText())) {
                bubble2.setText(coins_bubble_list.get(i).getType() == 1 ? String.valueOf(coins_bubble_list.get(i).getPoints()) : "?");
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
            } else if (bubbleMap.get("3") && TextUtils.isEmpty(bubble3.getText())) {
                bubble3.setText(coins_bubble_list.get(i).getType() == 1 ? String.valueOf(coins_bubble_list.get(i).getPoints()) : "?");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_money:
                if (stepInfo != null) {
                    if (stepInfo.getButton_state() != 4) {
                        return;
                    }
                    OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                            .reward(UserEntity.getInstance().getUserId())
                            .compose(CommonSchedulers.<BaseResponse<RewardBean>>observableIO2Main(getActivity()))
                            .subscribe(new BaseObserver<RewardBean>() {
                                @Override
                                public void onSuccess(RewardBean demo) {
                                    UserEntity.getInstance().setPoints(demo.getTotal_points());
                                    UserEntity.getInstance().setCash(demo.getCash());
                                    GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getContext())
                                            .isVisible(false)
                                            .setShowAgain(false)
                                            .setGold(demo.getPoints())
                                            .setScenario(ScenarioEnum.stage_of_reward_native)
                                            .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                                @Override
                                                public void doubleBtnClick(Dialog dialog) {

                                                }

                                                @Override
                                                public void closeBtnClick(Dialog dialog) {
                                                    dialog.dismiss();
                                                    getUserStepInfo();
                                                    if(AppUtil.HWIsShowAd()){
                                                        new InterActionExpressAdView.Builder(getActivity())
                                                                .setScenarioEnum(ScenarioEnum.low_plaque)
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

                                                @Override
                                                public void bottomBtnClick(Dialog dialog) {

                                                }
                                            }).build();
                                    dialog.show();
                                }

                                @Override
                                public void onFailure(Throwable e, String errorMsg, String code) {

                                }
                            });
                }
                break;
            case R.id.sync_wechet_step://跳转到微信小程序
                if (UserEntity.getInstance().getUserInfo() == null) {
                    WeChatLoginActivity.IntoSettings(getActivity());
                    return;
                }
                isSyncWechat = true;
                String appId = WeixinHandler.APPID;//应用的APPID
                IWXAPI api = WXAPIFactory.createWXAPI(getContext(), appId);
                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                req.userName = "gh_a3f579451e76";//小程序原始ID
                req.path = "pages/index/index?userid=" +
                        UserEntity.getInstance().getUserId()
                        + "&token=" + UserEntity.getInstance().getToken()
                        + "&phoneType=1";
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;
                api.sendReq(req);
                break;
            case R.id.bubble1:
                ButtonStatistical.coinsBubbleBtn();
                bubble1.setVisibility(View.GONE);
                bubbleMap.put("1", false);
                SPUtil.putHashMapData(MyApplication.getContext(), "coins", bubbleMap);//控件点击隐藏，更新map数据
                bubble1.setText("");//置空控件，方便赋值
                bubbleClick(bubble1_id, bubble1_type, bubble1_type_message, bubble1_coins, bubble1_closeType, bubble1_informationAd, bubble1_h1fg, bubble1is_jump, bubble1jump_type, bubble1jump_url, bubble1download);
                break;
            case R.id.bubble2:
                ButtonStatistical.coinsBubbleBtn();
                bubble2.setVisibility(View.GONE);
                bubbleMap.put("2", false);
                SPUtil.putHashMapData(MyApplication.getContext(), "coins", bubbleMap);
                bubble2.setText("");
                bubbleClick(bubble2_id, bubble2_type, bubble2_type_message, bubble2_coins, bubble2_closeType, bubble2_informationAd, bubble2_h1fg, bubble2is_jump, bubble2jump_type, bubble2jump_url, bubble2download);
                break;
            case R.id.bubble3:
                ButtonStatistical.coinsBubbleBtn();
                bubble3.setVisibility(View.GONE);
                bubbleMap.put("3", false);
                SPUtil.putHashMapData(MyApplication.getContext(), "coins", bubbleMap);
                bubble3.setText("");
                bubbleClick(bubble3_id, bubble3_type, bubble3_type_message, bubble3_coins, bubble3_closeType, bubble3_informationAd, bubble3_h1fg, bubble3is_jump, bubble3jump_type, bubble3jump_url, bubble3download);
                break;
            case R.id.bubble4:
                ButtonStatistical.exchangeStepBubbleCount();
                exchangeStep();
                break;
            case R.id.tv_strategy:
                ButtonStatistical.earnCoinsBtn();
                LoadWebViewActivity.IntoStrategy(getActivity(), UserEntity.getInstance().getConfigEntity().getStrategy());
                break;
            case R.id.layout_data:
                ButtonStatistical.todayDataBtn();
                StepHistoryActivity.IntoStepHistory(getActivity());
                break;
            case R.id.activity_one:
                jump(hotActivityLists.get(1).judge_type, hotActivityLists.get(1).jump_url, hotActivityLists.get(1).params, null);
                break;
            case R.id.activity_two:
                jump(hotActivityLists.get(0).judge_type, hotActivityLists.get(0).jump_url, hotActivityLists.get(0).params, null);
                break;
            case R.id.layout_sign:
                EventBus.getDefault().post(new ViewpagerEvent());
                break;
            case R.id.layout_card:
                if (cardState != null) {
                    if (cardState.getState() == 1) {
                        AcceleratorCardDialog dialog=new AcceleratorCardDialog.Builder(getContext())
                                .setNumber(cardState.getPoints())
                                .setTime(cardState.getDeadline_msg())
                                .setButtonClick(new AcceleratorCardDialog.onButtonClick() {
                                    @Override
                                    public void onConfirmClick(Dialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .build();
                        dialog.show();
                       // ToastUtils.showShort("累计收益金币：" + cardState.getPoints());
                    } else if (cardState.getState() == 2) {
//                        ToastUtils.showShort("跳转我的卡券页面");
                        MyCardActivity.intoMyCard(getContext());
                    }
                }
                break;
        }
    }

    /**
     * 步数换金币
     */
    private void exchangeStep() {
        if (stepInfo != null) {
            StepBubble bubble = stepInfo.getBubble();
            if(bubble.getExchange_coins()<50){//可兑换金币小于50金币
                showDialog(3);
            } else if (bubble.getExchange_coins() > 50 && !bubble.isIs_exchange_limit()) {//可以直接兑换金币
                exchangeStepService();
            } else if (bubble.getExchange_coins() > 50 && bubble.isIs_exchange_limit()) {//步数兑换超过2000步，需看看视频兑换
                if(bubble.isIs_time_limit()){
                    showDialog(5);//需要等待继续兑换
                }else {
                    showDialog(1);
                }
            } else if (bubble.isIs_today_limit()) {//步数兑换到达上限
                showDialog(2);
            }
        }


    }


    private void exchangeStepService() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .stepExchangeCoins(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<StepExchangeCoins>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<StepExchangeCoins>() {
                    @Override
                    public void onSuccess(StepExchangeCoins demo) {
                        if (demo != null) {
                            UserEntity.getInstance().setCash(demo.getCash());
                            UserEntity.getInstance().setPoints(demo.getTotal_points());
                            //步数换金币弹窗
                            GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getContext())
                                    .isVisible(false)
                                    .setGold(demo.getPoints())
                                    .setScenario(ScenarioEnum.step_togold_native)
                                    .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                        @Override
                                        public void doubleBtnClick(Dialog dialog) {

                                        }

                                        @Override
                                        public void closeBtnClick(Dialog dialog) {
                                            dialog.dismiss();
                                            getUserStepInfo();
                                            new InterActionExpressAdView.Builder(getActivity())
                                                    .setScenarioEnum(ScenarioEnum.low_plaque)
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

                                        @Override
                                        public void bottomBtnClick(Dialog dialog) {

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
                               // getBubble();
                                getUserStepInfo();
                                GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getContext())
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
                                                new RewardVideoAd.Builder(getContext())
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
                new RewardVideoAd.Builder(getContext())
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
                GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getContext())
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
                                new RewardVideoAd.Builder(getContext())
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
                               // getBubble();
                                getUserStepInfo();
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
                               // getBubble();
                                getUserStepInfo();
                                //金币泡泡弹出获得金币数量弹窗
                                GetGoldNewDialog dialogs = new GetGoldNewDialog.Builder(getContext())
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
                                                new RewardVideoAd.Builder(getContext())
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
            AppUtil.startActivityFromAction(getContext(), jump_url, parm);
        } else if (jump_type == 2) {//H5
            if (jump_url.contains("turn3") && !jump_url.contains("scratchList")) {
                ButtonStatistical.bubbleToTurn();
            } else if (jump_url.contains("scratchList")) {
                ButtonStatistical.bubbleToScratch();
            }
            String url = jump_url
                    + "?token=" + UserEntity.getInstance().getToken()
                    + "&user_id=" + UserEntity.getInstance().getUserId()
                    +"&version="+AppUtil.packageName(MyApplication.getContext());
            LoadWebViewActivity.IntoLoadWebView(getContext(), url);
        } else if (jump_type == 3) {
            EventBus.getDefault().post(new CheckTabEvent(jump_url));
        } else if (jump_type == 4) {//下载
            if (AppUtil.getNetworkTypeName().equals("WIFI")) {
                if (downLoadBean != null) {
                    if (AppUtil.checkAppInstalled(getContext(), downLoadBean.pkgName)) {
                        //APP已安装
                    } else {
                        //APP未安装
                        if (AppUtil.checkDownLoad(downLoadBean.pkgName, getContext())) {
                            //已下载
                            AppUtil.installAPP(getContext(), AppUtil.getFilePath(downLoadBean.pkgName, getContext()));
                        } else {
                            //未下载
                            MyDownLoadManager.downLoadApk(getContext(), downLoadBean.downloadUrl, downLoadBean.pkgName, downLoadBean.appName);
                        }
                    }
                }
            }
        }

    }

    //弹框关闭时展示广告
    private void showAd(int closeAdType) {
        if (closeAdType == 1) {//插屏
            new InterActionExpressAdView.Builder(getActivity())
                    .setScenarioEnum(ScenarioEnum.default_plaque)
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

        }
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
                       // getBubble();
                        getUserStepInfo();
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


    //插屏弹框
    private void showPlaqueDialog(final BubbleClickBean demo) {
        GoldDialog dialog = new GoldDialog.Builder(getContext())
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
                new InterActionExpressAdView.Builder(getContext())
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
                new InterActionExpressAdView.Builder(getContext())
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
                InterActionDialog actionDialog = new InterActionDialog.Builder(getContext())
                        .setH1fg(false)
                        .setEnum(ScenarioEnum.gold_bubble_native)
                        .build();
                actionDialog.show();
                break;
            case 3004:
                InterActionDialog actionDialog2 = new InterActionDialog.Builder(getContext())
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

    //信息流弹框
    private void showGoldDialog(BubbleClickBean demo, int pop_type, boolean informationAd) {
        //金币泡泡弹出获得金币数量弹窗
        GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getContext())
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

    /**
     * 获取用户当前金币数
     * type 1 表示兑换上限 2表示积分不足
     */
    private void showDialog(final int type) {
        String title = null;
        String content=null;
        switch (type){
            case 1:
                title="步数兑换超过限额";
                content="当天累计超过2000步，需要看视频才能兑换";
                break;
            case 2:
                title="今日步数兑换达到上限";
                content="参与活动可更快获得金币奖励";
                break;
            case 3:
                title="当前可兑换步数不足";
                content="每10步数兑换1金币，满50金币可进行兑换";
                break;
            case 5:
                title="您兑换的太频繁啦";
                content=getResources().getString(R.string.string_exchange_step,String.valueOf(stepInfo.getBubble().getLast_time()/60));
                break;
        }
        StepCeilingNewDialog dialog = new StepCeilingNewDialog.Builder(getActivity())
                .setTitle(title)
                .setContent(content)
                .setType(type)
                .setDialogClick(new StepCeilingNewDialog.StepDialogNewClick() {
                    @Override
                    public void moreBtnClick(Dialog dialog) {
                        ButtonStatistical.exchangeStepMoreTaskCount();
                        dialog.dismiss();
                        //    EventBus.getDefault().post(new ViewpagerEvent());
                        EventBus.getDefault().post(new CheckTabEvent("activity"));
                    }

                    @Override
                    public void watchVideo(Dialog dialog) {
                        ButtonStatistical.exchangeStepContinueCount();
                        if(AppUtil.HWIsShowAd()){
                            new RewardVideoAd.Builder(getContext())
                                    .setSupportDeepLink(true)
                                    .setScenario(ScenarioEnum.step_shangxian_video)
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
                                            exchangeStepService();
                                        }

                                        @Override
                                        public void onAdShow(String channel) {

                                        }
                                    }).build();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void closeBtnClick(Dialog dialog) {
                        dialog.dismiss();
                    }
                }).build();
        dialog.show();
    }


    //步数通知回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(StepEvent info) {
        if (info != null) {
            current_Step = info.getStep();
        }
    }

    //游客登录获取到token后请求数据（有可能游客登录时token未获取到）
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(RequestStep requestStep) {
        getUserStepInfo();
      //  getBubble();
        getBanner();
        getCardState();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //登录、退出登录之后改变文字
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(UserInfoEvent info) {
        //登录状态切换，置空
        bubbleMap.put("1", false);
        bubbleMap.put("2", false);
        bubbleMap.put("3", false);
        SPUtil.putHashMapData(MyApplication.getContext(), "coins", bubbleMap);
        bubble1.setText("");
        bubble2.setText("");
        bubble3.setText("");
        getBanner();
     //   getBubble();
        getUserStepInfo();
        getCardState();
        if (UserEntity.getInstance().isRed()) {
            getRed();
            UserEntity.getInstance().setRed(false);
        }
    }

    //领取新人红包
    private void getRed() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getRedCoins(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<RedCoinsBean>>observableIO2Main(getContext()))
                .subscribe(new BaseObserver<RedCoinsBean>() {
                    @Override
                    public void onSuccess(final RedCoinsBean demo) {
                        if (demo != null) {
                            ButtonStatistical.receiveRedpacket();
                            UserEntity.getInstance().setPoints(demo.getTotal_points());
                            UserEntity.getInstance().setCash(demo.getTotal_cash());
                            GetRedDialog dialog=new GetRedDialog.Builder(getContext())
                                    .setCash(demo.getReward_cash())
                                    .setType(4)
                                    .setCan_withdraw(demo.isCan_withdraw())
                                    .setTipString(demo.getButton_content())
                                    .setDialogListener(new GetRedDialog.GetRedDialogListener() {
                                        @Override
                                        public void closeClick(Dialog dialog) {
                                            dialog.dismiss();
                                            if(demo.isCan_withdraw()){
                                                getActivity().startActivity(new Intent(getActivity(), WithdrawalActivity.class));
                                            }
                                        }
                                    })
                                    .build();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

}
