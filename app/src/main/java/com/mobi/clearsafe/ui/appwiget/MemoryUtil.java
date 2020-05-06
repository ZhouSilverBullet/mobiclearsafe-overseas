package com.mobi.clearsafe.ui.appwiget;

import android.content.ComponentName;
import android.content.Intent;

import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.clear.control.scan.MemoryTask;
import com.mobi.clearsafe.ui.common.global.AppGlobalConfig;
import com.mobi.clearsafe.ui.common.util.SpUtil;

import static com.mobi.clearsafe.ui.appwiget.CleanSpeedService.REFRESH_APP_WIDGET;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/29 16:30
 * @Dec 略
 */
public class MemoryUtil {
    public static void findMemory() {
        //获取内存
        new MemoryTask(new MemoryTask.IMemoryCallback() {
            @Override
            public void onPercent(long percent) {
                SpUtil.putInt(Const.SPEED_TEMP_VALUE, (int) percent);

                Intent intent = new Intent(REFRESH_APP_WIDGET);
                intent.putExtra("progress", (int) percent);
                //8.0接收不到广播
                //https://blog.csdn.net/weixin_42605341/article/details/82656103
                intent.setComponent(new ComponentName(MyApplication.getContext(), CleanAppWidget.class));
                MyApplication.getContext().sendBroadcast(intent);
            }
        }).executeOnExecutor(AppGlobalConfig.APP_THREAD_POOL_EXECUTOR);
    }
}
