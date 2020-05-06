package com.mobi.clearsafe.ui.clear.entity;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/24 10:56
 * @Dec 略
 */
public class CleanListBean {

    /**
     * total_num : 5
     * list : [{"id":1,"points":20,"pop_type":1000,"pop_up_msg":""},{"id":2,"points":30,"pop_type":1001,"pop_up_msg":"金币翻倍"},{"id":3,"points":40,"pop_type":1002,"pop_up_msg":""},{"id":4,"points":50,"pop_type":1003,"pop_up_msg":"立即领取"},{"id":5,"points":60,"pop_type":1004,"pop_up_msg":"领取60X3金币"}]
     */

    private int total_num;
    private List<ListBean> list;

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 1
         * points : 20
         * pop_type : 1000
         * pop_up_msg :
         */

        private int id;
        private int points;
        private int pop_type;
        private String pop_up_msg;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public int getPop_type() {
            return pop_type;
        }

        public void setPop_type(int pop_type) {
            this.pop_type = pop_type;
        }

        public String getPop_up_msg() {
            return pop_up_msg;
        }

        public void setPop_up_msg(String pop_up_msg) {
            this.pop_up_msg = pop_up_msg;
        }
    }
}
