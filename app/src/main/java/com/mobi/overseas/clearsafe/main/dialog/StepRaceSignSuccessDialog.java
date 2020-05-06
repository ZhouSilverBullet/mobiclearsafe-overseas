package com.mobi.overseas.clearsafe.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;
import com.mobi.overseas.clearsafe.utils.DateUtils;

/**
 * author : liangning
 * date : 2019-12-05  20:01
 */
public class StepRaceSignSuccessDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private String mNper;
    private String mGold;
    private StepRaceSignSuccessListener mListener;
    private TextView tv_title, tv_btn;
    private long create_time;//报名时间
    private long opening_time;//开赛时间
    private long lottery_time;//开奖时间
    private TextView tv_today,tv_beginrace,tv_endrace,tv_kaijiang;

    public StepRaceSignSuccessDialog(@NonNull Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mNper = builder.mNper;
        this.mGold = builder.mGold;
        this.mListener = builder.mListener;
        this.create_time = builder.create_time;
        this.opening_time = builder.opening_time;
        this.lottery_time = builder.lottery_time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_steprace_signsuccess_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initWindow();
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(mContext.getResources().getString(R.string.steprace_signsuccess_title, mNper));
        tv_btn = findViewById(R.id.tv_btn);
       // tv_btn.setText(mContext.getResources().getString(R.string.signsuccess_btn, mGold));
        tv_btn.setOnClickListener(this);
        tv_today = findViewById(R.id.tv_today);
        tv_today.setText(mContext.getResources().getString(R.string.signsuccess_today, DateUtils.longToTimeStr(create_time,"MM-dd")));
        tv_beginrace = findViewById(R.id.tv_beginrace);
        tv_beginrace.setText(mContext.getResources().getString(R.string.success_racebegin_time,DateUtils.longToTimeStr(opening_time,"MM-dd")));
        tv_endrace = findViewById(R.id.tv_endrace);
        tv_endrace.setText(mContext.getResources().getString(R.string.success_endrace_time,DateUtils.longToTimeStr(opening_time,"MM-dd")));
        tv_kaijiang = findViewById(R.id.tv_kaijiang);
        tv_kaijiang.setText(mContext.getResources().getString(R.string.success_kaijiang_time,DateUtils.longToTimeStr(lottery_time,"MM-dd")));
    }

    private void initWindow() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.c_7D000000);
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
            case R.id.tv_btn:
                if (mListener != null) {
                    mListener.btnClick(StepRaceSignSuccessDialog.this);
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
        private String mNper;
        private String mGold;
        private StepRaceSignSuccessListener mListener;
        private long create_time;//报名时间
        private long opening_time;//开赛时间
        private long lottery_time;//开奖时间

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setNper(String nper) {
            this.mNper = nper;
            return this;
        }

        public Builder setGold(String gold) {
            this.mGold = gold;
            return this;
        }
        public Builder setCreatTimt(long create_time){
            this.create_time = create_time;
            return this;
        }
        public Builder setOpeningTime(long opening_time){
            this.opening_time = opening_time;
            return this;
        }
        public Builder setLottertTime(long lottery_time){
            this.lottery_time = lottery_time;
            return this;
        }

        public Builder setListener(StepRaceSignSuccessListener listener) {
            this.mListener = listener;
            return this;
        }

        public StepRaceSignSuccessDialog build() {
            return new StepRaceSignSuccessDialog(this);
        }
    }

    public interface StepRaceSignSuccessListener {
        void btnClick(Dialog dialog);
    }

}
