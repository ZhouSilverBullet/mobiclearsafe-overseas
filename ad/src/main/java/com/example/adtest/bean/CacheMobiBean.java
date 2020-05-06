package com.example.adtest.bean;

import com.example.adtest.utils.DateUtils;
import com.mobi.adsdk.nativeexpress.MobiNativeExpressAdView;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2020-04-17  19:47
 */
public class CacheMobiBean implements Serializable {
    public MobiNativeExpressAdView mobiNativeExpressAdView;
    public long create_time = DateUtils.getNowTimeLong();
}
