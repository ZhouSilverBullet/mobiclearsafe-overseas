package com.mobi.overseas.clearsafe.main.adapter.data;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClearBean implements MultiItemEntity {
    public int itemType;

    public int icon;

    //清理的类型
    public int clearType;
    //清理的名字
    public String clearName;

    public String dec;

    public int id;

    public int points;

    //这个就给加速的用
    public int memoryValue;
    //是否已经清理
    public boolean isClear = true;

    @ColorRes
    public int color = R.color.black_33;

    public ClearBean(int itemType, int clearType, String clearName) {
        this.itemType = itemType;
        this.clearType = clearType;
        this.clearName = clearName;
    }

    public ClearBean(int itemType, int clearType, String clearName, String dec, int color) {
        this.itemType = itemType;
        this.clearType = clearType;
        this.clearName = clearName;
        this.dec = dec;
        this.color = color;
    }

    public ClearBean(int itemType, int icon, int clearType, String clearName, String dec, int color) {
        this.itemType = itemType;
        this.icon = icon;
        this.clearType = clearType;
        this.clearName = clearName;
        this.dec = dec;
        this.color = color;
    }

    public ClearBean() {
    }

    public static List<ClearBean> getRvGridData() {
        List<ClearBean> list = new CopyOnWriteArrayList<>();
        list.add(new ClearBean(1, R.drawable.clean_icon_speed_orange, 1, MyApplication.getResString(R.string.phoneSpeed), "一键清理，释放空间", R.color.black_33));
//        list.add(new ClearBean(1, R.drawable.clean_icon_home_wechat, 2, "微信专清", "正在获取...", R.color.black_33));
        list.add(new ClearBean(1, R.drawable.clean_icon_powercontrol, 3, MyApplication.getResString(R.string.powerManagement), "正在获取...", R.color.black_33));
        list.add(new ClearBean(1, R.drawable.clean_icon_power, 4, MyApplication.getResString(R.string.phoneCool), "快来给手机降温", R.color.black_33));
        list.add(new ClearBean(1, R.drawable.clean_icon_bigfile, 5, MyApplication.getResString(R.string.installationPackageManagement), "", R.color.black_33));
        list.add(new ClearBean(1, R.drawable.clean_icon_notice, 6, MyApplication.getResString(R.string.notificationClean), "", R.color.black_33));
        return list;
    }

    /**
     * 工具箱
     *
     * @return
     */
    public static List<ClearBean> getRvGridBoxData() {
        List<ClearBean> list = new CopyOnWriteArrayList<>();
        list.add(new ClearBean(1, R.drawable.toolbox_apk, 1, MyApplication.getResString(R.string.installationPackageManagement), "", R.color.black_33));
        list.add(new ClearBean(1, R.drawable.toolbox_file, 2, MyApplication.getResString(R.string.bigFileClean), "", R.color.black_33));
        list.add(new ClearBean(1, R.drawable.toolbox_safe_power, 3, MyApplication.getResString(R.string.superPower), "", R.color.black_33));
        list.add(new ClearBean(1, R.drawable.toolbox_notice, 4, MyApplication.getResString(R.string.notificationClean), "", R.color.black_33));
        list.add(new ClearBean(1, R.drawable.toolbox_powering, 5, MyApplication.getResString(R.string.powerManagement), "", R.color.black_33));
//        list.add(new ClearBean(1, R.drawable.toolbox_appwidget, 6, MyApplication.getResString(R.string.cleanTools), "", R.color.black_33));
        return list;
    }

    /**
     * 工具箱
     *
     * @return
     */
    public static List<ClearBean> getRvGridBoxData2() {
        List<ClearBean> list = new CopyOnWriteArrayList<>();
        list.add(new ClearBean(1, R.drawable.tool_speed, 1,  MyApplication.getResString(R.string.phoneSpeed), "一键清理，释放空间", R.color.black_33));
//        list.add(new ClearBean(1, R.drawable.toolbox_wechat, 2, "微信专清", "正在获取...", R.color.black_33));
//        list.add(new ClearBean(1, R.drawable.toolbox_qq, 3, "QQ专清", "正在获取...", R.color.black_33));
        list.add(new ClearBean(1, R.drawable.toolbox_cool, 4, MyApplication.getResString(R.string.phoneCool), "快来给手机降温", R.color.black_33));

//        list.add(new ClearBean(1, R.drawable.toolbox_power, 1, "超级省电", "", R.color.black_33));
//        list.add(new ClearBean(1, R.drawable.toolbox_cool, 2, "手机降温", "", R.color.black_33));
//        list.add(new ClearBean(1, R.drawable.toolbox_wechat, 3, "微信专清", "", R.color.black_33));
//        list.add(new ClearBean(1, R.drawable.toolbox_qq, 4, "QQ专清", "", R.color.black_33));
        return list;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
