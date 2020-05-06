package com.mobi.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;

/**
 * 提现成功弹窗提示
 * author : liangning
 * date : 2019-11-18  18:41
 */
public class WithdrawalDialog extends BaseDialog implements View.OnClickListener {

    private Button btn_click;
    private Context mContent;
    private DialogClickListner mListener;


    public WithdrawalDialog(Builder builder) {
        super(builder.mContent);
        this.mContent = builder.mContent;
        this.mListener = builder.mListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_withdrawal_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initWindow();
        initView();
    }

    private void initView(){
        btn_click = findViewById(R.id.btn_click);
        btn_click.setOnClickListener(this);
    }

    private void initWindow() {
        Window dialogWindow = this.getWindow();
//        dialogWindow.setBackgroundDrawableResource(R.color.c_CC000000);
        WindowManager.LayoutParams lps = dialogWindow.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels > dm.heightPixels ? dm.heightPixels
                : dm.widthPixels;
        int h = dm.widthPixels > dm.heightPixels ? dm.widthPixels
                : dm.heightPixels;
        lps.width = w;
//        lps.height = h;
        lps.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lps);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_click:
                if (mListener!=null){
                    mListener.btnClick(WithdrawalDialog.this);
                }
                break;
        }
    }

    @Override
    protected Context getActivityContext() {
        return mContent;
    }

    public static class Builder{

        private Context mContent;
        private DialogClickListner mListener;

        public Builder(Context context){
            this.mContent = context;
        }

        public Builder setDialogClickListener(DialogClickListner listener){
            this.mListener = listener;
            return this;
        }

        public WithdrawalDialog build(){
            return new WithdrawalDialog(this);
        }

    }

    public interface DialogClickListner{
        void btnClick(Dialog dialog);
    }
}
