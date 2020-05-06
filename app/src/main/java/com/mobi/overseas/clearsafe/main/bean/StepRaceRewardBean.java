package com.mobi.overseas.clearsafe.main.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-12-06  23:22
 */
public class StepRaceRewardBean implements Serializable {


    /**
     * cash : 0.71
     * points : 0
     * total_points : 7061
     */

    public float cash;
    public int points;
    public int total_points;

    public static StepRaceRewardBean objectFromData(String str) {

        return new Gson().fromJson(str, StepRaceRewardBean.class);
    }
}
