package com.mobi.overseas.clearsafe.statistical.errorlog;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/19
 * desc: 上传错误日志实体
 */
public class ErrorLogEntity implements Serializable {
    private String day ;
    private String time ;
    private String deviceid ;
    private String platform ;
    private String sdkv ;//版本号
    private String channel_no ;//渠道
    private String errorCode;//错误码

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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
