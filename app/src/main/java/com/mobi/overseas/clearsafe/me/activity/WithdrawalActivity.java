package com.mobi.overseas.clearsafe.me.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adtest.interaction.InterActionExpressAdView;
import com.example.adtest.interaction.InterActionLoadListener;
import com.example.adtest.manager.ScenarioEnum;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.WeChatLoginActivity;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.base.adapter.CommonAdapter;
import com.mobi.overseas.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.overseas.clearsafe.eventbean.UserInfoEvent;
import com.mobi.overseas.clearsafe.main.activity.FriendManagerActivity;
import com.mobi.overseas.clearsafe.main.activity.InviteActivity;
import com.mobi.overseas.clearsafe.me.bean.PointsBean;
import com.mobi.overseas.clearsafe.me.bean.WithDrawalData;
import com.mobi.overseas.clearsafe.me.bean.WithDrawalItemBean;
import com.mobi.overseas.clearsafe.me.bean.WithdrawBean;
import com.mobi.overseas.clearsafe.me.bean.WithdrawCheckBean;
import com.mobi.overseas.clearsafe.moneyactivity.BindPhoneActivity;
import com.mobi.overseas.clearsafe.moneyactivity.ShareActivity;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.DataUtils;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.utils.UiUtils;
import com.mobi.overseas.clearsafe.utils.imageloader.ImageLoader;
import com.mobi.overseas.clearsafe.widget.BindPhoneDialog;
import com.mobi.overseas.clearsafe.widget.BlockDialog;
import com.mobi.overseas.clearsafe.widget.GetRedDialog;
import com.mobi.overseas.clearsafe.widget.NoPaddingTextView;
import com.mobi.overseas.clearsafe.widget.TiXianSurpriseDialog;
import com.mobi.overseas.clearsafe.widget.WithdrawalDialog;
import com.mobi.overseas.clearsafe.widget.WithdrawalFailureDialog;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;
import com.mobi.overseas.clearsafe.wxapi.bean.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现页
 * author : liangning
 * date : 2019-10-24  14:04
 */
