package com.mobi.clearsafe.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;


import com.meituan.android.walle.WalleChannelReader;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * author : liangning
 * date : 2019-10-24  09:26
 */
public class AppUtil {
    public static final String TAG = "AppUtil";

    private static long pkgCode = -1;//应用版本号
    private static Locale mLocale = null;//当前系统语言

    /**
     * 获取 应用版本号
     *
     * @param context
     * @return
     */
    public static long packageCode(Context context) {
        if (pkgCode != -1) {
            return pkgCode;
        } else {
            PackageManager pm = context.getPackageManager();

            try {
                PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    pkgCode = info.getLongVersionCode();
                } else {
                    pkgCode = info.versionCode;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return pkgCode;
    }

    /**
     * 获取应用版本名称
     *
     * @param context
     * @return
     */
    public static String packageName(Context context) {
        PackageManager pm = context.getPackageManager();
        String name = "";
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获取手机内 安装应用列表
     *
     * @return
     */
    public static List<AppInfo> getAppList(Context context) {
        List<AppInfo> list = new ArrayList<>();
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        int size = packages.size();
        for (int i = 0; i < size; i++) {
            PackageInfo info = packages.get(i);
            AppInfo appInfo = new AppInfo();
            appInfo.appName = info.applicationInfo.loadLabel(context.getPackageManager()).toString();
            appInfo.packageName = info.packageName;
            appInfo.versionName = info.versionName;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appInfo.versionCode = info.getLongVersionCode();
            } else {
                appInfo.versionCode = info.versionCode;
            }
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {//非系统应用
                list.add(appInfo);
            }
        }
        return list;
    }


    /**
     * 复制内容到剪切板
     *
     * @param copyStr
     * @return
     */
    public static boolean copy(Context context, String copyStr) {
        try {
            //获取剪贴板管理器
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyStr);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取系统语言
     *
     * @return
     */
    public static Locale getLocale(Context context) {
        if (mLocale != null) {
            return mLocale;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mLocale = context.getResources().getConfiguration().getLocales().get(0);
            } else {
                mLocale = context.getResources().getConfiguration().locale;
            }
        }
        return mLocale;
    }


    /**
     * 版本更新 格式化富文本
     *
     * @param str
     * @return
     */
    public static String[] splitPTag(String str) {
        String[] strResult;

        if (!TextUtils.isEmpty(str)) {
            strResult = str.split("<p>");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < strResult.length; i++) {
                if (i != 0) {
                    list.add(strResult[i].replace("</p>", ""));
                }
            }

            String[] result = new String[list.size()];
            result = list.toArray(result);
            return result;
        } else {
            return null;
        }
    }


    /**
     * 获取网络类型
     *
     * @return
     */
    public static String getNetworkTypeName() {
        ConnectivityManager ConnectivityManager = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return networkInfo.getSubtypeName();
            } else {
                return networkInfo.getTypeName();
            }
        }

