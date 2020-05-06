package com.mobi.overseas.clearsafe.fragment.bean;

import com.google.gson.Gson;

/**
 * author : liangning
 * date : 2019-10-22  18:18
 */
public class TaskBean {

    /**
     * 第三个position
     * 用于打广告
     */
    public static final int TYPE_AD = 1;
    /**
     * activity_id : 1
     * class_id : 4
     * name : 幸运大转盘
     * introduction : 看视频广告赢金币
     * title_picture :
     * points : 1000
     * times : 0
     * total_times : 100
     * jump_address :
     * jump_type :
     * activity_state : 0
     */

    public int activity_id;
    public int class_id;
    public String name;
    public String introduction;
    public String title_picture;
    public int points;
    public int times;
    public int total_times;
    public String jump_address;
    public String jump_type;
    public int activity_state;
    public int countdown;
    public int pop_type;
    public String pop_up_message;
    public String bg_url;
    /**
     * 第三个显示广告位
     */
    private int type;

    public static TaskBean objectFromData(String str) {

        return new Gson().fromJson(str, TaskBean.class);
    }

    public String getState() {
        String state = null;
        switch (activity_state) {
            case 0://默认状态
                break;
            case 1://已完成
                state = "已完成";
                break;
            case 2://去绑定
                state = "去绑定";
                break;
            case 3://去赚钱
                state = "去赚钱";
                break;
            case 4://立即领取
                state = "立即领取";
                break;
            case 5://去看看
                state = "去看看";
                break;
            case 7://去完成
                state = "去完成";
                break;
        }
        return state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
