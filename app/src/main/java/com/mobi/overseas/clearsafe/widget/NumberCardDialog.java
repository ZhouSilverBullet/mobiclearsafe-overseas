package com.mobi.overseas.clearsafe.widget;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;

/**
 * author:zhaijinlu
 * date: 2020/2/9
 * desc: 获得数字弹框
 */
public class NumberCardDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private onButtonClick mButtonClick;
    private String title_content;
    private String des_content;
    private String btn_text;
    private String needNumStr;
    private boolean isComplete;
    private TextView des;
    private TextView title;
    private TextView btn_content, tv_number;
    private ImageView iv_close;
    private TextView tv_needNum;
    private TextView tv_look;
    private int number;


    public NumberCardDialog(Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mButtonClick = builder.mButtonClick;
        this.title_content = builder.title_content;
        this.des_content = builder.des_content;
        this.btn_text = builder.btn_text;
        this.number = builder.number;
        this.isComplete = builder.isComplete;
        this.needNumStr = builder.needNumStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numcard_dialog_layout);
        initView();
        initWindow();
    }

    private void initView() {
        title = findViewById(R.id.title);
        btn_content = findViewById(R.id.btn_content);
        btn_content.setOnClickListener(this);
        des = findViewById(R.id.des);
        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        tv_number = findViewById(R.id.tv_number);
        tv_needNum = findViewById(R.id.tv_needNum);
        tv_look = findViewById(R.id.tv_look);
        tv_look.setText(Html.fromHtml(mContext.getResources().getString(R.string.numberCard_look)));
        if (isComplete) {//集齐
            tv_needNum.setTextColor(mContext.getResources().getColor(R.color.c_FF3737));
        } else {//未集齐
            tv_needNum.setTextColor(mContext.getResources().getColor(R.color.black_33));
        }
        if (!TextUtils.isEmpty(needNumStr)) {
            tv_needNum.setText(needNumStr);
        }
        if (!TextUtils.isEmpty(title_content)) {
            title.setText(title_content);
        }
        if (!TextUtils.isEmpty(des_content)) {
            des.setText(des_content);
        }
        if (!TextUtils.isEmpty(btn_text)) {
            btn_content.setText(btn_text);
        }
        tv_number.setText(number + "");
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
            case R.id.btn_content:
                if (mButtonClick != null) {
                    mButtonClick.onConfirmClick(this);
                }
                break;
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
        private String title_content;
        private String des_content;
        private String btn_text;
        private int number;
        private String needNumStr = "";
        private boolean isComplete;
        private onButtonClick mButtonClick;

        public Builder(Context context) {
            this.mContext = context;
        }


        public Builder setTitleContent(String title_content) {
            this.title_content = title_content;
            return this;
        }

        public Builder setDesContent(String des_content) {
            this.des_content = des_content;
            return this;
        }

        public Builder setNeedNumStr(String neednum) {
            this.needNumStr = neednum;
            return this;
        }

        public Builder setNumber(int number) {
            this.number = number;
            return this;
        }

        public Builder setIsComplete(boolean isComplete) {
            this.isComplete = isComplete;
            return this;
        }

        public Builder setBtnText(String btn_text) {
            this.btn_text = btn_text;
            return this;
        }

        public Builder setButtonClick(onButtonClick buttonClick) {
            this.mButtonClick = buttonClick;
            return this;
        }

        public NumberCardDialog build() {
            return new NumberCardDialog(this);
        }

    }

    public interface onButtonClick {
        void onConfirmClick(Dialog dialog);
    }
}
