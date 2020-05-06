package com.example.adtest.bean;

import com.example.adtest.utils.DateUtils;
import com.qq.e.ads.nativ.NativeUnifiedADData;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2020-01-06  17:14
 */
public class SinceCacheGDTBean implements Serializable {

    public NativeUnifiedADData mAdData;
    public long create_time = DateUtils.getNowTimeLong();

}