public class WithdrawalActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private WithDrawalData data;

    public static Intent IntoWithdrawal(Activity activity) {
        Intent intent = new Intent(activity, WithdrawalActivity.class);
        return intent;
    }

    //    private TextView tv_tixianfangshi;
    private RecyclerView rv_withdrawal;
    private NoPaddingTextView tv_balance_num;
    private CommonAdapter<WithDrawalItemBean> adapter;
    private List<WithDrawalItemBean> mList = new ArrayList<>();
    private int selectIndex = 0;
    private ImageView iv_av;
    private TextView tv_username, tv_tixian;
    private int TiXianID = -1;
    private int need_coins = -1;
    private static final int TiXianType = 1;
    private TextView tv_tips;
    private InterActionExpressAdView interView = null;
    private ImageView block_amount,invite_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_layout);
        EventBus.getDefault().register(this);
        ButtonStatistical.withdrawalPage();
        UiUtils.setTitleBar(this, "", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, getString(R.string.withdrawal),
                null, getString(R.string.wothdrawal_record),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ButtonStatistical.withdrawalRecordBtn();
                        WithdrawalHistoryActivity.IntoWithdrawalHistory(WithdrawalActivity.this);
                    }
                });
        initView();
        getWithDrawalData();
        if(AppUtil.HWIsShowAd()){
            interView = new InterActionExpressAdView.Builder(this)
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
    protected void onDestroy() {
        super.onDestroy();
        if (interView != null) {
            interView.destory();
            interView = null;
        }
    }

    private void initView() {
        block_amount=findViewById(R.id.block_amount);
        block_amount.setOnClickListener(this);
        tv_tips = findViewById(R.id.tv_tips);
        rv_withdrawal = findViewById(R.id.rv_withdrawal);
        tv_balance_num = findViewById(R.id.tv_balance_num);
        tv_tixian = findViewById(R.id.tv_tixian);
        iv_av = findViewById(R.id.iv_av);
        tv_username = findViewById(R.id.tv_username);
        tv_balance_num.setText(String.valueOf(UserEntity.getInstance().getPoints()));
        if(UserEntity.getInstance().getUserInfo()!=null){
            ImageLoader.loadCircleImage(WithdrawalActivity.this, iv_av, UserEntity.getInstance().getUserInfo().getHead_img_url());
            tv_username.setText(UserEntity.getInstance().getUserInfo().getNickname());
        } else {
            tv_username.setText(UserEntity.getInstance().getNickname());
        }

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        adapter = new CommonAdapter<WithDrawalItemBean>(this, R.layout.item_withdrawal_money_layout, mList) {
            @Override
            public void convert(final RecycleViewHolder holder, final WithDrawalItemBean bean) {
                ImageView iv_new_icon = holder.getView(R.id.iv_new_icon);
                TextView tv_money = holder.getView(R.id.tv_money);
                ImageView iv_fl_icon=holder.getView(R.id.iv_fl_icon);
                if (bean.is_new_user == 1) {
                    iv_new_icon.setVisibility(View.VISIBLE);
                    iv_fl_icon.setVisibility(View.GONE);
                }
                if(bean.amount==200){
                    iv_new_icon.setVisibility(View.GONE);
                    iv_fl_icon.setVisibility(View.VISIBLE);
                }
                tv_money.setText(getResources().getString(R.string.money_rmd, DataUtils.Fen2Yuan(bean.amount)));
                TextView tv_real_money = holder.getView(R.id.tv_real_money);
//                if (bean.amount >= bean.real_amount) {
//                    tv_real_money.setVisibility(View.GONE);
//                } else {
//                    tv_real_money.setText(Html.fromHtml(getResources().getString(R.string.real_money, DataUtils.Fen2Yuan(bean.real_amount))));
//                }
                tv_real_money.setText(getResources().getString(R.string.string_sale_coins,String.valueOf(bean.need_coins)));
                RelativeLayout rl_item = holder.getView(R.id.rl_item);
                if (holder.getAdapterPosition() == selectIndex) {
                    rl_item.setBackgroundResource(R.mipmap.withdrawal_item_select);
                    tv_money.setTextColor(getResources().getColor(R.color.c_2C9DFF));
                } else {
                    rl_item.setBackgroundResource(R.mipmap.withdrawal_item_unselect);
                    tv_money.setTextColor(getResources().getColor(R.color.c_3D3F5C));
                }
                rl_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.getAdapterPosition() != selectIndex) {
                            TiXianID = bean.id;
                            need_coins = bean.need_coins;
                            selectIndex = holder.getAdapterPosition();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        };
        rv_withdrawal.setLayoutManager(manager);
        rv_withdrawal.setAdapter(adapter);
        tv_tixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonStatistical.clickWithdrawalBtn();
                if (UserEntity.getInstance().getUserInfo() == null) {
                    WeChatLoginActivity.IntoSettings(getActivity());
                    return;
                }
                if (TextUtils.isEmpty(UserEntity.getInstance().getUserInfo().getPhone())) {
                    BindPhoneDialog dialog = new BindPhoneDialog.Builder(WithdrawalActivity.this)
                            .setOnButtonClick(new BindPhoneDialog.onButtonClick() {
                                @Override
                                public void onConfirmClick(Dialog dialog) {
                                    BindPhoneActivity.IntoBindPhone(WithdrawalActivity.this);
                                    dialog.dismiss();
                                }
                            })
                            .build();
                    dialog.show();
                    return;
                }

                TiXian();
            }
        });

        invite_layout=findViewById(R.id.invite_layout);
        invite_layout.setOnClickListener(this);

    }

    /**
     * 请求提现item数据
     */
    private void getWithDrawalData() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getWithDrawalItems(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<WithDrawalData>>observableIO2Main(WithdrawalActivity.this))
                .subscribe(new BaseObserver<WithDrawalData>() {
                    @Override
                    public void onSuccess(WithDrawalData demo) {
                        if (demo != null) {
                            data = demo;
                            if (demo.list != null && demo.list.size() > 0) {
                                selectIndex = 0;
                                TiXianID = demo.list.get(0).id;
                                need_coins = demo.list.get(0).need_coins;
                            }
                            tv_tips.setText(demo.description);
                            if (demo.list != null) {
                                mList = demo.list;
                                adapter.replaceList(mList);
                            }
                        //    tv_balance_num.setText(String.valueOf(demo.cash));
                            if(demo.block_amount>0){
                                block_amount.setVisibility(View.VISIBLE);
                            }else {
                                block_amount.setVisibility(View.GONE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }

                });
    }

    /**
     * 如果积分符合 则请求服务器验证 是否可提现
     */
    private void TiXian() {
        if (need_coins > 0 && UserEntity.getInstance().getPoints() >= need_coins) {
            if (TiXianID > 0) {
                Check();
            }
        } else {
            ToastUtils.showShort("金币不足");
        }
    }

    /**
     * 验证是否可提现
     */
    private void Check() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .withDrawCheck(TiXianID, UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<WithdrawCheckBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<WithdrawCheckBean>() {
                    @Override
                    public void onSuccess(final WithdrawCheckBean demo) {
                        if (demo != null) {
                            if (demo.can_withdraw) {
                                requestTiXian();
                            } else {
                                ButtonStatistical.withdrawExceptionCount();
                                WithdrawalFailureDialog dialog = new WithdrawalFailureDialog.Builder(WithdrawalActivity.this)
                                        .setContent(demo.rule)
                                        .setListener(new WithdrawalFailureDialog.WithFailureListener() {
                                            @Override
                                            public void CloseListener(Dialog dialog) {
                                                dialog.dismiss();
                                                if (demo.red_envelope) {
                                                    UserEntity.getInstance().setCash(demo.cash);
                                                    UserEntity.getInstance().setPoints(demo.total_points);
                                                    TiXianSurpriseDialog dialog1 = new TiXianSurpriseDialog.Builder(WithdrawalActivity.this)
                                                            .setRewardCash(demo.reward_cash)
                                                            .setRewardPoint(demo.reward_points)
                                                            .setTips(demo.tips)
                                                            .build();
                                                    dialog1.show();
                                                }
                                            }

                                            @Override
                                            public void inviteBtn(Dialog dialog) {
                                                dialog.dismiss();
                                                startActivity(new Intent(WithdrawalActivity.this, ShareActivity.class));
                                            }
                                        }).build();
                                dialog.show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        if (!TextUtils.isEmpty(errorMsg)) {
                            ToastUtils.showShort(errorMsg);
                        }
                    }
                });
    }

    /**
     * 请求提现接口
     */
    private void requestTiXian() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .TiXian(UserEntity.getInstance().getUserId(), TiXianID, TiXianType)
                .compose(CommonSchedulers.<BaseResponse<WithdrawBean>>observableIO2Main(WithdrawalActivity.this))
                .subscribe(new BaseObserver<WithdrawBean>() {
                    @Override
                    public void onSuccess(final WithdrawBean demo) {
                        if (demo != null) {
                            UserEntity.getInstance().setCash(demo.cash);
                            UserEntity.getInstance().setPoints(demo.total_points);
                            tv_balance_num.setText(String.valueOf(demo.total_points));

                            WithdrawalDialog dialog = new WithdrawalDialog.Builder(getActivity())
                                    .setDialogClickListener(new WithdrawalDialog.DialogClickListner() {
                                        @Override
                                        public void btnClick(Dialog dialog) {
                                            dialog.dismiss();
                                            if (demo.red_envelope) {
                                                //发红包
                                                TiXianSurpriseDialog dialog1 = new TiXianSurpriseDialog.Builder(WithdrawalActivity.this)
                                                        .setRewardPoint(demo.reward_points)
                                                        .setRewardCash(demo.reward_cash)
                                                        .setDialogListener(new TiXianSurpriseDialog.TiXianDialogListener() {
                                                            @Override
                                                            public void openred(Dialog dialog) {
                                                                dialog.dismiss();
                                                                GetRedDialog redDialog = new GetRedDialog.Builder(WithdrawalActivity.this)
                                                                        .setCash(demo.reward_cash)
                                                                        .setType(2)
                                                                        .setTips(demo.tips)
                                                                        .setDialogListener(new GetRedDialog.GetRedDialogListener() {
                                                                            @Override
                                                                            public void closeClick(Dialog dialog) {
                                                                                if (dialog != null) {
                                                                                    dialog.dismiss();
                                                                                }
                                                                                startActivity(new Intent(WithdrawalActivity.this,ShareActivity.class));
                                                                                finish();
                                                                            }
                                                                        })
                                                                        .build();
                                                                redDialog.show();
                                                            }
                                                        })
                                                        .build();
                                                dialog1.show();
                                            } else {
                                                //不发红包
                                                finish();
                                            }

                                        }
                                    }).build();
                            dialog.show();
                        }
                        getWithDrawalData();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        if (!TextUtils.isEmpty(errorMsg)) {
                            ToastUtils.showShort(errorMsg);
                        }
                    }

                });
//        new RewardVideoAd.Builder(WithdrawalActivity.this)
//                .setSupportDeepLink(true)
//                .setScenario(ScenarioEnum.withdrawal_page_video)
//                .setRewardVideoLoadListener(new RewardVideoLoadListener() {
//                    @Override
//                    public void onAdClick(String channel) {
//
//                    }
//
//                    @Override
//                    public void onVideoComplete(String channel) {
//
//                    }
//
//                    @Override
//                    public void onLoadFaild(String channel, int faildCode, String faildMsg) {
//
//                    }
//
//                    @Override
//                    public void onAdClose(String channel) {
//
//                    }
//
//                    @Override
//                    public void onAdShow(String channel) {
//
//                    }
//                }).build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.block_amount:
                if(data!=null){
                    BlockDialog dialog=new BlockDialog.Builder(this)
                            .setCash(data.block_amount)
                            .setButtonClick(new BlockDialog.onButtonClick() {
                                @Override
                                public void onConfirmClick(Dialog dialog) {
                                    dialog.dismiss();
                                    startActivity(new Intent(WithdrawalActivity.this, FriendManagerActivity.class));
                                }

                                @Override
                                public void onMissClick(Dialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .build();
                    dialog.show();
                }
                break;
            case R.id.invite_layout:
                startActivity(new Intent(this, ShareActivity.class));
                break;
        }
    }

    //登录之后会收到通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(UserInfoEvent info) {
        if (info != null && info.getUserInfo() != null) {
            UserInfo userInfo = info.getUserInfo();
            tv_username.setText(userInfo.getNickname());
            ImageLoader.loadCircleImage(WithdrawalActivity.this, iv_av, userInfo.getHead_img_url());
            getWithDrawalData();
            getData();
        }

    }

    private void getData() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getPoints(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<PointsBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<PointsBean>() {
                    @SuppressLint("StringFormatMatches")
                    @Override
                    public void onSuccess(PointsBean demo) {
                        if (demo != null) {
                            UserEntity.getInstance().setCash(demo.getCash());
                            UserEntity.getInstance().setPoints(demo.getPoints());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        ToastUtils.showShort(errorMsg);
                    }

                });

    }
}
