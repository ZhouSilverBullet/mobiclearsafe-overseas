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
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;

/**
 * 步数挑战赛未达标弹窗
 * author : liangning
 * date : 2019-12-05  17:20
 */
public class StepFailureDialog extends BaseDialog implements View.OnClickListener {
    private Context mContext;
    private String mNper;
    private StepFailureListener mListener;
    private TextView tv_title;
    private TextView tv_signNext;
    private LinearLayout ll_close;


    public StepFailureDialog(@NonNull Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mNper = builder.mNper;
        this.mListener = builder.mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_stepfailure_layout);
        initWindow();
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(mContext.getResources().getString(R.string.stepfailure,mNper));
        tv_signNext = findViewById(R.id.tv_signNext);
        tv_signNext.setOnClickListener(this);
        ll_close = findViewById(R.id.ll_close);
        ll_close.setOnClickListener(this);
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
            case R.id.tv_signNext:
                if (mListener != null) {
                    mListener.signNextClick(StepFailureDialog.this);
                }
                break;
            case R.id.ll_close:
                if (mListener != null) {
                    mListener.closeClick(StepFailureDialog.this);
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
        private StepFailureListener mListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setNper(String nper) {
            this.mNper = nper;
            return this;
        }

        public Builder setListener(StepFailureListener listener) {
            this.mListener = listener;
            return this;
        }

        public StepFailureDialog build() {
            return new StepFailureDialog(this);
        }


    }

    public interface StepFailureListener {
        void closeClick(Dialog dialog);

        void signNextClick(Dialog dialog);
    }
}
