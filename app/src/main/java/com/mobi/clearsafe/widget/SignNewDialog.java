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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;

/**
 * author:zhaijinlu
 * date: 2020/1/14
 * desc:
 */
public class SignNewDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private int mGold;
    private String btn_text;//按钮文案
    private DialogClick mClick;
    private TextView tv_title, tv_gold, tv_double,tv_continues;
    private ImageView bg_light,lable;
    private int mSignDay;//已签到天数


    public SignNewDialog(Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mGold = builder.mGold;
        this.mClick = builder.mDialogClick;
        this.btn_text=builder.btn_text;
        this.mSignDay=builder.mSignDay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_signdialog_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initView();
        initWindow();
    }

    @Override
    public void show() {
        super.show();

    }



    private void initView() {
        tv_double = findViewById(R.id.tv_double);
        tv_title = findViewById(R.id.tv_title);
        tv_gold = findViewById(R.id.tv_gold);
        tv_double.setOnClickListener(this);
        bg_light=findViewById(R.id.bg_light);
        lable=findViewById(R.id.lable);
        tv_continues=findViewById(R.id.tv_continues);
        findViewById(R.id.ivClose).setOnClickListener(this);
        tv_continues.setOnClickListener(this);

        if(!TextUtils.isEmpty(btn_text)){
            tv_double.setText(btn_text);
        }

        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_light);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        bg_light.startAnimation(operatingAnim);

        Animation scaleAnim = AnimationUtils.loadAnimation(mContext, R.anim.btn_scale);
        LinearInterpolator lins = new LinearInterpolator();
        scaleAnim.setInterpolator(lins);
        tv_double.startAnimation(scaleAnim);

        tv_title.setText( mContext.getResources().getString(R.string.sign_text,mSignDay+""));

        tv_gold.setText(mGold+"");

    }

    private void initWindow() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.c_D9000000);
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
            case R.id.ivClose:
            case R.id.tv_continues:
                if (mClick != null) {
                    mClick.closeBtnClick(SignNewDialog.this);
                }
                break;
            case R.id.tv_double:
                if (mClick != null) {
                    mClick.doubleBtnClick(SignNewDialog.this);
                }
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }

    public static class Builder {
        private Context mContext;
        private int mGold = 0;//本次获得金币数量
        private DialogClick mDialogClick;
        private String btn_text;
        private int mSignDay;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setGold(int gold) {
            this.mGold = gold;
            return this;
        }

        public Builder setmSignDay(int mSignDay) {
            this.mSignDay = mSignDay;
            return this;
        }


        public Builder setBtnText(String btnText) {
            this.btn_text = btnText;
            return this;
        }



        public Builder setDialogClick(DialogClick click) {
            this.mDialogClick = click;
            return this;
        }

        public SignNewDialog build() {
            return new SignNewDialog(this);
        }
    }

    public interface DialogClick {
        //金币翻倍按钮 点击事件
        void doubleBtnClick(Dialog dialog);

        //关闭按钮 点击事件
        void closeBtnClick(Dialog dialog);

    }

}
