package com.mobi.overseas.clearsafe.ui.repair.data;

import com.google.gson.annotations.SerializedName;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/17 11:58
 * @Dec 略
 */
public class CleanAuthBean {
    /**
     * id : 1
     * points : 0
     * pop_type : 0
     * pop_up_msg :
     * state : 2
     */

    @SerializedName("id")
    private int id;
    @SerializedName("points")
    private int points;
    @SerializedName("pop_type")
    private int popType;
    @SerializedName("pop_up_msg")
    private String popUpMsg;
    @SerializedName("state")
    private int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
