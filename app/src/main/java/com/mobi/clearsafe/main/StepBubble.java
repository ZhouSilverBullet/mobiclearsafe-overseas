package com.mobi.clearsafe.main;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/28
 * desc: 步数换金币泡泡
 */
public class StepBubble implements Serializable {
    private int exchange_coins;//可兑换金币数
    private boolean is_exchange_limit;//是否已达2000上限
    private boolean is_today_limit;//是否已达今日上限
    private int pop_type;//弹框类型
    private String pop_up_message;//按钮文案
    private String introduction;
    private boolean is_time_limit;//是否需要等待兑换
    private long last_time;//等待时长 秒

    public boolean isIs_time_limit() {
        return is_time_limit;
    }

    public void setIs_time_limit(boolean is_time_limit) {
        this.is_time_limit = is_time_limit;
    }

    public long getLast_time() {
        return last_time;
    }

    public void setLast_time(long last_time) {
        this.last_time = last_time;
    }

    public int getExchange_coins() {
        return exchange_coins;
    }

    public void setExchange_coins(int exchange_coins) {
        this.exchange_coins = exchange_coins;
    }

    public boolean isIs_exchange_limit() {
        return is_exchange_limit;
    }

    public void setIs_exchange_limit(boolean is_exchange_limit) {
        this.is_exchange_limit = is_exchange_limit;
    }

    public boolean isIs_today_limit() {
        return is_today_limit;
    }

    public void setIs_today_limit(boolean is_today_limit) {
        this.is_today_limit = is_today_limit;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
