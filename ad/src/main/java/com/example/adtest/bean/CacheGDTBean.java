package com.example.adtest.bean;

import com.example.adtest.utils.DateUtils;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-12-14  14:35
 */
public class CacheGDTBean implements Serializable {
    public long create_time = DateUtils.getNowTimeLong();

}
