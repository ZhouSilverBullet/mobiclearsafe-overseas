package com.mobi.overseas.clearsafe.main.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 赛事信息
 * author : liangning
 * date : 2019-12-06  14:38
 */
public class StepRaceDetailBean implements Serializable {


    /**
     * id : 17
     * name : 5000步赛事
     * system_id : 1
     * class_id : 16
     * create_time : 2019-12-05T11:47:41+08:00
     * activity_id : 1
     * stages_number : 20191206
     * jackpot_points : 0
     * per_capita_contribution : 30
     * enrolment_number : 0
     * qualified_persons : 0
     * competition_type : 5000
     * opening_time : 2019-12-06T00:00:00+08:00
     * lottery_time : 2019-12-07T00:00:00+08:00
     * enroll_points : 0
     * enroll_type : 2
     * enroll_success_points : 20
     */

    public int id;
    public String name;
    public int system_id;
    public int class_id;
    public long create_time;
    public int activity_id;
    public String stages_number; //期号
    public int jackpot_points;//奖池积分
    public int per_capita_contribution;
    public int enrolment_number;//报名人数
    public int qualified_persons;//达标人数
    public int competition_type;//赛事类型
    public long opening_time;//开赛时间
    public long lottery_time;//开奖时间
    public int enroll_points;//报名金额
    public int enroll_type;//报名方式类型 1支付金币报名 2看视频报名
    public int enroll_success_points;//报名成功获得金币数
    public int opening_time_countdown;//开赛倒计时
    public int lottery_time_countdown;//开奖倒计时
    public int expect_points;//预期奖金
    public String enroll_button;//报名按钮第一行文字
    public String enroll_button2;//报名按钮第二行文字

    public static StepRaceDetailBean objectFromData(String str) {

        return new Gson().fromJson(str, StepRaceDetailBean.class);
    }
}
