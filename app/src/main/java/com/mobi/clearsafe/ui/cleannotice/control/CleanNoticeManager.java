package com.mobi.clearsafe.ui.cleannotice.control;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.cleannotice.data.CleanNoticeBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/20 20:28
 * @Dec 略
 */

public class CleanNoticeManager {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
    public static final String QQ_PKG = "com.tencent.mobileqq";
    public static final String WECHAT_PKG = "com.tencent.mm";
    //USB 等一些通知
    public static final String ANDROID_SYSTEM = "android";

    private CleanNoticeManager() {
    }

    public static CleanNoticeManager getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public void clearAll() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        NotificationListener service = NotificationListener.getService();
        if (service != null) {
            service.cancelAllNotifications();
        }
    }

    private static class SingletonHolder {
        private static final CleanNoticeManager INSTANCE = new CleanNoticeManager();
    }

    public void init(Context context) {
        //注册好running的广播
        NoticeReceiver noticeReceiver = new NoticeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotificationListener.ACTION_NOTIFY_LISTENER_SERVICE_CONNECT);
        intentFilter.addAction(NotificationListener.ACTION_NOTIFY_LISTENER_SERVICE_REMOVED);
        intentFilter.addAction(NotificationListener.ACTION_NOTIFY_LISTENER_SERVICE_POSTED);
        context.registerReceiver(noticeReceiver, intentFilter);

        if (!isRunning()) {
            toggleNotificationListenerService(context);
        }
    }

    private void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, NotificationListener.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName(context, NotificationListener.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public List<CleanNoticeBean> getNoticeBean() {
        ArrayList<CleanNoticeBean> list = new ArrayList<>();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return list;
        }

        if (!isRunning()) {
            return list;
        }

        NotificationListener service = NotificationListener.getService();
        if (service == null) {
            return list;
        }

        StatusBarNotification[] activeNotifications = service.getActiveNotifications();
        for (StatusBarNotification activeNotification : activeNotifications) {
            CleanNoticeBean cleanNoticeBean = new CleanNoticeBean();

            Bundle extras = activeNotification.getNotification().extras;
            String title = extras.getString(Notification.EXTRA_TITLE); //通知title
            String content = extras.getString(Notification.EXTRA_TEXT); //通知内容
//            Bitmap largeIcon = extras.getParcelable(Notification.EXTRA_LARGE_ICON); //通知的大图标，注意和获取小图标的区别
//            Bitmap largeIcon = extras.getParcelable(Notification.EXTRA_LARGE_ICON_BIG); //通知的大图标，注意和获取小图标的区别

//            int smallIconId = extras.getInt(Notification.EXTRA_SMALL_ICON); //通知小图标id
            String packageName = activeNotification.getPackageName();

            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)
                    //USB调试什么的都调用这个
                    && !isWhiteName(packageName)) {
                cleanNoticeBean.setTitle(title);
                cleanNoticeBean.setContent(content);
//                cleanNoticeBean.setIcon(getSmallIcon(MyApplication.getContext(), packageName, smallIconId));
                cleanNoticeBean.setDrawableIcon(getAppIcon(MyApplication.getContext(), packageName));
//                cleanNoticeBean.setBigIcon(getSmallIcon(MyApplication.getContext(), packageName, activeNotification.getNotification().icon));
                cleanNoticeBean.setTime(sdf.format(activeNotification.getPostTime()));

                list.add(cleanNoticeBean);
            }

        }
        return list;
    }

    /**
     * 过滤的名单，是这些的都不进行显示
     * @param packageName
     * @return
     */
    private boolean isWhiteName(String packageName) {
        return ANDROID_SYSTEM.equals(packageName)
                || QQ_PKG.equals(packageName)
                || WECHAT_PKG.equals(packageName);
    }

    public boolean isRunning() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return false;
        }
        return NotificationListener.isRunning();
    }

    public interface INoticeCallback {

        /**
         * 数据刷新的时候回调
         */
        void onNotificationNotify();
    }

    private INoticeCallback mNoticeCallback;

    public void setNoticeCallback(INoticeCallback noticeCallback) {
        mNoticeCallback = noticeCallback;
    }

    static class NoticeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            INoticeCallback noticeCallback = CleanNoticeManager.getInstance().mNoticeCallback;
            if (noticeCallback != null) {
                noticeCallback.onNotificationNotify();
            }
        }
    }

    /**
     * @param context
     * @param id      图标id
     * @param pkgName 图标所在的包名
     * @return bitmap
     */
    public static Bitmap getSmallIcon(Context context, String pkgName, int id) {
        Bitmap smallIcon = null;
        Context remotePkgContext;
        try {
            remotePkgContext = context.createPackageContext(pkgName, 0);
            Drawable drawable = remotePkgContext.getResources().getDrawable(id);
            if (drawable != null) {
                smallIcon = ((BitmapDrawable) drawable).getBitmap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return smallIcon;
    }

    /**
     * 根据包名获取App的Icon
     *
     * @param pkgName 包名
     */
    public static Drawable getAppIcon(Context context, String pkgName) {
        try {
            if (null != pkgName) {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo info = pm.getApplicationInfo(pkgName, 0);
                return info.loadIcon(pm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getResources().getDrawable(R.drawable.power_scan_az_new_icon);
    }

}
