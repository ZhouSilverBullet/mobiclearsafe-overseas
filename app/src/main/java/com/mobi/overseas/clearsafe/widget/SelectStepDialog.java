package com.mobi.overseas.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2019-10-28  10:59
 */
public class SelectStepDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private TextView tv_cancle, tv_submit;
    private WheelView wheelview;
    private DataListener mListener;
    private List<String> mList = new ArrayList<>();
    private int BaseStep = 3000;
    private int mNowStep = 8000;
    private int selectIndex = 8;

    public SelectStepDialog(Context context, int nowStep, DataListener listener) {
        super(context,R.style.AllDialog);
        this.mContext = context;
        this.mListener = listener;
        this.mNowStep = nowStep;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selectstep_layout);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels > dm.heightPixels ? dm.heightPixels
                : dm.widthPixels;
        lp.width = w;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
        for (int i = 0; i < 55; i++) {
            int itemStep = BaseStep + i * 500;
            if (itemStep == mNowStep) {
                selectIndex = i;
            }
            mList.add(String.valueOf(itemStep));
        }
        initView();
    }

    private void initView() {
        tv_cancle = findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(this);
        tv_submit = findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
        wheelview = findViewById(R.id.wheelview);
        wheelview.setItems(mList);
        wheelview.setSeletion(selectIndex);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                if (mListener != null) {
                    mListener.cancleClick(SelectStepDialog.this);
                }
                break;
            case R.id.tv_submit:
                if (mListener != null) {
                    String select = wheelview.getSeletedItem();
                    mListener.submitClick(SelectStepDialog.this, select);
                }
                break;
        }
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }

    public interface DataListener {
        void cancleClick(Dialog dialog);

        void submitClick(Dialog dialog, String data);
    }
}
