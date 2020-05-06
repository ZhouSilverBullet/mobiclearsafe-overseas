package com.example.adtest.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;


import static android.content.Context.TELEPHONY_SERVICE;

/**
 * author : liangning
 * date : 2019-11-14  14:31
 */
public class PhoneUtils {

    /**
     * 获取IMEI 没有权限则返回默认值 ""
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //没有获取到权限
            return "";
        } else {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            String IMEI = "";
            if (tm.getDeviceId() != null) {
                IMEI = tm.getDeviceId();
            } else {
                IMEI = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            return IMEI;
        }
    }

}
