package com.mobi.overseas.clearsafe.main.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;

/**
 * 步数挑战赛 达标奖励弹窗
 * author : liangning
 * date : 2019-12-05  14:34
 */
public class StepRewardDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private String mNper;
    private String mGold;
    private StepRewardListener mListener;
    private TextView tv_title;
    private TextView tv_gold;
    private TextView tv_Signnext;
    private LinearLayout ll_close;

    public StepRewardDialog(@NonNull Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mNper = builder.mNper;
        this.mGold = builder.mGold;
        this.mListener = builder.mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_stepreward_layout);
        initWindow();
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_gold = findViewById(R.id.tv_gold);
        tv_Signnext = findViewById(R.id.tv_Signnext);
        tv_Signnext.setOnClickListener(this);
        ll_close = findViewById(R.id.ll_close);
        ll_close.setOnClickListener(this);
        if (!TextUtils.isEmpty(mNper)) {
            tv_title.setText(mContext.getResources().getString(R.string.stepreward, mNper));
        }
        if (!TextUtils.isEmpty(mGold)) {
            tv_gold.setText(mContext.getResources().getString(R.string.steprace_gold, mGold));
        }
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
            case R.id.tv_Signnext:
                if (mListener != null) {
                    mListener.btnClick(StepRewardDialog.this);
                }
                break;
            case R.id.ll_close:
                if (mListener != null) {
                    mListener.closeClick(StepRewardDialog.this);
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
        private StepRewardListener mListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setNper(String nper) {
            this.mNper = nper;
            return this;
        }

        public Builder setGoldString(String gold) {
            this.mGold = gold;
            return this;
        }

        public Builder setListener(StepRewardListener listener) {
            this.mListener = listener;
            return this;
        }

        public StepRewardDialog build() {
            return new StepRewardDialog(this);
        }

    }

    public interface StepRewardListener {
        void btnClick(Dialog dialog);

        void closeClick(Dialog dialog);
    }
}
