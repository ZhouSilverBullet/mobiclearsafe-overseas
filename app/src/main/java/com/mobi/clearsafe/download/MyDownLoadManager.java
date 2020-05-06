package com.mobi.clearsafe.download;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.adtest.utils.SharedPreferencesUtils;
import com.mobi.clearsafe.utils.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * author : liangning
 * date : 2019-12-26  10:51
 */
public class MyDownLoadManager {

    private static DownLoadCompleteReceiver receiver;

    public static HashMap<Long, String> downloadMap = new HashMap<>();//下载中 以下载ID当key

    public static List<String> installedAPP = new ArrayList<>();//已经在下载过的APP


    /**
     * 注册监听广播
     *
     * @param context
     */
    public static void registerBroadCast(Context context) {
        receiver = new DownLoadCompleteReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //应用下载完成广播
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        //应用安装完成广播
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
//        intentFilter.addDataScheme("package");
        context.registerReceiver(receiver, intentFilter);
    }

    /**
     * 取消监听广播
     *
     * @param context
     */
    public static void unRegisterBroadCast(Context context) {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
        }
    }

    public static boolean downLoadApk(Context context, String url, String pkgName,String appName) {
        try {

            //创建request对象
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

            //设置什么网络下可以下载  如不设置则默认全部网络可下载
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
            //设置通知栏标题
            request.setTitle("下载");
            //设置通知栏的message
            request.setDescription(appName);
            //设置漫游状态下是否可下载
            request.setAllowedOverRoaming(true);

            String fileName = DataUtils.PkgPointTo_(pkgName);
            //设置文件存放目录
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName);
            //设置系统服务
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            //进行下载
            long id = downloadManager.enqueue(request);
            downloadMap.put(id, pkgName);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 查找自己记录的下载 id列表中是否有该id
     *
     * @param receiverID
     * @return
     */
    public static String getDownFileName(long receiverID) {
        String downLoadName = null;
        if (downloadMap.get(receiverID) != null) {
            downLoadName = downloadMap.get(receiverID);
        }
        return downLoadName;
    }

    public static void setInstalledAPP(Context context, String pkgName) {
        if (installedAPP == null) {
            installedAPP = new ArrayList<>();
        }
        installedAPP.add(pkgName);
        SharedPreferencesUtils.saveArray(context, installedAPP);
    }

    public static List<String> getInstalledApp() {
        return installedAPP;
    }

    public static void initInstalledAPP(Context context) {
        if (installedAPP == null) {
            installedAPP = new ArrayList<>();
        }
        SharedPreferencesUtils.loadArray(context, installedAPP);
    }

}
