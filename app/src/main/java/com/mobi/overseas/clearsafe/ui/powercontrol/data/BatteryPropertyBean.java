package com.mobi.overseas.clearsafe.ui.powercontrol.data;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/22 15:44
 * @Dec 略
 */
public class BatteryPropertyBean {
    private String title;
    private String content;

    public BatteryPropertyBean(String title, String content) {
        this.title = title;
        this.content = content;
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
}
