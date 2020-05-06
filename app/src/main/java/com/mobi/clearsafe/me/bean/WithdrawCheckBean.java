package com.mobi.clearsafe.me.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-12-19  20:57
 */
public class WithdrawCheckBean implements Serializable {


    /**
     * can_withdraw : false
     * rule : 测试提现
     */

    public boolean can_withdraw;//true 可提现 false-不可提现
    public String rule="";
    public boolean red_envelope;//false不发红包 true-发红包
    public int reward_points;//红包金额
    public float reward_cash;//可兑换金钱
    public int total_points;//当前总金额
    public float cash;//当前可兑换金钱
    public String tips;//提现红包文案提示


    public static WithdrawCheckBean objectFromData(String str) {

        return new Gson().fromJson(str, WithdrawCheckBean.class);
    }
}
