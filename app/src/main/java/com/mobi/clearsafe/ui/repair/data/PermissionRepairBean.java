package com.mobi.clearsafe.ui.repair.data;

import android.Manifest;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.main.fragment.HomeFragPresenter;
import com.mobi.clearsafe.ui.common.util.NotificationsUtils;
import com.mobi.clearsafe.ui.common.util.SpUtil;
import com.mobi.clearsafe.ui.repair.util.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/17 11:00
 * @Dec 略
 */
public class PermissionRepairBean implements MultiItemEntity {
    /**
     * 判断本地是否有权限访问
     */
    private boolean isHasPermission;

    private int icon;
    private String title;
    private String dec;


    /**
     * 是否数据加载了
     */
    private boolean isLoaded;


    private int id;
    /**
     * 金币数量
     */
    private int points;
    private int popType;
    private String popUpMsg;

    /**
     * 0-未领取 1-领取未翻倍 2-已翻倍
     */
    private int state;

    /**
     * 跳转类型
     */
    private int skipType;

    private String adPath;

    private int itemType;

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public int getSkipType() {
        return skipType;
    }

    public void setSkipType(int skipType) {
        this.skipType = skipType;
    }

    public String getAdPath() {
        return adPath;
    }

    public void setAdPath(String adPath) {
        this.adPath = adPath;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public int getPopType() {
        return popType;
    }

    public void setPopType(int popType) {
        this.popType = popType;
    }

    public String getPopUpMsg() {
        return popUpMsg;
    }

    public void setPopUpMsg(String popUpMsg) {
        this.popUpMsg = popUpMsg;
    }

    public int getState() {
        return state;
    }

    public boolean hasGetPoints() {
        return state == 0;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isHasPermission() {
        return isHasPermission;
    }

    public void setHasPermission(boolean hasPermission) {
        isHasPermission = hasPermission;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PermissionRepairBean(int icon, String title, String dec, int skipType, String adPath, int itemType, boolean isHasPermission) {
        this.icon = icon;
        this.title = title;
        this.dec = dec;
        this.skipType = skipType;
        this.adPath = adPath;
        this.itemType = itemType;
        this.isHasPermission = isHasPermission;
    }

    public PermissionRepairBean() {
    }

    public static List<PermissionRepairBean> createList() {
        ArrayList<PermissionRepairBean> list = new ArrayList<>();
        list.add(new PermissionRepairBean(R.drawable.permission_repair_speed,
                "释放空间，手机瘦身", "需开启允许查看使用情况权限", 1, "", 0,
                hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)));
        list.add(new PermissionRepairBean(R.drawable.permission_repair_notice,
                "空间不足、金币到账信息告知", "需开启允许查看使用情况权限", 2, "", 0,
                NotificationsUtils.isNotificationEnabled()));

        list.add(new PermissionRepairBean(R.drawable.permission_repair_location,
                "定位权限", "需开启允许查看使用情况权限", 3, "", 0,
                hasPermission(HomeFragPresenter.PERMISSION_NEED[1])));

        list.add(new PermissionRepairBean(R.drawable.permission_repair_phonenumber,
                "获取手机设备ID", "需开启允许查看使用情况权限", 4, "", 0,
                hasPermission(HomeFragPresenter.PERMISSION_NEED[2])));

        list.add(new PermissionRepairBean(R.drawable.permission_repair_appwidget,
                "开启桌面小工具", "需开启允许查看使用情况权限", 5, "", 0,
                SpUtil.getBoolean(Const.APP_WIDGET_ENABLE, false)));

//        list.add(new PermissionRepairBean(R.drawable.clean_icon_ad,
//                "空间不足、金币到账信息告知", "需开启允许查看使用情况权限", 3, "", 0,
//                hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)));
        return list;
    }

    private static boolean hasPermission(String permission) {
        return !PermissionUtil.lacksPermission(MyApplication.getApplication(), permission);
    }
}
