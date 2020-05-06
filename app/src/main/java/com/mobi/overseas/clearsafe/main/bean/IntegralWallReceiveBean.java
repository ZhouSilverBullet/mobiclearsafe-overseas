package com.mobi.overseas.clearsafe.main.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-12-27  17:47
 */
public class IntegralWallReceiveBean implements Serializable {


    /**
     * cash : 0.83
     * points : 1000
     * pop_type : 1000
     * total_points : 8333
     */

    public double cash;
    public int points;
    public int pop_type;
    public int total_points;

    public static IntegralWallReceiveBean objectFromData(String str) {

        return new Gson().fromJson(str, IntegralWallReceiveBean.class);
    }
}
