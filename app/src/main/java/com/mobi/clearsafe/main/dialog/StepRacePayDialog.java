package com.mobi.clearsafe.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;
import com.mobi.clearsafe.widget.NoPaddingTextView;

/**
 * author : liangning
 * date : 2019-12-05  21:16
 */
public class StepRacePayDialog extends BaseDialog implements View.OnClickListener {
    private TextView tv_btn, tv_title;
    private Context mContext;
    private String mNper;
    private StepRacePayListener mListener;
    private NoPaddingTextView tv_gold;
    private String mGold;

    public StepRacePayDialog(@NonNull Builder builder) {
        super(builder.mContext, R.style.AllDialog);
        this.mContext = builder.mContext;
        this.mNper = builder.mNper;
        this.mListener = builder.mListener;
        this.mGold = builder.mGold;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_steprace_pay_layout);
        initWindow();
        initView();
    }

    private void initView() {
        tv_btn = findViewById(R.id.tv_btn);
        tv_btn.setOnClickListener(this);
        tv_title = findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(mNper)) {
            tv_title.setText(mContext.getResources().getString(R.string.steprace_pay_title, mNper));
        }
        tv_gold = findViewById(R.id.tv_gold);
        if (!TextUtils.isEmpty(mGold)){
            tv_gold.setText(mGold);
        }
    }

    private void initWindow() {
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels > dm.heightPixels ? dm.heightPixels
                : dm.widthPixels;
        lp.width = w;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_btn:
                if (mListener != null) {
                    mListener.payClick(StepRacePayDialog.this);
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
        private StepRacePayListener mListener;
        private String mGold;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setNper(String nper) {
            this.mNper = nper;
            return this;
        }

        public Builder setGold(String gold){
            this.mGold = gold;
            return this;
        }

        public Builder setListener(StepRacePayListener listener) {
            this.mListener = listener;
            return this;
        }

        public StepRacePayDialog build() {
            return new StepRacePayDialog(this);
        }
    }

    public interface StepRacePayListener {
        void payClick(Dialog dialog);
    }
}
