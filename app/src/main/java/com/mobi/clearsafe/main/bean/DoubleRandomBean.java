package com.mobi.clearsafe.main.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/12/18
 * desc: 最高翻六倍
 */
public class DoubleRandomBean implements Serializable {

    private int points;
    private int total_points;
    private float cash;
    private int pop_type;
    private String pop_up_message;
    private boolean is_ad;

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public boolean isIs_ad() {
        return is_ad;
    }

    public void setIs_ad(boolean is_ad) {
        this.is_ad = is_ad;
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

    public int getPop_type() {
        return pop_type;
    }

    public void setPop_type(int pop_type) {
        this.pop_type = pop_type;
    }

    public String getPop_up_message() {
        return pop_up_message;
    }

    public void setPop_up_message(String pop_up_message) {
        this.pop_up_message = pop_up_message;
    }
}
