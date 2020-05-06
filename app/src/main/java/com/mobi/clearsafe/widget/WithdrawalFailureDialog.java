package com.mobi.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.support.annotation.NonNull;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;

/**
 * 提现失败弹窗
 * author : liangning
 * date : 2019-12-19  18:39
 */
public class WithdrawalFailureDialog extends BaseDialog implements View.OnClickListener {
    private WithFailureListener mListener;
    private String mContent;
    private Context mContext;
    private TextView tv_content;
    private LinearLayout ll_close;
    private TextView btn_invite;

    public WithdrawalFailureDialog(@NonNull Builder builder) {
        super(builder.mContext);
        this.mListener = builder.mListener;
        this.mContent = builder.mContent;
        this.mContext = builder.mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_withdrawal_failure_layout);
        initWindow();
        initView();
    }

    private void initView() {
        tv_content = findViewById(R.id.tv_content);
        ll_close = findViewById(R.id.ll_close);
        ll_close.setOnClickListener(this);
        if (!TextUtils.isEmpty(mContent)) {
            tv_content.setText(Html.fromHtml(mContent));
        }
        btn_invite=findViewById(R.id.btn_invite);
        btn_invite.setOnClickListener(this);
    }

    private void initWindow() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.c_7D000000);
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
            case R.id.ll_close:
                if (mListener != null) {
                    mListener.CloseListener(WithdrawalFailureDialog.this);
                }
                break;
            case R.id.btn_invite:
                if (mListener != null) {
                    mListener.inviteBtn(WithdrawalFailureDialog.this);
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
        private String mContent;
        private WithFailureListener mListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setContent(String content) {
            this.mContent = content;
            return this;
        }

        public Builder setListener(WithFailureListener listener) {
            this.mListener = listener;
            return this;
        }
        public WithdrawalFailureDialog build(){
            return new WithdrawalFailureDialog(this);
        }
    }

    public interface WithFailureListener {
        void CloseListener(Dialog dialog);
        void inviteBtn(Dialog dialog);
    }

}
