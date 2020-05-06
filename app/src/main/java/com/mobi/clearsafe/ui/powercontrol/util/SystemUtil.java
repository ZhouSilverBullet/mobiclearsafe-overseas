package com.mobi.clearsafe.ui.powercontrol.util;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import com.mobi.clearsafe.app.MyApplication;

import java.lang.annotation.Retention;
import java.lang.reflect.Method;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/22 16:56
 * @Dec 略
 */
public class SystemUtil {


    public static boolean getWifiState() {
        Context context = MyApplication.getContext();
        int state = ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getWifiState();
        boolean bool = false;
        switch (state) {
            default:
                return false;
            case 2:
            case 3:
                bool = true;
        }
        return bool;
    }

    public static boolean openWifi() {
        Context context = MyApplication.getContext();
        WifiManager localWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return localWifiManager.setWifiEnabled(!getWifiState());
    }

    public static void setMobileDataEnabled() {
        Context context = MyApplication.getContext();
        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
//        if (isMobile(context)) {
////            setGprsEnabled(context, false);
//        } else {
////            setGprsEnabled(context, true);
//        }
    }

    //判断移动数据是否打开
//    public static boolean isMobile(Context context) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
//            return true;
//        }
//        return false;
//    }
//
//    //打开或关闭GPRS
//    public static boolean gprsSetter(Context context) {
//        //检测当前GPRS是否打开
//        boolean isOpen = gprsIsOpenMethod(context);
//
//        if (isOpen) {
//            //GPRS已打开，则执行关闭
//            setGprsEnabled(context, false);
//            System.out.println("关闭");
//        } else {
//            //GPRS已关闭，则执行打开
//            setGprsEnabled(context, true);
//            System.out.println("开启");
//        }
//
//        //返回修改后的GPRS状态
//        return !isOpen;
//    }

    //Android 判断是否打开移动网络开关
    public static boolean gprsIsOpenMethod(Context context) {

        ConnectivityManager mCM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass = mCM.getClass();
        Class[] argClasses = null;
        Object[] argObject = null;

        Boolean isOpen = false;

        try {
            Method method = cmClass.getMethod("getMobileDataEnabled", argClasses);
            isOpen = (Boolean) method.invoke(mCM, argObject);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isOpen;
    }

    //开启/关闭GPRS
    public static void setGprsEnabled(Context context, boolean isEnable) {

        ConnectivityManager mCM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass = mCM.getClass();
        Class[] argClasses = new Class[1];
        argClasses[0] = boolean.class;

        try {
            Method method = cmClass.getMethod("setMobileDataEnabled", argClasses);
            method.invoke(mCM, isEnable);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void bluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Context context = MyApplication.getContext();
        if (Build.VERSION.SDK_INT < 18) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        } else {
            BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = bluetoothManager.getAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bluetoothAdapter.disable();
            } else {
                bluetoothAdapter.enable();
            }
        }
    }

    public static boolean isBluetooth() {
        Context context = MyApplication.getContext();
        if (Build.VERSION.SDK_INT < 18) {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            return bluetoothAdapter.isEnabled();
        } else {
            BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
            return bluetoothAdapter.isEnabled();
        }
    }

    public static boolean checkPermission() {
        Context context = MyApplication.getContext();
        if (Build.VERSION.SDK_INT >= 23 && !isPermission(context)) {

            Uri selfPackageUri = Uri.parse("package:"
                    + context.getPackageName());
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                    selfPackageUri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            return Settings.System.canWrite(context);
        }
        return true;
    }

    public static boolean isPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return Settings.System.canWrite(context);
    }


    // 开启亮度自动调节
    public static void startAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

        Uri uri = Settings.System
                .getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        context.getContentResolver().notifyChange(uri, null);
    }


    // 停止自动亮度调节
    public static void stopAutoBrightness(Context context) {

        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

    }

    // 获取状态
    public static boolean getAutoBrightness(Context context) {

        int i = 0;
        try {
            i = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i == 1;

    }

    // 开启屏幕旋转
    public static void setAccelerometerRotation(Context context, boolean isOpen) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION,
                isOpen ? 1 : 0);

//        Uri uri = Settings.System
//                .getUriFor(Settings.System.ACCELEROMETER_ROTATION);
//        context.getContentResolver().notifyChange(uri, null);
    }

    // 获取状态
    public static boolean getAccelerometerRotation(Context context) {

        int i = 0;
        try {
            i = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i == 1;

    }

    /**
     * 情景模式
     *
     * @return
     */
    public static int getAudioMode() {
        AudioManager audioManager = (AudioManager) MyApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        switch (ringerMode) {
            case AudioManager.RINGER_MODE_NORMAL:
                //normal
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                //vibrate
                break;
            case AudioManager.RINGER_MODE_SILENT:
                //silent
                break;
        }
        return ringerMode;
    }

    /**
     * case AudioManager.RINGER_MODE_NORMAL:
     * case AudioManager.RINGER_MODE_VIBRATE:
     * case AudioManager.RINGER_MODE_SILENT:
     * <p>
     * 情景模式设置的时候要加通知权限判断
     *
     * @return
     */
    public static void setAudioMode() {
        NotificationManager notificationManager = (NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23 && !notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent("android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getContext().startActivity(intent);
            return;
        }

        AudioManager audioManager = (AudioManager) MyApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
        int ringerMode = audioManager.getRingerMode();
        switch (ringerMode) {
            case 2:
                audioManager.setRingerMode(1);
                break;
            case 1:
                audioManager.setRingerMode(0);
                break;
            default:
                audioManager.setRingerMode(2);
                break;
        }

    }


    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isGpsOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    public static int getScreenTimeOut(Context context) {
        int result = 0;
        try {
            result = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static final int[] SCREEN_TIME = {30000, 60000, 120000};

    public static void setScreenTimeOut(Context context, int level) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, SCREEN_TIME[(level + 1) % 3]);
    }
}

