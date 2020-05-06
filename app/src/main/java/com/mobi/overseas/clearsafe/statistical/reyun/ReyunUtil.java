package com.mobi.overseas.clearsafe.statistical.reyun;

import android.app.Application;

import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.reyun.tracking.sdk.Tracking;

/**
 * author:zhaijinlu
 * date: 2019/11/15
 * desc: 热云sdk
 */
public class ReyunUtil {



    public static void initSDK(Application mContext){
        //channel：渠道channel 、toutiao1、toutiao2
        String channelName = AppUtil.getChannelName(mContext);
        Tracking.initWithKeyAndChannelId(mContext, Const.getRYAPPkey(),channelName);
        Tracking.setDebugMode(Const.DEBUG);
    }

    //统计用户注册数据
    public static void setRegisterWithAccountID(String accountId){
        Tracking.setRegisterWithAccountID(accountId);
    }

    //统计用户登录数据
    public static void setLoginSuccessBusiness(String accountId){
        Tracking.setLoginSuccessBusiness(accountId);
    }







}
