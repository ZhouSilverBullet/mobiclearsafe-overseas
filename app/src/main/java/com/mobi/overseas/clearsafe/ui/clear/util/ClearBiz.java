package com.mobi.overseas.clearsafe.ui.clear.util;

import android.annotation.TargetApi;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.ui.clear.data.Consts;
import com.mobi.overseas.clearsafe.ui.clear.entity.App;
import com.mobi.overseas.clearsafe.ui.clear.entity.Group;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CWQ on 2016/8/9.
 */
public class ClearBiz implements Consts {
    public static final String TAG = "ClearBiz";
    IPackageDataObserver.Stub dataObserver = new IPackageDataObserver.Stub() {
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
        }
    };
    private Context context;
    private Handler handler;
    private PackageManager packageManager;
    private List<Group> groups = new ArrayList<>();
    private List<App> userApps = new ArrayList<>();
    private List<App> systemApps = new ArrayList<>();
    private long totalCacheSize;
    private long userCacheSize;
    private long systemCacheSize;
    private int count;
    private int applicationInfosSize;
    IPackageStatsObserver.Stub statsObserver = new IPackageStatsObserver.Stub() {
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            String packageName = pStats.packageName;
            long cacheSize = pStats.cacheSize;
            handleGetCompleted(packageName, cacheSize);
        }
    };

    private void handleGetCompleted(String packageName, long cacheSize) {
        try {

            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            Drawable icon = applicationInfo.loadIcon(packageManager);
            String name = (String) applicationInfo.loadLabel(packageManager);
            if (cacheSize > 0) {
                boolean isSystem;
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    isSystem = true;
                    systemApps.add(new App(icon, name, packageName, cacheSize, isSystem));
                    systemCacheSize += cacheSize;
                } else {
                    isSystem = false;
                    userApps.add(new App(icon, name, packageName, cacheSize, isSystem));
                    userCacheSize += cacheSize;
                }

                totalCacheSize += cacheSize;
            }

            groups.clear();
            groups.add(new Group(KEY_USER_APP, userApps, userCacheSize, 0));
            groups.add(new Group(KEY_SYSTEM_APP, systemApps, 0, systemCacheSize));

            Message message = Message.obtain();
            message.what = MSG_SCANNIG;
            Bundle data = new Bundle();
            data.putLong(KEY_TOTAL_CACHESIZE, totalCacheSize);
            data.putString(KEY_NAME, name);
            data.putParcelableArrayList(KEY_GROUPS, (ArrayList<? extends Parcelable>) groups);
            message.setData(data);
            handler.sendMessage(message);

            count++;
            if (count == applicationInfosSize) {
                synchronized (ClearBiz.this) {
                    ClearBiz.this.notify();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean mIsRunning;

    public ClearBiz(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void scan() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    packageManager = context.getPackageManager();
                    List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(0);
                    applicationInfosSize = applicationInfos.size();
                    Message message = Message.obtain();
                    message.what = MSG_PROGRESSBAR_SET_MAX;
                    message.obj = applicationInfosSize;
                    handler.sendMessage(message);
                    for (int i = 0; i < applicationInfosSize; i++) {
                        ApplicationInfo applicationInfo = applicationInfos.get(i);
                        String packageName = applicationInfo.packageName;
//                        Log.e(TAG, packageName);
                        getAppCacheInfo(packageName);
//                        getPkgSize(MyApplication.getContext(), packageName);
                        if (i != applicationInfosSize - 1) {
                            sleep(50);
                        }
                    }
                    synchronized (ClearBiz.this) {
                        ClearBiz.this.wait();
                    }
                    groups.clear();
                    groups.add(new Group(KEY_USER_APP, userApps, userCacheSize, 0));
                    groups.add(new Group(KEY_SYSTEM_APP, systemApps, 0, systemCacheSize));
                    message = Message.obtain();
                    message.what = MSG_SCAN_FINISH;
                    message.obj = groups;
                    handler.sendMessage(message);
                    mIsRunning = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getAppCacheInfo(String packageName) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getAppCacheInfo8(packageName);
        } else  {
            getAppCacheInfoLowVersion(packageName);
        }
    }

    private void getAppCacheInfoLowVersion(String packageName) {
        try {
            Class<?> clazz = Class.forName("android.content.pm.PackageManager");
            Method method = clazz.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            method.invoke(packageManager, packageName, statsObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void getAppCacheInfo8(String packageName) {
        StorageStatsManager statsManager = (StorageStatsManager) context.getSystemService(Context.STORAGE_STATS_SERVICE);
        UserHandle handler = UserHandle.getUserHandleForUid(-2);

        try {
            StorageStats stats = statsManager.queryStatsForPackage(StorageManager.UUID_DEFAULT, packageName, handler);
            long codeBytes = stats.getAppBytes();
            long dataBytes = stats.getDataBytes();
            long cacheBytes = stats.getCacheBytes();
            handleGetCompleted(packageName, cacheBytes);
            Log.e(TAG, packageName + " codeBytes = " + FileUtil.getFileSize(codeBytes) + ", dataBytes = " + FileUtil.getFileSize(dataBytes) + ", cacheBytes = " + FileUtil.getFileSize(cacheBytes));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            //这里说明没有权限，没有权限只能查询自身应用大小
            e.printStackTrace();
        }
    }

    public void updataTotalDataSiz() {

        mIsRunning = true;

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    while (mIsRunning) {
                        handler.sendEmptyMessage(MSG_UPDATE_TOTAL_CACHESIZE);
                        sleep(500);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void clear() {
        try {
            PackageManager packageManager = context.getPackageManager();
            Class<?> clazz = Class.forName("android.content.pm.PackageManager");
            Method method = clazz.getMethod("freeStorageAndNotify", long.class, IPackageDataObserver.class);
            method.invoke(packageManager, Long.MAX_VALUE, dataObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
