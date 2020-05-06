package com.mobi.overseas.clearsafe.wxapi.bean;

import android.text.TextUtils;

import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.main.ConfigBean;
import com.mobi.overseas.clearsafe.utils.SPUtil;

/**
 * author:zhaijinlu
 * date: 2019/10/28
 * desc:  单例保存用户信息
 */
public class UserEntity {


    private String token;
    private String userId;
    private UserInfo userInfo;
    private ConfigBean configEntity;
    private int points;//当前用户金币数
    private float cash;//可兑换现金数
    private static UserEntity userEntity;
    private int target_step;//目标步数
    private String imei;
    private String mac;
    private String android_id;
    private String nickname;//游客昵称

    private boolean isRed;//是否领取红包
    private UserEntity() {

    }

    public static UserEntity getInstance() {
        if (userEntity == null) {
            synchronized (UserEntity.class) {
                if (userEntity == null) {
                    userEntity = new UserEntity();
                }
            }
        }
        return userEntity;
    }



    public UserInfo getUserInfo() {
        if (userInfo == null) {
            userInfo = (UserInfo) SPUtil.readObject(MyApplication.getContext(), "userInfo");
        }
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        SPUtil.saveObject(MyApplication.getContext(), "userInfo", userInfo);
        this.userInfo = userInfo;
    }


    public ConfigBean getConfigEntity() {
        if (configEntity == null) {
            configEntity = (ConfigBean) SPUtil.readObject(MyApplication.getContext(), "config");
        }
        return configEntity;
    }

    public void setConfigEntity(ConfigBean configEntity) {
        SPUtil.saveObject(MyApplication.getContext(), "config", configEntity);
        this.configEntity = configEntity;
    }

    public String getToken() {
        if (TextUtils.isEmpty(token)) {
            token = (String) SPUtil.getParam(MyApplication.getContext(), "token", "");
        }
        return token;
    }

    public void setToken(String token) {
        SPUtil.setParam(MyApplication.getContext(), "token", token);
        this.token = token;
    }

    public boolean isRed() {
        isRed = (boolean) SPUtil.getParam(MyApplication.getContext(), "isRed", false);
        return isRed;
    }

    public void setRed(boolean red) {
        SPUtil.setParam(MyApplication.getContext(), "isRed", red);
        this.isRed = red;
    }

    public String getNickname() {
        if (TextUtils.isEmpty(nickname)) {
            nickname = (String) SPUtil.getParam(MyApplication.getContext(), "nickname","");
        }
        return nickname;
    }

    public void setNickname(String  nickname) {
        SPUtil.setParam(MyApplication.getContext(), "nickname", nickname);
        this.nickname = nickname;
    }

    public String getUserId() {
        if (TextUtils.isEmpty(userId)) {
            userId = (String) SPUtil.getParam(MyApplication.getContext(), "userId", "");
        }
        return userId;
    }

    public void setUserId(String userId) {
        SPUtil.setParam(MyApplication.getContext(), "userId", userId);
        this.userId = userId;
    }

    public int getPoints() {
        points = (int) SPUtil.getParam(MyApplication.getContext(), "points", 0);
        return points;
    }

    public void setPoints(int points) {
        SPUtil.setParam(MyApplication.getContext(), "points", points);
        this.points = points;
    }

    public float getCash() {
        cash = (float) SPUtil.getParam(MyApplication.getContext(), "cash", 0f);
        return cash;
    }

    public void setCash(float cash) {
        SPUtil.setParam(MyApplication.getContext(), "cash", cash);
        this.cash = cash;
    }

    public int getTarget_step() {
        return target_step;
    }

    public void setTarget_step(int target_step) {
        this.target_step = target_step;
    }

    public String getImei() {
        if(TextUtils.isEmpty(imei)){
            imei = (String) SPUtil.getParam(MyApplication.getContext(), "imei", "");
        }
        return imei;
    }

    public void setImei(String imei) {
        SPUtil.setParam(MyApplication.getContext(), "imei", imei);
        this.imei = imei;
    }

    public String getMac() {
        if(TextUtils.isEmpty(mac)){
            mac = (String) SPUtil.getParam(MyApplication.getContext(), "mac", "");
        }
        return mac;
    }

    public void setMac(String mac) {
        SPUtil.setParam(MyApplication.getContext(), "mac", mac);
        this.mac = mac;
    }

    public String getAndroid_id() {
        if(TextUtils.isEmpty(android_id)){
            android_id = (String) SPUtil.getParam(MyApplication.getContext(), "android_id", "");
        }
        return android_id;
    }

    public void setAndroid_id(String android_id) {
        SPUtil.setParam(MyApplication.getContext(), "android_id", android_id);
        this.android_id = android_id;
    }

    /**
     * 用于退出登录
     */
    public void clearInfo() {
        setUserInfo(null);
        setUserId("");
        setToken("");
        setCash(0);
        setPoints(0);

    }
}
