package com.mobi.overseas.clearsafe.eventbean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/12/19
 * desc:
 */
public class TabTipEvent implements Serializable {
    private int poins;

    public TabTipEvent(int poins) {
        this.poins = poins;
    }

    public int getPoins() {
        return poins;
    }

    public void setPoins(int poins) {
        this.poins = poins;
    }
}
