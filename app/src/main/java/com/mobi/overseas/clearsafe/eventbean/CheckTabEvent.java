package com.mobi.overseas.clearsafe.eventbean;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-11-29  20:35
 */
public class CheckTabEvent implements Serializable {

    private String tab_name;

    public CheckTabEvent(String tab_name){
        this.tab_name = tab_name;
    }

    public String getTab_name() {
        return tab_name;
    }

    public void setTab_name(String tab_name) {
        this.tab_name = tab_name;
    }
}
