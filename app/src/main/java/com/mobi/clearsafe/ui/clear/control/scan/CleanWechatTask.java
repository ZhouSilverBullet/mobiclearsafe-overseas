package com.mobi.clearsafe.ui.clear.control.scan;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.clear.data.WechatBean;
import com.mobi.clearsafe.ui.clear.util.FileUtil;
import com.mobi.clearsafe.ui.common.Bugs;
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
public class CleanWechatTask extends AsyncTask<Void, Void, Void> {
    public static final String TAG = "CleanWechatTask";


    public static final String PATH_WECHAT = Environment.getExternalStorageDirectory().getPath() + "/tencent/MicroMsg/";
    public static final String PATH_WECHAT_AD = PATH_WECHAT + "sns_ad_landingpages/";

    public static final String PATH_VIDEO_CACHE = PATH_WECHAT + "videocache";
    public static final String PATH_VPROXY = PATH_WECHAT + "vproxy";
    public static final String PATH_WALLET_IMAGES = PATH_WECHAT + "wallet_images";
    public static final String PATH_MIX_AUDIO = PATH_WECHAT + "mix_audio";


    //要18 MD5 长度的文件
    public static final String PATH_EMOJI = "emoji";
    public static final String PATH_SNS = "sns";

    private final IScanCallback<WechatBean> mCallback;
    //搜一层就可以了
    private final int SCAN_LEVEL = 10;

    public List<WechatBean> mWechatGabargeList;
    public List<WechatBean> mWechatFriendCacheList;
    public List<WechatBean> mWechatAdList;
    private ExecutorService executorService;


    public CleanWechatTask(IScanCallback<WechatBean> callback) {
        mCallback = callback;
        mWechatGabargeList = new CopyOnWriteArrayList<>();
        mWechatFriendCacheList = new CopyOnWriteArrayList<>();
        mWechatAdList = new CopyOnWriteArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (mCallback != null) {
            mCallback.onBegin();
        }

        List<Runnable> runnableList = new ArrayList<>();

        //广告的
        Runnable adRunnable = new Runnable() {
            @Override
            public void run() {

                File externalDir = new File(PATH_WECHAT_AD);
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
                mWechatAdList.add(bean);

                onProgress(bean);
            }
        };

        runnableList.add(adRunnable);

        //微信垃圾
        Runnable cacheRun = new Runnable() {
            @Override
            public void run() {
                File file1 = new File(PATH_VIDEO_CACHE);
                File file2 = new File(PATH_VPROXY);
                File file3 = new File(PATH_WALLET_IMAGES);
                File file4 = new File(PATH_MIX_AUDIO);

                handleWechatGarbage(file1);
                handleWechatGarbage(file2);
                handleWechatGarbage(file3);
                handleWechatGarbage(file4);
            }
        };
        runnableList.add(cacheRun);

        //朋友圈缓存
        File externalDir = new File(PATH_WECHAT);
        @Bugs("files 会为空")
        File[] files = externalDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                //过滤掉隐藏文件
                return s.length() >= 32;
            }
        });

//        if (files == null) {
//            files = externalDir.listFiles();
//        }

        //不为空的时候进行扫描
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            handleWechatFriendGarbage(new File(f + "/" + PATH_EMOJI));
                        }
                    };
                    runnableList.add(runnable);
                    Runnable runnable2 = new Runnable() {
                        @Override
                        public void run() {
                            handleWechatFriendGarbage(new File(f + "/" + PATH_SNS));
                        }
                    };
                    runnableList.add(runnable2);
                }
            }
        } else {
            handleWechatFriendGarbage(new File(""));
        }

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

//        mWechatAdList = removeZeroFile(mWechatAdList);
//        mWechatGabargeList = removeZeroFile(mWechatGabargeList);
//        mWechatFriendCacheList = removeZeroFile(mWechatFriendCacheList);

        //完成
        if (mCallback != null) {
            mCallback.onFinish();
        }

        return null;
    }

    private void handleWechatGarbage(File file) {

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
        mWechatGabargeList.add(bean);

        onProgress(bean);
    }


    private void handleWechatFriendGarbage(File file) {

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
        mWechatFriendCacheList.add(bean);

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
