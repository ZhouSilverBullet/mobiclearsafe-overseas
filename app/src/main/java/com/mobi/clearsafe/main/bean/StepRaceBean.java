package com.mobi.clearsafe.main.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-12-06  14:37
 */
public class StepRaceBean implements Serializable {
    public int step_props_count = 0;//步数卡剩余个数
    public String step_card_rule = "";//步数卡使用规则
    public StepRaceItemBean step_5000;//5000步赛事信息
    public StepRaceItemBean step_8000;//8000步赛事信息
    public StepRaceItemBean step_3000;//3000步赛事信息


    public static StepRaceBean objectFromData(String str) {

        return new Gson().fromJson(str, StepRaceBean.class);
    }

}
