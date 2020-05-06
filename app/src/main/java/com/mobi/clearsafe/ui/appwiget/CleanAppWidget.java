package com.mobi.clearsafe.ui.appwiget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.mobi.clearsafe.MainActivity;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.WelcomeActivity;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.clear.GarbageActivity;
import com.mobi.clearsafe.ui.clear.SpeedPhoneActivity;
import com.mobi.clearsafe.ui.clear.control.scan.MemoryTask;
import com.mobi.clearsafe.ui.common.global.AppGlobalConfig;
import com.mobi.clearsafe.ui.common.util.SpUtil;

import static com.mobi.clearsafe.ui.appwiget.CleanSpeedService.REFRESH_APP_WIDGET;

/**
 * Implementation of App Widget functionality.
 */
public class CleanAppWidget extends AppWidgetProvider {
    public static final String TAG = "CleanAppWidget";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId, Intent extra) {

//        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
//        views.setTextViewText(R.id.tvGarbage, widgetText);
//        views.setOnClickPendingIntent(R.id.llSpeed, getOpenPendingIntent(context, SpeedPhoneActivity.class));
        int progress;
        String status;
        if (extra != null) {
            progress = extra.getIntExtra("progress", 70);
            status = extra.getStringExtra("status");
            if (TextUtils.isEmpty(status)) {
                status = "亚健康";
            }
        } else {
            progress = SpUtil.getInt(Const.HOME_POINT_TEMP_VALUE, 86);
            status = SpUtil.getString(Const.HOME_POINT_TEMP_STATUS, "亚健康");
        }
        //写两个布局
        RemoteViews views;
        if (progress < 100) {
            views = new RemoteViews(context.getPackageName(), R.layout.clean_app_widget);
        } else {
            views = new RemoteViews(context.getPackageName(), R.layout.clean_app_normal_widget);
        }
        views.setProgressBar(R.id.pbProgress, 100, progress, false);
        views.setTextViewText(R.id.tvProgress, progress + "分 " + status);
        views.setOnClickPendingIntent(R.id.llClear, getOpenPendingIntent(context, GarbageActivity.class));
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * 获取 打开 MainActivity 的 PendingIntent
     */
    private PendingIntent getOpenPendingIntent(Context context, Class<? extends AppCompatActivity> clazz) {
        Intent intent = new Intent();
        intent.setClass(context, clazz);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        return pi;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.startsWith("com.mobi.clearsafe.")) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), this.getClass().getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            onUpdate(context, appWidgetManager, appWidgetIds, intent);
        } else {
            super.onReceive(context, intent);
        }
//        Log.e(TAG, "---onReceive---");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.e(TAG, "---onUpdate---");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, null);
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Intent intent) {
        // There may be multiple widgets active, so update all of them
        Log.e(TAG, "---onUpdate---");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, intent);
        }
    }

    /**
     * 小工具被添加的时候，调用
     *
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        Log.e(TAG, "---onEnabled---");
//        SpUtil.putBoolean(Const);
        SpUtil.putBooleanCommit(Const.APP_WIDGET_ENABLE, true);

        //立马去获取一次内存数据
//        MemoryUtil.findMemory();

    }

    /**
     * 清除了所有的 小工具的时候被调用
     *
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        Log.e(TAG, "---onDisabled---");
        SpUtil.putBooleanCommit(Const.APP_WIDGET_ENABLE, false);
    }

    /**
     * 每清除一个就加入
     *
     * @param context
     * @param appWidgetIds
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.e(TAG, "---onDeleted---");
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        Log.e(TAG, "---onAppWidgetOptionsChanged---");
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        Log.e(TAG, "---onAppWidgetOptionsChanged---");
    }
}

