package com.mobi.clearsafe.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * author:zhaijinlu
 * date: 2019/10/29
 * desc:  闹钟0点广播
 */
public class TodayStepAlertReceive extends BroadcastReceiver {

    public static final String ACTION_STEP_ALERT = "action_step_alert";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_STEP_ALERT.equals(intent.getAction())) {
//            boolean separate = intent.getBooleanExtra(StepService.INTENT_NAME_0_SEPARATE, false);
//            Intent stepInent = new Intent(context, StepService.class);
//            stepInent.putExtra(StepService.INTENT_NAME_0_SEPARATE, separate);
//            context.startService(stepInent);

//            StepAlertManagerUtils.set0SeparateAlertManager(context.getApplicationContext());
        }

    }
}
