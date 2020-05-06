package com.mobi.clearsafe.moneyactivity.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/7
 * desc:
 */
public class ActivityBean implements Serializable {
    private float cash;
    private int points;
    private int total_points;

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public float getCash() {
        return cash;
    }

    public void setCash(float cash) {
        this.cash = cash;
    }
}
