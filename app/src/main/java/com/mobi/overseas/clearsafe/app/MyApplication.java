package com.mobi.overseas.clearsafe.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.Utils;
import com.downloader.PRDownloader;
import com.example.adtest.config.TTAdManagerHolder;
import com.example.adtest.manager.Constants;
import com.mobi.overseas.clearsafe.BuildConfig;
import com.mobi.overseas.clearsafe.push.UmPush;
import com.mobi.overseas.clearsafe.statistical.errorlog.ErrorLogUtil;
import com.mobi.overseas.clearsafe.statistical.umeng.UmDataUtil;
import com.mobi.overseas.clearsafe.ui.cleannotice.control.CleanNoticeManager;
import com.mobi.overseas.clearsafe.ui.clear.control.ProcessManager;
import com.mobi.overseas.clearsafe.ui.clear.manager.MemoryManager;
import com.mobi.overseas.clearsafe.ui.common.AppExceptionHandler;
import com.mobi.overseas.clearsafe.ui.powersaving.util.BatteryStatsUtil;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.wanjian.cockroach.Cockroach;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;

public class MyApplication extends MultiDexApplication {

    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new MaterialHeader(mContext).setColorSchemeColors(0xff000000);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(mContext).setDrawableSize(20);
            }
        });
    }

    private static Context mContext;
    private static MyApplication sApplication;

    public static PackageManager PM;
    public static List<ApplicationInfo> INSTALLED_APPS;
    public static int sWidth;
    public static int sHeight;

    //手机电池的温度
    public static double PHONE_CELSIUS = 31;

    private List<Activity> mActivityList = new LinkedList<>();

    private boolean isRun;

    @Override
    public void onCreate() {
        super.onCreate();
//        Debug.startMethodTracing("MyApp");
        //这个可以捕获没有必要的异常
        //点击事件等,但是要放在最前面
        if (!BuildConfig.DEBUG) {
            Cockroach.install(this, new AppExceptionHandler(this));
        }

        mContext = this;
        sApplication = this;
        //多进程适配
        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance()
                .setLog(false)
                .setCustomFragment(true);
        UmDataUtil.UmDataInit();
        UmPush.init(this);
        ErrorLogUtil.init();
        PRDownloader.initialize(getApplicationContext());
        if (!TTAdManagerHolder.getInitStatus()) {
            TTAdManagerHolder.singleInit(getApplicationContext(), Constants.TT_APPID, Constants.TT_APPNAME);
        }
        //   SDKManager.InitValPub(this);
        PM = getPackageManager();
        INSTALLED_APPS = PM.getInstalledApplications(0);

        registerNeedReceiver();

        ProcessManager.init(this);
        MemoryManager.init(this);
        Utils.init(this);
        sWidth = ScreenUtils.getScreenWidth();
        sHeight = ScreenUtils.getScreenHeight();

        //初始化获取通知的
        CleanNoticeManager.getInstance().init(this);

//        Debug.stopMethodTracing();
        registerActivityLifecycleCallbacks(new CleanLifecycleCallback() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                super.onActivityCreated(activity, savedInstanceState);
                mActivityList.add(activity);
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                super.onActivityDestroyed(activity);
                mActivityList.remove(activity);
            }
        });
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    private void registerNeedReceiver() {
        BatteryBroadcastReceiver receiver = new BatteryBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(receiver, filter);

    }

    public static String getResString(@StringRes int id) {
        return mContext.getResources().getString(id);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static MyApplication getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return mContext;
    }

    public List<Activity> getActivityList() {
        return mActivityList;
    }

    /**
     * 获取电池电量的广播
     */
    private static class BatteryBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);///电池剩余电量
            intent.getIntExtra("scale", 0);  ///获取电池满电量数值
            intent.getStringExtra("technology");  ///获取电池技术支持
            int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);///获取电池状态
            int plugged = intent.getIntExtra("plugged", 0);///获取电源信息
            intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);  ///获取电池健康度
            intent.getIntExtra("voltage", 0);  ///获取电池电压
            int temperature = intent.getIntExtra("temperature", 0);///获取电池温度

            double celsius = getCelsius(temperature);
//            Log.e("BatteryBroadcast", "level : " + level + " temperature: " + celsius);
            if (celsius > 50) {
                celsius = (((int) (new Random().nextDouble() * 100)) / 100.0 + new Random(1).nextInt(20) + 40);
            } else if (celsius < 20) {
                celsius = (((int) (new Random().nextDouble() * 100)) / 100.0 + new Random(1).nextInt(10) + 35);
            }
            PHONE_CELSIUS = celsius;

            BatteryStatsUtil.getInstance().setBatteryState(status, plugged, level, MyApplication.getContext());
        }

        /**
         * 华氏度转摄氏度
         *
         * @param fahrenheit
         * @return
         */
        private double getCelsius(double fahrenheit) {
            double value = ((int) ((fahrenheit - 273.15) * 100)) / 100.0;
            return value;
        }
    }


}
