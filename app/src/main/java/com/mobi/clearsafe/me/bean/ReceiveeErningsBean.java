package com.mobi.clearsafe.me.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2020-02-12  15:16
 */
public class ReceiveeErningsBean implements Serializable {


    /**
     * cast : 1.78//现金
     * points : 2//获得积分
     * pop_type : 1000//弹窗类型
     * pop_up_message :
     * total_points : 17814//用户总金币
     */

    public double cast;
    public int points;
    public int pop_type;
    public String pop_up_message;
    public int total_points;

    public static ReceiveeErningsBean objectFromData(String str) {

        return new Gson().fromJson(str, ReceiveeErningsBean.class);
    }
}
