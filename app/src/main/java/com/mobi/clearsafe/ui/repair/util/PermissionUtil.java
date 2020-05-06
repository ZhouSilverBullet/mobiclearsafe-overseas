package com.mobi.clearsafe.ui.repair.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/17 14:33
 * @Dec 略
 */
public class PermissionUtil {

    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限  false-表示权限已开启
     */
    public static boolean lacksPermissions(Context mContexts, String[] permissions) {
        for (String permission : permissions) {
            if (lacksPermission(mContexts, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限  false-表示权限已开启
     */
    public static boolean lacksPermissions(Context mContexts, List<String> permissions) {
        for (String permission : permissions) {
            if (lacksPermission(mContexts, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否缺少权限
     */
    public static boolean lacksPermission(Context mContexts, String permission) {

        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;

    }

    /**
     * 判断是否有权限
     */
    public static boolean enablePermission(Context mContexts, String permission) {

        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_GRANTED;

    }

    /**
     * 判断是否有权限
     */
    public static boolean enablePermission(Context mContexts, String...permissions) {
        for (String permission : permissions) {
            if (!enablePermission(mContexts, permission)) {
                return false;
            }
        }
        return true;

    }
}
