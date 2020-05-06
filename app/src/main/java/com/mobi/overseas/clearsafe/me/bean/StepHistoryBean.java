package com.mobi.overseas.clearsafe.me.bean;

import com.google.gson.Gson;

/**
 * author : liangning
 * date : 2019-10-31  18:44
 */
public class StepHistoryBean {

    /**
     * time : 2019-11-01
     * target_step : 10000
     * step : 2000
     * kilometer : 40.1
     * minute : 200
     * calories : 134.22
     * co2 : 100
     * heat : 1.5
     */

    public String time;
    public int target_step;
    public int step;
    public double kilometer;
    public int minute;
    public double calories;
    public int co2;
    public double heat;

    public static StepHistoryBean objectFromData(String str) {

        return new Gson().fromJson(str, StepHistoryBean.class);
    }
}
