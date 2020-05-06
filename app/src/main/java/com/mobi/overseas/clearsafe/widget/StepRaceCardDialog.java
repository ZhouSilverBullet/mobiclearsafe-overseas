package com.mobi.overseas.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;

/**
 * author : liangning
 * date : 2020-01-16  15:56
 */
public class StepRaceCardDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private TextView tv_content, tv_title;
    private LinearLayout ll_close;
    private String mTitle;
    private String mContent;

    public StepRaceCardDialog(@NonNull Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mTitle = builder.mTitle;
        this.mContent = builder.mContent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_stepracecard_layout);
        initWindow();
        initView();
    }

    private void initView() {
        tv_content = findViewById(R.id.tv_content);
        ll_close = findViewById(R.id.ll_close);
        ll_close.setOnClickListener(this);
        tv_title = findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(mTitle)) {
            tv_title.setText(mTitle);
        }
        if (!TextUtils.isEmpty(mContent)) {
            tv_content.setText(Html.fromHtml(mContent));
        }
    }

    private void initWindow() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialogWindow.setBackgroundDrawableResource(R.color.c_D9000000);
        WindowManager.LayoutParams lps = dialogWindow.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels > dm.heightPixels ? dm.heightPixels
                : dm.widthPixels;
        int h = dm.widthPixels > dm.heightPixels ? dm.widthPixels
                : dm.heightPixels;
//        lps.width = w;
        lps.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lps.height = h;
        lps.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lps);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
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
        private String mTitle;
        private String mContent;

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

        public StepRaceCardDialog build() {
            return new StepRaceCardDialog(this);
        }
    }
}
