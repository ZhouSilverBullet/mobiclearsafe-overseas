package com.mobi.clearsafe.ui.cleannotice.control;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.mobi.clearsafe.BuildConfig;
import com.mobi.clearsafe.app.MyApplication;

import anet.channel.Config;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/16 15:19
 * @Dec 略
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class NotificationListener extends NotificationListenerService {
    public static final String TAG = "NotificationListener";
    public static final String ACTION_NOTIFY_LISTENER_SERVICE_CONNECT = "action_notify_listener_service_connect";
    public static final String ACTION_NOTIFY_LISTENER_SERVICE_REMOVED = "action_notify_listener_service_removed";
    public static final String ACTION_NOTIFY_LISTENER_SERVICE_POSTED = "action_notify_listener_service_posted";

    private static NotificationListener sService;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "onCreate");
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            onListenerConnected();
        }
    }

    @Override
    public void onNotificationPosted(final StatusBarNotification sbn) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "onNotificationRemoved");
        }

        //发送广播,服务发布
        Intent intent = new Intent(ACTION_NOTIFY_LISTENER_SERVICE_POSTED);
        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onNotificationRemoved(sbn);
        }
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "onNotificationRemoved");
        }

        //发送广播,服务移除通知
        Intent intent = new Intent(ACTION_NOTIFY_LISTENER_SERVICE_REMOVED);
        sendBroadcast(intent);
    }

    @Override
    public void onListenerConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onListenerConnected();
        }

        Log.i(TAG, "onListenerConnected");
        sService = this;

        //发送广播，已经连接上了
        Intent intent = new Intent(ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        sService = null;
        //发送广播，已经连接上了
//        Intent intent = new Intent(Config.ACTION_NOTIFY_LISTENER_SERVICE_DISCONNECT);
//        sendBroadcast(intent);
    }

    /**
     * 是否启动通知栏监听
     */
    public static boolean isRunning() {
        if (sService == null) {
            return false;
        }
        return true;
    }


    static NotificationListener getService() {
        return sService;
    }
}
