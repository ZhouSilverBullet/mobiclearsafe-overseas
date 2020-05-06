package com.mobi.clearsafe.fragment.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2020/2/7
 * desc: 每日任务
 */
public class TodayTaskBean implements Serializable {

    private int id;
    private String name;
    private String bg_url;
    private int total_num;  //总个数
    private int finished_num;  //已完成
    private int points;
    private int status;// 1- 未完成 2-可领取 3-已完成
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

    public String getBg_url() {
        return bg_url;
    }

    public void setBg_url(String bg_url) {
        this.bg_url = bg_url;
    }

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public int getFinished_num() {
        return finished_num;
    }

    public void setFinished_num(int finished_num) {
        this.finished_num = finished_num;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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
