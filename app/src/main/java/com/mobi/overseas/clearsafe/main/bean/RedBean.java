package com.mobi.overseas.clearsafe.main.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/12/19
 * desc:
 */
public class RedBean implements Serializable {
    private int type;////3-激励红包  4-新人红包  新人红包手动领取  激励红包自动发放
    private boolean is_red_envelope;//是否发放红包
    private boolean is_bind_weixin;
    private TodayReward today_reward;
    private NextDayReward next_day_reward;


    public boolean isIs_bind_weixin() {
        return is_bind_weixin;
    }

    public void setIs_bind_weixin(boolean is_bind_weixin) {
        this.is_bind_weixin = is_bind_weixin;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isIs_red_envelope() {
        return is_red_envelope;
    }

    public void setIs_red_envelope(boolean is_red_envelope) {
        this.is_red_envelope = is_red_envelope;
    }

    public TodayReward getToday_reward() {
        return today_reward;
    }

    public void setToday_reward(TodayReward today_reward) {
        this.today_reward = today_reward;
    }

    public NextDayReward getNext_day_reward() {
        return next_day_reward;
    }

    public void setNext_day_reward(NextDayReward next_day_reward) {
        this.next_day_reward = next_day_reward;
    }

    public class TodayReward implements Serializable{
        private int total_points;
        private int points;
        private float cash;
        private float reward_cash;

        public int getTotal_points() {
            return total_points;
        }

        public void setTotal_points(int total_points) {
            this.total_points = total_points;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public float getCash() {
            return cash;
        }

        public void setCash(float cash) {
            this.cash = cash;
        }

        public float getReward_cash() {
            return reward_cash;
        }

        public void setReward_cash(float reward_cash) {
            this.reward_cash = reward_cash;
        }
    }

    public class NextDayReward implements Serializable{
        private int points;
        private float reward_cash;

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public float getReward_cash() {
            return reward_cash;
        }

        public void setReward_cash(float reward_cash) {
            this.reward_cash = reward_cash;
        }
    }

}
