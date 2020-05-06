package com.mobi.overseas.clearsafe.ui.clear.control.scan;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.ui.clear.data.GarbageBean;
import com.mobi.overseas.clearsafe.ui.clear.data.WechatBean;
import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;
import com.mobi.overseas.clearsafe.ui.clear.util.GarbageClearUtil;
import com.mobi.overseas.clearsafe.ui.common.Bugs;
import com.mobi.overseas.clearsafe.ui.common.global.AppGlobalConfig;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanQQTask.PATH_QQ_CACHE;
import static com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanQQTask.PATH_QQ_ICON;
import static com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanQQTask.PATH_QQ_VIDEO;
import static com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanWechatTask.PATH_EMOJI;
import static com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanWechatTask.PATH_MIX_AUDIO;
import static com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanWechatTask.PATH_SNS;
import static com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanWechatTask.PATH_VIDEO_CACHE;
import static com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanWechatTask.PATH_VPROXY;
import static com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanWechatTask.PATH_WALLET_IMAGES;
import static com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanWechatTask.PATH_WECHAT;
import static com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanWechatTask.PATH_WECHAT_AD;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/27 15:43
 * @Dec 略
 */
public class GarbageTask extends AsyncTask<Void, Void, Void> implements PathChangeControl.IPathCallback {
    public static final String TAG = "GarbageTask";

    public static final String PATH_DATA = Environment.getExternalStorageDirectory() + "/Android/data/";
    public static final String PATH_ALL = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String WECHAT_PACKAGE = "com.tencent.mm";
    public static final String QQ_PACKAGE = "com.tencent.mobileqq";

    private final IScanCallback<GarbageBean> mCallback;
    //搜一层就可以了
    private final int SCAN_LEVEL = 10;
    private final PathChangeControl mPathChangeControl;
    public List<GarbageBean> mBeanList;
    public List<GarbageBean> mCacheList;
    public List<GarbageBean> mSystemGarbageList;
    public List<GarbageBean> mAdGarbageList;
    public List<GarbageBean> mUninstallList;
    private ExecutorService executorService;


    public GarbageTask(IScanCallback<GarbageBean> callback) {
        mCallback = callback;
        mBeanList = new CopyOnWriteArrayList<>();
        mCacheList = new CopyOnWriteArrayList<>();
        mSystemGarbageList = new CopyOnWriteArrayList<>();
        mAdGarbageList = new CopyOnWriteArrayList<>();
        mUninstallList = new CopyOnWriteArrayList<>();

        mPathChangeControl = new PathChangeControl();
        mPathChangeControl.setPathCallback(this);

    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (mCallback != null) {
            mCallback.onBegin();
        }

        if (mPathChangeControl != null) {
            mPathChangeControl.runTask();
        }

        mBeanList = getInstallList();
        final List<Runnable> runnableList = new ArrayList<>();

        String myPackageName = MyApplication.getContext().getPackageName();
        //系统
        for (GarbageBean garbageBean : mBeanList) {
            String packageName = garbageBean.packageName;

            //名字是自己的，不进行清扫
            if (myPackageName.equals(packageName)) {
                continue;
            }
//            if (QQ_PACKAGE.equals(packageName)) {
//                addQQRunnable(garbageBean, runnableList);
//            } else if (WECHAT_PACKAGE.equals(packageName)) {
//                addWechatRunnable(garbageBean, runnableList);
//            } else {
            OverRunnable runnable = new OverRunnable() {
                @Override
                public void execTask() {
                    File externalDir = new File(PATH_DATA + packageName);
                    travelPath(garbageBean, externalDir, 6);
                }
            };

            runnableList.add(runnable);
//            }
        }

        OverRunnable allFindRun = new OverRunnable() {
            @Override
            public void execTask() {
                File externalDir = new File(PATH_ALL);
                travelSdkCard(externalDir, 6);
            }
        };

        runnableList.add(allFindRun);

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
                mPathChangeControl.setOffer(false);
                break;
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mCacheList = removeZeroFile(mCacheList);
        //按照文件大小进行排序
        Collections.sort(mCacheList);

        mSystemGarbageList = removeZeroFile(mSystemGarbageList);
        //按照文件大小进行排序
        Collections.sort(mSystemGarbageList);

        mAdGarbageList = removeZeroFile(mAdGarbageList);
        //按照文件大小进行排序
        Collections.sort(mAdGarbageList);

        mUninstallList = removeZeroFile(mUninstallList);
        //按照文件大小进行排序
        Collections.sort(mUninstallList);

        //全为空，加一个默认2.89kb用来弄进度条
        if (isAllIsEmpty()) {
            createOneSystemGarbage();
        }

        //完成
        if (mCallback != null) {
            mCallback.onFinish();
        }

        return null;
    }

