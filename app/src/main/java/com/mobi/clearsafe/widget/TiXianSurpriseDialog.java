package com.mobi.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.main.activity.InviteActivity;
import com.mobi.clearsafe.moneyactivity.ShareActivity;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;

/**
 * author : liangning
 * date : 2020-02-13  15:47
 */
public class TiXianSurpriseDialog extends BaseDialog implements View.OnClickListener {

    private TextView red_title;
    private Context mContext;
    private int reward_points;
    private float reward_cash;
    private ImageView iv_open;
    private TiXianDialogListener mListener;
    private String tips;

    public TiXianSurpriseDialog( Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.reward_points = builder.reward_points;
        this.reward_cash = builder.reward_cash;
        this.mListener = builder.mListener;
        this.tips=builder.tips;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_red_pack);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initWindow();
        initView();
    }

    private void initView() {
        red_title = findViewById(R.id.red_title);
        red_title.setText(mContext.getResources().getString(R.string.surprised_red));
        iv_open = findViewById(R.id.open_red);
        iv_open.setOnClickListener(this);
    }

    private void initWindow() {
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
            case R.id.open_red:
                if (mListener != null) {
                    mListener.openred(TiXianSurpriseDialog.this);
                } else {
                    dismiss();
                    GetRedDialog dialog = new GetRedDialog.Builder(mContext)
                            .setCash(reward_cash)
                            .setType(2)
                            .setTips(tips)
                            .setDialogListener(new GetRedDialog.GetRedDialogListener() {
                                @Override
                                public void closeClick(Dialog dialog) {
                                    dialog.dismiss();
                                   mContext.startActivity(new Intent(mContext, ShareActivity.class));
                                }
                            })
                            .build();
                    dialog.show();
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
        private int reward_points;
        private float reward_cash;
        private TiXianDialogListener mListener;
        private String tips;


        public Builder setTips(String tips) {
            this.tips = tips;
            return  this;
        }

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setRewardPoint(int reward_points) {
            this.reward_points = reward_points;
            return this;
        }

        public Builder setRewardCash(float reward_cash) {
            this.reward_cash = reward_cash;
            return this;
        }

        public Builder setDialogListener(TiXianDialogListener listener) {
            this.mListener = listener;
            return this;
        }

        public TiXianSurpriseDialog build() {
            return new TiXianSurpriseDialog(this);
        }
    }

    public interface TiXianDialogListener {
        void openred(Dialog dialog);
    }
}
