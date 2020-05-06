//package com.mobi.overseas.clearsafe.ui.clear.control;
//
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.Drawable;
//import android.os.Environment;
//import android.util.Log;
//
//import com.mobi.overseas.clearsafe.app.MyApplication;
//import com.mobi.overseas.clearsafe.ui.clear.data.GarbageBean;
//import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;
//import com.mobi.overseas.clearsafe.ui.clear.util.GarbageClearUtil;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Vector;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author zhousaito
// * @version 1.0
// * @date 2020/3/26 20:56
// * @Dec 略
// */
//public class AbstractGarbageScan {
//    public static final String TAG = "AbstractGarbageScan";
//
//    public static void scan(List<GarbageBean> list, IGarbageScanListener listener) {
//
//        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//            if (listener != null) {
//                listener.onFinish(list);
//            }
//        }
//
//        PackageManager packageManager = MyApplication.PM;
//        List<ApplicationInfo> installedApps = MyApplication.INSTALLED_APPS;
//        int applicationInfosSize = installedApps.size();
//
//
//        for (int i = 0; i < applicationInfosSize; i++) {
//
//            ApplicationInfo applicationInfo = installedApps.get(i);
//
//            String packageName = applicationInfo.packageName;
//            Drawable drawable = applicationInfo.loadIcon(packageManager);
//
//            GarbageBean garbageBean = new GarbageBean();
//            garbageBean.packageName = packageName;
//            //获取应用的名字
//            garbageBean.name = applicationInfo.loadLabel(packageManager).toString();
//            garbageBean.imageDrawable = drawable;
//            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
//                garbageBean.isSystemApp = true;
//            } else {
//                garbageBean.isSystemApp = false;
//            }
//
//            boolean isNeed = !GarbageClearUtil.notAddOtherName(packageName) && !garbageBean.isSystemApp;
//            if (!isNeed) {
//                //如果不是需要的就直接下一个app
//                continue;
//            }
//
//            //用线程同步的来进行
//            List<File> fileList = new Vector<>();
//            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//                File externalStorageDirectory = Environment.getExternalStorageDirectory();
//                File file = new File(externalStorageDirectory.getPath() + "/Android/data/" + packageName + "/");
//                if (!file.exists()) {
//                    continue;
//                }
//
//                if (!garbageBean.isSystemApp) {
//                    list.add(garbageBean);
//
//                    if (listener != null) {
//                        listener.onFindLoad(list, garbageBean, file, fileList);
//                    }
//                    //把garbageBean传进去
////                    findLogFile(garbageBean, file, fileList, (bean, length) -> {
////                        bean.fileSize = length;
////                        bean.sizeAndUnit = FileUtil.getFileSize0(length);
////
////                        sendLengthToHandler(length);
////
////                    });
//                }
//            }
//        }
//
//        list = removeZeroFile(list);
//        //按照文件大小进行排序
//        Collections.sort(list);
//
//
//        if (listener != null) {
//            listener.onFinish(list);
//        }
////        if (handler != null) {
////            sendMessage(list, H_SYSTEM_GARBAGE_DATA_FINISH);
////        }
//
//    }
//
//    public static void timeScan(List<GarbageBean> beanList, IGarbageScanListener listener) {
//        try {
//            FileRunnable<GarbageBean> r = new FileRunnable<GarbageBean>(beanList) {
//                @Override
//                public void run() {
//                    PackageManager packageManager = MyApplication.PM;
//                    List<ApplicationInfo> installedApps = MyApplication.INSTALLED_APPS;
//                    int applicationInfosSize = installedApps.size();
//
//                    for (int i = 0; i < applicationInfosSize; i++) {
//
//                        ApplicationInfo applicationInfo = installedApps.get(i);
//
//                        String packageName = applicationInfo.packageName;
//                        Drawable drawable = applicationInfo.loadIcon(packageManager);
//
//                        GarbageBean garbageBean = new GarbageBean();
//                        garbageBean.packageName = packageName;
//                        //获取应用的名字
//                        garbageBean.name = applicationInfo.loadLabel(packageManager).toString();
//                        garbageBean.imageDrawable = drawable;
//                        if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
//                            garbageBean.isSystemApp = true;
//                        } else {
//                            garbageBean.isSystemApp = false;
//                        }
//
//                        boolean isNeed = !GarbageClearUtil.notAddOtherName(packageName) && !garbageBean.isSystemApp;
//                        if (!isNeed) {
//                            //如果不是需要的就直接下一个app
//                            continue;
//                        }
//
//                        //用线程同步的来进行
//                        List<File> fileList = new Vector<>();
//                        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//                            File externalStorageDirectory = Environment.getExternalStorageDirectory();
//                            File file = new File(externalStorageDirectory.getPath() + "/Android/data/" + packageName + "/");
//                            if (!file.exists()) {
//                                continue;
//                            }
//
//                            if (!garbageBean.isSystemApp) {
////                                list.add(garbageBean);
//
//                                if (listener != null) {
//                                    listener.onFindLoad(list, garbageBean, file, fileList);
//                                }
////                                //把garbageBean传进去
////                                findLogFile(garbageBean, file, fileList, (bean, length) -> {
////                                    bean.fileSize = length;
////                                    bean.sizeAndUnit = FileUtil.getFileSize0(length);
////
////                                    sendLengthToHandler(length);
////
////                                });
//                            }
//                        }
//                    }
//
//                    list = removeZeroFile(list);
//                    //按照文件大小进行排序
//                    Collections.sort(list);
//
//                    if (listener != null) {
//                        listener.onFinish(list);
//                    }
////                    if (handler != null) {
////                        sendMessage(list, H_SYSTEM_GARBAGE_DATA_FINISH);
////                    }
//                }
//            };
//            TimedRun.timedRun(5L, TimeUnit.SECONDS, r, () -> {
//                //取消的时候发送
//                List<GarbageBean> list = removeZeroFile(beanList);
//                //按照文件大小进行排序
//                Collections.sort(list);
//
//                if (listener != null) {
//                    listener.onFinish(list);
//                }
//
////                if (handler != null) {
////                    sendMessage(list, H_SYSTEM_GARBAGE_DATA_FINISH);
////                }
//                Log.e(TAG, " 任务取消了 直接这里取消发送");
//            });
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * cache的时候监听
//     */
//    public interface IGarbageScanListener {
//        void onFindLoad(List<GarbageBean> beanList, GarbageBean garbageBean, File file, List<File> fileList);
//
//        void onFinish(List<GarbageBean> beanList);
//    }
//
//    private static List<GarbageBean> removeZeroFile(List<GarbageBean> list) {
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
//}