    private void createOneSystemGarbage() {
        GarbageBean bean = new GarbageBean();
        bean.fileList.add(new File(""));
        bean.icon = R.drawable.clean_icon_system;
        bean.imageDrawable = MyApplication.getContext().getResources().getDrawable(R.drawable.clean_icon_system);
        bean.garbageType = GarbageBean.TYPE_SYSTEM_GARBAGE;
        bean.name = "系统垃圾";
        //2.98kb
        bean.fileSize = 2960;
        bean.sizeAndUnit = FileUtil.getFileSize0(2960);

        mSystemGarbageList.add(bean);
        if (mCallback != null) {
            mCallback.onProgress(bean);
        }
    }

    private boolean isAllIsEmpty() {
        return mCacheList.isEmpty() && mSystemGarbageList.isEmpty()
                && mAdGarbageList.isEmpty() && mUninstallList.isEmpty();
    }

    private List<GarbageBean> getInstallList() {
        PackageManager packageManager = MyApplication.PM;
        List<ApplicationInfo> installedApps = MyApplication.INSTALLED_APPS;
        int applicationInfosSize = installedApps.size();

        for (int i = 0; i < applicationInfosSize; i++) {
            ApplicationInfo applicationInfo = installedApps.get(i);

            String packageName = applicationInfo.packageName;
            Drawable drawable = applicationInfo.loadIcon(packageManager);

            GarbageBean garbageBean = new GarbageBean();
            garbageBean.packageName = packageName;
            //获取应用的名字
            garbageBean.name = applicationInfo.loadLabel(packageManager).toString();
            garbageBean.imageDrawable = drawable;
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                garbageBean.isSystemApp = true;
            } else {
                garbageBean.isSystemApp = false;
            }

            boolean isNeed = !GarbageClearUtil.notAddOtherName(packageName) && !garbageBean.isSystemApp;
            if (!isNeed) {
                //如果不是需要的就直接下一个app
                continue;
            }

            mBeanList.add(garbageBean);
        }
        return mBeanList;
    }

    private void travelPath(GarbageBean garbageBean, File root, int level) {
        if (root == null || !root.exists() || level > SCAN_LEVEL) {
            return;
        }

        @Bugs() final File[] lists = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                //过滤掉隐藏文件
                return !file.getName().startsWith(".") || !file.isHidden();
            }
        });

        if (lists == null) {
            return;
        }

        for (File file : lists) {
            //调用，然后外面知道扫描了什么路径
            callPath(file.getPath());

            if (file.isFile()) {
//                String name = file.getName();
//                if (name.endsWith(".log") || name.equals(".temp")) {
//                    GarbageBean bean = new GarbageBean();
//                    bean.fileList.add(file);
//                    bean.icon = garbageBean.icon;
//                    bean.garbageType = GarbageBean.TYPE_SYSTEM_GARBAGE;
//                    bean.fileSize = file.length();
//                    bean.sizeAndUnit = FileUtil.getFileSize0(file.length());
//
//                    mSystemGarbageList.add(bean);
//                    if (mCallback != null) {
//                        mCallback.onProgress(bean);
//                    }
//                } else if (name.endsWith(".bat") || name.endsWith(".sh")) {
//                    GarbageBean bean = new GarbageBean();
//                    bean.fileList.add(file);
//                    bean.icon = garbageBean.icon;
//                    bean.garbageType = GarbageBean.TYPE_AD_GARBAGE;
//                    bean.fileSize = file.length();
//                    bean.sizeAndUnit = FileUtil.getFileSize0(file.length());
//
//                    mAdGarbageList.add(bean);
//                    if (mCallback != null) {
//                        mCallback.onProgress(bean);
//                    }
//                } else if (name.equals(".apk")) {
//                    GarbageBean bean = new GarbageBean();
//                    bean.fileList.add(file);
//                    bean.icon = garbageBean.icon;
//                    bean.garbageType = GarbageBean.TYPE_UNINSTALL;
//                    bean.fileSize = file.length();
//                    bean.sizeAndUnit = FileUtil.getFileSize0(file.length());
//
//                    mUninstallList.add(bean);
//                    if (mCallback != null) {
//                        mCallback.onProgress(bean);
//                    }
//                }
            } else {
                if (file.isDirectory()) {
                    //读出cache名字的
                    if ("cache".equals(file.getName())) {
                        long folderSize = FileUtil.getFolderSize(file);
                        if (garbageBean != null) {
                            garbageBean.fileSize = folderSize;
                            garbageBean.sizeAndUnit = FileUtil.getFileSize0(folderSize);
                            garbageBean.fileList.add(file);
                            garbageBean.garbageType = GarbageBean.TYPE_CACHE;

                            mCacheList.add(garbageBean);
                            if (mCallback != null) {
                                mCallback.onProgress(garbageBean);
                            }
                        }
                        return;
                    }
                }

                if (level < SCAN_LEVEL) {
                    travelPath(garbageBean, file, level + 1);
                }
            }
        }
    }


    private void travelSdkCard(File root, int level) {
        if (root == null || !root.exists() || level > SCAN_LEVEL) {
            return;
        }
        @Bugs("容错一下") final File[] lists = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                //过滤掉隐藏文件
                return !file.getName().startsWith(".") || !"Android".equals(s) || !file.isHidden();
            }
        });
        Log.e(TAG, "travelSdkCard: " + root);
