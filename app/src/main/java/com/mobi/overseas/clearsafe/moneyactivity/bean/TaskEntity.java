package com.mobi.overseas.clearsafe.moneyactivity.bean;

import com.mobi.overseas.clearsafe.fragment.bean.TaskBean;
import com.mobi.overseas.clearsafe.main.bean.HotNoticeBean;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2019/12/13
 * desc: 任务列表
 */
public class TaskEntity implements Serializable {

    private int new_user_done_count;//新人活动已完成个数
    private int new_user_total_count;//新人活动总个数
    private boolean can_receive;//是否可领取收益卡
    private boolean is_receive_income_card;//是否已领取收益卡 true已领取

    private List<TaskBean> new_user_list;
    private List<TaskBean> normal_list;
    private List<HotNoticeBean.HotNotice> broadcast_list;


    public List<HotNoticeBean.HotNotice> getBroadcast_list() {
        return broadcast_list;
    }

    public void setBroadcast_list(List<HotNoticeBean.HotNotice> broadcast_list) {
        this.broadcast_list = broadcast_list;
    }

    public boolean isIs_receive_income_card() {
        return is_receive_income_card;
    }

    public void setIs_receive_income_card(boolean is_receive_income_card) {
        this.is_receive_income_card = is_receive_income_card;
    }

    public int getNew_user_done_count() {
        return new_user_done_count;
    }

    public void setNew_user_done_count(int new_user_done_count) {
        this.new_user_done_count = new_user_done_count;
    }

    public int getNew_user_total_count() {
        return new_user_total_count;
    }

    public void setNew_user_total_count(int new_user_total_count) {
        this.new_user_total_count = new_user_total_count;
    }

    public boolean isCan_receive() {
        return can_receive;
    }

    public void setCan_receive(boolean can_receive) {
        this.can_receive = can_receive;
    }

    public List<TaskBean> getNew_user_list() {
        return new_user_list;
    }

    public void setNew_user_list(List<TaskBean> new_user_list) {
        this.new_user_list = new_user_list;
    }

    public List<TaskBean> getNormal_list() {
        return normal_list;
    }

    public void setNormal_list(List<TaskBean> normal_list) {
        this.normal_list = normal_list;
    }
}
