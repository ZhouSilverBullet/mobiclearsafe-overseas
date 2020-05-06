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
import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;
import com.mobi.overseas.clearsafe.ui.common.Bugs;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/10 10:53
 * @Dec 略
 */
public class CleanBigFileTask extends BaseCleanTask<ScanFileBean> {

    public static final String TAG = "CleanBigFileTask";

    private final int SCAN_LEVEL = 10;
    public static final String PATH_ALL = Environment.getExternalStorageDirectory().getAbsolutePath();

    // 10*1024*1024 10MB
    public static final long MINIMUM_MAX_LENGTH = 10_485_760L;

    private List<ScanFileBean> mGarbageList;

    public CleanBigFileTask(Context context) {
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
//        Log.d(TAG, "travelSdkCard: " + root);
        if (lists == null) {
            return;
        }
        for (File file : lists) {
            callPath(file.getPath());
            if (file.isFile()) {

                long fileLength = file.length();
                if (fileLength < MINIMUM_MAX_LENGTH) {
                    //如果小于10MB就继续
                    continue;
                }

                String name = file.getName();

                if (name.endsWith(".log") || name.endsWith(".temp") || name.endsWith(".tmp")) {
                    ScanFileBean bean = new ScanFileBean();
                    bean.fileList.add(file);
                    bean.icon = R.drawable.clean_icon_system;
                    bean.imageDrawable = MyApplication.getContext().getResources().getDrawable(R.drawable.clean_icon_system);
                    bean.garbageType = ScanFileBean.TYPE_SYSTEM_GARBAGE;
                    bean.name = "系统垃圾";
                    bean.fileSize = file.length();
                    bean.sizeAndUnit = FileUtil.getFileSize0(file.length());

                    mGarbageList.add(bean);

                    onProgress(bean);

                } else if (name.endsWith(".bat") || name.endsWith(".sh")) {
                    ScanFileBean bean = new ScanFileBean();
                    bean.fileList.add(file);
                    bean.icon = R.drawable.clean_icon_ad;
                    bean.imageDrawable = MyApplication.getContext().getResources().getDrawable(R.drawable.clean_icon_ad);
                    bean.garbageType = ScanFileBean.TYPE_AD_GARBAGE;
                    bean.name = "广告垃圾";
                    bean.fileSize = file.length();
                    bean.sizeAndUnit = FileUtil.getFileSize0(file.length());

                    mGarbageList.add(bean);

                    onProgress(bean);

                } else if (name.toLowerCase().endsWith(".png")
                        || name.toLowerCase().endsWith(".jpg")
                        || name.toLowerCase().endsWith(".jpeg")) {

                    ScanFileBean bean = new ScanFileBean();
                    bean.fileList.add(file);
                    bean.icon = R.drawable.clean_icon_ad;
                    bean.imageDrawable = MyApplication.getContext().getResources().getDrawable(R.drawable.clean_icon_ad);
                    bean.garbageType = ScanFileBean.TYPE_PNG_OR_JPG;
                    bean.name = name;
                    bean.fileSize = file.length();
                    bean.sizeAndUnit = FileUtil.getFileSize0(file.length());

                    mGarbageList.add(bean);

                    onProgress(bean);

                } else if (name.toLowerCase().endsWith(".flv")
                        || name.toLowerCase().endsWith(".avi")
                        || name.toLowerCase().endsWith(".mov")
                        || name.toLowerCase().endsWith(".mp4")
                        || name.toLowerCase().endsWith(".wmv")) {

                    ScanFileBean bean = new ScanFileBean();
                    bean.fileList.add(file);
                    bean.icon = R.drawable.clean_icon_ad;
                    bean.imageDrawable = MyApplication.getContext().getResources().getDrawable(R.drawable.clean_icon_ad);
                    bean.garbageType = ScanFileBean.TYPE_MOVIE;
                    bean.name = name;
                    bean.fileSize = file.length();
                    bean.sizeAndUnit = FileUtil.getFileSize0(file.length());

                    mGarbageList.add(bean);

                    onProgress(bean);

                } else if (name.endsWith(".apk") || name.endsWith(".apk.1")) {

                    PackageManager pm = MyApplication.getContext().getPackageManager();
                    PackageInfo info = pm.getPackageArchiveInfo(file.getPath(), PackageManager.GET_ACTIVITIES);
                    if (info != null) {
                        ApplicationInfo appInfo = info.applicationInfo;
                        String appName = pm.getApplicationLabel(appInfo).toString();
//                        String packageName = appInfo.packageName; //得到安装包名称
                        String version = info.versionName; //得到版本信息
                        Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息

                        ScanFileBean bean = new ScanFileBean();
                        bean.fileList.add(file);
                        bean.icon = R.mipmap.ic_launcher;
                        bean.name = appName;
                        bean.dec = version;
                        bean.imageDrawable = icon;
                        bean.garbageType = ScanFileBean.TYPE_UNINSTALL;
                        bean.fileSize = file.length();
                        bean.sizeAndUnit = FileUtil.getFileSize0(file.length());

                        mGarbageList.add(bean);

                        onProgress(bean);
                    }

                } else { //以上情况都不是，就是暂时定义的垃圾文件


                    ScanFileBean bean = new ScanFileBean();
                    bean.fileList.add(file);
                    bean.icon = R.drawable.clean_big_file_noname_icon;
                    bean.imageDrawable = MyApplication.getContext().getResources().getDrawable(R.drawable.clean_big_file_noname_icon);
                    bean.garbageType = ScanFileBean.TYPE_AD_GARBAGE;
                    bean.name = file.getName();
                    bean.fileSize = file.length();
                    bean.sizeAndUnit = FileUtil.getFileSize0(file.length());

                    mGarbageList.add(bean);

                    onProgress(bean);
                    Log.d(TAG, "travelSdkCard apk : " + file.getPath() + " name = " + file.getName() + " length: " + fileLength);
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
}
