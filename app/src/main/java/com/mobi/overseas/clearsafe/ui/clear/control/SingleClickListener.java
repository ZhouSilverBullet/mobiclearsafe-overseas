package com.mobi.overseas.clearsafe.ui.clear.control;

import android.view.View;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.utils.ToastUtils;


/**
 * Email: zhousaito@163.com
 * Created by zhousaito 2019-07-31 11:38
 * Version: 1.0
 * Description: future表示需要被现实的一些类
 * 表示一些不能单独的的类
 *
 */
public abstract class SingleClickListener implements View.OnClickListener {
    private long mLastClickTime;
    private long timeInterval = 3000L;

    public SingleClickListener() {

    }

    public SingleClickListener(long interval) {
        this.timeInterval = interval;
    }

    @Override
    public final void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) {
            // 单次点击事件
            onSingleClick(v);
            mLastClickTime = nowTime;
        } else {
            // 快速点击事件
            onFastClick();
        }
    }

    public abstract void onSingleClick(View v);

    public void onFastClick() {
        ToastUtils.showShort(MyApplication.getResString(R.string.clickTooFast2));
    }
}
