package com.example.adtest.statistical;

import com.example.adtest.manager.Constants;
import com.example.adtest.utils.DateUtils;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2019-11-18  13:49
 */
public class StatisticalBaseBean implements Serializable {

    public String day = DateUtils.getStringDateDay();
    public String time = DateUtils.getStringDateMin();
    public String deviceid = Constants.DEVICEID;
    public String platform = Constants.PLATFORM;
    public String sdkv = Constants.SDK_VERSION;
    public String channel_no = Constants.CHANNEL;

}
