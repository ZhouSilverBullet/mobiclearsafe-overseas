package com.mobi.overseas.clearsafe.main;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/1
 * desc: 步数换金币
 */
public class StepExchangeCoins implements Serializable {

    private int total_points;
    private int points;
    private float cash;

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

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
}
