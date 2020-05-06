package com.mobi.clearsafe.ui.clear.control;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;

import com.blankj.utilcode.utils.AppUtils;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.clear.entity.AppProcessInfo;
import com.mobi.clearsafe.ui.clear.widget.dustbinview.ComparatorApp;

import org.greenrobot.eventbus.Subscribe;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by panruijie on 16/12/28.
 * Email : zquprj@gmail.com
 * 进程管理
 */

public class ProcessManager {

    private static ProcessManager sInstance;
    private Context mContext;
    private List<AppProcessInfo> mTempList;
    private List<AppProcessInfo> mRunningProcessList;
    private PackageManager mPackageManager;
    private ActivityManager mActivityManager;
    private ActivityManager.MemoryInfo mMemoryInfo;

    private int mRandomProcessMemoryValue;
    private MemoryRandom memoryRandom;

    private ProcessManager(Context context) {
        this.mContext = context;
        mRunningProcessList = new ArrayList<>();
        mPackageManager = mContext.getPackageManager();
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mMemoryInfo = new ActivityManager.MemoryInfo();

        createMemoryRandomValue();
    }

    public static void init(Context context) {

        if (sInstance == null) {
            sInstance = new ProcessManager(context);
        }
    }

    public static ProcessManager getInstance() {

        if (sInstance == null) {
            throw new IllegalStateException("You must be init ProcessManager first");
        }
        return sInstance;
    }

    public List<AppProcessInfo> getRunningProcessList() {

        mTempList = new ArrayList<>();
        ApplicationInfo appInfo = null;
        AppProcessInfo abAppProcessInfo = null;

        for (ActivityManager.RunningAppProcessInfo info : AndroidProcesses.getRunningAppProcessInfo(mContext)) {

            if (!info.processName.equals(AppUtils.getAppPackageName(mContext))) {
                abAppProcessInfo = new AppProcessInfo(info.processName, info.pid, info.uid);

                try {
                    appInfo = mPackageManager.getApplicationInfo(info.processName, 0);
                    if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        abAppProcessInfo.setSystem(true);
                    } else {
                        abAppProcessInfo.setSystem(false);
                    }
                    Drawable icon = appInfo.loadIcon(mPackageManager) == null ?
                            ActivityCompat.getDrawable(mContext, R.mipmap.ic_launcher)
                            : appInfo.loadIcon(mPackageManager);
                    String name = appInfo.loadLabel(mPackageManager).toString();
                    abAppProcessInfo.setIcon(icon);
                    abAppProcessInfo.setAppName(name);

                } catch (PackageManager.NameNotFoundException e) {

                    /*名字没找到，可能是应用的服务*/
                    if (info.processName.contains(":")) {
                        appInfo = getApplicationInfo(info.processName.split(":")[0]);
                        if (appInfo != null) {
                            Drawable icon = appInfo.loadIcon(mPackageManager);
                            abAppProcessInfo.setIcon(icon);
                        } else {
                            abAppProcessInfo.setIcon(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
                        }
                    }
                    abAppProcessInfo.setSystem(true);
                    abAppProcessInfo.setAppName(info.processName);
                }

                long memsize = mActivityManager.getProcessMemoryInfo(new int[]{info.pid})[0].getTotalPrivateDirty() * 1024;
                abAppProcessInfo.setMemory(memsize);

                if (!abAppProcessInfo.isSystem()) {
                    mTempList.add(abAppProcessInfo);
                }
            }

        }

        //APP去重
        ComparatorApp comparator = new ComparatorApp();
        Collections.sort(mTempList, comparator);
        int lastUid = 0;
        int index = -1;
        mRunningProcessList.clear();

        for (AppProcessInfo info : mTempList) {
            if (lastUid == info.getUid()) {
                AppProcessInfo nowInfo = mTempList.get(index);
                mRunningProcessList.get(index).setMemory(nowInfo.getMemory() + info.getMemory());
            } else {
                index++;
                mRunningProcessList.add(info);
                lastUid = info.getUid();
            }
        }

        return mRunningProcessList;
    }

    public ApplicationInfo getApplicationInfo(String processName) {
        if (processName == null) {
            return null;
        }
        List<ApplicationInfo> appList = mPackageManager
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo appInfo : appList) {
            if (processName.equals(appInfo.processName)) {
                return appInfo;
            }
        }
        return null;
    }

    public Observable<List<AppProcessInfo>> getRunningAppListUsingObservable() {

        return Observable.create(new ObservableOnSubscribe<List<AppProcessInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppProcessInfo>> subscriber) throws Exception {
                subscriber.onNext(getRunningProcessList());
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public List<AppProcessInfo> getRunningAppList() {
        return mRunningProcessList;
    }

    public long killAllRunningApp() {

        long beforeMemory = 0;
        long endMemory = 0;

        mActivityManager.getMemoryInfo(mMemoryInfo);
        beforeMemory = mMemoryInfo.availMem;

        for (AppProcessInfo info : getRunningProcessList()) {
            killBackgroundProcesses(info.getProcessName());
        }

        mActivityManager.getMemoryInfo(mMemoryInfo);
        endMemory = mMemoryInfo.availMem;

        return endMemory - beforeMemory;
    }

    public long killRunningApp(String processName) {

        long beforeMemory = 0;
        long endMemory = 0;

        mActivityManager.getMemoryInfo(mMemoryInfo);
        beforeMemory = mMemoryInfo.availMem;

        try {
            killBackgroundProcesses(processName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mActivityManager.getMemoryInfo(mMemoryInfo);
        endMemory = mMemoryInfo.availMem;

        return endMemory - beforeMemory;
    }

    public void killBackgroundProcesses(String processName) {

        String packageName = null;
        try {
            if (!processName.contains(":")) {
                packageName = processName;
            } else {
                packageName = processName.split(":")[0];
            }

            mActivityManager.killBackgroundProcesses(packageName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Observable<Long> killAllRunningAppUsingObservable() {

        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> subscriber) throws Exception {
                subscriber.onNext(killAllRunningApp());
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Long> killRunningAppUsingObservable(Set<String> packageNameSet) {

        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> subscriber) throws Exception {
                long memory = 0L;
                for (String string : packageNameSet) {
                    memory += killRunningApp(string);
                }

                subscriber.onNext(memory);
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Long> killRunningAppUsingObservable(String packageName) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> subscriber) throws Exception {
                subscriber.onNext(killRunningApp(packageName));
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    ///制造假内存
    private void createMemoryRandomValue() {
        memoryRandom = new MemoryRandom();
        memoryRandom.setNewTime(false);
        memoryRandom.setRandomListener(random -> {
            mRandomProcessMemoryValue = random;
            if (processMemoryListener != null) {
                processMemoryListener.onMemoryValue(mRandomProcessMemoryValue);
            }
        });
    }

    /**
     * 刷新造内存的时间器
     * @param randomNewTime
     */
    public void setRandomNewTime(boolean randomNewTime) {
        if (memoryRandom != null) {
            memoryRandom.setNewTime(true);
        }
    }

    private IProcessMemoryListener processMemoryListener;

    public void setProcessMemoryListener(IProcessMemoryListener processMemoryListener) {
        this.processMemoryListener = processMemoryListener;
    }

    public interface IProcessMemoryListener {
        void onMemoryValue(int memoryValue);
    }

    public void removeMemoryListener() {
        processMemoryListener = null;
    }
}
