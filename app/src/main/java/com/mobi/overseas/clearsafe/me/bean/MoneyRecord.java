package com.mobi.overseas.clearsafe.me.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/1
 * desc:
 */
public class MoneyRecord implements Serializable {

    private long time;
    private int points;
    private String cash;
    private String activity_name;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }
}
