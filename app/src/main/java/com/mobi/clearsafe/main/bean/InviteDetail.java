package com.mobi.clearsafe.main.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2020/2/21
 * desc:获取奖励详情
 */
public class InviteDetail implements Serializable {

    private int invite_count;//邀请人数
    private float max_income;//最大收益
    private float total_income;//总收益
    private String rule_url;
    private Info first_day;
    private Info first_invite;
    private Info income;
    private Info second_day;
    private Info third_day;

    public String getRule_url() {
        return rule_url;
    }

    public void setRule_url(String rule_url) {
        this.rule_url = rule_url;
    }

    public int getInvite_count() {
        return invite_count;
    }

    public void setInvite_count(int invite_count) {
        this.invite_count = invite_count;
    }

    public float getMax_income() {
        return max_income;
    }

    public void setMax_income(float max_income) {
        this.max_income = max_income;
    }

    public float getTotal_income() {
        return total_income;
    }

    public void setTotal_income(float total_income) {
        this.total_income = total_income;
    }

    public Info getFirst_day() {
        return first_day;
    }

    public void setFirst_day(Info first_day) {
        this.first_day = first_day;
    }

    public Info getFirst_invite() {
        return first_invite;
    }

    public void setFirst_invite(Info first_invite) {
        this.first_invite = first_invite;
    }

    public Info getIncome() {
        return income;
    }

    public void setIncome(Info income) {
        this.income = income;
    }

    public Info getSecond_day() {
        return second_day;
    }

    public void setSecond_day(Info second_day) {
        this.second_day = second_day;
    }

    public Info getThird_day() {
        return third_day;
    }

    public void setThird_day(Info third_day) {
        this.third_day = third_day;
    }

    public class Info implements Serializable{
        private float cash;
        private String tips;

        public float getCash() {
            return cash;
        }

        public void setCash(float cash) {
            this.cash = cash;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }
    }
}
