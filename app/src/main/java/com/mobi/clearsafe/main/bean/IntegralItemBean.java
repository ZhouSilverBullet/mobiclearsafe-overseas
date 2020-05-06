package com.mobi.clearsafe.main.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-12-26  21:49
 */
public class IntegralItemBean implements Serializable {


    /**
     * app_id : 3
     * name : 支付宝
     * type : 2
     * icon_src :
     * points : 1000
     * package_name :
     * deep_link_url :
     * is_gain : 0
     */

    public int app_id;
    public String name;//app名称
    public int type;//1-下载 2-试玩
    public String icon_src;//app图标
    public int points;//奖励金币
    public String package_name;//包名
    public String deep_link_url;//打开其他APP的链接
    public int is_gain;//金币是否己领取 0未领取 1己领取
    public String download_url;//下载地址
    public String introduce;//描述

    public static IntegralItemBean objectFromData(String str) {

        return new Gson().fromJson(str, IntegralItemBean.class);
    }
}
