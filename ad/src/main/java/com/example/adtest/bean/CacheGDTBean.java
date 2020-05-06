package com.example.adtest.bean;

import com.example.adtest.utils.DateUtils;
import com.qq.e.ads.nativ.NativeExpressADView;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-12-14  14:35
 */
public class CacheGDTBean implements Serializable {

    public NativeExpressADView mNative;
    public long create_time = DateUtils.getNowTimeLong();

}
