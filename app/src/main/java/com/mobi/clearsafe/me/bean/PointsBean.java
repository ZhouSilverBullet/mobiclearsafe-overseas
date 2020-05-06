package com.mobi.clearsafe.me.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/1
 * desc:获取用户当前金币数
 */
public class PointsBean implements Serializable {
    private int points;//金币数
    private float cash;//可兑换现金数

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
