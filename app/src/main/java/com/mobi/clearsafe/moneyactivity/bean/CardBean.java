package com.mobi.clearsafe.moneyactivity.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2020/2/6
 * desc: 领取金币收益卡
 */
public class CardBean implements Serializable {

    private String title;
    private String content;
    private String image;
    private String dead_line;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDead_line() {
        return dead_line;
    }

    public void setDead_line(String dead_line) {
        this.dead_line = dead_line;
    }
}
