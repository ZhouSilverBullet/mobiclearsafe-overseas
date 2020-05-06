package com.mobi.overseas.clearsafe.main.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.base.adapter.CommonAdapter;
import com.mobi.overseas.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.overseas.clearsafe.main.bean.InviteRecord;
import com.mobi.overseas.clearsafe.moneyactivity.bean.ShareContentBean;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.utils.DialogUtils;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.utils.UiUtils;
import com.mobi.overseas.clearsafe.widget.InviteDialog;
import com.mobi.overseas.clearsafe.widget.InviteTcDidalog;
import com.mobi.overseas.clearsafe.widget.ShareDialog;
import com.mobi.overseas.clearsafe.wxapi.WeixinHandler;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友管理
 */
public class FriendManagerActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private TextView money,tv_withdrawal,tv_conins;
    private LinearLayout layout_ask;
    private TextView tv_title,tv_money,tv_des;
    private TextView tv_title2,tv_money2,tv_des2;
    private TextView tv_title3,tv_money3,tv_des3;
    private TextView tv_title4,tv_money4,tv_des4;
    private TextView tv_title5,tv_money5,tv_des5;
    private RecyclerView recycler;
    private LinearLayout friend_layout,empty_layout;
    private ImageView invite_btn;
    private CommonAdapter<InviteRecord.Persion> adapter;
    private List<InviteRecord.Persion> list=new ArrayList<>();
    private ShareContentBean mShareBean;
    private InviteRecord info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_manager);
        initView();
        getShareContent();
        getDate();

    }

    private void getDate() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .inviteRecord(UserEntity.getInstance().getUserId(),1,10)
                .compose(CommonSchedulers.<BaseResponse<InviteRecord>>observableIO2Main(this))
                .subscribe(new BaseObserver<InviteRecord>() {
                    @Override
                    public void onSuccess(InviteRecord demo) {
                        if(demo!=null){
                            info = demo;
                            money.setText(String.valueOf(demo.getTotal_reward()));
                            tv_conins.setText(String.valueOf(demo.getCommission()));
                            tv_title.setText("第一天");
                            tv_money.setText("+"+demo.getDetail().getFirst_day().getCash());
                            tv_des.setText(demo.getDetail().getFirst_day().getTips());

                            tv_title2.setText("第二天");
                            tv_money2.setText("+"+demo.getDetail().getSecond_day().getCash());
                            tv_des2.setText(demo.getDetail().getSecond_day().getTips());

                            tv_title3.setText("第三天");
                            tv_money3.setText("+"+demo.getDetail().getThird_day().getCash());
                            tv_des3.setText(demo.getDetail().getThird_day().getTips());

                            tv_title4.setText("首次邀请");
                            tv_money4.setText("+"+demo.getDetail().getFirst_invite().getCash());
                            tv_des4.setText(demo.getDetail().getFirst_invite().getTips());

                            tv_title5.setText("365天");
                            tv_money5.setText("+"+demo.getDetail().getIncome().getCash());
                            tv_des5.setText(demo.getDetail().getIncome().getTips());
                            if(demo.getRes_list().size()>0){
                                friend_layout.setVisibility(View.VISIBLE);
                                empty_layout.setVisibility(View.GONE);
                                list=demo.getRes_list();
                                adapter.replaceList(list);
                            }else {
                                friend_layout.setVisibility(View.GONE);
                                empty_layout.setVisibility(View.VISIBLE);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    private void initView() {
        UiUtils.setTitleBar(this, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.string_invite_tip2), null);

        money=findViewById(R.id.money);
        layout_ask=findViewById(R.id.layout_ask);
        layout_ask.setOnClickListener(this);
        tv_withdrawal=findViewById(R.id.tv_withdrawal);
        tv_withdrawal.setOnClickListener(this);
        tv_conins=findViewById(R.id.tv_conins);
        tv_title=findViewById(R.id.title);
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
        recycler=findViewById(R.id.recycler);
        friend_layout=findViewById(R.id.friend_layout);
        empty_layout=findViewById(R.id.empty_layout);
        invite_btn=findViewById(R.id.invite_btn);
        invite_btn.setOnClickListener(this);
        adapter=new CommonAdapter<InviteRecord.Persion>(this,R.layout.friend_manager_item, list) {
            @Override
            public void convert(RecycleViewHolder holder, InviteRecord.Persion persion) {
                TextView nick_name=holder.getView(R.id.nick_name);
                TextView date=holder.getView(R.id.date);
                TextView money=holder.getView(R.id.money);
                TextView coins=holder.getView(R.id.coins);
                TextView earnings=holder.getView(R.id.earnings);
                TextView action=holder.getView(R.id.action);
                nick_name.setText(persion.getNick_name());
                date.setText(persion.getCreate_time());
                money.setText(persion.getSum_cash()+"元");
                coins.setText(persion.getSum_income()+"金币");
                action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        doShare(false);
                    }
                });
            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

    }

    private void doShare(boolean toTimeline) {
        if (!WeixinHandler.getInstance().isWeixinInstalled()) {
            ToastUtils.showShort(R.string.wechat_login_tip);
            return;
        }
        WeixinHandler.getInstance().shareToWeixin(mShareBean.getName(),mShareBean.getIntroduction(),mShareBean.getJump_address()+ "?code=" + mShareBean.getCode()+ "?user_id=" +UserEntity.getInstance().getUserId(),R.mipmap.icon_round,toTimeline);
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
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_ask:
                InviteTcDidalog didalog=new InviteTcDidalog.Builder(this)
                        .setButtonClick(new InviteTcDidalog.onButtonClick() {
                            @Override
                            public void onConfirmClick(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build();
                didalog.show();
                break;
            case R.id.tv_withdrawal:
                if(info!=null){
                    InviteDialog dialog=new InviteDialog.Builder(this)
                            .setCash(info.getTotal_reward())
                            .setCoins(info.getCommission())
                            .setListener(new InviteDialog.ClickListener() {
                                @Override
                                public void onBtnClick(Dialog dialog) {
                                    dialog.dismiss();
                                    if(mShareBean!=null){
                                        final ShareDialog shareDialog = ShareDialog.getInstance(mShareBean, new ShareDialog.ClickListener() {
                                            @Override
                                            public void onClick() {
                                            }
                                        });
                                        DialogUtils.showDialog(getSupportFragmentManager(), shareDialog, ShareDialog.TAG);
                                    }
                                }
                            })
                            .build();
                    dialog.show();
                }
                break;
            case R.id.invite_btn:
                if(mShareBean!=null){
                    final ShareDialog shareDialog = ShareDialog.getInstance(mShareBean, new ShareDialog.ClickListener() {
                        @Override
                        public void onClick() {
                        }
                    });
                    DialogUtils.showDialog(getSupportFragmentManager(), shareDialog, ShareDialog.TAG);
                }
                break;

        }
    }
}
