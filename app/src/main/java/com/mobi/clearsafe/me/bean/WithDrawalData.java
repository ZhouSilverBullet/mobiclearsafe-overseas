package com.mobi.clearsafe.me.bean;

import java.util.List;

/**
 * author : liangning
 * date : 2019-11-18  22:06
 */
public class WithDrawalData {

    public String description;
    public float cash;//用户余额
    public float block_amount;//冻结金额
    public List<WithDrawalItemBean> list;
}
