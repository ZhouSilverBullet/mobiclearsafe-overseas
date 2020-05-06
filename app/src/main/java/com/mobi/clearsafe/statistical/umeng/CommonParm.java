package com.mobi.clearsafe.statistical.umeng;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/18
 * desc: 数据上报公共参数
 */
public class CommonParm implements Serializable {

    private String day ;//日期
    private String time ;//时间
    private String deviceid ;
    private String platform ;//平台 1 android  2 ios
    private String sdkv ;//版本号
    private String channel_no ;//渠道
    private String eventId;//事件ID
    private String device_model;//设备号
    private String userID;
    private String appID;

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSdkv() {
        return sdkv;
    }

    public void setSdkv(String sdkv) {
        this.sdkv = sdkv;
    }

    public String getChannel_no() {
        return channel_no;
    }

    public void setChannel_no(String channel_no) {
        this.channel_no = channel_no;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
