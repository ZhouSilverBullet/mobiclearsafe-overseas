package com.mobi.overseas.clearsafe.ui.clear.entity;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/24 20:22
 * @Dec 略
 */
public class CleanRewardBean {

    /**
     * points : 40
     * pop_type : 1000
     * pop_up_msg :
     * total_points : 2080
     * totol_cash : 0.21
     */

    private int points;
    private int pop_type;
    private String pop_up_msg;
    private int total_points;
    private double totol_cash;

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

    public String getPop_up_msg() {
        return pop_up_msg;
    }

    public void setPop_up_msg(String pop_up_msg) {
        this.pop_up_msg = pop_up_msg;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public double getTotol_cash() {
        return totol_cash;
    }

    public void setTotol_cash(double totol_cash) {
        this.totol_cash = totol_cash;
    }
}
