package com.mobi.overseas.clearsafe.main;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 惊喜奖励 获取显示在哪页
 * author : liangning
 * date : 2019-11-30  00:42
 */
public class PleasantlyBean extends PleasantlyReceiveBean implements Serializable {
    /**
     * is_gain : 0
     * points : 18
     * pop_type : 1001
     * pop_up_message : 金币翻倍
     * show_page : 3
     */


    public int show_page;

    public static PleasantlyBean objectFromData(String str) {

        return new Gson().fromJson(str, PleasantlyBean.class);
    }
//    public int show_page;

}
