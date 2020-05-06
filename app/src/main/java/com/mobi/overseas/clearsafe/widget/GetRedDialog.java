package com.mobi.overseas.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;

/**
 * author:zhaijinlu
 * date: 2020/2/13
 * desc:
 */
public class GetRedDialog extends BaseDialog implements View.OnClickListener {


    private Context mContext;
    private ImageView jump_app;
    private TextView tv_gold;
    private float cash;
    private int type;//4代表新人红包  3惊喜红包  2提现红包
    private TextView tv_hint;
    private GetRedDialogListener mListener;
    private TextView tv_des;
    private String tipString;//可提现文案
    private boolean can_withdraw;//是否可提现
    private String tips;//提现红包提示文案

    public GetRedDialog(Builder builder) {
        super(builder.mContext);
        this.cash = builder.cash;
        this.mContext = builder.mContext;
        this.type = builder.type;
        this.mListener = builder.mListener;
        this.tipString=builder.tipString;
        this.can_withdraw=builder.can_withdraw;
        this.tips=builder.tips;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getred_dialog_layout);
        initView();
        initWindow();
    }

    private void initView() {
        jump_app = findViewById(R.id.jump_app);
        jump_app.setOnClickListener(this);
        tv_gold = findViewById(R.id.tv_gold);
        tv_gold.setText(mContext.getResources().getString(R.string.red_rmb, cash + ""));
        tv_hint = findViewById(R.id.tv_hint);
        tv_des=findViewById(R.id.tv_des);
        if (type == 4) {//新人红包  几元提现展示  判断是否可提现
            tv_hint.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(tipString)){
                tv_hint.setText(tipString);
            }
            tv_des.setVisibility(View.GONE);
            if(can_withdraw){
                jump_app.setImageResource(R.mipmap.redpacket_withdraw);
            }else {
                jump_app.setImageResource(R.mipmap.red_jump);
            }
        } else if(type==3){//惊喜红包
            tv_hint.setVisibility(View.GONE);
            jump_app.setImageResource(R.mipmap.red_jump);
            tv_des.setVisibility(View.GONE);
        }else if (type==2){//提现红包
            tv_hint.setVisibility(View.GONE);
            tv_des.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(tips)){
                tv_des.setText(tips);
            }
            jump_app.setImageResource(R.mipmap.withdraw_red_btn);
        }
    }

    private void initWindow() {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump_app:
                if (mListener != null) {
                    mListener.closeClick(GetRedDialog.this);
                }
                break;
        }
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }


    public static class Builder {
        private Context mContext;
        private float cash;
        private int type;
        private GetRedDialogListener mListener;
        private String tipString;//可提现文案
        private boolean can_withdraw;//是否可提现
        private String tips;//提现红包提示文案

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setCan_withdraw(boolean can_withdraw) {
            this.can_withdraw = can_withdraw;
            return this;
        }

        public Builder setTips(String tips) {
            this.tips = tips;
            return this;
        }

        public Builder setTipString(String tipString) {
            this.tipString = tipString;
            return this;
        }

        public Builder setCash(float cash) {
            this.cash = cash;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setDialogListener(GetRedDialogListener listener) {
            this.mListener = listener;
            return this;
        }

        public GetRedDialog build() {
            return new GetRedDialog(this);
        }

    }

    public interface GetRedDialogListener {
        void closeClick(Dialog dialog);
    }

}
