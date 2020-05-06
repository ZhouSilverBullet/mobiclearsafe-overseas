package com.mobi.clearsafe.main.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-12-06  14:46
 */
public class StepRaceItemBean implements Serializable {

    public StepRaceDetailBean detail_prev_stages;//上期赛事详情
    public StepRaceDetailBean detail_curr_stages;//本期赛事详情
    public StepRaceDetailBean detail_next_stages;//下期赛事详情
    public StepRaceUserInfoBean user_prev_stages;//用户上期参赛信息
    public StepRaceUserInfoBean user_curr_stages;//用户本期参赛信息
    public StepRaceUserInfoBean user_next_stages;//用户下期参赛信息

    public static StepRaceItemBean objectFromData(String str) {

        return new Gson().fromJson(str, StepRaceItemBean.class);
    }

}
