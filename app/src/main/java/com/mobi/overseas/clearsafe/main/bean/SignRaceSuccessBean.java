package com.mobi.overseas.clearsafe.main.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-12-06  21:37
 */
public class SignRaceSuccessBean implements Serializable {


    /**
     * cash : 1.82
     * enroll_type : 2
     * points : 20
     * total_points : 18161
     */

    public float cash;
    public int enroll_type;
    public int points;
    public int total_points;

    public static SignRaceSuccessBean objectFromData(String str) {

        return new Gson().fromJson(str, SignRaceSuccessBean.class);
    }
}
