package com.mobi.overseas.clearsafe.utils;

import java.io.Serializable;

/**
 * 手机内安装应用信息
 * author : liangning
 * date : 2019-11-01  16:27
 */
public class AppInfo implements Serializable {
    public String appName ="";
    public String packageName = "";
    public String versionName = "";
    public long versionCode = 0;
}