//        File[] files = root.listFiles();
        if (lists == null) {
            return;
        }
        for (File file : lists) {
            callPath(file.getPath());
            if (file.isFile()) {
                String name = file.getName();
                if (name.endsWith(".log") || name.endsWith(".temp") || name.endsWith(".tmp")) {
                    GarbageBean bean = new GarbageBean();
                    bean.fileList.add(file);
                    bean.icon = R.drawable.clean_icon_system;
                    bean.imageDrawable = MyApplication.getContext().getResources().getDrawable(R.drawable.clean_icon_system);
                    bean.garbageType = GarbageBean.TYPE_SYSTEM_GARBAGE;
                    bean.name = "系统垃圾";
                    bean.fileSize = file.length();
                    bean.sizeAndUnit = FileUtil.getFileSize0(file.length());

                    mSystemGarbageList.add(bean);
                    if (mCallback != null) {
                        mCallback.onProgress(bean);
                    }
                } else if (name.endsWith(".bat") || name.endsWith(".sh")) {
                    GarbageBean bean = new GarbageBean();
                    bean.fileList.add(file);
                    bean.icon = R.drawable.clean_icon_ad;
                    bean.imageDrawable = MyApplication.getContext().getResources().getDrawable(R.drawable.clean_icon_ad);
                    bean.garbageType = GarbageBean.TYPE_AD_GARBAGE;
                    bean.name = "广告垃圾";
                    bean.fileSize = file.length();
                    bean.sizeAndUnit = FileUtil.getFileSize0(file.length());

                    mAdGarbageList.add(bean);
                    if (mCallback != null) {
                        mCallback.onProgress(bean);
                    }
                } else if (name.endsWith(".apk") || name.endsWith(".apk.1")) {

                    PackageManager pm = MyApplication.getContext().getPackageManager();
                    PackageInfo info = pm.getPackageArchiveInfo(file.getPath(), PackageManager.GET_ACTIVITIES);
                    Log.e(TAG, "travelSdkCard apk : " + file.getPath() + " " + info);
                    if (info != null) {
                        ApplicationInfo appInfo = info.applicationInfo;
                        String appName = pm.getApplicationLabel(appInfo).toString();
                        String packageName = appInfo.packageName; //得到安装包名称
                        String version = info.versionName; //得到版本信息
                        Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息

                        GarbageBean bean = new GarbageBean();
                        bean.fileList.add(file);
                        bean.icon = R.mipmap.ic_launcher;
                        bean.name = appName;
                        bean.dec = version;
                        bean.imageDrawable = icon;
                        bean.garbageType = GarbageBean.TYPE_UNINSTALL;
                        bean.fileSize = file.length();
                        bean.sizeAndUnit = FileUtil.getFileSize0(file.length());

                        mUninstallList.add(bean);
                        if (mCallback != null) {
                            mCallback.onProgress(bean);
                        }
                    }

                }
            } else {

                if (level < SCAN_LEVEL) {
                    travelSdkCard(file, level + 1);
                }
            }
        }
    }

    private static List<GarbageBean> removeZeroFile(List<GarbageBean> list) {
        List<GarbageBean> changeList = new Vector<>();
        for (GarbageBean garbageBean : list) {
            if (garbageBean.fileSize != 0) {
                changeList.add(garbageBean);
            }
        }
        return changeList;
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

    private void callPath(String path) {
        if (mPathChangeControl != null) {
            mPathChangeControl.offer(path);
        }
    }

    @Override
    public void onPath(String path) {
        if (mCallback != null) {
            mCallback.onPath(path);
        }
    }

    //qq 和 微信 新添加的
    private void addQQRunnable(GarbageBean garbageBean, List<Runnable> runnableList) {
        //qq缓存垃圾
        Runnable adRunnable = new Runnable() {
            @Override
            public void run() {

                File externalDir = new File(PATH_QQ_CACHE);
                long folderSize = FileUtil.getFolderSize(externalDir);
//                String[] fileSize0 = FileUtil.getFileSize0(folderSize);
//                WechatBean bean = new WechatBean();
//                bean.itemType = 1;
//                bean.name = MyApplication.getContext().getString(R.string.clear_wechat_ad_title);
//                bean.dec = MyApplication.getContext().getString(R.string.clear_wechat_ad_dec);
//                bean.sizeAndUnit = fileSize0;
//                bean.isCheck = true;
//                bean.icon = R.mipmap.ic_launcher;
//                mQQGarbageList.add(bean);
                garbageBean.fileSize += folderSize;
                garbageBean.sizeAndUnit = FileUtil.getFileSize0(folderSize);
                garbageBean.fileList.add(externalDir);
                garbageBean.garbageType = GarbageBean.TYPE_CACHE;

                if (mCallback != null) {
                    mCallback.onProgress(garbageBean);
                }
            }
        };

        runnableList.add(adRunnable);

        //qq垃圾
        Runnable cacheRun = new Runnable() {
            @Override
            public void run() {
                File file1 = new File(PATH_QQ_ICON);

                handleQQIcon(garbageBean, file1);

            }
        };
        runnableList.add(cacheRun);

        //video
        Runnable videoRun = new Runnable() {
            @Override
            public void run() {
                File file1 = new File(PATH_QQ_VIDEO);

                handleVideo(garbageBean, file1);

            }
        };
        runnableList.add(videoRun);
    }

    private void handleQQIcon(GarbageBean garbageBean, File file) {
        long folderSize = FileUtil.getFolderSize(file);
//        String[] fileSize0 = FileUtil.getFileSize0(folderSize);
//        WechatBean bean = new WechatBean();
//        bean.itemType = 1;
//        bean.name = MyApplication.getContext().getString(R.string.clear_wechat_ad_title);
//        bean.dec = MyApplication.getContext().getString(R.string.clear_wechat_ad_dec);
//        bean.sizeAndUnit = fileSize0;
//        bean.isCheck = true;
//        bean.icon = R.mipmap.ic_launcher;
//        bean.fileList.add(file);
//        bean.fileSize = folderSize;
        garbageBean.fileSize += folderSize;
        garbageBean.sizeAndUnit = FileUtil.getFileSize0(folderSize);
        garbageBean.fileList.add(file);
        garbageBean.garbageType = GarbageBean.TYPE_CACHE;

//        if (mCallback != null) {
//            mCallback.onProgress(garbageBean);
//        }
    }

    private void handleVideo(GarbageBean garbageBean, File file) {

        Log.e(TAG, "FILe: " + file);

        long folderSize = FileUtil.getFolderSize(file);
        garbageBean.fileSize += folderSize;
        garbageBean.sizeAndUnit = FileUtil.getFileSize0(folderSize);
        garbageBean.fileList.add(file);
        garbageBean.garbageType = GarbageBean.TYPE_CACHE;

//        if (mCallback != null) {
//            mCallback.onProgress(garbageBean);
//        }

    }


    private void addWechatRunnable(GarbageBean garbageBean, List<Runnable> runnableList) {

        //广告的
        Runnable adRunnable = new Runnable() {
            @Override
            public void run() {

                File externalDir = new File(PATH_WECHAT_AD);
                long folderSize = FileUtil.getFolderSize(externalDir);
                garbageBean.fileSize += folderSize;
                garbageBean.sizeAndUnit = FileUtil.getFileSize0(folderSize);
                garbageBean.fileList.add(externalDir);
                garbageBean.garbageType = GarbageBean.TYPE_CACHE;

//                if (mCallback != null) {
//                    mCallback.onProgress(garbageBean);
//                }
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

                handleWechatGarbage(garbageBean, file1);
                handleWechatGarbage(garbageBean, file2);
                handleWechatGarbage(garbageBean, file3);
                handleWechatGarbage(garbageBean, file4);
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
                            handleWechatFriendGarbage(garbageBean, new File(f + "/" + PATH_EMOJI));
                        }
                    };
                    runnableList.add(runnable);
                    Runnable runnable2 = new Runnable() {
                        @Override
                        public void run() {
                            handleWechatFriendGarbage(garbageBean, new File(f + "/" + PATH_SNS));
                        }
                    };
                    runnableList.add(runnable2);
                }
            }
        } else {
            handleWechatFriendGarbage(garbageBean, new File(""));
        }
    }

    private void handleWechatGarbage(GarbageBean garbageBean, File file) {

        long folderSize = FileUtil.getFolderSize(file);
        garbageBean.fileSize += folderSize;
        garbageBean.sizeAndUnit = FileUtil.getFileSize0(folderSize);
        garbageBean.fileList.add(file);
        garbageBean.garbageType = GarbageBean.TYPE_CACHE;

//        if (mCallback != null) {
//            mCallback.onProgress(garbageBean);
//        }
    }


    private void handleWechatFriendGarbage(GarbageBean garbageBean, File file) {

        Log.e(TAG, "FILe: " + file);

        long folderSize = FileUtil.getFolderSize(file);
        garbageBean.fileSize += folderSize;
        garbageBean.sizeAndUnit = FileUtil.getFileSize0(folderSize);
        garbageBean.fileList.add(file);
        garbageBean.garbageType = GarbageBean.TYPE_CACHE;

//        if (mCallback != null) {
//            mCallback.onProgress(garbageBean);
//        }
    }

    /**
     * 并发使用，不然，容易产生任务堵塞
     */
    public void execTask() {
        executeOnExecutor(AppGlobalConfig.APP_THREAD_POOL_EXECUTOR);
    }
}
