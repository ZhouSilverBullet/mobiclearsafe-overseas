package com.mobi.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.nativeexpress.NativeExpressAd;
import com.example.adtest.nativeexpress.NativeLoadListener;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 签到弹窗
 * author : liangning
 * date : 2019-10-26  11:20
 */
public class SignDialog extends BaseDialog implements View.OnClickListener {
    private Context mContext;
    private TextView tv_content, tv_gold_money, tv_double, tv_time;
    private ImageView bg_light;
    private DialogClick dialogClick;
    private int mSignDay;// 已签到天数
    private int mGold;//本次签到获得金币数量
    private int mAllGold;//当前拥有的所以金币数量
    private float mMoney;//所有金币数相当于多少钱
    private boolean isVisible;
    private CircleAroundView fl_ad;
    private NativeExpressAd ad;
    private ScenarioEnum scenario;
    private String postID;
    private LoadingTips loadTips;
    private long countDownTime = 3000;
    private LinearLayout ll_close;
    private boolean isShowAd=true;//是否显示信息流广告
//    private Handler handler = new Handler();
//    private Runnable handlerRun = new Runnable() {
//        @Override
//        public void run() {
//            setViewStatusVisible();
//            countdown();
//        }
//    };
    public SignDialog(Builder builder) {
        super(builder.mContext);
        this.mSignDay = builder.mSignDay;
        this.mGold = builder.mGold;
        this.mAllGold = builder.mAllGold;
        this.mContext = builder.mContext;
        this.dialogClick = builder.mDialogClick;
        this.mMoney = builder.mMoney;
        this.isVisible = builder.isVisible;
        this.scenario = builder.scenario;
        this.postID=builder.postID;
        this.isShowAd=builder.isShowAd;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sign_layout);
        initWindow();
        initView();
        if (UserEntity.getInstance().getConfigEntity()!=null&&UserEntity.getInstance().getConfigEntity().getCount_down_time()>0){
            countDownTime = UserEntity.getInstance().getConfigEntity().getCount_down_time();
        }
//        handler.postDelayed(handlerRun,countDownTime);
    }

    @Override
    public void show() {
        super.show();
    }

    private void countdown(){
        Observable.intervalRange(0, 4, 0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return 4 - aLong - 1;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        tv_time.setText(String.valueOf(aLong));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        tv_time.setVisibility(View.GONE);
                        ll_close.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void initView() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        loadTips = findViewById(R.id.loadTips);
        tv_content = findViewById(R.id.tv_content);
        tv_gold_money = findViewById(R.id.tv_gold_money);
        ll_close = findViewById(R.id.ll_close);
        tv_double = findViewById(R.id.tv_double);
        tv_time = findViewById(R.id.tv_time);
        ll_close.setOnClickListener(this);
        tv_double.setOnClickListener(this);
        bg_light=findViewById(R.id.dialog_light);

        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_light);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        bg_light.startAnimation(operatingAnim);

        Animation scaleAnim = AnimationUtils.loadAnimation(mContext, R.anim.btn_scale);
        LinearInterpolator lins = new LinearInterpolator();
        scaleAnim.setInterpolator(lins);
        tv_double.startAnimation(scaleAnim);


        String content = mContext.getResources().getString(R.string.sign_dialog_content);
        String realStr = String.format(content, mSignDay, mGold);
        tv_content.setText(Html.fromHtml(realStr));

        String allMoney = mContext.getResources().getString(R.string.sign_dialog_allmoney);
        String realAllMoney = String.format(allMoney, mAllGold, String.valueOf(mMoney));
        tv_gold_money.setText(Html.fromHtml(realAllMoney));
        tv_double.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        fl_ad = findViewById(R.id.fl_ad);
//        setViewStatusGone();
        if(isShowAd){
            fl_ad.setVisibility(View.VISIBLE);
            ad = new NativeExpressAd.Builder(getContext())
                    .setAdCount(1)
                    .setADViewSize(335, 0)
                    .setHeightAuto(true)
                    .setSupportDeepLink(true)
                    .setBearingView(fl_ad)
                    .setScenario(scenario)
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
//                        if (handler!=null){
//                            handler.removeCallbacks(handlerRun);
//                        }
//                        setViewStatusVisible();
//                        countdown();
                        }
                    })
                    .build();
            countdown();
        }else {
            fl_ad.setVisibility(View.GONE);
            tv_time.setVisibility(View.GONE);
            ll_close.setVisibility(View.VISIBLE);
        }
    }

    private void initWindow() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.c_CC000000);
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
    public void setViewStatusGone(){
        loadTips.setLoadingTip(LoadingTips.LoadStatus.loading);
    }

    public void setViewStatusVisible(){
        loadTips.setLoadingTip(LoadingTips.LoadStatus.finish);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                if (dialogClick != null) {
                    dialogClick.closeBtnClick(SignDialog.this);
                }
                break;
            case R.id.tv_double:
                if (dialogClick != null) {
                    dialogClick.doubleBtnClick(SignDialog.this);
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
//        if (handler!=null){
//            handler.removeCallbacks(handlerRun);
//            handler=null;
//            handlerRun=null;
//        }
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }

    public static class Builder {
        private Context mContext;
        private int mSignDay;// 已签到天数
        private int mGold;//本次签到获得金币数量
        private int mAllGold;//当前拥有的所以金币数量
        private float mMoney;//所有金币数约等于多少钱
        private DialogClick mDialogClick;
        private boolean isVisible;
        private ScenarioEnum scenario;
        private String postID;
        private boolean isShowAd;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setSignDay(int signDay) {
            this.mSignDay = signDay;
            return this;
        }

        public Builder isVisible(boolean isVisible) {
            this.isVisible = isVisible;
            return this;
        }
        public Builder setIsShowAd(boolean isShowAd) {
            this.isShowAd = isShowAd;
            return this;
        }

        public Builder setPostID(String postID) {
            this.postID = postID;
            return this;
        }
        public Builder setGold(int gold) {
            this.mGold = gold;
            return this;
        }

        public Builder setAllGold(int allGold) {
            this.mAllGold = allGold;
            return this;
        }

        public Builder setDialogClick(DialogClick dialogClick) {
            this.mDialogClick = dialogClick;
            return this;
        }

        public Builder setMoney(float money) {
            this.mMoney = money;
            return this;
        }

        public Builder setScenario(ScenarioEnum scenario) {
            this.scenario = scenario;
            return this;
        }

        public SignDialog build() {
            return new SignDialog(this);
        }
    }

    public interface DialogClick {
        //金币翻倍按钮 点击事件
        void doubleBtnClick(Dialog dialog);

        //关闭按钮 点击事件
        void closeBtnClick(Dialog dialog);
    }
}
