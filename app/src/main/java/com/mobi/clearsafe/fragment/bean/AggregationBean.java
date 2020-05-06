package com.mobi.clearsafe.fragment.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2020/2/7
 * desc: 活动聚合页详情
 */
public class AggregationBean implements Serializable {

        private List<TopBean> top_list;
        private List<ActivityListBean> activity_list;

    public List<TopBean> getTop_list() {
        return top_list;
    }

    public void setTop_list(List<TopBean> top_list) {
        this.top_list = top_list;
    }

    public List<ActivityListBean> getActivity_list() {
        return activity_list;
    }

    public void setActivity_list(List<ActivityListBean> activity_list) {
        this.activity_list = activity_list;
    }
}
