package com.mobi.clearsafe.ui.powersaving.presenter;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.clear.util.GarbageTextAnimUtil;
import com.mobi.clearsafe.ui.common.global.AppGlobalConfig;
import com.mobi.clearsafe.ui.common.mvp.BasePresenter;
import com.mobi.clearsafe.ui.common.mvp.IMvpView;
import com.mobi.clearsafe.ui.powersaving.data.PowerSavingBean;
import com.mobi.clearsafe.ui.powersaving.util.BatteryStatsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/15 19:07
 * @Dec 略
 */
public class PowerPresenter extends BasePresenter<PowerPresenter.IView> {

    private volatile Drawable mShowDrawable1;
    private volatile Drawable mShowDrawable2;

    public void calculatePowerSaving() {

        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
            onRunTask();
        });

    }

    private void onRunTask() {
        long startTime = System.currentTimeMillis();

        List<PowerSavingBean> list = new ArrayList<>();

        int size = MyApplication.INSTALLED_APPS.size();
        //计算前面两个drawable给显示上
        int k = 0;
        for (int i = 0; i < size; i++) {
            ApplicationInfo applicationInfo = MyApplication.INSTALLED_APPS.get(i);

            //系统app不进行获取了
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                continue;
            }

            PowerSavingBean powerSavingBean = new PowerSavingBean();
            Drawable imageDrawable = applicationInfo.loadIcon(MyApplication.PM);
            powerSavingBean.setImageDrawable(imageDrawable);
            powerSavingBean.setName(applicationInfo.loadLabel(MyApplication.PM).toString());
            powerSavingBean.setCheck(true);
            list.add(powerSavingBean);

            if (i == 0) {
                k++;
                mShowDrawable1 = imageDrawable;
            } else if (k == 1) {
                k++;
                mShowDrawable2 = imageDrawable;

                //直接把两张图，先设置上去
                if (mView != null) {
                    mView.onTwoDrawable(mShowDrawable1, mShowDrawable2);
                }
            }
        }

        long endTime = System.currentTimeMillis();
        long currentTime = endTime - startTime;

        if (mView != null) {
            mView.onPowerSavingList(currentTime, list);
        }

    }

    public interface IView extends IMvpView {
        void onPowerSavingList(long currentTime, List<PowerSavingBean> list);

        void onTwoDrawable(Drawable drawable1, Drawable drawable2);
    }


}
