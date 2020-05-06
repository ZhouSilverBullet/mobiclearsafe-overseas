package com.mobi.overseas.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;


/**
 * author:zhaijinlu
 * date: 2019/12/13
 * desc: 开启签到提醒
 */
public class RemindDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private TextView open;
    private ImageView iv_close;
    private RemindDialogClick mClick;



    public RemindDialog(Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mClick = builder.mClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remind_dialog_layout);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initWindow();
        initView();
    }

    @Override
    public void show() {
        super.show();
    }


    private void initView() {
        open=findViewById(R.id.btn_remind);
        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        open.setOnClickListener(this);

    }




    private void initWindow() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);
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
                dismiss();
                break;
            case R.id.btn_remind:
                if (mClick != null) {
                    mClick.openBtnClick(RemindDialog.this);
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
        private RemindDialogClick mClick;


        public Builder(Context context) {
            this.mContext = context;
        }



        public Builder setDialogClick(RemindDialogClick click) {
            this.mClick = click;
            return this;
        }


        public RemindDialog build() {
            return new RemindDialog(this);
        }

    }

    public interface RemindDialogClick {
        //开启签到提醒事件
        void openBtnClick(Dialog dialog);

    }
}

