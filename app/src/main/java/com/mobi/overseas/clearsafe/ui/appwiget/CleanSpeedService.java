package com.mobi.overseas.clearsafe.ui.appwiget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mobi.overseas.clearsafe.ui.clear.control.MemoryRandom;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/29 15:41
 * @Dec 略
 */
public class CleanSpeedService extends Service implements MemoryRandom.IRandomListener {

    public static final String REFRESH_APP_WIDGET = "com.mobi.overseas.clearsafe.RESFRESH";

    private MemoryRandom mMemoryRandom;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        mMemoryRandom = new MemoryRandom();
//        mMemoryRandom.setRandomListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMemoryRandom != null) {
            mMemoryRandom.setRandomListener(null);
        }
    }

    @Override
    public void onRandom(int random) {
//        MemoryUtil.findMemory();
    }
}
