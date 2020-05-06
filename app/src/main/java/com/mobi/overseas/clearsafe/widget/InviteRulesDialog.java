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

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;

/**
 * author:zhaijinlu
 * date: 2020/2/22
 * desc: 邀请活动规则
 */
public class InviteRulesDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private onButtonClick mButtonClick;
    private ImageView iv_close;
    private LoadWebView lwv;
    private String url;


    public InviteRulesDialog(Builder builder) {
        super(builder.mContext);
        this.mContext=builder.mContext;
        this.mButtonClick = builder.mButtonClick;
        this.url=builder.url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invitrules_dialog_layout);
        initView();
        initWindow();
    }

    private void initView() {
        iv_close=findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        lwv=findViewById(R.id.lwv);
        if(!TextUtils.isEmpty(url)){
            lwv.loadUrl(url);
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
            case R.id.iv_close:
                if (mButtonClick != null) {
                    mButtonClick.onConfirmClick(this);
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

        private onButtonClick mButtonClick;
        private String url;


        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setButtonClick(onButtonClick buttonClick) {
            this.mButtonClick = buttonClick;
            return this;
        }

        public InviteRulesDialog build() {
            return new InviteRulesDialog(this);
        }

    }

    public interface onButtonClick {
        void onConfirmClick(Dialog dialog);
    }
}
