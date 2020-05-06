package com.mobi.overseas.clearsafe.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.nativeexpress.NativeExpressAd;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.WeChatLoginActivity;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.eventbean.CheckTabEvent;
import com.mobi.overseas.clearsafe.eventbean.PleasanEvent;
import com.mobi.overseas.clearsafe.eventbean.UserInfoEvent;
import com.mobi.overseas.clearsafe.fragment.bean.TargetStepBean;
import com.mobi.overseas.clearsafe.main.activity.InviteActivity;
import com.mobi.overseas.clearsafe.me.activity.MyCardActivity;
import com.mobi.overseas.clearsafe.me.activity.MyWalletActivity;
import com.mobi.overseas.clearsafe.me.activity.SettingsActivity;
import com.mobi.overseas.clearsafe.me.activity.StepHistoryActivity;
import com.mobi.overseas.clearsafe.me.activity.WithdrawalActivity;
import com.mobi.overseas.clearsafe.me.bean.PointsBean;
import com.mobi.overseas.clearsafe.moneyactivity.InputInviteCodeActivity;
import com.mobi.overseas.clearsafe.moneyactivity.ShareActivity;
import com.mobi.overseas.clearsafe.moneyactivity.bean.ShareContentBean;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.ui.common.util.StatusBarPaddingUtil;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.StatusBarUtil;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.utils.UiUtils;
import com.mobi.overseas.clearsafe.utils.imageloader.ImageLoader;
import com.mobi.overseas.clearsafe.widget.LazyLoadFragment;
import com.mobi.overseas.clearsafe.widget.RoundImageView;
import com.mobi.overseas.clearsafe.widget.SelectStepDialog;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;
import com.mobi.overseas.clearsafe.wxapi.bean.UserInfo;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class meFragment extends LazyLoadFragment implements View.OnClickListener {

    private RelativeLayout iv_setting, rl_addgroup, rl_targetstep, rl_steprecord;
    private LinearLayout rl_goldhistory,layout_coupon,layout_invite,layout_center,layout_yqm;
    private TextView tv_withdrawal;

    private RoundImageView head_photo;
    private TextView nick_name;
    private TextView current_coins;
    private TextView tv_money;
    private RelativeLayout fl_ad;
    private TextView tv_qqnum;
    private TextView tv_yqm,tv_copy;

    @Override
    protected int setContentView() {
        return R.layout.fragment_me;
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("meFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("meFragment");

        if (head_photo != null && nick_name != null  && tv_withdrawal != null && current_coins != null) {
            if (UserEntity.getInstance().getUserInfo() == null) {
                head_photo.setImageResource(R.mipmap.default_head_icon);
                nick_name.setText(UserEntity.getInstance().getNickname());
                getShareContent();
            }
            getData();
        }
    }

    @Override
    protected void lazyLoad() {
        StatusBarPaddingUtil.statusBar(getActivity(),true);

        ButtonStatistical.minePage();
        if (fl_ad == null) {
            fl_ad = findViewById(R.id.fl_ad);
        }
        if (fl_ad != null) {
            if(AppUtil.HWIsShowAd()){
                new NativeExpressAd.Builder(getContext())
                        .setAdCount(1)
                        .setADViewSize(350, 0)
                        .setHeightAuto(true)
                        .setSupportDeepLink(true)
                        .setBearingView(fl_ad)
                      //  .setPosID("1024018")
                        .setScenario(ScenarioEnum.me_page_native)
                        .build();
            }
        }
        if (Const.pBean != null) {
            if (Const.pBean != null && Const.pBean.show_page == 4) {
                EventBus.getDefault().post(new PleasanEvent(Const.pBean.show_page));
            }
        }
    }

    @Override
    protected void firstIn() {
        EventBus.getDefault().register(this);

//        StatusBarPaddingUtil.topViewPadding(findViewById(R.id.nsvScroll));
        StatusBarPaddingUtil.topViewPadding(findViewById(R.id.llMeBg));
        StatusBarPaddingUtil.statusBar(getActivity(),true);

        initView();
    }

    private void initView() {
        tv_money=findViewById(R.id.tv_money);
        layout_yqm=findViewById(R.id.layout_yqm);
        layout_yqm.setOnClickListener(this);
        layout_coupon=findViewById(R.id.layout_coupon);
        layout_coupon.setOnClickListener(this);
        layout_invite=findViewById(R.id.layout_invite);
        layout_invite.setOnClickListener(this);
        layout_center=findViewById(R.id.layout_center);
        layout_center.setOnClickListener(this);
        rl_goldhistory = findViewById(R.id.rl_goldhistory);
        rl_goldhistory.setOnClickListener(this);
        iv_setting = findViewById(R.id.iv_setting);
        tv_withdrawal = findViewById(R.id.tv_withdrawal);
        tv_withdrawal.setOnClickListener(this);
        rl_addgroup = findViewById(R.id.rl_addgroup);
        rl_addgroup.setOnClickListener(this);

        rl_targetstep = findViewById(R.id.rl_targetstep);
        rl_targetstep.setOnClickListener(this);
        rl_steprecord = findViewById(R.id.rl_steprecord);
        rl_steprecord.setOnClickListener(this);
        head_photo = findViewById(R.id.head_photo);
        nick_name = findViewById(R.id.tv_name);
        nick_name.setOnClickListener(this);
        current_coins = findViewById(R.id.current_coins);
        iv_setting.setOnClickListener(this);
        tv_qqnum = findViewById(R.id.tv_qqnum);
        tv_qqnum.setText(getContext().getResources().getString(R.string.add_qq_group, UserEntity.getInstance().getConfigEntity().getQqqun()));
        tv_yqm=findViewById(R.id.tv_yqm);

        tv_copy=findViewById(R.id.tv_copy);
        tv_copy.setOnClickListener(this);
    }

    //通过分享接口得到游客邀请码
    private void getShareContent() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getShareContent(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<ShareContentBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<ShareContentBean>() {
                    @Override
                    public void onSuccess(ShareContentBean demo) {
                        if(demo!=null){
                            if(!TextUtils.isEmpty(demo.getCode())){
                                tv_yqm.setText(demo.getCode());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            UserInfo userInfo = UserEntity.getInstance().getUserInfo();
            if (userInfo != null) {
                ImageLoader.loadImage(getActivity(), head_photo, userInfo.getHead_img_url());
                nick_name.setText(userInfo.getNickname());
                tv_yqm.setText(userInfo.getInvite_code());
            }else {
                getShareContent();
                nick_name.setText(UserEntity.getInstance().getNickname());
            }
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
                            current_coins.setText(demo.getPoints() + "");
                            tv_money.setText("≈"+demo.getCash());
                        }
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
        if (info != null && info.getUserInfo() != null) {
            UserInfo userInfo = info.getUserInfo();
            nick_name.setText(userInfo.getNickname());
            tv_yqm.setText(userInfo.getInvite_code());
            ImageLoader.loadImage(getActivity(), head_photo, userInfo.getHead_img_url());
            getData();
        }

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_yqm:
                if (UserEntity.getInstance().getUserInfo() == null) {
                    ToastUtils.showShort("请先微信登录");
                    WeChatLoginActivity.IntoSettings(getActivity());
                    return;
                }
                getActivity().startActivity(new Intent(getActivity(), InputInviteCodeActivity.class));
                break;
            case R.id.iv_setting:
                ButtonStatistical.settingBtn();
                SettingsActivity.IntoSettings(getActivity());
                break;
            case R.id.rl_goldhistory://我的钱包
                ButtonStatistical.coinsRecordBtn();
//                GoldHistoryActivity.IntoGoldHistory(getActivity());
                MyWalletActivity.IntoMyWallet(getContext());
                break;
            case R.id.tv_withdrawal:
                if (UserEntity.getInstance().getUserInfo() == null) {
                    WeChatLoginActivity.IntoSettings(getActivity());
                    return;
                }
                ButtonStatistical.withdrawalBtn();
                Intent intent = WithdrawalActivity.IntoWithdrawal(getActivity());
                startActivity(intent);
                break;
            case R.id.rl_addgroup:
                ButtonStatistical.UserFeedbackBtn();
                if (!UiUtils.joinQQGroup(getActivity(), UserEntity.getInstance().getConfigEntity().getQqqunkey())) {
                    if (AppUtil.copy(getContext(), UserEntity.getInstance().getConfigEntity().getQqqun())) {
                        ToastUtils.showShort(R.string.qqgroup_copy);
                    }
                }

                break;
            case R.id.tv_name:
                if (UserEntity.getInstance().getUserInfo() == null) {
                    WeChatLoginActivity.IntoSettings(getActivity());
                    return;
                }
                break;
            case R.id.rl_targetstep:
                ButtonStatistical.targetStepBtn();
                SelectStepDialog dialog = new SelectStepDialog(getContext(), UserEntity.getInstance().getTarget_step(), new SelectStepDialog.DataListener() {
                    @Override
                    public void cancleClick(Dialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void submitClick(Dialog dialog, String data) {
                        setTargetStep(Integer.parseInt(data));
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.rl_steprecord:
                ButtonStatistical.movementRecordBtn();
                StepHistoryActivity.IntoStepHistory(getActivity());
                break;
            case R.id.layout_coupon://卡券中心
//                ToastUtils.showShort("卡券中心");
//                ButtonStatistical.numberMycard();
//                MyCardActivity.intoMyCard(getContext());
                EventBus.getDefault().post(new CheckTabEvent("main"));
                break;
            case R.id.layout_invite://邀请好友
                getActivity().startActivity(new Intent(getActivity(), ShareActivity.class));
                break;
            case R.id.layout_center://活动中心
                //ToastUtils.showShort("跳转聚合页");
                EventBus.getDefault().post(new CheckTabEvent("activity"));
                break;
            case R.id.tv_copy:
                if (AppUtil.copy(getContext(),tv_yqm.getText().toString().trim())) {
                    ToastUtils.showShort(getResources().getString(R.string.yqm_tip));
                }
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 设置目标步数
     */
    private void setTargetStep(final int targetStep) {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .setTargetStep(UserEntity.getInstance().getUserId(), targetStep)
                .compose(CommonSchedulers.<BaseResponse<TargetStepBean>>observableIO2Main(getContext()))
                .subscribe(new BaseObserver<TargetStepBean>() {
                    @Override
                    public void onSuccess(TargetStepBean demo) {
                        UserEntity.getInstance().setTarget_step(targetStep);
                        ToastUtils.showShort("设置成功");
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        ToastUtils.showShort(errorMsg);
                    }


                });
    }
}
