package com.mobi.clearsafe.ui.clear.util;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mobi.clearsafe.ui.common.global.AppGlobalConfig;

import java.io.File;
import java.util.List;

public class FileManager {
    public static final String TAG = "FileManager";

    /**
     * 扫描 android/data/xx.xx.xx/cache类似的缓存
     */
    public static final int CACHE = 1;

    public static int currentLength;

    public static void scaFile(Handler handler) {
        currentLength = 0;
        //判断是挂载
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
                File externalStorageDirectory = Environment.getExternalStorageDirectory();
                File file = new File(externalStorageDirectory.getPath() + "/Android/data");

                //把所有的Cache目录扫出来
                List<File> cacheFolder = FileUtil.getCacheFolder(file, new IFileLengthListener() {
                    @Override
                    public void onFileLen(long length) {
                        currentLength += length;
                        Message message = Message.obtain();
                        message.what = 1000;
                        message.obj = FileUtil.getFileSize0(currentLength);
                        handler.sendMessage(message);
                    }
                });

                Log.e(TAG, "扫描完毕");

            });
        } else {
            //文件读取失败
        }
    }

}
