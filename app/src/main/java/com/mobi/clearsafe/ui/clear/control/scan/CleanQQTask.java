package com.mobi.clearsafe.ui.clear.control.scan;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.clear.data.WechatBean;
import com.mobi.clearsafe.ui.clear.util.FileUtil;
import com.mobi.clearsafe.ui.common.global.AppGlobalConfig;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/27 15:43
 * @Dec 略
 */
public class CleanQQTask extends AsyncTask<Void, Void, Void> {
    public static final String TAG = "CleanQQTask";

    //cache
    public static final String PATH_QQ_CACHE = Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.tencent.mobileqq/cache/";
    public static final String PATH_QQ_DISKCACHE = Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.tencent.mobileqq/Tencent/MobileQQ/diskcache";

    //icon
    public static final String PATH_QQ_ICON = Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.tencent.mobileqq/Tencent/MobileQQ/head";
    //视频
    public static final String PATH_QQ_VIDEO = Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.tencent.mobileqq/files/video_story";


    //要18 MD5 长度的文件
    public static final String PATH_EMOJI = "emoji";
    public static final String PATH_SNS = "sns";

    private final IScanCallback<WechatBean> mCallback;
    //搜一层就可以了
    private final int SCAN_LEVEL = 10;

    public List<WechatBean> mQQGarbageList;
    public List<WechatBean> mQQIconCacheList;
    public List<WechatBean> mQQVideoList;
    private ExecutorService executorService;


    public CleanQQTask(IScanCallback<WechatBean> callback) {
        mCallback = callback;
        mQQGarbageList = new CopyOnWriteArrayList<>();
        mQQIconCacheList = new CopyOnWriteArrayList<>();
        mQQVideoList = new CopyOnWriteArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (mCallback != null) {
            mCallback.onBegin();
        }

        List<Runnable> runnableList = new ArrayList<>();

        //qq缓存垃圾
        Runnable adRunnable = new Runnable() {
            @Override
            public void run() {

                File externalDir = new File(PATH_QQ_CACHE);
                long folderSize = FileUtil.getFolderSize(externalDir);
                String[] fileSize0 = FileUtil.getFileSize0(folderSize);
                WechatBean bean = new WechatBean();
                bean.itemType = 1;
                bean.name = MyApplication.getContext().getString(R.string.clear_wechat_ad_title);
                bean.dec = MyApplication.getContext().getString(R.string.clear_wechat_ad_dec);
                bean.sizeAndUnit = fileSize0;
                bean.isCheck = true;
                bean.icon = R.mipmap.ic_launcher;
                bean.fileList.add(externalDir);
                bean.fileSize = folderSize;
                mQQGarbageList.add(bean);

                onProgress(bean);
            }
        };

        runnableList.add(adRunnable);

        //qq垃圾
        Runnable cacheRun = new Runnable() {
            @Override
            public void run() {
                File file1 = new File(PATH_QQ_ICON);

                handleQQIcon(file1);

            }
        };
        runnableList.add(cacheRun);

        //video
        Runnable videoRun = new Runnable() {
            @Override
            public void run() {
                File file1 = new File(PATH_QQ_VIDEO);

                handleVideo(file1);

            }
        };
        runnableList.add(videoRun);

        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (Runnable runnable : runnableList) {
            Log.e(TAG, "-----runnable-----");
            executorService.submit(runnable);
        }

        Log.e(TAG, "-----shutdown start-----");
        executorService.shutdown();
        Log.e(TAG, "-----shutdown end-----");

        //等待线程池中的所有线程运行完成
        while (true) {
            if (executorService.isTerminated()) {
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        mQQGarbageList = removeZeroFile(mQQGarbageList);
//        mQQIconCacheList = removeZeroFile(mQQIconCacheList);
//        mQQVideoList = removeZeroFile(mQQVideoList);

        //完成
        if (mCallback != null) {
            mCallback.onFinish();
        }

        return null;
    }

    private void handleQQIcon(File file) {
        long folderSize = FileUtil.getFolderSize(file);
        String[] fileSize0 = FileUtil.getFileSize0(folderSize);
        WechatBean bean = new WechatBean();
        bean.itemType = 1;
        bean.name = MyApplication.getContext().getString(R.string.clear_wechat_ad_title);
        bean.dec = MyApplication.getContext().getString(R.string.clear_wechat_ad_dec);
        bean.sizeAndUnit = fileSize0;
        bean.isCheck = true;
        bean.icon = R.mipmap.ic_launcher;
        bean.fileList.add(file);
        bean.fileSize = folderSize;
        mQQIconCacheList.add(bean);

        onProgress(bean);
    }


    private void handleVideo(File file) {

        Log.e(TAG, "FILe: " + file);

        long folderSize = FileUtil.getFolderSize(file);
        String[] fileSize0 = FileUtil.getFileSize0(folderSize);
        WechatBean bean = new WechatBean();
        bean.itemType = 1;
        bean.name = MyApplication.getContext().getString(R.string.clear_wechat_ad_title);
        bean.dec = MyApplication.getContext().getString(R.string.clear_wechat_ad_dec);
        bean.sizeAndUnit = fileSize0;
        bean.isCheck = true;
        bean.icon = R.mipmap.ic_launcher;
        bean.fileList.add(file);
        bean.fileSize = folderSize;
        mQQVideoList.add(bean);

        onProgress(bean);
    }

    private static List<WechatBean> removeZeroFile(List<WechatBean> list) {
        List<WechatBean> changeList = new Vector<>();
        for (WechatBean garbageBean : list) {
            if (garbageBean.fileSize != 0) {
                changeList.add(garbageBean);
            }
        }
        return changeList;
    }

    private void onProgress(WechatBean bean) {
        if (mCallback != null) {
            mCallback.onProgress(bean);
        }
    }

    public void shutNowDownExecutor() {
        try {
            if (executorService != null) {
                if (!executorService.isShutdown()) {
                    executorService.shutdownNow();
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 并发使用，不然，容易产生任务堵塞
     */
    public void execTask() {
        executeOnExecutor(AppGlobalConfig.APP_THREAD_POOL_EXECUTOR);
    }
}
