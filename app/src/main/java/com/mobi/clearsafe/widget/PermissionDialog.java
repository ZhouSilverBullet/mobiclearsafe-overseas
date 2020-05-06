package com.mobi.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;


/**
 * 第一次进入APP时 拒绝了部分权限后 弹出的 弹窗提示
 */
public class PermissionDialog extends BaseDialog implements View.OnClickListener {

    private final Context mContext;
    private TextView tv_title, tv_content,tv_info;
    private Button btn_auto;
    private onButtonClick buttonClick;
    private String mTitle, mContent, mButtonText;


    public PermissionDialog(Builder builder) {
        super(builder.mContext);
        mContext = builder.mContext;
        this.buttonClick = builder.buttonClick;
        this.mTitle = builder.mTitle;
        this.mContent = builder.mContent;
        this.mButtonText = builder.mButtonText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_permission_layout);
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        tv_info=findViewById(R.id.tv_info);
        if (!TextUtils.isEmpty(mTitle)) {
            tv_title.setText(mTitle);
        }
        if (!TextUtils.isEmpty(mContent)) {
            tv_content.setText(mContent);
        }
        btn_auto = findViewById(R.id.btn_auto);
        if (!TextUtils.isEmpty(mButtonText)) {
            btn_auto.setText(mButtonText);
        }
        btn_auto.setOnClickListener(this);
        setCancelable(false);
        initWindow();
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
//        lps.width = (int) (w * 0.88);
//        lps.height = (int) (h * 0.757);
        lps.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lps);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_auto:
                if (buttonClick != null) {
                    buttonClick.onConfirmClick(PermissionDialog.this);
                }
                break;
        }
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }

    public static class Builder {
        private onButtonClick buttonClick;
        private Context mContext;
        private String mTitle;
        private String mContent;
        private String mButtonText;

        public Builder(Context context) {
            this.mContext = context;
        }


        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setContent(String content) {
            this.mContent = content;
            return this;
        }

        public Builder setButtonText(String buttonText) {
            this.mButtonText = buttonText;
            return this;
        }

        public Builder setOnButtonClick(onButtonClick button) {
            this.buttonClick = button;
            return this;
        }

        public PermissionDialog build() {
            return new PermissionDialog(this);
        }

    }

    public interface onButtonClick {
        void onConfirmClick(Dialog dialog);
    }

}
