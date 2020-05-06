package com.mobi.overseas.clearsafe.main.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2020/2/21
 * desc:
 */
public class InviteRecord implements Serializable {

    private float total_reward;//总收益
    private int commission;//有效金币数
    private int total_count;//好友列表总个数
    private int res_count;//好友列表返回个数
    private List<Persion> res_list;
    private InviteDetail detail;


    public InviteDetail getDetail() {
        return detail;
    }

    public void setDetail(InviteDetail detail) {
        this.detail = detail;
    }

    public float getTotal_reward() {
        return total_reward;
    }

    public void setTotal_reward(float total_reward) {
        this.total_reward = total_reward;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getRes_count() {
        return res_count;
    }

    public void setRes_count(int res_count) {
        this.res_count = res_count;
    }

    public List<Persion> getRes_list() {
        return res_list;
    }

    public void setRes_list(List<Persion> res_list) {
        this.res_list = res_list;
    }

    public class Persion implements Serializable{

        private String user_id;
        private String nick_name;
        private int sum_income;
        private float sum_cash;
        private String create_time;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public int getSum_income() {
            return sum_income;
        }

        public void setSum_income(int sum_income) {
            this.sum_income = sum_income;
        }

        public float getSum_cash() {
            return sum_cash;
        }

        public void setSum_cash(float sum_cash) {
            this.sum_cash = sum_cash;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }




}
