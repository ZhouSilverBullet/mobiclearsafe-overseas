package com.mobi.clearsafe.ui.common;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.wanjian.cockroach.ExceptionHandler;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/8 11:41
 * @Dec 用于捕获异常，然后让app不进行没必要的崩溃
 */
public class AppExceptionHandler extends ExceptionHandler {
    public static final String TAG = "AppExceptionHandler";
    private final Thread.UncaughtExceptionHandler sysExcepHandler;

    public AppExceptionHandler(Context context) {
        CrashHandler.getInstance().init(context);
        sysExcepHandler = CrashHandler.getInstance();
    }

    @Override
    protected void onUncaughtExceptionHappened(Thread thread, Throwable throwable) {
        Log.e(TAG, "--->onUncaughtExceptionHappened:" + thread + "<---", throwable);
    }

    @Override
    protected void onBandageExceptionHappened(Throwable throwable) {
        Log.e(TAG, "--->onBandageExceptionHappened:" + "<---", throwable);
        throwable.printStackTrace();
    }

    @Override
    protected void onMayBeBlackScreen(Throwable e) {
        Thread thread = Looper.getMainLooper().getThread();
        Log.e(TAG, "--->onUncaughtExceptionHappened:" + thread + "<---", e);
        //黑屏时建议直接杀死app
        sysExcepHandler.uncaughtException(thread, new RuntimeException("black screen"));
    }

    @Override
    protected void onEnterSafeMode() {

    }
}
