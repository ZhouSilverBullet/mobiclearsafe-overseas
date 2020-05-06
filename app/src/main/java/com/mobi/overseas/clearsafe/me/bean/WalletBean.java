package com.mobi.overseas.clearsafe.me.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2020-02-10  15:21
 */
public class WalletBean implements Serializable {

    public int points;//当前剩余积分
    public int today_points;//今日积分
    public int total_points;//累计总收入
    public int rate;//汇率
    public int total_count;//总条数
    public int res_count;
    public List<WalletItemBean> res_list = new ArrayList<>();

}
