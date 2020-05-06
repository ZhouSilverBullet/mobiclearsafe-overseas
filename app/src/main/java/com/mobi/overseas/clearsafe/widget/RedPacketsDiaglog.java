package com.mobi.overseas.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.WeChatLoginActivity;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.main.bean.RedBean;
import com.mobi.overseas.clearsafe.main.bean.RedCoinsBean;
import com.mobi.overseas.clearsafe.me.activity.WithdrawalActivity;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;

/**
 * author:zhaijinlu
 * date: 2019/11/16
 * desc: 第一次注册新人红包diglog
 */
public class RedPacketsDiaglog extends BaseDialog implements View.OnClickListener {


    private Context mContext;
    private RelativeLayout red_pack_layout;
    private ImageView iv_open;
    private RedBean bean;
    private TextView red_title;


    public RedPacketsDiaglog(Builder builder) {
        super(builder.mContext);
        this.bean=builder.bean;
        this.mContext=builder.mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_red_pack);
        initView();
        initWindow();
    }

    private void initView() {
        red_pack_layout = findViewById(R.id.layout_red_pack);
        iv_open=findViewById(R.id.open_red);
        iv_open.setOnClickListener(this);
        red_title=findViewById(R.id.red_title);

        if(bean.getType()==4){
            red_title.setText(mContext.getResources().getString(R.string.new_red));
          //  tv_text.setText(mContext.getResources().getString(R.string.new_red_title));
        }else if(bean.getType()==3) {//立即红包
            red_title.setText(mContext.getResources().getString(R.string.surprised_red));
          //  tv_text.setText(mContext.getResources().getString(R.string.surprised_red_title));
        }


    }

    private void initWindow(){
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lps = dialogWindow.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels > dm.heightPixels ? dm.heightPixels
                : dm.widthPixels;
        int h = dm.widthPixels > dm.heightPixels ? dm.widthPixels
                : dm.heightPixels;
        lps.width = w;
        lps.height = h;
//        lps.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lps);
        dialogWindow.setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_red:
                dismiss();
                ButtonStatistical.clickRedpacket();
                if(bean.getType()==4){
                    if(!bean.isIs_bind_weixin()){
                        WeChatLoginActivity.IntoSettings((getContext()));
                        //登录后需要领取红包
                        UserEntity.getInstance().setRed(true);
                        return;
                    }else {
                        getRed();
                    }
                }else {
                    GetRedDialog dialog=new GetRedDialog.Builder(mContext)//新人惊喜红包
                            .setCash(bean.getToday_reward().getReward_cash())
                            .setType(3)
                            .setDialogListener(new GetRedDialog.GetRedDialogListener() {
                                @Override
                                public void closeClick(Dialog dialog) {
                                    dialog.dismiss();
                                }
                            })
                            .build();
                    dialog.show();
                }
                break;

        }
    }

    //领取新人红包
    private void getRed() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getRedCoins(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<RedCoinsBean>>observableIO2Main(mContext))
                .subscribe(new BaseObserver<RedCoinsBean>() {
                    @Override
                    public void onSuccess(final RedCoinsBean demo) {
                        if(demo!=null){
                            ButtonStatistical.receiveRedpacket();
                            UserEntity.getInstance().setPoints(demo.getTotal_points());
                            UserEntity.getInstance().setCash(demo.getTotal_cash());
                            GetRedDialog dialog=new GetRedDialog.Builder(mContext)
                                    .setCash(demo.getReward_cash())
                                    .setType(4)
                                    .setCan_withdraw(demo.isCan_withdraw())
                                    .setTipString(demo.getButton_content())
                                    .setDialogListener(new GetRedDialog.GetRedDialogListener() {
                                        @Override
                                        public void closeClick(Dialog dialog) {
                                            dialog.dismiss();
                                            if(demo.isCan_withdraw()){
                                                mContext.startActivity(new Intent(mContext, WithdrawalActivity.class));
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

    @Override
    protected Context getActivityContext() {
        return mContext;
    }

    public static class Builder {
        private Context mContext;
        private RedBean bean;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setCash(RedBean bean) {
            this.bean = bean;
            return this;
        }


        public RedPacketsDiaglog build() {
            return new RedPacketsDiaglog(this);
        }

    }

}

