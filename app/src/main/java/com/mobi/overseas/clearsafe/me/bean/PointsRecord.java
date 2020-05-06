package com.mobi.overseas.clearsafe.me.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2019/11/1
 * desc: 金币收益记录
 */
public class PointsRecord implements Serializable {

    private int res_count;
    private int total_count;
    private List<RecordBean> res_list;

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

    public List<RecordBean> getRes_list() {
        return res_list;
    }

    public void setRes_list(List<RecordBean> res_list) {
        this.res_list = res_list;
    }
}
