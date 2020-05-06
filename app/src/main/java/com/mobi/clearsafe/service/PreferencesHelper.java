package com.mobi.clearsafe.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.mobi.clearsafe.utils.LogUtils;


/**
 * @ClassName: PreferencesHelper
 * @Description: (公用类，用于缓存一些key——value类型的数据)
 */

class PreferencesHelper {

    private static final String TAG = "PreferencesHelper";

    public static final String APP_SHARD = "today_step_share_prefs";

    // 上一次计步器的步数
    public static final String LAST_SENSOR_TIME = "last_sensor_time";
    // 步数补偿数值，每次传感器返回的步数-offset=当前步数
    public static final String STEP_OFFSET = "step_offset";
    // 当天，用来判断是否跨天
    public static final String STEP_TODAY = "step_today";
    // 清除步数
    public static final String CLEAN_STEP = "clean_step";
    // 当前步数
    public static final String CURR_STEP = "curr_step";
    //手机关机监听
    public static final String SHUTDOWN = "shutdown";
    //系统运行时间
    public static final String ELAPSED_REALTIMEl = "elapsed_realtime";

    /**
     * Get SharedPreferences
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SHARD, Context.MODE_PRIVATE);
    }

    public static void setLastSensorStep(Context context, float lastSensorStep){
        LogUtils.e("setLastSensorStep");
        getSharedPreferences(context).edit().putFloat(LAST_SENSOR_TIME,lastSensorStep).commit();
    }

    public static float getLastSensorStep(Context context){
        LogUtils.e("getLastSensorStep");
        return getSharedPreferences(context).getFloat(LAST_SENSOR_TIME,0.0f);
    }

    public static void setStepOffset(Context context, float stepOffset){
        LogUtils.e("setStepOffset");
        getSharedPreferences(context).edit().putFloat(STEP_OFFSET,stepOffset).commit();
    }

    public static float getStepOffset(Context context){
        LogUtils.e("getStepOffset");
        return getSharedPreferences(context).getFloat(STEP_OFFSET,0.0f);
    }

    public static void setStepToday(Context context, String stepToday){
        LogUtils.e("setStepToday");
        getSharedPreferences(context).edit().putString(STEP_TODAY,stepToday).commit();
    }

    public static String getStepToday(Context context){
        LogUtils.e("getStepToday");
        return getSharedPreferences(context).getString(STEP_TODAY,"");
    }

    /**
     * true清除步数从0开始，false否
     * @param context
     * @param cleanStep
     */
    public static void setCleanStep(Context context, boolean cleanStep){
        LogUtils.e("setCleanStep");
        getSharedPreferences(context).edit().putBoolean(CLEAN_STEP,cleanStep).commit();
    }

    /**
     * true 清除步数，false否
     * @param context
     * @return
     */
    public static boolean getCleanStep(Context context){
        LogUtils.e("getCleanStep");
        return getSharedPreferences(context).getBoolean(CLEAN_STEP,true);
    }

    public static void setCurrentStep(Context context, float currStep){
        LogUtils.e("setCurrentStep");
        getSharedPreferences(context).edit().putFloat(CURR_STEP,currStep).commit();
    }

    public static float getCurrentStep(Context context){
        LogUtils.e("getCurrentStep");
        return getSharedPreferences(context).getFloat(CURR_STEP,0.0f);
    }

    public static void setShutdown(Context context, boolean shutdown){
        LogUtils.e("setShutdown");
        getSharedPreferences(context).edit().putBoolean(SHUTDOWN,shutdown).commit();
    }

    public static boolean getShutdown(Context context){
        LogUtils.e("getShutdown");
        return getSharedPreferences(context).getBoolean(SHUTDOWN, false);
    }

    public static void setElapsedRealtime(Context context, long elapsedRealtime){
        LogUtils.e("setElapsedRealtime");
        getSharedPreferences(context).edit().putLong(ELAPSED_REALTIMEl,elapsedRealtime).commit();
    }

    public static long getElapsedRealtime(Context context){
        LogUtils.e("getElapsedRealtime");
        return getSharedPreferences(context).getLong(ELAPSED_REALTIMEl, 0L);
    }
}