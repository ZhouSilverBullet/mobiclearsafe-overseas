package com.mobi.overseas.clearsafe.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;


import java.io.File;

/**
 * author : liangning
 * date : 2019-12-26  11:44
 */
public class DownLoadCompleteReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // 监听下载完成广播
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            //在广播中取出下载任务的id
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            String fileName = MyDownLoadManager.getDownFileName(id);
            if (fileName != null) {
//                Toast.makeText(context, "编号：" + id + "的下载任务已经完成！", Toast.LENGTH_SHORT).show();
                DownloadManager.Query query = new DownloadManager.Query();
                DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                query.setFilterById(id);
                Cursor c = dm.query(query);
                if (c != null) {
                    try {
                        if (c.moveToFirst()) {
                            //获取文件下载路径
//                            String filename = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//                            String filePath = null;
//                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//                                if (filename != null) {
//                                    filePath = Uri.parse(filename).getPath();
//                                }
//                            } else {
//                                //Android 7.0以上的方式：请求获取写入权限，这一步报错
//                                //过时的方式：DownloadManager.COLUMN_LOCAL_FILENAME
//                                int fileNameIdx = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
//                                filePath = c.getString(fileNameIdx);
//                            }

                            String filePath;
                            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                                filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            }else{
                                filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                            }
                            int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                //启动安装
//                                Uri uri = getUriForFile(context, new File(filePath));
//                                if (uri != null) {
//                                    Intent install = new Intent(Intent.ACTION_VIEW);
//                                    install.setDataAndType(uri, "application/vnd.android.package-archive");
//                                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                                    }
//                                    context.startActivity(install);
//                                }
                                if (MyDownLoadManager.downloadMap != null) {
                                    if (MyDownLoadManager.downloadMap.get(id) != null) {
                                        installApl(context,MyDownLoadManager.downloadMap.get(id));
                                        MyDownLoadManager.setInstalledAPP(context, MyDownLoadManager.downloadMap.get(id));
                                    }
                                    MyDownLoadManager.downloadMap.remove(id);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    } finally {
                        c.close();
                    }

                }
            }
        } else if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {

//            Log.e("已安装", "已安装");
        } else if (Intent.ACTION_INSTALL_PACKAGE.equals(intent.getAction())) {
//            Log.e("已安装", "已安装");
        }
    }

    //解决Android 7.0之后的Uri安全问题
    private Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    private void installApl(Context context,String pkgName){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath()
                + "/" + pkgName.replace(".", "_") + ".apk";
//        File file = new File(Environment.getExternalStorageDirectory()+"/"+Environment.DIRECTORY_DOWNLOADS,pkgName.replace(".","_")+".apk");
        File file = new File(filePath);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
//            File file = new File(context.getExternalFilesDir(null)+"/"+ Environment.DIRECTORY_DOWNLOADS,context.getString(R.string.app_name_en)+".apk");
            Uri apkUri= FileProvider.getUriForFile(context, "com.mobi.overseas.clearsafe.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

}
