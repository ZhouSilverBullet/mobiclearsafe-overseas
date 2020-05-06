package com.mobi.overseas.clearsafe.ui.clear.control.scan;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.mobi.overseas.clearsafe.ui.common.global.AppGlobalConfig;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/10 10:54
 * @Dec 略
 */
public abstract class BaseCleanTask<Data> extends AsyncTask<Void, Void, Void> implements PathChangeControl.IPathCallback {
    private static final String TAG = "BaseCleanTask";

    private IScanCallback<Data> mCallback;

    private ExecutorService executorService;
    private PathChangeControl mPathChangeControl;

    //judge view lifecycle
    private WeakReference<Context> mContextDef;

    /**
     * @param context
     * @param isNeedCallPath 是否要穿件，一个给外面显示的一个队列，textView展示
     */
    public BaseCleanTask(Context context, boolean isNeedCallPath) {
        mContextDef = new WeakReference<>(context);
        setNeedCallPath(isNeedCallPath);
    }


    public void setCallback(IScanCallback<Data> callback) {
        mCallback = callback;
    }

    /**
     * 如果需要往外面 传连接出去
     *
     * @param needCallPath
     */
    protected void setNeedCallPath(boolean needCallPath) {
        if (needCallPath) {
            if (mPathChangeControl == null) {
                mPathChangeControl = new PathChangeControl();
            }
            mPathChangeControl.setPathCallback(this);
            mPathChangeControl.runTask();
        }
    }

    @Override
    protected final Void doInBackground(Void... voids) {
        List<Runnable> runnableList = new ArrayList<>();
        addRunnableInBackground(runnableList);

        executorService = Executors.newFixedThreadPool(calculateThreadCount(runnableList.size()));
        for (Runnable runnable : runnableList) {
            Log.d(TAG, "-----runnable-----");
            executorService.submit(runnable);
        }

        Log.d(TAG, "-----shutdown start-----");
        executorService.shutdown();
        Log.d(TAG, "-----shutdown end-----");

        //等待线程池中的所有线程运行完成
        while (true) {
            if (executorService.isTerminated()) {
                //让这个显示path的就停止了 
                if (mPathChangeControl != null) {
                    mPathChangeControl.setOffer(false);
                }
                break;
            }
            //如果中间已经关闭了UI （如：activity finish了）
            //就直接退出当前检测
            if (isUIFinish()) {
                Log.w(TAG, " activity or ui finish ");
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!isUIFinish()) {
            prepareFinish();
        }

        if (!isUIFinish()) {
            //完成
            if (mCallback != null) {
                mCallback.onFinish();
            }
        }

        return null;
    }

    @Override
    public void onPath(String path) {
        if (mCallback != null && !isUIFinish()) {
            mCallback.onPath(path);
        }
    }

    protected void callPath(String path) {
        if (mPathChangeControl != null) {
            mPathChangeControl.offer(path);
        }
    }

    /**
     * 把数据传递回去
     *
     * @param data
     */
    public void onProgress(Data data) {
        if (mCallback != null) {
            mCallback.onProgress(data);
        }
    }


    /**
     * 准备调用结束前，需要做的事情
     * 比如这个清除0kb的数据
     * <p>
     * mQQGarbageList = removeZeroFile(mQQGarbageList);
     * mQQIconCacheList = removeZeroFile(mQQIconCacheList);
     * mQQVideoList = removeZeroFile(mQQVideoList);
     */
    protected void prepareFinish() {

    }

    /**
     * 任务添加这个扫描
     *
     * @param runnableList
     */
    protected abstract void addRunnableInBackground(List<Runnable> runnableList);

    /**
     * 用于去除0 kb长度的数据
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends IFileSize> List<T> removeZeroFile(List<T> list) {
        List<T> changeList = new Vector<>();
        for (T garbageBean : list) {
            if (garbageBean.getFileSize() > 0) {
                changeList.add(garbageBean);
            }
        }
        return changeList;
    }

    public interface IFileSize {
        /**
         * 获取文件长度，进行筛选
         *
         * @return
         */
        long getFileSize();
    }

    /**
     * 判断生命周期
     *
     * @return
     */
    public boolean isUIFinish() {
        if (mContextDef == null || mContextDef.get() == null) {
            return true;
        }
        Context context = mContextDef.get();
        if (context instanceof Activity) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return ((Activity) context).isFinishing() || ((Activity) context).isDestroyed();
            }
            return ((Activity) context).isFinishing();
        }
        return false;
    }

    /**
     * 判断任务的个数，然后进行计算
     *
     * @param runnableSize 任务的数量
     * @return
     */
    public int calculateThreadCount(int runnableSize) {
        int cpuCount = Runtime.getRuntime().availableProcessors();
        return Math.min(runnableSize, cpuCount);
    }

    /**
     * 并发使用，不然，容易产生任务堵塞
     */
    public void execTask() {
        executeOnExecutor(AppGlobalConfig.APP_THREAD_POOL_EXECUTOR);
    }
}
