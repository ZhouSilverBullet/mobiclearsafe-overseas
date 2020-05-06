package com.mobi.clearsafe.main.bean;

import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * author : liangning
 * date : 2019-12-07  15:35
 */
public class StepRaceHistoryBean implements Serializable {


    /**
     * attend_number : 3
     * max_points : 0
     * max_step : 0
     */

    public int attend_number;//参赛次数
    public int max_points; //最高奖励
    public int max_step;//最高步数
    public int sum_points;//总积分
    public List<HistoryItemBean> list;

    public static StepRaceHistoryBean objectFromData(String str) {

        return new Gson().fromJson(str, StepRaceHistoryBean.class);
    }
}