        return "";
    }


    /**
     * 获取运营商
     *
     * @return
     */
    public static String getSimOperatorInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) MyApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String operatorString = telephonyManager.getSimOperator();

        if (operatorString == null) {
            return "";
        }
        if (operatorString.equals("46000") || operatorString.equals("46002") || operatorString.equals("46007")) {
            //中国移动
            return "yidong";
        } else if (operatorString.equals("46001") || operatorString.equals("46006") || operatorString.equals("46009")) {
            //中国联通
            return "liantong";
        } else if (operatorString.equals("46003") || operatorString.equals("46005") || operatorString.equals("46011")) {
            //中国电信
            return "dianxin";
        }
        //error
        return "";
    }

    /**
     * 获取渠道名
     *
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Context mContext) {
        if (mContext == null) {
            return null;
        }
//        String channelName = "";
        //美团walle打渠道包
        String channelName = WalleChannelReader.getChannel(mContext.getApplicationContext());
        Log.e(TAG, "channelName : " + channelName);
        if (!TextUtils.isEmpty(channelName)) {
            return channelName;
        }

        try {
            PackageManager packageManager = mContext.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.
                        getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 通过全路径包名启动Activity
     */
    public static void startActivityFromStrName(Context context, String pkgName) {
        try {
            Class activityClass = Class.forName(pkgName);
            Intent intent = new Intent(context, activityClass);
            context.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过别名启动Activity 传入参数key值固定 如需其他参数 请自定义
     *
     * @param context
     * @param action
     * @param putData 传入的参数 json格式 各个activity自己读取
     */
    public static void startActivityFromAction(Context context, String action, String putData) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (!TextUtils.isEmpty(putData)) {
            Bundle bundle = new Bundle();
            bundle.putString("putJson", putData);
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 获取固定传入activity key的参数
     *
     * @param context
     * @return
     */
    public static String getBundleData(Activity context) {
        String s = "";
        Bundle bundle = context.getIntent().getExtras();
        if (bundle != null) {
            s = bundle.getString("putJson");
        }
        return s;
    }


    /**
     * 获取IMEI 没有权限则返回默认值 ""
     *
     * @param
     * @return
     */
    public static String getIMEI() {
        String imei = "";
        if (!TextUtils.isEmpty(UserEntity.getInstance().getImei())) {
            imei = UserEntity.getInstance().getImei();
        } else {
            if (ActivityCompat.checkSelfPermission(MyApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                //没有获取到权限
                imei = "";
            } else {
                TelephonyManager tm = (TelephonyManager) MyApplication.getContext().getSystemService(TELEPHONY_SERVICE);
                if (tm.getDeviceId() != null) {
                    imei = tm.getDeviceId();
                } else {
                    imei = Settings.Secure.getString(MyApplication.getContext().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                }
                UserEntity.getInstance().setImei(imei);
            }
        }
        return TextUtils.isEmpty(imei) ? "" : imei;
    }

    /**
     * 获取mac 没有权限则返回默认值 ""
     *
     * @param
     * @return
     */
    public static String getMac() {
        String mac = "";
        if (!TextUtils.isEmpty(UserEntity.getInstance().getMac())) {
            mac = UserEntity.getInstance().getMac();
        } else {
            mac = GetDeviceId.getLocalMac(MyApplication.getContext()).replace(":", "");
            UserEntity.getInstance().setMac(mac);
        }
        return TextUtils.isEmpty(mac) ? "" : mac;
    }


    /**
     * 获取androidID 没有权限则返回默认值 ""
     *
     * @param
     * @return
     */
    public static String getAndroidID() {
        String android_id = "";
        if (!TextUtils.isEmpty(UserEntity.getInstance().getAndroid_id())) {
            android_id = UserEntity.getInstance().getAndroid_id();
        } else {
            android_id = Settings.Secure.getString(
                    MyApplication.getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );
            UserEntity.getInstance().setAndroid_id(android_id);
        }

        return TextUtils.isEmpty(android_id) ? "" : android_id;
    }

    // return x, x px = v dp
    public static int dpToPx(float v) {
        Resources resource = Resources.getSystem();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resource.getDisplayMetrics());
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }


    public static String Md5(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(string.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                int c = b & 0xff; //负数转换成正数
                String result = Integer.toHexString(c); //把十进制的数转换成十六进制的书
                if (result.length() < 2) {
                    sb.append(0); //让十六进制全部都是两位数
                }
                sb.append(result);
            }
            return sb.toString(); //返回加密后的密文
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * 判断某个应用是否安装
     *
     * @param context
     * @param pkgName
     * @return true 已安装  false 未安装
     */
    public static boolean checkAppInstalled(Context context, String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            packageInfo = null;
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通过 deeplink启动APP
     *
     * @param deeplink
     * @return
     */
    public static boolean startAPPforDeepLink(Context context, String deeplink) {
        boolean isStarted = false;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deeplink));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            isStarted = true;
        } catch (Exception e) {
            isStarted = false;
        }
        return isStarted;

    }

    /**
     * 通过文件路径 安装APP
     *
     * @param filePath
     */
    public static void installAPP(Context context, String filePath) {

        //启动安装
        Uri uri = getUriForFile(context, new File(filePath));
        if (uri != null) {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(uri, "application/vnd.android.package-archive");
            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            context.startActivity(install);
        }

    }


    //解决Android 7.0之后的Uri安全问题
    public static Uri getUriForFile(Context context, File file) {
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

    /**
     * 判断程序是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean isAppIsBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //getRunningTask方法在Android5.0以上已经被废弃，只会返回自己和系统的一些不敏感的task，不再返回其他应用的task，
        // 用此方法来判断自身App是否处于后台，仍然是有效的，但是无法判断其他应用是否位于前台，因为不再能获取信息
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        if (componentInfo.getPackageName().equals(context.getPackageName())) {
            isInBackground = false;
        }
        return isInBackground;
    }


    /**
     * 检测当前选中的APP是否已下载
     */
    public static boolean checkDownLoad(String pkgName, Context context) {
        String path = getFilePath(pkgName, context);
        return FileUtils.checkFileExists(path);
    }

    /**
     * 获取文件存储地址
     *
     * @param pkgName
     * @return
     */
    public static String getFilePath(String pkgName, Context context) {
        String filePath = "";
        if (pkgName == null) {
            return "";
        }
        try {
            filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath()
                    + "/" + pkgName.replace(".", "_") + ".apk";
        } catch (Exception e) {

        }
        return filePath;
    }


    //将字符串中的指定内容改变颜色
    public static SpannableString matcherSearchText(int color, String text, String keyword) {
        SpannableString string = new SpannableString(text);
        Pattern pattern = Pattern.compile(keyword);
        Matcher matcher = pattern.matcher(string);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            string.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return string;
    }


    //判断华为渠道是否出广告
    public static boolean HWIsShowAd() {
        if (AppUtil.getChannelName(MyApplication.getContext()).equals("huawei") && AppUtil.packageName(MyApplication.getContext()).equals(UserEntity.getInstance().getConfigEntity().getAndroid_version()) && UserEntity.getInstance().getConfigEntity().isIs_ad() == false) {
            // Log.e("rrrrr","不展示");
            return false;//华为不展示广告
        } else {
            //Log.e("rrrrr","展示");
            return true;//展示广告
        }
    }

    //判断小米平台展示数据情况
    public static boolean isXiaomi() {
        if (AppUtil.getChannelName(MyApplication.getContext()).equals("xiaomi") && AppUtil.packageName(MyApplication.getContext()).equals(UserEntity.getInstance().getConfigEntity().getAndroid_version()) && UserEntity.getInstance().getConfigEntity().isMi_test() == true) {
            return true;//小米审核状态
        } else {
            return false;
        }
    }


}
