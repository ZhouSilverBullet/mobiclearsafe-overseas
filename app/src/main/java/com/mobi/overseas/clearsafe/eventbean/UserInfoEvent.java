package com.mobi.overseas.clearsafe.eventbean;

import com.mobi.overseas.clearsafe.wxapi.bean.UserInfo;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/10/28
 * desc:发送事件修改用户信息
 */
public class UserInfoEvent implements Serializable {

    private UserInfo userInfo;

    public UserInfoEvent(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
