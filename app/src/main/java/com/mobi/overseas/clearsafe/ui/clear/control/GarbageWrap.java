//package com.mobi.overseas.clearsafe.ui.clear.control;
//
//import android.content.Context;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.Drawable;
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//
//import com.chad.library.adapter.base.entity.MultiItemEntity;
//import com.mobi.overseas.clearsafe.app.MyApplication;
//import com.mobi.overseas.clearsafe.main.adapter.data.ClearBean;
//import com.mobi.overseas.clearsafe.ui.clear.data.GarbageBean;
//import com.mobi.overseas.clearsafe.ui.clear.data.GarbageHeaderBean;
//import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;
//import com.mobi.overseas.clearsafe.ui.clear.util.GarbageClearUtil;
//
//import java.io.File;
//import java.util.Collections;
//import java.util.List;
//import java.util.Vector;
//import java.util.concurrent.Callable;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.concurrent.FutureTask;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * @author zhousaito
// * @version 1.0
// * @date 2020/3/20 10:54
// * @Dec 略
// * <p>
// * 按钮要数据
// * top要数据
// * 然后列表要数据
// */
//public class GarbageWrap {
//
//    /**
//     * 用来设置中断标志
//     */
//    public AtomicBoolean cacheSuccess = new AtomicBoolean(false);
//    public AtomicBoolean systemSuccess = new AtomicBoolean(false);
//    public AtomicBoolean adSuccess = new AtomicBoolean(false);
//
//    public static final String TAG = "GarbageWrap";
//
//    //全部完成了
//    public static final int H_ALL_FINISH = 98;
//
//    public static final int H_PROGRESS = 99;
//    public static final int H_GARBAGE_DATA_FINISH = 100;
//    //无效安装包
//    public static final int H_INVALID_INSTALLATION_PACKAGE_DATA = 101;
//    //无效安装包扫描完成
//    public static final int H_INVALID_INSTALLATION_PACKAGE_DATA_FINISH = 102;
//
//    //系统垃圾
//    public static final int H_SYSTEM_GARBAGE_DATA = 103;
//    //系统垃圾扫描完成
//    public static final int H_SYSTEM_GARBAGE_DATA_FINISH = 104;
//
//    //广告垃圾
//    public static final int H_AD_GARBAGE_DATA = 105;
//    //广告垃圾扫描完成
//    public static final int H_AD_GARBAGE_DATA_FINISH = 106;
//
//    //卸载垃圾
//    public static final int H_UNINSTALL_GARBAGE_DATA = 107;
//    //卸载残留完成
//    public static final int H_UNINSTALL_GARBAGE_DATA_FINISH = 108;
//
//
//    private Context context;
//    private Handler handler;
//    private PackageManager packageManager;
//
//    private AtomicLong allSizeAic = new AtomicLong(0);
//
//    /**
//     * 完成的个数
//     */
//    private AtomicLong finishCount = new AtomicLong(0);
//    //任务完成的数量，是5个
//    public static final int TASK_FINISH_MAX_COUNT = 5;
//
////    private AtomicLong garbageSizeAic = new AtomicLong();
////    private AtomicLong invalidSizeAic = new AtomicLong();
//
//
//    public GarbageWrap(Context context, Handler handler) {
//        this.context = context.getApplicationContext();
//        this.handler = handler;
//    }
//
//    public void clear(List<MultiItemEntity> data, ClearQQWrap.IClearCallback callback) {
//        cacheSuccess.set(false);
//        adSuccess.set(false);
//        systemSuccess.set(false);
//
//        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
//            for (MultiItemEntity datum : data) {
//                if (datum instanceof GarbageHeaderBean) {
//                    if (((GarbageHeaderBean) datum).isCheck) {
//                        List<GarbageBean> subItems = ((GarbageHeaderBean) datum).getSubItems();
//                        for (int i = 0; i < subItems.size(); i++) {
//                            boolean check = subItems.get(i).isCheck;
//                            if (check) {
//                                for (File file : subItems.get(i).fileList) {
//                                    FileUtil.deleteFolderFile(file.getPath(), false);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            allSizeAic.set(0);
//            if (callback != null) {
//                callback.onFinish();
//            }
//        });
//
//
//    }
//
////    public void scan() {
////        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
////            packageManager = context.getPackageManager();
////            List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(0);
////            int applicationInfosSize = applicationInfos.size();
////            List<GarbageBean> list = new ArrayList<>();
////            for (int i = 0; i < applicationInfosSize; i++) {
////                ApplicationInfo applicationInfo = applicationInfos.get(i);
////                String packageName = applicationInfo.packageName;
////                int icon = applicationInfo.icon;
////                Drawable drawable = applicationInfo.loadIcon(packageManager);
////
////
////                GarbageBean garbageBean = new GarbageBean();
////                garbageBean.packageName = packageName;
//////                garbageBean.name = applicationInfo.name;
////                //获取应用的名字
////                garbageBean.name = applicationInfo.loadLabel(packageManager).toString();
////                garbageBean.imageDrawable = drawable;
////                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
////                    garbageBean.isSystemApp = true;
////                } else {
////                    garbageBean.isSystemApp = false;
////                    list.add(garbageBean);
////                }
//////                        Log.e(TAG, packageName);
//////                getAppCacheInfo(packageName);
//////                        getPkgSize(MyApplication.getContext(), packageName);
////                if (i != applicationInfosSize - 1) {
//////                    sleep(50);
////                }
////            }
////            if (handler != null) {
////                Message message = Message.obtain();
////                message.obj = list;
////                message.what = 100;
////                handler.sendMessage(message);
////            }
////        });
////    }
//
//    public void scan() {
//
//        findCache();
//        findSystemGarbage();
//        findAdGarbage();
//        findUninstallGarbage();
//        findApk();
//
//    }
//
//    private void findUninstallGarbage() {
//        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
//            sendMessage(null, H_UNINSTALL_GARBAGE_DATA_FINISH);
//        });
//    }
//
//    private void findAdGarbage() {
//        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
//            List<GarbageBean> list = new CopyOnWriteArrayList<>();
//            AbstractGarbageScan.timeScan(list, new AbstractGarbageScan.IGarbageScanListener() {
//                @Override
//                public void onFindLoad(List<GarbageBean> beanList, GarbageBean garbageBean, File file, List<File> fileList) {
//                    if (!adSuccess.get()) {
//                        beanList.add(garbageBean);
//                    }
//                    findAdFile(garbageBean, file, fileList, (bean, length) -> {
//                        bean.fileSize = length;
//                        bean.sizeAndUnit = FileUtil.getFileSize0(length);
//
//                        if (!adSuccess.get()) {
//                            sendLengthToHandler(length);
//                        }
//                    });
//                }
//
//                @Override
//                public void onFinish(List<GarbageBean> beanList) {
//                    if (handler != null) {
//                        adSuccess.set(true);
//                        sendMessage(beanList, H_AD_GARBAGE_DATA_FINISH);
//                    }
//                }
//            });
//        });
//    }
//
//    /**
//     * splash关键字
//     * temp
//     */
//    private void findSystemGarbage() {
//        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(new Runnable() {
//            @Override
//            public void run() {
//                List<GarbageBean> beanList = new CopyOnWriteArrayList<>();
//                AbstractGarbageScan.timeScan(beanList, new AbstractGarbageScan.IGarbageScanListener() {
//                    @Override
//                    public void onFindLoad(List<GarbageBean> beanList, GarbageBean garbageBean, File file, List<File> fileList) {
//                        if (!systemSuccess.get()) {
//                            beanList.add(garbageBean);
//                        }
//                        //把garbageBean传进去
//                        findLogFile(garbageBean, file, fileList, (bean, length) -> {
//                            bean.fileSize = length;
//                            bean.sizeAndUnit = FileUtil.getFileSize0(length);
//
//                            if (!systemSuccess.get()) {
//                                sendLengthToHandler(length);
//                            }
//
//                        });
//                    }
//
//                    @Override
//                    public void onFinish(List<GarbageBean> beanList) {
//                        if (handler != null) {
//                            systemSuccess.set(true);
//                            sendMessage(beanList, H_SYSTEM_GARBAGE_DATA_FINISH);
//                        }
//                    }
//                });
//
////                try {
////                    List<GarbageBean> fileList = new Vector<>();
////                    FileRunnable r = new FileRunnable(fileList) {
////                        @Override
////                        public void run() {
////                            packageManager = MyApplication.PM;
////                            List<ApplicationInfo> installedApps = MyApplication.INSTALLED_APPS;
////                            int applicationInfosSize = installedApps.size();
////
////                            for (int i = 0; i < applicationInfosSize; i++) {
////
////                                ApplicationInfo applicationInfo = installedApps.get(i);
////
////                                String packageName = applicationInfo.packageName;
////                                Drawable drawable = applicationInfo.loadIcon(packageManager);
////
////                                GarbageBean garbageBean = new GarbageBean();
////                                garbageBean.packageName = packageName;
////                                //获取应用的名字
////                                garbageBean.name = applicationInfo.loadLabel(packageManager).toString();
////                                garbageBean.imageDrawable = drawable;
////                                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
////                                    garbageBean.isSystemApp = true;
////                                } else {
////                                    garbageBean.isSystemApp = false;
////                                }
////
////                                boolean isNeed = !GarbageClearUtil.notAddOtherName(packageName) && !garbageBean.isSystemApp;
////                                if (!isNeed) {
////                                    //如果不是需要的就直接下一个app
////                                    continue;
////                                }
////
////                                //用线程同步的来进行
////                                List<File> fileList = new Vector<>();
////                                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
////                                    File externalStorageDirectory = Environment.getExternalStorageDirectory();
////                                    File file = new File(externalStorageDirectory.getPath() + "/Android/data/" + packageName + "/");
////                                    if (!file.exists()) {
////                                        continue;
////                                    }
////
////                                    if (!garbageBean.isSystemApp) {
////                                        list.add(garbageBean);
////
////                                        //把garbageBean传进去
////                                        findLogFile(garbageBean, file, fileList, (bean, length) -> {
////                                            bean.fileSize = length;
////                                            bean.sizeAndUnit = FileUtil.getFileSize0(length);
////
////                                            sendLengthToHandler(length);
////
////                                        });
////                                    }
////                                }
////                            }
////
////                            list = removeZeroFile(list);
////                            //按照文件大小进行排序
////                            Collections.sort(list);
////
////                            if (handler != null) {
////                                sendMessage(list, H_SYSTEM_GARBAGE_DATA_FINISH);
////                            }
////                        }
////                    };
////                    TimedRun.timedRun(5L, TimeUnit.SECONDS, r, () -> {
////                        //取消的时候发送
////                        List<GarbageBean> list = removeZeroFile(fileList);
////                        //按照文件大小进行排序
////                        Collections.sort(list);
////
////                        if (handler != null) {
////                            sendMessage(list, H_SYSTEM_GARBAGE_DATA_FINISH);
////                        }
////                        Log.e(TAG, " 任务取消了 直接这里取消发送");
////                    });
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
//            }
//        });
//
//    }
//
//
//    private void findCache() {
//        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
//            List<GarbageBean> beanList = new CopyOnWriteArrayList<>();
//            AbstractGarbageScan.timeScan(beanList, new AbstractGarbageScan.IGarbageScanListener() {
//                @Override
//                public void onFindLoad(List<GarbageBean> beanList, GarbageBean garbageBean, File file, List<File> fileList) {
//                    if (!cacheSuccess.get()) {
//                        beanList.add(garbageBean);
//                    }
//                    //把garbageBean传进去
//                    findCacheFile(garbageBean, file, fileList, (bean, length) -> {
//                        bean.fileSize = length;
//                        bean.sizeAndUnit = FileUtil.getFileSize0(length);
//
////                            long allSize = GarbageClearUtil.getAllSize(list);
//                        if (!cacheSuccess.get()) {
//                            sendLengthToHandler(length);
//                        }
//                    });
//                }
//
//                @Override
//                public void onFinish(List<GarbageBean> beanList) {
//                    if (handler != null) {
//                        cacheSuccess.set(true);
//                        sendMessage(beanList, H_GARBAGE_DATA_FINISH);
//                    }
//                }
//            });
//        });
////        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
////            packageManager = MyApplication.PM;
////            List<ApplicationInfo> installedApps = MyApplication.INSTALLED_APPS;
////            int applicationInfosSize = installedApps.size();
////
////            List<GarbageBean> list = new Vector<>();
////
////            for (int i = 0; i < applicationInfosSize; i++) {
////
////                ApplicationInfo applicationInfo = installedApps.get(i);
////
////                String packageName = applicationInfo.packageName;
////                Drawable drawable = applicationInfo.loadIcon(packageManager);
////
////                GarbageBean garbageBean = new GarbageBean();
////                garbageBean.packageName = packageName;
////                //获取应用的名字
////                garbageBean.name = applicationInfo.loadLabel(packageManager).toString();
////                garbageBean.imageDrawable = drawable;
////                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
////                    garbageBean.isSystemApp = true;
////                } else {
////                    garbageBean.isSystemApp = false;
//////                    list.add(garbageBean);
////                }
////
////                boolean isNeed = !GarbageClearUtil.notAddOtherName(packageName) && !garbageBean.isSystemApp;
////                if (!isNeed) {
////                    //如果不是需要的就直接下一个app
////                    continue;
////                }
////
////                //用线程同步的来进行
////                List<File> fileList = new Vector<>();
////                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
////                    File externalStorageDirectory = Environment.getExternalStorageDirectory();
////                    File file = new File(externalStorageDirectory.getPath() + "/Android/data/" + packageName + "/");
////                    if (!file.exists()) {
////                        continue;
////                    }
////
////                    if (!garbageBean.isSystemApp) {
////                        list.add(garbageBean);
////
////                        //把garbageBean传进去
////                        findCacheFile(garbageBean, file, fileList, (bean, length) -> {
////                            bean.fileSize = length;
////                            bean.sizeAndUnit = FileUtil.getFileSize0(length);
////
//////                            long allSize = GarbageClearUtil.getAllSize(list);
////
////                            sendLengthToHandler(length);
////                        });
////                    }
////                }
////            }
////
////            list = removeZeroFile(list);
////            //按照文件大小进行排序
////            Collections.sort(list);
////
////            if (handler != null) {
////
//////                Log.e(TAG, "--> <--" + allSizeAic.get());
//////                Message message = Message.obtain();
//////                message.obj = list;
//////                message.what = H_GARBAGE_DATA;
//////                handler.sendMessage(message);
////                sendMessage(list, H_GARBAGE_DATA_FINISH);
////            }
////        });
//    }
//
//    private List<GarbageBean> removeZeroFile(List<GarbageBean> list) {
//        List<GarbageBean> changeList = new Vector<>();
//        for (GarbageBean garbageBean : list) {
//            if (garbageBean.fileSize != 0) {
//                changeList.add(garbageBean);
//            }
//        }
//        return changeList;
//    }
//
//
//    private void findApk() {
//        GarbageClearUtil.findApk(new GarbageClearUtil.IGarbageListener() {
//            @Override
//            public void onFindLoad(GarbageBean bean) {
//
//                //发送长度数据给外面
//                sendLengthToHandler(bean.fileSize);
//
//                //发送bean给到外面处理
//                Message message = Message.obtain();
//                message.obj = bean;
//                message.what = H_INVALID_INSTALLATION_PACKAGE_DATA;
//                handler.sendMessage(message);
//
//            }
//
//            @Override
//            public void onFinish() {
//                //无效安装包完成
////                handler.sendEmptyMessage(H_INVALID_INSTALLATION_PACKAGE_DATA_FINISH);
//                sendMessage(null, H_INVALID_INSTALLATION_PACKAGE_DATA_FINISH);
//            }
//        });
//    }
//
//    private static void findCacheFile(GarbageBean garbageBean, File file, List<File> files, IGarbageLengthListener listener) {
//        try {
//            java.io.File[] fileList = file.listFiles();
//            for (int i = 0; i < fileList.length; i++) {
//                if (fileList[i].isDirectory()) {
//                    if ("cache".equals(fileList[i].getName())) {
//                        files.add(fileList[i]);
//                        garbageBean.fileList.add(fileList[i]);
//                        long folderSize = FileUtil.getFolderSize(fileList[i]);
//                        if (folderSize != 0) {
//                            listener.onFileLen(garbageBean, folderSize);
//                        }
//                        return;
//                    }
//                    findCacheFile(garbageBean, fileList[i], files, listener);
//                }
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    private static void findLogFile(GarbageBean garbageBean, File file, List<File> files, IGarbageLengthListener listener) {
//        try {
//            java.io.File[] fileList = file.listFiles();
//            for (int i = 0; i < fileList.length; i++) {
//                if (fileList[i].isDirectory()) {
//                    findLogFile(garbageBean, fileList[i], files, listener);
//                } else {
//                    if (fileList[i].getName().endsWith(".log") || fileList[i].getName().endsWith(".temp")) {
//                        files.add(fileList[i]);
//                        garbageBean.fileList.add(fileList[i]);
//                        long folderSize = fileList[i].length();
//                        if (folderSize != 0) {
//                            listener.onFileLen(garbageBean, folderSize);
//                        }
//                        return;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    private static void findAdFile(GarbageBean garbageBean, File file, List<File> files, IGarbageLengthListener listener) {
//        try {
//            java.io.File[] fileList = file.listFiles();
//            for (int i = 0; i < fileList.length; i++) {
//                if (fileList[i].isDirectory()) {
//                    findAdFile(garbageBean, fileList[i], files, listener);
//                } else {
//                    String fileName = fileList[i].getName();
//                    if (fileName.endsWith(".bat") || fileName.endsWith(".sh")) {
//                        files.add(fileList[i]);
//                        garbageBean.fileList.add(fileList[i]);
//                        long folderSize = fileList[i].length();
//                        if (folderSize != 0) {
//                            listener.onFileLen(garbageBean, folderSize);
//                        }
//                        return;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    private void sendMessage(Object obj, int what) {
//        Message message = Message.obtain();
//        message.obj = obj;
//        message.what = what;
//        handler.sendMessage(message);
//
//        finishCount.incrementAndGet();
//
//        if (finishCount.get() == TASK_FINISH_MAX_COUNT) {
//            //发送任务全部完成消息
//            Message msg = Message.obtain();
//            msg.obj = obj;
//            msg.what = H_ALL_FINISH;
//            handler.sendMessage(msg);
//        }
//    }
//
//    private void sendLengthToHandler(long length) {
//        //重新设置这个的长度
//        allSizeAic.set(allSizeAic.get() + length);
//
//        Message message = Message.obtain();
//        message.obj = allSizeAic;
//        message.what = H_PROGRESS;
//        handler.sendMessage(message);
//    }
//
//    public interface IGarbageLengthListener {
//        void onFileLen(GarbageBean garbageBean, long length);
//    }
//}
