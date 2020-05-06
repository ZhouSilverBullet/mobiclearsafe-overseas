package com.mobi.overseas.clearsafe.ui.cleannotice.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import com.mobi.overseas.clearsafe.ui.cleannotice.GuideDialogActivity;
import com.mobi.overseas.clearsafe.ui.cleannotice.SingleTaskBlankActivity;
import com.mobi.overseas.clearsafe.utils.ToastUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/16 15:25
 * @Dec 略
 */
public class CleanNoticeUtil {
    public static boolean gotoNotificationAccessSetting(Context context) {
        try {

            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            SingleTaskBlankActivity.start(context, intent);

            if (Build.VERSION.SDK_INT < 22) {
                return true;
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent2 = new Intent(context, GuideDialogActivity.class);
                    context.startActivity(intent2);
                }
            }, 500L);

            return true;

        } catch (ActivityNotFoundException e) {//普通情况下找不到的时候需要再特殊处理找一次
            try {
                Intent intent = new Intent();
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");

                context.startActivity(intent);



                return true;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            ToastUtils.showLong("对不起，您的手机暂不支持");
            e.printStackTrace();
            return false;
        }
    }





}
