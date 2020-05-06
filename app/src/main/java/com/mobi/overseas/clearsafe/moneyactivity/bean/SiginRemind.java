package com.mobi.overseas.clearsafe.moneyactivity.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/12/11
 * desc: 签到提醒
 */
public class SiginRemind implements Serializable {

    private String title;
    private long start_time;
    private long end_time;
    private String all_day;
    private String notes;//备注
    private long alarm_str;//提醒时长秒

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getAll_day() {
        return all_day;
    }

    public void setAll_day(String all_day) {
        this.all_day = all_day;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getAlarm_str() {
        return alarm_str;
    }

    public void setAlarm_str(long alarm_str) {
        this.alarm_str = alarm_str;
    }
}
