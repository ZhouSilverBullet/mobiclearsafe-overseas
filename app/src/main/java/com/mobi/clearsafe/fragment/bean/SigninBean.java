package com.mobi.clearsafe.fragment.bean;

import com.google.gson.Gson;

/**
 * 签到请求 数据
 * author : liangning
 * date : 2019-11-01  18:27
 */
public class SigninBean {

    /**
     * points : 10
     * record_id : 41
     */

    private int continuity_signin_number;//连续签到次数
    private int is_today_signin;//今日是否己签到 1己签到 0未签到
    public int points;
    private int total_points;
    private float cash;

    public static SigninBean objectFromData(String str) {

        return new Gson().fromJson(str, SigninBean.class);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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

    public int getContinuity_signin_number() {
        return continuity_signin_number;
    }

    public void setContinuity_signin_number(int continuity_signin_number) {
        this.continuity_signin_number = continuity_signin_number;
    }

    public int getIs_today_signin() {
        return is_today_signin;
    }

    public void setIs_today_signin(int is_today_signin) {
        this.is_today_signin = is_today_signin;
    }
}
