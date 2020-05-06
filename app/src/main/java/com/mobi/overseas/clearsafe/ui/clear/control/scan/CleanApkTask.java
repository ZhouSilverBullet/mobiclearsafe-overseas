package com.mobi.overseas.clearsafe.ui.clear.control.scan;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.ui.clear.data.ScanFileBean;
import com.mobi.overseas.clearsafe.ui.clear.data.ScanFileBean;
import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;
import com.mobi.overseas.clearsafe.ui.common.Bugs;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/13 15:24
 * @Dec 略
 */
public class CleanApkTask extends BaseCleanTask<ScanFileBean> {

    public static final String TAG = "CleanApkTask";

    private final int SCAN_LEVEL = 10;
    public static final String PATH_ALL = Environment.getExternalStorageDirectory().getAbsolutePath();

    private List<ScanFileBean> mGarbageList;

    public CleanApkTask(Context context) {
        super(context, true);
        mGarbageList = new CopyOnWriteArrayList<>();
    }

    @Override
    protected void addRunnableInBackground(List<Runnable> runnableList) {
        OverRunnable allFindRun = new OverRunnable() {
            @Override
            public void execTask() {
                File externalDir = new File(PATH_ALL);
                travelSdkCard(externalDir, 6);
            }
        };

        runnableList.add(allFindRun);
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
        if (lists == null) {
            return;
        }
        for (File file : lists) {
            callPath(file.getPath());
            Log.i(TAG, file.getPath());

            if (file.isFile()) {
                String name = file.getName();
                if (name.endsWith(".apk") || name.endsWith(".apk.1")) {
                    PackageManager pm = MyApplication.getContext().getPackageManager();
                    PackageInfo info = pm.getPackageArchiveInfo(file.getPath(), PackageManager.GET_ACTIVITIES);
                    if (info != null) {
                        ApplicationInfo appInfo = info.applicationInfo;
                        String appName = pm.getApplicationLabel(appInfo).toString();
                        String packageName = appInfo.packageName; //得到安装包名称
                        String version = info.versionName; //得到版本信息
                        Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息

                        ScanFileBean bean = new ScanFileBean();
                        bean.fileList.add(file);
                        bean.icon = R.mipmap.ic_launcher;
                        bean.name = appName;
                        boolean isInstalled = checkAppInstalled(pm, packageName);
                        bean.dec = isInstalled ? "已经安装" : "未安装";
                        bean.imageDrawable = icon;
                        bean.isInstalled = isInstalled;
                        bean.isCheck = isInstalled;
                        bean.garbageType = ScanFileBean.TYPE_UNINSTALL;
                        bean.fileSize = file.length();
                        bean.sizeAndUnit = FileUtil.getFileSize0(file.length());

                        mGarbageList.add(bean);

                        onProgress(bean);
                    }
                }
            } else {

                if (level < SCAN_LEVEL) {
                    travelSdkCard(file, level + 1);
                }
            }
        }
    }

    public List<ScanFileBean> getGarbageList() {
        return mGarbageList;
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
}

