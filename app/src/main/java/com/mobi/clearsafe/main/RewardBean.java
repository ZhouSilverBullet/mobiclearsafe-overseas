package com.mobi.clearsafe.main;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/29
 * desc:
 */
public class RewardBean implements Serializable {
    private int points;
    private int pop_type;
    private String pop_up_message;
    private int total_points;
    private float cash;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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
}
