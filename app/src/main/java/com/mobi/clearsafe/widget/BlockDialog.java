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
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;

/**
 * author:zhaijinlu
 * date: 2020/2/21
 * desc: 提现页冻结金额弹框
 */
public class BlockDialog  extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private onButtonClick mButtonClick;
    private ImageView iv_close;
    private TextView tv_cash;
    private TextView tv_btn;
    private float cash;


    public BlockDialog(Builder builder) {
        super(builder.mContext);
        this.mContext=builder.mContext;
        this.mButtonClick = builder.mButtonClick;
        this.cash=builder.cash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_dialog_layout);
        initView();
        initWindow();
    }

    private void initView() {
        iv_close=findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        tv_cash=findViewById(R.id.tv_cash);
        tv_btn=findViewById(R.id.tv_btn);
        tv_btn.setOnClickListener(this);
        tv_cash.setText(Html.fromHtml(mContext.getResources().getString(R.string.string_block,String.valueOf(cash))));
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
            case R.id.iv_close:
                if (mButtonClick != null) {
                    mButtonClick.onMissClick(this);
                }
                break;
            case R.id.tv_btn:
                if(mButtonClick!=null){
                    mButtonClick.onConfirmClick(this);
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

        private onButtonClick mButtonClick;
        private float cash;

        public Builder setCash(float cash) {
            this.cash = cash;
            return this;
        }

        public Builder(Context context) {
            this.mContext = context;
        }


        public Builder setButtonClick(onButtonClick buttonClick) {
            this.mButtonClick = buttonClick;
            return this;
        }

        public BlockDialog build() {
            return new BlockDialog(this);
        }

    }

    public interface onButtonClick {
        void onConfirmClick(Dialog dialog);
        void onMissClick(Dialog dialog);

    }
}

