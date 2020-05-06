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
import com.mobi.overseas.clearsafe.utils.imageloader.ImageLoader;

/**
 * author:zhaijinlu
 * date: 2020/2/6
 * desc: 领取收益卡弹框
 */
public class CardDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private onButtonClick mButtonClick;
    private String title_content;
    private String des_content;
    private String btn_text;
    private TextView des;
    private TextView title;
    private TextView btn_content;
    private ImageView iv_close,iamge;
    private String imageUrl;
    private int type;//100 翻倍卡  101 步数 102收益


    public CardDialog(Builder builder) {
        super(builder.mContext);
        this.mContext=builder.mContext;
        this.mButtonClick = builder.mButtonClick;
        this.title_content=builder.title_content;
        this.des_content = builder.des_content;
        this.btn_text = builder.btn_text;
        this.type=builder.type;
        this.imageUrl=builder.imageUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_dialog_layout);
        initView();
        initWindow();
    }

    private void initView() {
        title=findViewById(R.id.title);
        btn_content = findViewById(R.id.btn_content);
        btn_content.setOnClickListener(this);
        des = findViewById(R.id.des);
        iv_close=findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        iamge=findViewById(R.id.iv_image);
        if(!TextUtils.isEmpty(title_content)){
            title.setText(title_content);
        }
        if(type==101){
            des.setTextColor(mContext.getResources().getColor(R.color.black_33));
            des.setText(Html.fromHtml(mContext.getResources().getString(R.string.string_stepcard_des)));
        }else {
            des.setTextColor(mContext.getResources().getColor(R.color.c_FF3737));
            des.setText(des_content);
        }

        if (!TextUtils.isEmpty(btn_text)) {
            btn_content.setText(btn_text);
        }
        if(!TextUtils.isEmpty(imageUrl)){
            ImageLoader.loadImage(mContext,iamge,imageUrl);
        }else {
            if(type==101){
                iamge.setImageResource(R.mipmap.step_card);
            }else if(type==102){
                iamge.setImageResource(R.mipmap.coin_card);
            }
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
        private int type;
        private String imageUrl;
        private onButtonClick mButtonClick;

        public Builder(Context context) {
            this.mContext = context;
        }


        public Builder setTitleContent(String title_content) {
            this.title_content = title_content;
            return this;
        }
        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder setDesContent(String des_content) {
            this.des_content = des_content;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
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

        public CardDialog build() {
            return new CardDialog(this);
        }

    }

    public interface onButtonClick {
        void onConfirmClick(Dialog dialog);
    }
}
