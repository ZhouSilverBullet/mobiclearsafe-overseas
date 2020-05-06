package com.mobi.clearsafe.wxapi.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/10/28
 * desc:
 */
public class UserInfo implements Serializable {

    private String user_id;
    private int exchange_step;
    private String nickname;
    private String head_img_url;
    private int target_step;
    private String phone;
    private int guest;//1-游客 2-非游客
    private int weixin_bind_status;//0-未绑定微信 1-已绑定微信
    private int phone_bind_status;//0-未绑定手机号 1-已绑定手机号
    private String invite_code;//邀请码

    public int getGuest() {
        return guest;
    }

    public void setGuest(int guest) {
        this.guest = guest;
    }

    public int getWeixin_bind_status() {
        return weixin_bind_status;
    }

    public void setWeixin_bind_status(int weixin_bind_status) {
        this.weixin_bind_status = weixin_bind_status;
    }

    public int getPhone_bind_status() {
        return phone_bind_status;
    }

    public void setPhone_bind_status(int phone_bind_status) {
        this.phone_bind_status = phone_bind_status;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getExchange_step() {
        return exchange_step;
    }

    public void setExchange_step(int exchange_step) {
        this.exchange_step = exchange_step;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_img_url() {
        return head_img_url;
    }

    public void setHead_img_url(String head_img_url) {
        this.head_img_url = head_img_url;
    }

    public int getTarget_step() {
        return target_step;
    }

    public void setTarget_step(int target_step) {
        this.target_step = target_step;
    }
}
