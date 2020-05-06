package com.mobi.overseas.clearsafe.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mobi.overseas.clearsafe.utils.LogUtils;

/**
 * Created by jiahongfei on 2017/9/27.
 */

public class TodayStepShutdownReceiver extends BroadcastReceiver {

    private static final String TAG = "TodayStepShutdownReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            LogUtils.e("TodayStepShutdownReceiver");
            PreferencesHelper.setShutdown(context,true);
        }
    }

}
