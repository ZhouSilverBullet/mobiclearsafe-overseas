package com.mobi.clearsafe.me.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-10-25  12:01
 */
public class WithDrawalItemBean implements Serializable {


    /**
     * id : 2
     * amount : 500
     * real_amount : 0
     * is_new_user : 0
     */

    public int id;
    public int amount;
    public int real_amount;
    public int is_new_user;
    public int need_coins;//需要花费的金币数量

    public static WithDrawalItemBean objectFromData(String str) {

        return new Gson().fromJson(str, WithDrawalItemBean.class);
    }
}
