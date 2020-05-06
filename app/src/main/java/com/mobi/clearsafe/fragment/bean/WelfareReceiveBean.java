package com.mobi.clearsafe.fragment.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2020/2/10
 * desc: 每日福利领取
 */
public class WelfareReceiveBean implements Serializable {

    private int type;//100-翻倍卡  101-步数卡 102-收益卡  200-数字 10-金币
    private int total_points;
    private float cash;
    private int pop_type;
    private String pop_up_message;
    private String icon;
    private String title;
    private String content;
    private String jump_url;
    private int jump_type;
    private String params;
    private int number;
    private String number_tips = "";
    private String lack_number_msg = "";
    private boolean is_complete;
    private String button_msg = "";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public int getJump_type() {
        return jump_type;
    }

    public void setJump_type(int jump_type) {
        this.jump_type = jump_type;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

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
