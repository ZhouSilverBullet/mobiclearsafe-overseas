package com.mobi.overseas.clearsafe.moneyactivity.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/7
 * desc: 签到翻倍
 */
public class SigninDouble implements Serializable {
    private int points;//增加金币
    private int total_points;//当前用户总金币
    private float cash;

    public float getCash() {
        return cash;
    }

    public void setCash(float cash) {
        this.cash = cash;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }
}
