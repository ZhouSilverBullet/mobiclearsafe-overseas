package com.mobi.overseas.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;

/**
 * author:zhaijinlu
 * date: 2020/2/21
 * desc: 邀请提现弹框
 */
public class InviteDialog extends BaseDialog implements View.OnClickListener {

    private Context mContent;
    private ImageView iv_close,invite_btn;
    private TextView get_money,tv_status;
    private ClickListener listener;
    private ProgressBar progress_bar;
    private TextView tv_conins;
    private float cash;
    private int coins;



    public InviteDialog(Builder builder) {
        super(builder.mContent);
        this.mContent = builder.mContent;
        this.listener=builder.listener;
        this.cash=builder.cash;
        this.coins=builder.coins;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_dialog_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initWindow();
        initView();
    }




    private void initView() {
        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        get_money=findViewById(R.id.get_money);
        tv_status=findViewById(R.id.tv_status);
        invite_btn=findViewById(R.id.invite_btn);
        invite_btn.setOnClickListener(this);
        progress_bar=findViewById(R.id.progress_bar);
        tv_conins=findViewById(R.id.tv_conins);
        get_money.setText(String.valueOf(cash));
        tv_conins.setText(mContent.getResources().getString(R.string.string_manager_tip6,String.valueOf(10000-coins)));
        progress_bar.setProgress(coins);
    }


    private void initWindow() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.invite_btn:
                if(listener!=null){
                    listener.onBtnClick(this);
                }
                break;
        }
    }

    @Override
    protected Context getActivityContext() {
        return mContent;
    }

    public static class Builder {

        private Context mContent;
        private ClickListener listener;
        private float cash;
        private int coins;

        public Builder(Context context) {
            this.mContent = context;
        }

        public Builder setCash(float cash) {
            this.cash = cash;
            return this;
        }

        public Builder setCoins(int coins) {
            this.coins = coins;
            return this;
        }

        public Builder setListener(ClickListener listener) {
            this.listener = listener;
            return this;
        }

        public InviteDialog build() {
            return new InviteDialog(this);
        }

    }
    public interface ClickListener{
        void onBtnClick(Dialog dialog);
    }
}
