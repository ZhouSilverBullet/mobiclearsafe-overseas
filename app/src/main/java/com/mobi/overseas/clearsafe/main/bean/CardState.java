package com.mobi.overseas.clearsafe.main.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2020/2/4
 * desc:获取收益卡状态
 */
public class CardState implements Serializable {

    private int state;//0没有收益卡 1正式收益中 2领取收益 3收益己领取
    private String content;//吐司内容
    private int points;//累计收益金币
    private String deadline_msg;
    private int signin_last_points;//签到未领取金币数

    public int getSignin_last_points() {
        return signin_last_points;
    }

    public void setSignin_last_points(int signin_last_points) {
        this.signin_last_points = signin_last_points;
    }

    public String getDeadline_msg() {
        return deadline_msg;
    }

    public void setDeadline_msg(String deadline_msg) {
        this.deadline_msg = deadline_msg;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
