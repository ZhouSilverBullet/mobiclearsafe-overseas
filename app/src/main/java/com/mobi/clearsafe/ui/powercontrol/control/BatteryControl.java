package com.mobi.clearsafe.ui.powercontrol.control;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/21 16:26
 * @Dec 略
 */
public class BatteryControl {

    private BatteryControl() {
    }

    public static BatteryControl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final BatteryControl INSTANCE = new BatteryControl();
    }


}
