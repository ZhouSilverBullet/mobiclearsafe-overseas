package com.mobi.overseas.clearsafe.ui.powercontrol.data;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.ui.powercontrol.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/22 16:11
 * @Dec 略
 */
public class BatteryBean {
    private int type;
    private int level;

    private int icon;
    private String dec;
    private int color;
    private boolean isSelected;

    public BatteryBean(int type, int icon, String dec, int color, boolean isSelected) {
        this.icon = icon;
        this.dec = dec;
        this.color = color;
        this.isSelected = isSelected;
        this.type = type;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static List<BatteryBean> createList() {
        ArrayList<BatteryBean> list = new ArrayList<>();

        //wifi
        boolean wifiState = SystemUtil.getWifiState();
        if (wifiState) {
            list.add(new BatteryBean(0, R.drawable.power_control_wifi_selector, MyApplication.getResString(R.string.WiFi),
                    R.color.c_2C86FF, true));
        } else {
            list.add(new BatteryBean(0, R.drawable.power_control_wifi_selector, MyApplication.getResString(R.string.WiFi),
                    R.color.black_99, false));
        }

        boolean isMobile = SystemUtil.gprsIsOpenMethod(MyApplication.getContext());
        if (isMobile) {
            list.add(new BatteryBean(1, R.drawable.power_control_data_selector, MyApplication.getResString(R.string.mobileData),
                    R.color.c_2C86FF, true));
        } else {
            list.add(new BatteryBean(1, R.drawable.power_control_data_selector,  MyApplication.getResString(R.string.mobileData),
                    R.color.black_99, false));
        }

        boolean bluetooth = SystemUtil.isBluetooth();
        if (bluetooth) {
            list.add(new BatteryBean(2, R.drawable.power_control_bluetooth_selector,  MyApplication.getResString(R.string.Bluetooth),
                    R.color.c_2C86FF, true));
        } else {
            list.add(new BatteryBean(2, R.drawable.power_control_bluetooth_selector, MyApplication.getResString(R.string.Bluetooth),
                    R.color.black_99, false));
        }

        //屏幕保护

        if (SystemUtil.isPermission(MyApplication.getContext())) {
            int screenTimeOut = SystemUtil.getScreenTimeOut(MyApplication.getContext());
            int level = 0;
            if (screenTimeOut == SystemUtil.SCREEN_TIME[0]) {
                level = 0;
            } else if (screenTimeOut == SystemUtil.SCREEN_TIME[1]) {
                level = 1;
            } else {
                level = 2;
            }

            BatteryBean screenBean = new BatteryBean(3, R.drawable.power_control_lock_selector, MyApplication.getResString(R.string.interestRatesScreen),
                    R.color.c_2C86FF, true);
            screenBean.setLevel(level);
            list.add(screenBean);
        } else {
            BatteryBean screenBean = new BatteryBean(3, R.drawable.power_control_lock_selector,  MyApplication.getResString(R.string.interestRatesScreen),
                    R.color.black_99, false);
            screenBean.setLevel(3);
            list.add(screenBean);
        }

        //情景模式
        int audioMode = SystemUtil.getAudioMode();
        BatteryBean audioBean = new BatteryBean(4, R.drawable.power_control_status_all_selector, MyApplication.getResString(R.string.Sound),
                R.color.c_2C86FF, true);
        audioBean.setLevel(audioMode);
        list.add(audioBean);


        boolean autoBrightness = SystemUtil.getAutoBrightness(MyApplication.getContext());
        if (autoBrightness) {
            list.add(new BatteryBean(5, R.drawable.power_control_sun_selector, MyApplication.getResString(R.string.Brightness),
                    R.color.c_2C86FF, true));
        } else {
            list.add(new BatteryBean(5, R.drawable.power_control_sun_selector, MyApplication.getResString(R.string.Brightness),
                    R.color.black_99, false));
        }

        boolean accelerometerRotation = SystemUtil.getAccelerometerRotation(MyApplication.getContext());
        if (accelerometerRotation) {
            list.add(new BatteryBean(6, R.drawable.power_control_rotation_selector, MyApplication.getResString(R.string.screenRotate),
                    R.color.c_2C86FF, true));
        } else {
            list.add(new BatteryBean(6, R.drawable.power_control_rotation_selector, MyApplication.getResString(R.string.screenRotate),
                    R.color.black_99, false));
        }

        boolean isGps = SystemUtil.isGpsOPen(MyApplication.getContext());
        if (isGps) {
            list.add(new BatteryBean(7, R.drawable.power_control_location_selector, MyApplication.getResString(R.string.Location),
                    R.color.c_2C86FF, true));
        } else {
            list.add(new BatteryBean(7, R.drawable.power_control_location_selector, MyApplication.getResString(R.string.Location),
                    R.color.black_99, false));
        }

        return list;
    }

}
