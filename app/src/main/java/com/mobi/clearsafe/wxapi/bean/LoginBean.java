package com.mobi.clearsafe.wxapi.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/10/26
 * desc:
 */
public class LoginBean  implements Serializable  {

        private String user_id;
        private String token;
        private boolean is_new_user;//是否新用户
        private boolean is_red_envelope;//是否弹出新人红包
        private int reward_points;
        private float reward_cash;//奖励现金数
        private int total_points;//当前总金币
        private float cash;//当前可兑换总金额
        private String nickname="";//游客昵称

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    public boolean isIs_new_user() {
        return is_new_user;
    }

    public void setIs_new_user(boolean is_new_user) {
        this.is_new_user = is_new_user;
    }

    public int getReward_points() {
        return reward_points;
    }

    public void setReward_points(int reward_points) {
        this.reward_points = reward_points;
    }

    public float getReward_cash() {
        return reward_cash;
    }

    public void setReward_cash(float reward_cash) {
        this.reward_cash = reward_cash;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public float getCash() {
        return cash;
    }

    public void setCash(float cash) {
        this.cash = cash;
    }

    public boolean isIs_red_envelope() {
        return is_red_envelope;
    }

    public void setIs_red_envelope(boolean is_red_envelope) {
        this.is_red_envelope = is_red_envelope;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "user_id='" + user_id + '\'' +
                ", token='" + token + '\'' +
                ", is_new_user=" + is_new_user +
                ", is_red_envelope=" + is_red_envelope +
                ", reward_points=" + reward_points +
                ", reward_cash=" + reward_cash +
                ", total_points=" + total_points +
                ", cash=" + cash +
                '}';
    }
}
