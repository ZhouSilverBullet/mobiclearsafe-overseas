package com.mobi.clearsafe.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * author:zhaijinlu
 * date: 2019/10/23
 * desc:
 */
@Entity
public class TodayStepData {

    @Id(autoincrement = true)
    private Long id;
    private String userId;
    private String system_data;//系统步数
    private String current_data;//当前步数
    private String date;//插入日期
    private String time;//插入时间
    private String weChat_data;//微信步数
    @Generated(hash = 1592397556)
    public TodayStepData(Long id, String userId, String system_data,
            String current_data, String date, String time, String weChat_data) {
        this.id = id;
        this.userId = userId;
        this.system_data = system_data;
        this.current_data = current_data;
        this.date = date;
        this.time = time;
        this.weChat_data = weChat_data;
    }
    @Generated(hash = 1196082593)
    public TodayStepData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getSystem_data() {
        return this.system_data;
    }
    public void setSystem_data(String system_data) {
        this.system_data = system_data;
    }
    public String getCurrent_data() {
        return this.current_data;
    }
    public void setCurrent_data(String current_data) {
        this.current_data = current_data;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getWeChat_data() {
        return this.weChat_data;
    }
    public void setWeChat_data(String weChat_data) {
        this.weChat_data = weChat_data;
    }

    @Override
    public String toString() {
        return "TodayStepData{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", system_data='" + system_data + '\'' +
                ", current_data='" + current_data + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", weChat_data='" + weChat_data + '\'' +
                '}';
    }
}
