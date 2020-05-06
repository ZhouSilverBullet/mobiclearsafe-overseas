package com.mobi.clearsafe.main.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2020/1/3
 * desc: 退出弹框
 */
public class ExitDialogBean implements Serializable {

    private String title;
    private String content;
    private String button1;
    private String button2;

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

    public String getButton1() {
        return button1;
    }

    public void setButton1(String button1) {
        this.button1 = button1;
    }

    public String getButton2() {
        return button2;
    }

    public void setButton2(String button2) {
        this.button2 = button2;
    }
}
