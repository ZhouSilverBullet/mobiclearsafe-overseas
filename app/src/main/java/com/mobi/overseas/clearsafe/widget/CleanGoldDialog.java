package com.mobi.overseas.clearsafe.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.adtest.manager.ScenarioEnum;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.Bugs;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * author:zhaijinlu
 * date: 2019/12/18
 * desc:
 */
public class CleanGoldDialog extends BaseDialog implements View.OnClickListener {
    private Context mContext;
    private int mGold;
    private GetGoldDialogClick mClick;
    private TextView tv_gold_money;
    private String posId;
    private ScenarioEnum scenario;


    public CleanGoldDialog(Builder builder) {
        super(builder.mContext, R.style.cleanDialog);
        this.mContext = builder.mContext;
        this.mGold = builder.mGold;
        this.mClick = builder.mDialogClick;
        this.posId = builder.posId;
        this.scenario = builder.scenario;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clean_gold_dialog_dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initView();
        initWindow();
    }

    @Override
    public void show() {
        super.show();

    }


    private void countdown() {
        Observable.intervalRange(0, 3, 0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return 4 - aLong - 1;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        if (mClick != null) {
                            mClick.closeBtnClick(CleanGoldDialog.this);
                        }
                    }
                });
    }

    private void initView() {
        tv_gold_money = findViewById(R.id.tv_gold_money);
        String gold_num = mContext.getResources().getString(R.string.get_gold2);
        String realShow = String.format(gold_num, mGold);
        tv_gold_money.setText(Html.fromHtml(realShow));
        countdown();


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

    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

    public static class Builder {
        private Context mContext;
        private int mGold = 0;//本次获得金币数量
        private GetGoldDialogClick mDialogClick;
        private String posId;
        private ScenarioEnum scenario;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setGold(int gold) {
            this.mGold = gold;
            return this;
        }

        public Builder setPosId(String posId) {
            this.posId = posId;
            return this;
        }

        public Builder setScenario(ScenarioEnum scenario) {
            this.scenario = scenario;
            return this;
        }

        public Builder setDialogClick(GetGoldDialogClick click) {
            this.mDialogClick = click;
            return this;
        }


        public CleanGoldDialog build() {
            return new CleanGoldDialog(this);
        }
    }

    public interface GetGoldDialogClick {

        //关闭按钮 点击事件
        void closeBtnClick(Dialog dialog);


    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }
}
