package com.mobi.clearsafe.eventbean;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2020-01-03  16:40
 */
public class ScrathTabEvent implements Serializable {
    private String s;
    public ScrathTabEvent(String s){
        this.s = s;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
