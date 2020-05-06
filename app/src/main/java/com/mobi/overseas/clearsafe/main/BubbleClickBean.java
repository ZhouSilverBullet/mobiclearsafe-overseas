package com.mobi.overseas.clearsafe.main;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/8
 * desc: 点击金币泡泡
 */
public class BubbleClickBean implements Serializable {
        private String id;
        private int points;
        private int total;
        private int total_points;
        private float cash;
        private boolean is_ad;//判断是信息流还是插屏
        private int pop_type;
        private String pop_up_message;
        private boolean information_flow_ad;
        private int h1fg;


    public boolean isInformation_flow_ad() {
        return information_flow_ad;
    }

    public void setInformation_flow_ad(boolean information_flow_ad) {
        this.information_flow_ad = information_flow_ad;
    }

    public int getPop_type() {
        return pop_type;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
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

    public boolean isIs_ad() {
        return is_ad;
    }

    public void setIs_ad(boolean is_ad) {
        this.is_ad = is_ad;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public float getCash() {
        return cash;
    }

    public void setCash(float cash) {
        this.cash = cash;
    }

    public int getH1fg() {
        return h1fg;
    }

    public void setH1fg(int h1fg) {
        this.h1fg = h1fg;
    }
}
