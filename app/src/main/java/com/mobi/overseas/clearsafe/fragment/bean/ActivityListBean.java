package com.mobi.overseas.clearsafe.fragment.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2020/2/7
 * desc: 聚合页下方列表
 */
public class ActivityListBean implements Serializable {

    private String activity_bg;
    private String button_bg;
    private String button_content;
    private String title_picture;
    private String name;
    private String title;
    private String bg_url;
    private String jump_url;
    private int jump_type;
    private String params;

    public String getTitle_picture() {
        return title_picture;
    }

    public void setTitle_picture(String title_picture) {
        this.title_picture = title_picture;
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

    public String getActivity_bg() {
        return activity_bg;
    }

    public void setActivity_bg(String activity_bg) {
        this.activity_bg = activity_bg;
    }

    public String getButton_bg() {
        return button_bg;
    }

    public void setButton_bg(String button_bg) {
        this.button_bg = button_bg;
    }

    public String getButton_content() {
        return button_content;
    }

    public void setButton_content(String button_content) {
        this.button_content = button_content;
    }
}
