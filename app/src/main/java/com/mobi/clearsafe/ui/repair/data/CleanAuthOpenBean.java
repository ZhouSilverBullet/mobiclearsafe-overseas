package com.mobi.clearsafe.ui.repair.data;

import com.google.gson.annotations.SerializedName;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/17 13:35
 * @Dec 略
 */
public class CleanAuthOpenBean {

    /**
     * points : 40
     * pop_type : 1000
     * pop_up_msg :
     * total_points : 2080
     * totol_cash : 0.21
     */

    @SerializedName("points")
    private int points;
    @SerializedName("pop_type")
    private int popType;
    @SerializedName("pop_up_msg")
    private String popUpMsg;
    @SerializedName("total_points")
    private int totalPoints;
    @SerializedName("total_cash")
    private double totolCash;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPopType() {
        return popType;
    }

    public void setPopType(int popType) {
        this.popType = popType;
    }

    public String getPopUpMsg() {
        return popUpMsg;
    }

    public void setPopUpMsg(String popUpMsg) {
        this.popUpMsg = popUpMsg;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public double getTotolCash() {
        return totolCash;
    }

    public void setTotolCash(double totolCash) {
        this.totolCash = totolCash;
    }
}
