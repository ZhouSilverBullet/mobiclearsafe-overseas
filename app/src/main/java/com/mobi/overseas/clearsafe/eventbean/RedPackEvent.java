package com.mobi.overseas.clearsafe.eventbean;

import com.mobi.overseas.clearsafe.wxapi.bean.LoginBean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/18
 * desc:
 */
public class RedPackEvent implements Serializable {
    private LoginBean demo;

    public RedPackEvent(LoginBean demo) {
        this.demo = demo;
    }

    public LoginBean getDemo() {
        return demo;
    }

    public void setDemo(LoginBean demo) {
        this.demo = demo;
    }
}
