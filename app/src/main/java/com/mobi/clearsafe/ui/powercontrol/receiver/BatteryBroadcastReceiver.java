package com.mobi.clearsafe.ui.powercontrol.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.powercontrol.data.BatteryInfo;
import com.mobi.clearsafe.ui.powersaving.util.BatteryStatsUtil;

import java.util.Random;

import static android.os.BatteryManager.BATTERY_STATUS_CHARGING;
import static android.os.BatteryManager.BATTERY_STATUS_DISCHARGING;
import static android.os.BatteryManager.BATTERY_STATUS_FULL;
import static android.os.BatteryManager.BATTERY_STATUS_NOT_CHARGING;
import static android.os.BatteryManager.BATTERY_STATUS_UNKNOWN;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/21 16:24
 * @Dec 略
 */
public class BatteryBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        int level = intent.getIntExtra("level", 0);///电池剩余电量
//        intent.getIntExtra("scale", 0);  ///获取电池满电量数值
//        intent.getStringExtra("technology");  ///获取电池技术支持
//        int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);///获取电池状态
//        int plugged = intent.getIntExtra("plugged", 0);///获取电源信息
//        intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN);  ///获取电池健康度
//        intent.getIntExtra("voltage", 0);  ///获取电池电压
//        int temperature = intent.getIntExtra("temperature", 0);///获取电池温度

        int level = intent.getIntExtra("level", 0);//获取当前电量
        int scale = intent.getIntExtra("scale", 0);//获取总电量
        int voltage = intent.getIntExtra("voltage", 0);//获取电压(mv)
        int temperature = intent.getIntExtra("temperature", 0);//获取温度(数值)
        String technology = intent.getStringExtra("technology");//获取温度(数值)
        double t = temperature / 10.0;  //运算转换,电池摄氏温度，默认获取的非摄氏温度值

        int plugged = intent.getIntExtra("plugged", 0);///获取电源信息

        Log.i("aaa level", "" + level + "%");
        Log.i("aaa scale", "" + scale);
        Log.i("aaa voltage", "" + voltage / 1000 + "V");
        Log.i("aaa temperature", "" + t + "°C");
        Log.i("aaa technology", "" + technology);
        Log.i("aaa plugged", "" + plugged);

        String batteryStatus = "";
        int status = intent.getIntExtra("status", 1);

        Log.i("aaa status", "" + status);

        switch (status) {
            default:
                break;
            case BATTERY_STATUS_FULL:
                batteryStatus = "电已经充满";
                break;
            case BATTERY_STATUS_NOT_CHARGING:
                batteryStatus = "未在充电";
                break;
            case BATTERY_STATUS_DISCHARGING:
                batteryStatus = "放电状态";
                break;
            case BATTERY_STATUS_CHARGING:
                batteryStatus = "充电状态";
                break;
            case BATTERY_STATUS_UNKNOWN:
                batteryStatus = "未知";
        }

        String batteryTemp = "";
        switch (intent.getIntExtra("health", 1)) {
//            default:
//                break;
//            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
//                batteryTemp = "电压过高";
//                break;
//            case BatteryManager.BATTERY_HEALTH_DEAD:
//                batteryTemp = "电池没电";
//                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                batteryTemp = "过热";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                batteryTemp = "健康";
                break;
//            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
            default:
                batteryTemp = "未知";
                break;
        }

        Log.i("aaa batteryStatus", "" + batteryStatus);
        Log.i("aaa batteryTemp", "" + batteryTemp);
        Log.i("aaa BatteryCapacity", "" + getBatteryCapacity(context) + "mAh");
        Log.i("aaa BatteryCapacity", "---- ---- ----- ---- ---- ---- ");

        BatteryInfo batteryInfo = new BatteryInfo();
        batteryInfo.setLevel(level);
        batteryInfo.setStatusText(batteryStatus);
        batteryInfo.setHealth(batteryTemp);
        batteryInfo.setVoltage(String.valueOf(voltage / 1000) + "V");
        batteryInfo.setTemperature(String.valueOf(t) + "°C");
        batteryInfo.setLevelPercent(level + "%");
        batteryInfo.setBatteryCapacity(getBatteryCapacity(context) + "mAh");
        batteryInfo.setBatteryCapacityInt(getBatteryCapacity(context));
        batteryInfo.setTechnology(technology);
        batteryInfo.setScale(scale == 0 ? 100 : scale);
        batteryInfo.setStatus(status);

        if (mBatterChangeListener != null) {
            mBatterChangeListener.onChange(batteryInfo);
        }
    }

    private BatterChangeListener mBatterChangeListener;

    public void setBatterChangeListener(BatterChangeListener batterChangeListener) {
        mBatterChangeListener = batterChangeListener;
    }

    public interface BatterChangeListener {
        void onChange(BatteryInfo info);
    }


    /**
     * 获取电池容量 mAh
     * <p>
     * 源头文件:frameworks/base/core/res\res/xml/power_profile.xml
     * <p>
     * Java 反射文件：frameworks\base\core\java\com\android\internal\os\PowerProfile.java
     */
    public static int getBatteryCapacity(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class.forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (int) batteryCapacity;
    }

}