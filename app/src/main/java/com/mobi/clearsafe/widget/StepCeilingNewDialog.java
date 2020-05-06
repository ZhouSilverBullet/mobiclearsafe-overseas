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
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * author:zhaijinlu
 * date: 2020/1/8
 * desc:
 */
public class StepCeilingNewDialog extends BaseDialog implements View.OnClickListener {


    private Context mContext;
    private TextView tv_content;
    private TextView more_task,tv_title;
    private LinearLayout tv_double;
    private ImageView iv_close;
    private StepDialogNewClick mClick;
    private String title;
    private String mContent;
    private String btn_content;
    private int type;


    private long countDownTime = 3000;


    public StepCeilingNewDialog(Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mClick = builder.mClick;
        this.title=builder.title;
        this.mContent = builder.mContent;
        this.type=builder.type;
        this.btn_content=builder.btn_content;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stepceiling_newdialog_layout);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initWindow();
        initView();

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
                       // tv_time.setText(String.valueOf(aLong));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
//                        tv_time.setVisibility(View.GONE);
//                        ll_close.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        tv_double = findViewById(R.id.tv_double);
        more_task=findViewById(R.id.more_task);
        iv_close=findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);

        tv_double.setOnClickListener(this);
        more_task.setOnClickListener(this);
        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(mContent)) {
            tv_content.setText(mContent);
        }


        if(type==1){
            tv_double.setVisibility(View.VISIBLE);
            more_task.setVisibility(View.GONE);
        }else if(type==2||type==3||type==5){
            tv_double.setVisibility(View.GONE);
            more_task.setVisibility(View.VISIBLE);
        }else if(type==4){//砸金蛋提示
            tv_double.setVisibility(View.VISIBLE);
            more_task.setVisibility(View.GONE);
        }


        Animation scaleAnim = AnimationUtils.loadAnimation(mContext, R.anim.btn_scale);
        LinearInterpolator lins = new LinearInterpolator();
        scaleAnim.setInterpolator(lins);
        tv_double.startAnimation(scaleAnim);
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
            case R.id.iv_close:
                if (mClick != null) {
                    mClick.closeBtnClick(StepCeilingNewDialog.this);
                }
                break;
            case R.id.tv_double:
                if (mClick != null) {
                    mClick.watchVideo(StepCeilingNewDialog.this);
                }
                break;
            case R.id.more_task:
                if (mClick != null) {
                    mClick.moreBtnClick(StepCeilingNewDialog.this);
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
        private String title;
        private String mContent;
        private StepDialogNewClick mClick;
        private int type;
        private String btn_content;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setContent(String content) {
            this.mContent = content;
            return this;
        }

        public Builder setBtn_content(String btn_content) {
            this.btn_content = btn_content;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setType(int type) {
            this.type = type;
            return this;
        }




        public Builder setDialogClick(StepDialogNewClick click) {
            this.mClick = click;
            return this;
        }


        public StepCeilingNewDialog build() {
            return new StepCeilingNewDialog(this);
        }

    }

    public interface StepDialogNewClick {
        //金币翻倍按钮 点击事件
        void moreBtnClick(Dialog dialog);

        //金币翻倍按钮 点击事件
        void watchVideo(Dialog dialog);

        //关闭按钮 点击事件
        void closeBtnClick(Dialog dialog);
    }
}
