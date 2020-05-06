package com.mobi.clearsafe.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mobi.clearsafe.utils.LogUtils;

/**
 * 开机完成广播
 *
 */
public class TodayStepBootCompleteReceiver extends BroadcastReceiver {

    private static final String TAG = "TodayStepBootCompleteReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

//        Intent todayStepIntent = new Intent(context, StepService.class);
//        todayStepIntent.putExtra(StepService.INTENT_NAME_BOOT,true);
//        context.startService(todayStepIntent);

        LogUtils.e("TodayStepBootCompleteReceiver");

    }
}
