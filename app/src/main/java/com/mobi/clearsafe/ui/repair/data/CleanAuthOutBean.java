package com.mobi.clearsafe.ui.repair.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/17 11:56
 * @Dec 略
 */
public class CleanAuthOutBean {

    /**
     * list : [{"id":1,"points":0,"pop_type":0,"pop_up_msg":"","state":2},{"id":4,"points":50,"pop_type":1003,"pop_up_msg":"立即领取","state":0}]
     * total_num : 4
     */

    @SerializedName("total_num")
    private int totalNum;
    @SerializedName("list")
    private List<CleanAuthBean> list;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<CleanAuthBean> getList() {
        return list;
    }

    public void setList(List<CleanAuthBean> list) {
        this.list = list;
    }


}
