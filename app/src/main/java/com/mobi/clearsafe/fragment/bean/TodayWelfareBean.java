package com.mobi.clearsafe.fragment.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2020/2/7
 * desc: 每日福利
 */
public class TodayWelfareBean implements Serializable {

    private int id;
    private String name;
    private String title;
    private String bg_url;
    private String points;
    private String lucky_number;//幸运数字
    private int status;//2-可领取 3-已完成
    private String jump_url;
    private int jump_type;
    private String params;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBg_url() {
        return bg_url;
    }

    public void setBg_url(String bg_url) {
        this.bg_url = bg_url;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getLucky_number() {
        return lucky_number;
    }

    public void setLucky_number(String lucky_number) {
        this.lucky_number = lucky_number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
}
