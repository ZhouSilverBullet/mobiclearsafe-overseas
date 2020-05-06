package com.mobi.clearsafe.me.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2019/11/1
 * desc:
 */
public class MoneyRecordBean implements Serializable {

    private int res_count;
    private int total_count;
    private List<MoneyRecord> res_list;

    public int getRes_count() {
        return res_count;
    }

    public void setRes_count(int res_count) {
        this.res_count = res_count;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public List<MoneyRecord> getRes_list() {
        return res_list;
    }

    public void setRes_list(List<MoneyRecord> res_list) {
        this.res_list = res_list;
    }
}
