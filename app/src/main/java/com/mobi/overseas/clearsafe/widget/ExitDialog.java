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
 * date: 2020/1/3
 * desc: app退出时弹框
 */
public class ExitDialog extends BaseDialog implements View.OnClickListener {

    private final Context mContext;
    private onButtonClick mButtonClick;
    private String mContent;
    private String cancel_text;
    private String confirm_text;
    private TextView tv_content;
    private TextView cancel;
    private TextView confirm;
    private ImageView iv_close;


    public ExitDialog(Builder builder) {
        super(builder.mContext);
        mContext = builder.mContext;
        this.mButtonClick = builder.mButtonClick;
        this.mContent = builder.mContent;
        this.cancel_text = builder.cancel_text;
        this.confirm_text = builder.confirm_text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit_dialog_layout);
        initView();
        initWindow();
    }

    private void initView() {
        cancel = findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(this);
        confirm = findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(this);
        tv_content = findViewById(R.id.content);
        iv_close=findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        if (!TextUtils.isEmpty(mContent)) {
            tv_content.setText(mContent);
        }
        if (!TextUtils.isEmpty(cancel_text)) {
            cancel.setText(cancel_text);
        }
        if (!TextUtils.isEmpty(confirm_text)) {
            confirm.setText(confirm_text);
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
            case R.id.btn_cancel:
                if (mButtonClick != null) {
                    mButtonClick.onCancelClick(this);
                }
                break;
            case R.id.btn_confirm:
                if (mButtonClick != null) {
                    mButtonClick.onConfirmClick(this);
                }
                break;
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }

    public static class Builder {
        private Context mContext;
        private String mContent;
        private String cancel_text;
        private String confirm_text;
        private onButtonClick mButtonClick;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setContent(String content) {
            this.mContent = content;
            return this;
        }

        public Builder setCancelText(String cancel_text) {
            this.cancel_text = cancel_text;
            return this;
        }

        public Builder setConfirmText(String confirm_text) {
            this.confirm_text = confirm_text;
            return this;
        }

        public Builder setButtonClick(onButtonClick buttonClick) {
            this.mButtonClick = buttonClick;
            return this;
        }

        public ExitDialog build() {
            return new ExitDialog(this);
        }

    }

    public interface onButtonClick {
        void onConfirmClick(Dialog dialog);

        void onCancelClick(Dialog dialog);
    }
}
