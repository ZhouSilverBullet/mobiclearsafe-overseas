package com.mobi.clearsafe.main.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2020/2/13
 * desc: 领取红包
 */
public class RedCoinsBean implements Serializable {
    private int total_points;
    private int reward_points;
    private float total_cash;
    private float reward_cash;
    private boolean can_withdraw;//是否立即提现
    private String button_content;//可提现文案

    public boolean isCan_withdraw() {
        return can_withdraw;
    }

    public void setCan_withdraw(boolean can_withdraw) {
        this.can_withdraw = can_withdraw;
    }

    public String getButton_content() {
        return button_content;
    }

    public void setButton_content(String button_content) {
        this.button_content = button_content;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public int getReward_points() {
        return reward_points;
    }

    public void setReward_points(int reward_points) {
        this.reward_points = reward_points;
    }

    public float getTotal_cash() {
        return total_cash;
    }

    public void setTotal_cash(float total_cash) {
        this.total_cash = total_cash;
    }

    public float getReward_cash() {
        return reward_cash;
    }

    public void setReward_cash(float reward_cash) {
        this.reward_cash = reward_cash;
    }
}
