package com.mobi.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.text.Html;
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
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.nativeexpress.NativeExpressAd;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 达到步数上限的弹窗
 * author : liangning
 * date : 2019-10-26  17:44
 */
public class StepCeilingDialog extends BaseDialog implements View.OnClickListener {


    private Context mContext;
    private TextView tv_content;
    private TextView tv_double, tv_gold_money,tv_time,tv_title;
    private LinearLayout ll_close;
    private StepDialogClick mClick;
    private String title;
    private String mContent;
    private String mButtonText;
    private int mAllGold = 0;
    private float mMoney;//所有金币数约等于多少钱
    private FrameLayout fl_ad;
    private NativeExpressAd ad;
    private ScenarioEnum scenario;
    private ConstraintLayout cl_content;
    private LoadingTips loadTips;
    private LinearLayout ll_content;
    private long countDownTime = 3000;
    private Handler handler = new Handler();
    private Runnable handlerRun = new Runnable() {
        @Override
        public void run() {
         //   setViewStatusVisible();
            countdown();
        }
    };
    public StepCeilingDialog(Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mClick = builder.mClick;
        this.title=builder.title;
        this.mContent = builder.mContent;
        this.mButtonText = builder.mButtonText;
        this.mAllGold = builder.mAllGold;
        this.mMoney = builder.mMoney;
        this.scenario = builder.scenario;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_stepceiling_layout);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initWindow();
        initView();
//        if (UserEntity.getInstance().getConfigEntity()!=null&&UserEntity.getInstance().getConfigEntity().getCount_down_time()>0){
//            countDownTime = UserEntity.getInstance().getConfigEntity().getCount_down_time();
//        }
     //   handler.postDelayed(handlerRun,countDownTime);
     //   countdown();
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
                        return 4 - aLong-1;
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
        ll_content = findViewById(R.id.ll_content);
        cl_content = findViewById(R.id.cl_content);
        tv_content = findViewById(R.id.tv_content);
        loadTips = findViewById(R.id.loadTips);
        tv_double = findViewById(R.id.tv_double);
        tv_gold_money = findViewById(R.id.tv_gold_money);
        ll_close = findViewById(R.id.ll_close);
        ll_close.setOnClickListener(this);
        tv_double.setOnClickListener(this);
        tv_time = findViewById(R.id.tv_time);
        tv_title=findViewById(R.id.tv_title);
        tv_time.setVisibility(View.GONE);
        ll_close.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(mContent)) {
            tv_content.setText(mContent);
        }
        if (!TextUtils.isEmpty(mButtonText)) {
            tv_double.setText(mButtonText);
        }
        Animation scaleAnim = AnimationUtils.loadAnimation(mContext, R.anim.btn_scale);
        LinearInterpolator lins = new LinearInterpolator();
        scaleAnim.setInterpolator(lins);
        tv_double.startAnimation(scaleAnim);
        String allMoney = mContext.getResources().getString(R.string.sign_dialog_allmoney);
        String realAllMoney = String.format(allMoney, mAllGold, String.valueOf(mMoney));
        tv_gold_money.setText(Html.fromHtml(realAllMoney));
//        fl_ad = findViewById(R.id.fl_ad);
//        setViewStatusGone();
//        ad =new NativeExpressAd.Builder(mContext)
//                .setAdCount(1)
//                .setADViewSize(305, 0)
//                .setHeightAuto(true)
//                .setSupportDeepLink(true)
//                .setBearingView(fl_ad)
//                .setScenario(scenario)
//                .setNativeLoadListener(new NativeLoadListener() {
//                    @Override
//                    public void onAdClick(String channel) {
//
//                    }
//
//                    @Override
//                    public void onLoadFaild(String channel, int faildCode, String faildMsg) {
//
//                    }
//
//                    @Override
//                    public void onAdDismissed(String channel) {
//
//                    }
//
//                    @Override
//                    public void onAdRenderSuccess(String channel) {
//
//                    }
//
//                    @Override
//                    public void onAdShow(String channel) {
//                        if (handler!=null){
//                            handler.removeCallbacks(handlerRun);
//                        }
//                        setViewStatusVisible();
//                        countdown();
//                    }
//                })
//                .build();
    }

    public void setViewStatusGone(){
        loadTips.setLoadingTip(LoadingTips.LoadStatus.loading);
    }

    public void setViewStatusVisible(){
        loadTips.setLoadingTip(LoadingTips.LoadStatus.finish);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_close:
                if (mClick != null) {
                    mClick.closeBtnClick(StepCeilingDialog.this);
                }
                break;
            case R.id.tv_double:
                if (mClick != null) {
                    mClick.moreBtnClick(StepCeilingDialog.this);
                }
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (ad!=null){
            ad.destory();
        }
        if (handler!=null){
            handler.removeCallbacks(handlerRun);
            handler=null;
            handlerRun=null;
        }
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }

    public static class Builder {

        private Context mContext;
        private String title;
        private String mContent;
        private String mButtonText;
        private int mAllGold = 0;
        private float mMoney = 0.0f;//所有金币说相当于多少钱
        private StepDialogClick mClick;
        private ScenarioEnum scenario;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setContent(String content) {
            this.mContent = content;
            return this;
        }

        public Builder setButtonText(String buttonText) {
            this.mButtonText = buttonText;
            return this;
        }
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }


        public Builder setAllGold(int allGold) {
            this.mAllGold = allGold;
            return this;
        }
        public Builder setMoney(float money){
            this.mMoney = money;
            return this;
        }

        public Builder setDialogClick(StepDialogClick click) {
            this.mClick = click;
            return this;
        }

        public Builder setScenario(ScenarioEnum scenario){
            this.scenario = scenario;
            return this;
        }

        public StepCeilingDialog build() {
            return new StepCeilingDialog(this);
        }

    }

    public interface StepDialogClick {
        //金币翻倍按钮 点击事件
        void moreBtnClick(Dialog dialog);

        //关闭按钮 点击事件
        void closeBtnClick(Dialog dialog);
    }
}
