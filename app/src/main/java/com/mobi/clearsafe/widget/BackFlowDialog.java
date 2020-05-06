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
 * date: 2020/2/28
 * desc: 瓜瓜卡  大转盘倒流弹框
 */
public class BackFlowDialog extends BaseDialog implements View.OnClickListener {


    private Context mContext;
    private TextView tv_content;
    private TextView more_task, tv_title;
    private ImageView iv_close;
    private DialogClick mClick;
    private int type;


    public BackFlowDialog(Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mClick = builder.mClick;
        this.type = builder.type;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backflow_dialog_layout);
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
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        more_task = findViewById(R.id.more_task);
        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);

        more_task.setOnClickListener(this);
//        if(type==1){
//            tv_title.setText("今天的大转盘机会使用完毕");
//        }else if(type==2) {
//            tv_title.setText("今天的刮刮卡机会使用完毕");
//        }
        tv_title.setText(Html.fromHtml(mContext.getResources().getString(R.string.string_backflow_title)));



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
                    mClick.closeBtnClick(BackFlowDialog.this);
                }
                break;
            case R.id.more_task:
                if (mClick != null) {
                    mClick.moreBtnClick(BackFlowDialog.this);
                }
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public static class Builder {

        private Context mContext;

        private DialogClick mClick;
        private int type;

        public Builder(Context context) {
            this.mContext = context;
        }


        public Builder setType(int type) {
            this.type = type;
            return this;
        }


        public Builder setDialogClick(DialogClick click) {
            this.mClick = click;
            return this;
        }


        public BackFlowDialog build() {
            return new BackFlowDialog(this);
        }

    }

    public interface DialogClick {
        //金币翻倍按钮 点击事件
        void moreBtnClick(Dialog dialog);

        //关闭按钮 点击事件
        void closeBtnClick(Dialog dialog);
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }
}
