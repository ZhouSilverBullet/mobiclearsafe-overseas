package com.mobi.clearsafe.main;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-11-30  16:16
 */
public class PleasantlyReceiveBean implements Serializable {

    /**
     * is_gain : 1
     * cash : 1.52
     * points : 30
     * pop_type : 1001
     * pop_up_message : 金币翻倍
     * total_points : 15228
     */

    public int is_gain;
    public float cash;
    public int points;
    public int pop_type;
    public String pop_up_message;
    public int total_points;
    public int sign_points;
    public boolean is_ad;
    public boolean information_flow_ad;

    public static PleasantlyReceiveBean objectFromData(String str) {

        return new Gson().fromJson(str, PleasantlyReceiveBean.class);
    }
}
