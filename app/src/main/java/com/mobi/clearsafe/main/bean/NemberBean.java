package com.mobi.clearsafe.main.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2020/2/11
 * desc:体验活动领取数字
 */
public class NemberBean implements Serializable {
    private int number;
    private String number_tips = "";
    private String lack_number_msg = "";
    private boolean is_complete;
    private String button_msg = "";

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getNumber_tips() {
        return number_tips;
    }

    public void setNumber_tips(String number_tips) {
        this.number_tips = number_tips;
    }

    public String getLack_number_msg() {
        return lack_number_msg;
    }

    public void setLack_number_msg(String lack_number_msg) {
        this.lack_number_msg = lack_number_msg;
    }

    public boolean isIs_complete() {
        return is_complete;
    }

    public void setIs_complete(boolean is_complete) {
        this.is_complete = is_complete;
    }

    public String getButton_msg() {
        return button_msg;
    }

    public void setButton_msg(String button_msg) {
        this.button_msg = button_msg;
    }
}
