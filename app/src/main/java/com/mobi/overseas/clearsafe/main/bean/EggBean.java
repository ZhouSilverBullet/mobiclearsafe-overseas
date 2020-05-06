package com.mobi.overseas.clearsafe.main.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2020/2/11
 * desc: 金蛋列表
 */
public class EggBean implements Serializable {
    private List<Egg> egg_list;
    private int total_num;

    public int getTotal_num() {
        return total_num;
    }

    public void setTotal_num(int total_num) {
        this.total_num = total_num;
    }

    public List<Egg> getEgg_list() {
        return egg_list;
    }

    public void setEgg_list(List<Egg> egg_list) {
        this.egg_list = egg_list;
    }

    public class Egg implements Serializable{
        private int id;
        private int max_points;
        private int min_points;
        private int need_num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMax_points() {
            return max_points;
        }

        public void setMax_points(int max_points) {
            this.max_points = max_points;
        }

        public int getMin_points() {
            return min_points;
        }

        public void setMin_points(int min_points) {
            this.min_points = min_points;
        }

        public int getNeed_num() {
            return need_num;
        }

        public void setNeed_num(int need_num) {
            this.need_num = need_num;
        }
    }
}
