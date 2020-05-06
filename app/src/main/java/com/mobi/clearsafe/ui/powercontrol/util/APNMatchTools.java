package com.mobi.clearsafe.ui.powercontrol.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.mobi.clearsafe.app.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/22 17:22
 * @Dec 略
 */
public final class APNMatchTools {

    // 中国移动cmwap
    public static String CMWAP = "cmwap";

    // 中国移动cmnet
    public static String CMNET = "cmnet";

    // 中国联通3gwap APN

    public static String GWAP_3 = "3gwap";

    // 中国联通3gnet APN
    public static String GNET_3 = "3gnet";

    // 中国联通uni wap APN
    public static String UNIWAP = "uniwap";

    // 中国联通uni net APN
    public static String UNINET = "uninet";

    // 中国电信 ct wap APN
    public static String CTWAP = "ctwap";

    // 中国电信ct net　APN
    public static String CTNET = "ctnet";

    public static String matchAPN(String currentName) {

        if ("".equals(currentName) || null == currentName) {

            return "";
        }

        // 参数转为小写
        currentName = currentName.toLowerCase();
        // 检查参数是否与各APN匹配，返回匹配值
        if (currentName.startsWith(CMNET))
            return CMNET;
        else if (currentName.startsWith(CMWAP))
            return CMWAP;
        else if (currentName.startsWith(GNET_3))
            return GNET_3;

        else if (currentName.startsWith(GWAP_3))
            return GWAP_3;
        else if (currentName.startsWith(UNINET))
            return UNINET;

        else if (currentName.startsWith(UNIWAP))
            return UNIWAP;
        else if (currentName.startsWith(CTWAP))
            return CTWAP;
        else if (currentName.startsWith(CTNET))
            return CTNET;
        else if (currentName.startsWith("default"))
            return "default";
        else
            return "";
    }

    static Uri uri = Uri.parse("content://telephony/carriers/preferapn");

    // 开启APN
    public static void openAPN() {
        List<APN> list = getAPNList();
        for (APN apn : list) {
            ContentValues cv = new ContentValues();

            // 获取及保存移动或联通手机卡的APN网络匹配
            cv.put("apn", APNMatchTools.matchAPN(apn.apn));
            cv.put("type", APNMatchTools.matchAPN(apn.type));

            // 更新系统数据库，改变移动网络状态
            MyApplication.getContext().getContentResolver().update(uri, cv, "_id=?", new String[]{apn.id});
        }

    }

    // 关闭APN
    public static void closeAPN() {
        List<APN> list = getAPNList();
        for (APN apn : list) {
            // 创建ContentValues保存数据
            ContentValues cv = new ContentValues();
            // 添加"close"匹配一个错误的APN，关闭网络
            cv.put("apn", APNMatchTools.matchAPN(apn.apn) + "close");
            cv.put("type", APNMatchTools.matchAPN(apn.type) + "close");

            // 更新系统数据库，改变移动网络状态
            MyApplication.getContext().getContentResolver().update(uri, cv, "_id=?", new String[]{apn.id});
        }
    }

    public static class APN {
        String id;

        String apn;

        String type;
    }

    private static List<APN> getAPNList() {
        // current不为空表示可以使用的APN
        String projection[] = {"_id, apn, type, current"};
        // 查询获取系统数据库的内容
        Cursor cr = MyApplication.getContext().getContentResolver().query(uri, projection, null, null, null);

        // 创建一个List集合
        List<APN> list = new ArrayList<APN>();

        while (cr != null && cr.moveToNext()) {

            Log.d("ApnSwitch", "id" + cr.getString(cr.getColumnIndex("_id")) + " \n" + "apn"
                    + cr.getString(cr.getColumnIndex("apn")) + "\n" + "type"
                    + cr.getString(cr.getColumnIndex("type")) + "\n" + "current"
                    + cr.getString(cr.getColumnIndex("current")));

            APN a = new APN();

            a.id = cr.getString(cr.getColumnIndex("_id"));
            a.apn = cr.getString(cr.getColumnIndex("apn"));
            a.type = cr.getString(cr.getColumnIndex("type"));
            list.add(a);
        }

        if (cr != null)
            cr.close();

        return list;
    }

}