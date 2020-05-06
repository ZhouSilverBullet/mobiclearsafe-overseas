package com.mobi.clearsafe.ui.clear.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.clear.data.GarbageBean;
import com.mobi.clearsafe.ui.common.global.AppGlobalConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/21 09:29
 * @Dec 略
 */
public class GarbageClearUtil {
    public static final String TAG = "GarbageClearUtil";

    /**
     * 遍历所有的以.apk为结尾的文件
     *
     * @param listener
     */
    public static void findApk(IGarbageListener listener) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//            File file = new File(externalStorageDirectory.getPath() + "/" + );
            if (!file.exists()) {
                if (listener != null) {
                    listener.onFinish();
                }
                return;
            }

            AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
                List<File> list = new ArrayList<>();
                FileUtil.findSuffixFile(file, list, ".apk", (findFile, length) -> {

                    String[] fileSize0 = FileUtil.getFileSize0(length);

                    apkInfo(findFile, fileSize0, length, listener);

                    Log.e(TAG, " findApk apk " + fileSize0[0] + fileSize0[1]);
                });
                Log.e(TAG, " findApk apk " + list);
                if (listener != null) {
                    listener.onFinish();
                }
            });
        }
    }

    /**
     * cache的时候监听
     */
    public interface IGarbageListener {
        void onFindLoad(GarbageBean bean);

        void onFinish();
    }


    public static void apkInfo(File apkPath, String[] fileSize0, long length, IGarbageListener listener) {
        PackageManager pm = MyApplication.getContext().getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath.toString(), PackageManager.GET_ACTIVITIES);

        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            String appName = pm.getApplicationLabel(appInfo).toString();
            String packageName = appInfo.packageName; //得到安装包名称
            String version = info.versionName; //得到版本信息
            Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息
            //不是系统的apk
            if (listener != null && !isSystem(appInfo) && !notAddOtherName(appName)) {

                GarbageBean bean = new GarbageBean();
                bean.itemType = 0;
                bean.name = appName;
                boolean isInstalled = checkAppInstalled(pm, packageName);
                bean.dec = isInstalled ? "已经安装" : "未安装";
                bean.sizeAndUnit = fileSize0;
                //选择状态，通过这个来判断是否选中
                bean.isCheck = isInstalled;

                bean.icon = R.mipmap.ic_launcher;
                bean.imageDrawable = icon;
                bean.fileList.add(apkPath);
                bean.fileSize = length;

                listener.onFindLoad(bean);
            }
        }
    }

    /**
     * 排除一些带名字的名字的
     * android， application， plugin, flyme, miui ,vivo, oppo, tecent
     * 带application的
     * 防止一些系统文件，热修复，插件化等一些apk，先不删除
     *
     * @param appName
     * @return
     */
    public static boolean notAddOtherName(String appName) {
        return appName.contains("android")
                //Application  application
                || appName.contains("pplication")
                || appName.contains("plugin")
                || appName.contains("flyme")
                || appName.contains("vivo")
                || appName.contains("oppo")
                || appName.contains("huawei")
                || appName.contains("miui");
    }

    public static boolean isSystem(ApplicationInfo applicationInfo) {
        return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM;
    }

    /**
     * 判断应用是否已经安装
     *
     * @param pm
     * @param pkgName
     * @return
     */
    public static boolean checkAppInstalled(PackageManager pm, String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = pm.getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    public static long getAllSize(List<GarbageBean> list) {
        long allSize = 0L;
        if (list == null || list.size() == 0) {
            return allSize;
        }

        for (GarbageBean garbageBean : list) {
            allSize += garbageBean.fileSize;
        }
        return allSize;
    }

}
