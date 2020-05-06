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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.nativeexpress.NativeExpressAd;
import com.example.adtest.nativeexpress.NativeLoadListener;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;
import com.mobi.overseas.clearsafe.utils.AppUtil;

/**
 * author:zhaijinlu
 * date: 2020/1/14
 * desc:
 */
public class SurpriseNewDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private int mGold;
    private String btn_text;//按钮文案
    private SurpriseDialogClick mClick;
    private TextView tv_title, tv_gold, tv_double, tv_agin, btn_continue,tv_continues;
    private ImageView bg_light,lable;
    private boolean isVisible;//控制翻倍按钮显示隐藏
    private boolean lableIsVisible=false;//lable显隐
    private boolean fanbei_title=false;//判断title是不是翻倍成功
    private boolean isShowAd;//判断是否显示信息流广告
    private CircleAroundView fl_ad;
    private NativeExpressAd ad;
    private boolean showAgain = false;//控制再来一次按钮是否显示
    private ScenarioEnum scenario;
    private String postID;
    private LinearLayout layout;


    public SurpriseNewDialog(Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mGold = builder.mGold;
        this.mClick = builder.mDialogClick;
        this.isVisible = builder.isVisible;
        this.showAgain = builder.showAgain;
        this.scenario = builder.scenario;
        this.btn_text=builder.btn_text;
        this.postID=builder.postID;
        this.lableIsVisible=builder.lableIsVisible;
        this.fanbei_title=builder.fanbei_title;
        this.isShowAd=builder.isShowAd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.surprise_newdialog_layout);
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
        layout=findViewById(R.id.layout);
        tv_double = findViewById(R.id.tv_double);
        tv_title = findViewById(R.id.tv_title);
        tv_gold = findViewById(R.id.tv_gold);
        tv_agin = findViewById(R.id.tv_agin);
        tv_agin.setVisibility(showAgain ? View.VISIBLE : View.GONE);
        tv_agin.setOnClickListener(this);
        tv_double.setOnClickListener(this);
        bg_light=findViewById(R.id.bg_light);
        lable=findViewById(R.id.lable);
        lable.setVisibility(lableIsVisible?View.VISIBLE:View.GONE);
        btn_continue=findViewById(R.id.tv_continue);
        tv_continues=findViewById(R.id.tv_continues);
        btn_continue.setOnClickListener(this);
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

        if(fanbei_title){
            tv_title.setText(mContext.getResources().getString(R.string.new_dialog_title2));
        }else {
            tv_title.setText(mContext.getResources().getString(R.string.new_surprisedialog_title));
        }
        tv_gold.setText(mGold+"");

        tv_double.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        btn_continue.setVisibility(isVisible?View.GONE:View.VISIBLE);
        tv_continues.setVisibility(isVisible ? View.VISIBLE : View.GONE);

        fl_ad = findViewById(R.id.fl_ad);
        if(isShowAd){
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layout.getLayoutParams();
            lp.topMargin = AppUtil.dpToPx(43);
            layout.setLayoutParams(lp);
            fl_ad.setVisibility(View.VISIBLE);
            ad = new NativeExpressAd.Builder(getContext())
                    .setAdCount(1)
                    .setADViewSize(300, 0)
                    .setHeightAuto(true)
                    .setSupportDeepLink(true)
                    .setScenario(scenario)
                    .setPosID(postID)
                    .setBearingView(fl_ad)
                    .setNativeLoadListener(new NativeLoadListener() {
                        @Override
                        public void onAdClick(String channel) {

                        }

                        @Override
                        public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                        }

                        @Override
                        public void onAdDismissed(String channel) {

                        }

                        @Override
                        public void onAdRenderSuccess(String channel) {

                        }

                        @Override
                        public void onAdShow(String channel) {

                        }
                    })
                    .build();
        }else {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layout.getLayoutParams();
            lp.topMargin = AppUtil.dpToPx(130);
            layout.setLayoutParams(lp);
            fl_ad.setVisibility(View.GONE);
        }

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
            case R.id.tv_continues:
            case R.id.tv_continue:
                if (mClick != null) {
                    mClick.closeBtnClick(SurpriseNewDialog.this);
                }
                break;
            case R.id.tv_double:
                if (mClick != null) {
                    mClick.doubleBtnClick(SurpriseNewDialog.this);
                }
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (ad != null) {
            ad.destory();
        }
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }

    public static class Builder {
        private Context mContext;
        private int mGold = 0;//本次获得金币数量
        private SurpriseDialogClick mDialogClick;
        private boolean isVisible;
        private boolean showAgain = false;//
        private ScenarioEnum scenario;
        private String btn_text;
        private String postID;
        private boolean lableIsVisible;
        private boolean fanbei_title;
        private boolean isShowAd;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setGold(int gold) {
            this.mGold = gold;
            return this;
        }


        public Builder setIsShowAd(boolean isShowAd){
            this.isShowAd=isShowAd;
            return this;
        }


        public Builder setFanbeiTitle(boolean fanbei_title){
            this.fanbei_title=fanbei_title;
            return this;
        }
        public Builder setPostID(String postID) {
            this.postID = postID;
            return this;
        }

        public Builder setlableIsVisible(boolean lableIsVisible) {
            this.lableIsVisible = lableIsVisible;
            return this;
        }

        public Builder setBtnText(String btnText) {
            this.btn_text = btnText;
            return this;
        }

        public Builder isVisible(boolean isVisible) {
            this.isVisible = isVisible;
            return this;
        }


        public Builder setDialogClick(SurpriseDialogClick click) {
            this.mDialogClick = click;
            return this;
        }

        public Builder setShowAgain(boolean showAgain) {
            this.showAgain = showAgain;
            return this;
        }

        public Builder setScenario(ScenarioEnum scenario) {
            this.scenario = scenario;
            return this;
        }

        public SurpriseNewDialog build() {
            return new SurpriseNewDialog(this);
        }
    }

    public interface SurpriseDialogClick {
        //金币翻倍按钮 点击事件
        void doubleBtnClick(Dialog dialog);

        //关闭按钮 点击事件
        void closeBtnClick(Dialog dialog);

    }

}

