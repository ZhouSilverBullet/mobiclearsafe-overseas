package com.mobi.overseas.clearsafe.me.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-11-05  13:51
 */
public class WithdrawBean implements Serializable {

    /**
     * amount : 200
     * points : 20000
     */

    public int amount;//提现金额
    public int points;//话费金币数
    public int total_points;
    public float cash;
    public boolean red_envelope;//false不发红包 true-发红包
    public int reward_points;//红包金额
    public float reward_cash;//可兑换金钱
    public String  tips;//红包文案

    public static WithdrawBean objectFromData(String str) {

        return new Gson().fromJson(str, WithdrawBean.class);
    }
}
