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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;

/**
 * author:zhaijinlu
 * date: 2020/2/14
 * desc: 金币加速卡收益卡弹框
 */
public class AcceleratorCardDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private onButtonClick mButtonClick;
    private String title_content;
    private String des_content;
    private String btn_text;
    private TextView des;
    private TextView btn_content;
    private ImageView iv_close,icon;
    private int number;
    private String time;


    public AcceleratorCardDialog(Builder builder) {
        super(builder.mContext);
        this.mContext=builder.mContext;
        this.mButtonClick = builder.mButtonClick;
        this.title_content=builder.title_content;
        this.des_content = builder.des_content;
        this.btn_text = builder.btn_text;
        this.number=builder.number;
        this.time=builder.time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceleratorcard_layout);
        initView();
        initWindow();
    }

    private void initView() {
        btn_content = findViewById(R.id.btn_content);
        btn_content.setOnClickListener(this);
        des = findViewById(R.id.des);
        iv_close=findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        icon=findViewById(R.id.gold_icon);
        Glide.with(mContext).load(R.mipmap.gold_gif).into(icon);
        if (!TextUtils.isEmpty(time)) {
            des.setText(Html.fromHtml(mContext.getResources().getString(R.string.string_coinsacce_content,String.valueOf(number),time)));
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
        private String time;
        private onButtonClick mButtonClick;

        public Builder(Context context) {
            this.mContext = context;
        }


        public Builder setTime(String time) {
            this.time = time;
            return this;
        }

        public Builder setTitleContent(String title_content) {
            this.title_content = title_content;
            return this;
        }

        public Builder setDesContent(String des_content) {
            this.des_content = des_content;
            return this;
        }

        public Builder setNumber(int number) {
            this.number = number;
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

        public AcceleratorCardDialog build() {
            return new AcceleratorCardDialog(this);
        }

    }

    public interface onButtonClick {
        void onConfirmClick(Dialog dialog);
    }
}
