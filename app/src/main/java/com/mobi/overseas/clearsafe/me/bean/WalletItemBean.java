package com.mobi.overseas.clearsafe.me.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2020-02-10  15:23
 */
public class WalletItemBean implements Serializable {

    public String date;
    public boolean isShow = true;//是否显示标题
    public List<WalletItemRecordBean> record = new ArrayList<>();
}
