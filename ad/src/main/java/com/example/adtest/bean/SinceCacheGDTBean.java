package com.example.adtest.bean;

import com.example.adtest.utils.DateUtils;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2020-01-06  17:14
 */
public class SinceCacheGDTBean implements Serializable {

    public long create_time = DateUtils.getNowTimeLong();

}
