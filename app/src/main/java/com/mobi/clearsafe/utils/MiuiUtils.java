package com.mobi.clearsafe.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;


public class MiuiUtils {
    private MiuiUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    public static final int REQUEST_CODE_SERVICE_SMS = 100;


    /**
     * @return whether or not is MIUI
     * @link http://dev.xiaomi.com/doc/p=254/index.html
     */
    public static boolean isMIUI() {
        String device = Build.MANUFACTURER;
        LogUtils.e("Build.MANUFACTURER = " + device);
        if (device.equals("Xiaomi")) {
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(new File(Environment
                        .getRootDirectory(), "build.prop")));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } else {
            return false;
        }
    }


    public static void goPermissionSettings(Activity context) {
        Intent intent;
        try {//MIUI8/9
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivityForResult(intent, REQUEST_CODE_SERVICE_SMS);
        } catch (Exception e) {
            try {//MIUI5/6
                intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter",
                        "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getPackageName());
                context.startActivityForResult(intent, REQUEST_CODE_SERVICE_SMS);
            } catch (Exception e1) {
                //应用信息界面
                intent = new Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("com.dascz.bba", context.getPackageName(),
                        null);
                intent.setData(uri);
                context.startActivityForResult(intent, REQUEST_CODE_SERVICE_SMS);
            }
        }
    }

    /**
     * 判断MIUI版本
     * @param propName
     * @return
     */
    public static String getSystemProperty(String propName) {
        String line = null;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
            return line;
        } catch (IOException ex) {
            Log.e("---", "Unable to read sysprop " + propName, ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e("---", "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }

}
