package com.mobi.overseas.clearsafe.ui.clear.util;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.ui.clear.data.WechatBean;
import com.mobi.overseas.clearsafe.ui.common.global.AppGlobalConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class QQClearUtil {
    public static final String TAG = "WechatClearUtil";

    public static final String PATH_QQ_VIDEO = "/tencent/QQ_Video";
    //头像
    public static final String PATH_QQ_ICON = "/tencent/QQ_Video";

    public static final String PATH_QQ_CACHE = "/Android/data/com.tencent.mobileqq/cache/";

    //看点视频
    public static void findQQVideoCache(IAdCacheListener listener) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            File file = new File(externalStorageDirectory.getPath() + PATH_QQ_VIDEO);
            if (!file.exists()) {
                if (listener != null) {
                    listener.onFinish();
                }
                return;
            }
            AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
                long folderSize = FileUtil.getFolderSize(file);
                String[] fileSize0 = FileUtil.getFileSize0(folderSize);
                if (listener != null) {
                    WechatBean bean = new WechatBean();
                    bean.itemType = 1;
                    bean.name = MyApplication.getContext().getString(R.string.clear_qq_video_title);
                    bean.dec = MyApplication.getContext().getString(R.string.clear_qq_video_dec);
                    bean.sizeAndUnit = fileSize0;
                    bean.isCheck = true;
                    bean.icon = R.mipmap.ic_launcher;
                    bean.fileList.add(file);
                    bean.fileSize = folderSize;

                    listener.onFindLoad(bean);
                    listener.onFinish();
                }
                Log.e(TAG, " fileSize0 ad " + fileSize0[0] + fileSize0[1]);
            });
        }
    }

    /**
     * cache的时候监听
     */
    public interface IAdCacheListener {
        void onFindLoad(WechatBean bean);

        default void onFinish() {

        }
    }

    //好友头像
    public static void findFriendIconCache(IAdCacheListener listener) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            File file = new File(externalStorageDirectory.getPath() + PATH_QQ_ICON);
            if (!file.exists()) {
                if (listener != null) {
                    listener.onFinish();
                }
                return;
            }
            AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
                long folderSize = FileUtil.getFolderSize(file);
                String[] fileSize0 = FileUtil.getFileSize0(folderSize);
                if (listener != null) {
                    WechatBean bean = new WechatBean();
                    bean.itemType = 1;
                    bean.name = MyApplication.getContext().getString(R.string.clear_qq_friend_icon_title);
                    bean.dec = MyApplication.getContext().getString(R.string.clear_qq_friend_icon_dec);
                    bean.sizeAndUnit = fileSize0;
                    bean.isCheck = true;
                    bean.icon = R.mipmap.ic_launcher;
                    bean.fileList.add(file);
                    bean.fileSize = folderSize;

                    listener.onFindLoad(bean);
                    listener.onFinish();
                }
                Log.e(TAG, " fileSize0 ad " + fileSize0[0] + fileSize0[1]);
            });
        }
    }

    /**
     * qq的cache
     *
     * @param listener
     */
    public static void findQQCache(IAdCacheListener listener) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            File file = new File(externalStorageDirectory.getPath() + PATH_QQ_CACHE);
            if (!file.exists()) {
                return;
            }
            AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
                long folderSize = FileUtil.getFolderSize(file);
                String[] fileSize0 = FileUtil.getFileSize0(folderSize);
                if (listener != null) {
                    WechatBean bean = new WechatBean();
                    bean.itemType = 1;
                    bean.name = MyApplication.getContext().getString(R.string.clear_qq_title);
                    bean.dec = MyApplication.getContext().getString(R.string.clear_qq_title);
                    bean.sizeAndUnit = fileSize0;
                    bean.isCheck = true;
                    bean.icon = R.mipmap.ic_launcher;
                    bean.fileList.add(file);
                    bean.fileSize = folderSize;

                    listener.onFindLoad(bean);
                    listener.onFinish();
                }
                Log.e(TAG, " fileSize0 ad " + fileSize0[0] + fileSize0[1]);
            });
        }
    }

    public static WechatBean createWechatBean(String name, int icon, String dec) {
        WechatBean wechatBean = new WechatBean();
        wechatBean.fileSize = 0;
        wechatBean.name = name;
        wechatBean.icon = icon;
        wechatBean.dec = dec;
        return wechatBean;
    }
}
