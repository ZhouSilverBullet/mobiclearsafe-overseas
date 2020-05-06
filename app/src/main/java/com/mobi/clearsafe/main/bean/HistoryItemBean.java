package com.mobi.clearsafe.main.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-12-07  15:37
 */
public class HistoryItemBean implements Serializable {


    /**
     * is_standard : 0
     * jackpot_points : 1000
     * lottery_time : 2019-12-08T00:00:00+08:00
     * opening_time : 2019-12-07T00:00:00+08:00
     * points : 0
     * qualified_persons : 0
     * stages_number : 20191207
     * user_id : 1193725303495593984
     */

    public int is_standard;//是否达标 0未达标 1己达标
    public int jackpot_points;//奖池积分
    public long lottery_time;//开奖时间
    public long opening_time;//开赛时间
    public int points;//活动完成获得积分
    public int qualified_persons;//达标人数
    public String stages_number;//期号
    public int is_opening;//是否开赛 1己开赛 0未开赛
    public String user_id;

    public static HistoryItemBean objectFromData(String str) {

        return new Gson().fromJson(str, HistoryItemBean.class);
    }
}
