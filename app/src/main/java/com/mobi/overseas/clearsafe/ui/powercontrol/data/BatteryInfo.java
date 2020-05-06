package com.mobi.overseas.clearsafe.ui.powercontrol.data;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/21 17:10
 * @Dec 略
 */
public class BatteryInfo {
    private int level;
    private int status;
    private String statusText;


    private String health;
    //多少 v
    private String voltage;
    //多少度
    private String temperature;

    private String levelPercent;
    //电池容量
    private String batteryCapacity;
    private String technology;

    private int batteryCapacityInt;
    private int scale;

    public int getBatteryCapacityInt() {
        return batteryCapacityInt;
    }

    public void setBatteryCapacityInt(int batteryCapacityInt) {
        this.batteryCapacityInt = batteryCapacityInt;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getLevelPercent() {
        return levelPercent;
    }

    public void setLevelPercent(String levelPercent) {
        this.levelPercent = levelPercent;
    }

    public String getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(String batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }
}
