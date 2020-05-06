package com.mobi.clearsafe.main.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.main.bean.InviteDetail;
import com.mobi.clearsafe.moneyactivity.EwmActivity;
import com.mobi.clearsafe.moneyactivity.bean.ShareContentBean;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.DialogUtils;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.widget.InviteRulesDialog;
import com.mobi.clearsafe.widget.ShareDialog;
import com.mobi.clearsafe.wxapi.WeixinHandler;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import java.util.List;

/**
 * 邀请好友页
 */

public class InviteActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ViewFlipper viewflipper;
    private ImageView iv_back;
    private TextView tv_gz;
    private TextView get_money,tv_yqm,tv_copy;
    private ImageView invite_btn;
    private TextView tv_friendmanager;
    private TextView success_num,forecast_earnings;
    private TextView tv_title,tv_money,tv_des;
    private TextView tv_title2,tv_money2,tv_des2;
    private TextView tv_title3,tv_money3,tv_des3;
    private TextView tv_title4,tv_money4,tv_des4;
    private TextView tv_title5,tv_money5,tv_des5;
    private LinearLayout invite_layout;
    private LinearLayout share_to_weixin_friends;
    private LinearLayout share_to_weixin_timeline;
    private LinearLayout share_to_code;

    private ShareContentBean mShareBean;
    private InviteDetail inviteDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        ButtonStatistical.invitePageCount();
        initView();
        getShareContent();
        getDetail();
    }

    private void getDetail() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .inviteDetail(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<InviteDetail>>observableIO2Main(this))
                .subscribe(new BaseObserver<InviteDetail>() {
                    @Override
                    public void onSuccess(InviteDetail demo) {
                        if(demo!=null){
                            inviteDetail = demo;
                            get_money.setText(String.valueOf(demo.getMax_income()));
                            success_num.setText(String.valueOf(demo.getInvite_count()));
                            forecast_earnings.setText(String.valueOf(demo.getTotal_income()));
                            tv_title.setText("第一天");
                            tv_money.setText("+"+demo.getFirst_day().getCash());
                            tv_des.setText(demo.getFirst_day().getTips());

                            tv_title2.setText("第二天");
                            tv_money2.setText("+"+demo.getSecond_day().getCash());
                            tv_des2.setText(demo.getSecond_day().getTips());

                            tv_title3.setText("第三天");
                            tv_money3.setText("+"+demo.getThird_day().getCash());
                            tv_des3.setText(demo.getThird_day().getTips());

                            tv_title4.setText("首次邀请");
                            tv_money4.setText("+"+demo.getFirst_invite().getCash());
                            tv_des4.setText(demo.getFirst_invite().getTips());

                            tv_title5.setText("365天");
                            tv_money5.setText("+"+demo.getIncome().getCash());
                            tv_des5.setText(demo.getIncome().getTips());

                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    //得到分享内容
    private void getShareContent() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getShareContent(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<ShareContentBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<ShareContentBean>() {
                    @Override
                    public void onSuccess(ShareContentBean demo) {
                        if(demo!=null){
                            mShareBean=demo;
                            if(!TextUtils.isEmpty(demo.getCode())){
                                tv_yqm.setText(demo.getCode());
                            }
                            List<ShareContentBean.HotBroadCast> list = demo.getWithdraw_hot_broadcast();
                            for (int i=0;i<list.size();i++) {
                                View view = LayoutInflater.from(InviteActivity.this).inflate(R.layout.hot_notice_view,null);
                                SpannableString spannableString = AppUtil.matcherSearchText(getResources().getColor(R.color.c_FDD720),list.get(i).getTitle(), String.valueOf(list.get(i).getRed_sign()));
                                TextView tv1 =  view.findViewById(R.id.view1);
                                tv1.setTextColor(getResources().getColor(R.color.white));
                                tv1.setText(spannableString);
                                viewflipper.addView(view);
                            }
                            viewflipper.startFlipping();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }



    private void initView() {
        viewflipper=findViewById(R.id.viewflipper);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_gz=findViewById(R.id.tv_gz);
        tv_gz.setOnClickListener(this);
        get_money=findViewById(R.id.get_money);
        tv_yqm=findViewById(R.id.tv_yqm);
        tv_copy=findViewById(R.id.tv_copy);
        tv_copy.setOnClickListener(this);
        invite_btn=findViewById(R.id.invite_btn);
        invite_btn.setOnClickListener(this);
        tv_friendmanager=findViewById(R.id.tv_friendmanager);
        tv_friendmanager.setOnClickListener(this);
        success_num=findViewById(R.id.success_num);
        forecast_earnings=findViewById(R.id.forecast_earnings);
        tv_title=findViewById(R.id.tv_title);
        tv_money=findViewById(R.id.tv_money);
        tv_des=findViewById(R.id.tv_des);
        tv_title2=findViewById(R.id.tv_title2);
        tv_money2=findViewById(R.id.tv_money2);
        tv_des2=findViewById(R.id.tv_des2);
        tv_title3=findViewById(R.id.tv_title3);
        tv_money3=findViewById(R.id.tv_money3);
        tv_des3=findViewById(R.id.tv_des3);
        tv_title4=findViewById(R.id.tv_title4);
        tv_money4=findViewById(R.id.tv_money4);
        tv_des4=findViewById(R.id.tv_des4);
        tv_title5=findViewById(R.id.tv_title5);
        tv_money5=findViewById(R.id.tv_money5);
        tv_des5=findViewById(R.id.tv_des5);
        invite_layout=findViewById(R.id.invite_layout);
        share_to_weixin_friends = findViewById(R.id.share_to_weixin_friends);
        share_to_weixin_friends.setOnClickListener(this);
        share_to_weixin_timeline = findViewById(R.id.share_to_weixin_timeline);
        share_to_weixin_timeline.setOnClickListener(this);
        share_to_code = findViewById(R.id.share_to_code);
        share_to_code.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_gz:
                InviteRulesDialog dialog=new InviteRulesDialog.Builder(this)
                        .setUrl(inviteDetail.getRule_url())
                        .setButtonClick(new InviteRulesDialog.onButtonClick() {
                            @Override
                            public void onConfirmClick(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build();
                dialog.show();
                break;
            case R.id.tv_copy:
                if(!TextUtils.isEmpty(tv_yqm.getText())){
                    if (AppUtil.copy(this,tv_yqm.getText().toString().trim())) {
                        ToastUtils.showShort(getResources().getString(R.string.yqm_tip));
                    }
                }
                break;
            case R.id.invite_btn:
                if(mShareBean!=null){
                    invite_layout.setVisibility(View.GONE);
                    final ShareDialog shareDialog = ShareDialog.getInstance(mShareBean, new ShareDialog.ClickListener() {
                        @Override
                        public void onClick() {
                            invite_layout.setVisibility(View.VISIBLE);
                        }
                    });
                    DialogUtils.showDialog(getSupportFragmentManager(), shareDialog, ShareDialog.TAG);
                }
                break;
            case R.id.tv_friendmanager:
                startActivity(new Intent(this,FriendManagerActivity.class));
                break;
            case R.id.share_to_weixin_friends:
                doShare(false);
                break;
            case R.id.share_to_weixin_timeline:
                doShare(true);
                break;
            case R.id.share_to_code:
                if(mShareBean!=null){
                    Intent intent = new Intent(getActivity(), EwmActivity.class);
                    intent.putExtra("url",mShareBean.getJump_address()+ "?code=" + mShareBean.getCode());
                    startActivity(intent);
                }
                break;
        }
    }

    private void doShare(boolean toTimeline) {
        if (!WeixinHandler.getInstance().isWeixinInstalled()) {
            ToastUtils.showShort(R.string.wechat_login_tip);
            return;
        }
        WeixinHandler.getInstance().shareToWeixin(mShareBean.getName(),mShareBean.getIntroduction(),mShareBean.getJump_address()+ "?code=" + mShareBean.getCode()+ "?user_id=" +UserEntity.getInstance().getUserId(),R.mipmap.icon_round,toTimeline);
    }
}
