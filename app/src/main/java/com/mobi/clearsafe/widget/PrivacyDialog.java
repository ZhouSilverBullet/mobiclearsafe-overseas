package com.mobi.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

/**
 * author:zhaijinlu
 * date: 2019/11/16
 * desc: 用户条款及隐私协议dialog
 */
public class PrivacyDialog extends BaseDialog implements View.OnClickListener {


    private Context mContext;
    private TextView tv_service;
    private TextView tv_privacy;
    private TextView cancel;
    private TextView confirm;
    private onButtonClick mButtonClick;



    public PrivacyDialog(Builder builder) {
        super(builder.mContext);
        this.mContext=builder.mContext;
        this.mButtonClick = builder.mButtonClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_privacy_layout);
        initView();
        initWindow();
    }

    private void initView() {
        cancel = findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);
        confirm = findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(this);
        tv_service=findViewById(R.id.service_agreement);
        tv_service.setOnClickListener(this);
        tv_privacy=findViewById(R.id.privacy_agreement);
        tv_privacy.setOnClickListener(this);


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
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                LoadWebViewActivity.IntoPrivacyAgreement(mContext, UserEntity.getInstance().getConfigEntity().getPrivacy_policy());
                break;
            case R.id.btn_confirm:
                if (mButtonClick != null) {
                    mButtonClick.onConfirmClick(this);
                }
                break;
            case R.id.service_agreement:
                LoadWebViewActivity.IntoServiceAgreement(mContext, UserEntity.getInstance().getConfigEntity().getPact());
                break;
            case R.id.privacy_agreement:
                LoadWebViewActivity.IntoPrivacyAgreement(mContext, UserEntity.getInstance().getConfigEntity().getPrivacy_policy());
                break;
        }
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }

    public static class Builder {
        private Context mContext;
        private onButtonClick mButtonClick;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setButtonClick(onButtonClick buttonClick) {
            this.mButtonClick = buttonClick;
            return this;
        }


        public PrivacyDialog build() {
            return new PrivacyDialog(this);
        }

    }

    public interface onButtonClick {
        void onConfirmClick(Dialog dialog);
    }

}
