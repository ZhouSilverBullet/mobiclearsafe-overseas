package com.mobi.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.main.bean.RedBean;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * author:zhaijinlu
 * date: 2019/12/19
 * desc:
 */
public class RedDayPacketDialog extends BaseDialog implements View.OnClickListener {


    private Context mContext;
    private ImageView jump_app;

    private RedBean bean;
    private TextView tv_gold;
    private TextView tv_rmb;
    private TextView first_day;
    private TextView second_day;
    private TextView tv_tip;
    private TextView jump_gray;
    private LinearLayout layout_second;



    public RedDayPacketDialog(Builder builder) {
        super(builder.mContext);
        this.bean=builder.bean;
        this.mContext=builder.mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.red_day_packet_layout);
        initView();
        initWindow();
    }

    private void initView() {
        jump_app=findViewById(R.id.jump_app);
        jump_app.setOnClickListener(this);
        tv_gold=findViewById(R.id.tv_gold);
        tv_rmb=findViewById(R.id.tv_rmb);
        first_day=findViewById(R.id.first_day);

        second_day=findViewById(R.id.second_day);
        tv_tip=findViewById(R.id.tv_tip);
        jump_gray=findViewById(R.id.jump_gray);
        layout_second=findViewById(R.id.layout_second);
        Animation scaleAnim = AnimationUtils.loadAnimation(mContext, R.anim.btn_scale);
        LinearInterpolator lins = new LinearInterpolator();
        scaleAnim.setInterpolator(lins);
        layout_second.startAnimation(scaleAnim);

        tv_gold.setText(mContext.getResources().getString(R.string.red_gold, bean.getToday_reward().getPoints()+""));
        tv_rmb.setText(mContext.getResources().getString(R.string.red_rmb,bean.getToday_reward().getReward_cash()+""));
        tv_tip.setText(mContext.getResources().getString(R.string.tomorrow_text,bean.getNext_day_reward().getPoints()+""));
        first_day.setText(mContext.getResources().getString(R.string.tv_rmb,bean.getToday_reward().getReward_cash()+""));
        second_day.setText(mContext.getResources().getString(R.string.tv_rmb,bean.getNext_day_reward().getReward_cash()+""));
        countdown();

    }

    private void countdown() {
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
                        jump_gray.setText(mContext.getResources().getString(R.string.into_app)+"("+aLong+")");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        jump_gray.setVisibility(View.GONE);
                        jump_app.setVisibility(View.VISIBLE);
                    }
                });
    }
    private void initWindow(){
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
            case R.id.jump_app:
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
        private RedBean bean;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setCash(RedBean bean) {
            this.bean = bean;
            return this;
        }


        public RedDayPacketDialog build() {
            return new RedDayPacketDialog(this);
        }

    }

}
