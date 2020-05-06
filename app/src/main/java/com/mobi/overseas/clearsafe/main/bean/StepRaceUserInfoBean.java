package com.mobi.overseas.clearsafe.main.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 用户参赛信息
 * author : liangning
 * date : 2019-12-06  14:43
 */
public class StepRaceUserInfoBean implements Serializable {


    /**
     * id : 20
     * user_id : 1193725303495593984
     * activity_id : 1
     * create_time : 2019-12-04T22:27:10+08:00
     * system_id : 1
     * stages_number : 20191205
     * competition_type : 5000
     * opening_time : 2019-12-05T00:00:00+08:00
     * lottery_time : 2019-12-06T00:00:00+08:00
     * enroll_points : 0
     * enroll_points_status : 0
     * enroll_type : 2
     * enroll_success_points : 20
     * enroll_success_points_state : 0
     * state : 1
     */

    public int id;
    public String user_id;
    public int activity_id;
    public long create_time;
    public int system_id;
    public String stages_number;//期号
    public int competition_type;//赛事类型 5000步赛事 8000步赛事
    public long opening_time;//开赛时间
    public long lottery_time;//开奖时间
    public int enroll_points;//报名金额 5000步0 8000步1000
    public int enroll_points_status;//报名金额状态 0未扣除 1己扣除
    public int enroll_type;//报名方式弹窗类型 1支付金币报名 2看视频报名
    public int enroll_success_points;
    public int enroll_success_points_state;
    public int state;//参赛状态 0默认值 1报名成功 2己领取 3未中奖
    public int user_step;//用户步数
    public int is_standard;//是否达标 0未达标 1己达标
    public int opening_time_countdown;//开赛倒计时
    public int lottery_time_countdown;//开奖倒计时

    public static StepRaceUserInfoBean objectFromData(String str) {

        return new Gson().fromJson(str, StepRaceUserInfoBean.class);
    }
}
