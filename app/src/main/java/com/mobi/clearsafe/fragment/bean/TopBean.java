package com.mobi.clearsafe.fragment.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2020/2/7
 * desc:  聚合页顶部数据
 */
public class TopBean implements Serializable {
    private String name;
    private String title;
    private String icon;
    private int status;////1-去完成 2-立即领取 3-已完成 100-已集齐

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
