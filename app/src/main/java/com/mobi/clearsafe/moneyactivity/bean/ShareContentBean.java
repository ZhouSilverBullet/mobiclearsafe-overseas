package com.mobi.clearsafe.moneyactivity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2019/12/6
 * desc: 分享内容
 */
public class ShareContentBean implements Serializable {

    private String name;
    private String introduction;
    private String title_picture;
    private String jump_address;
    private String code;
    private int max;
    private int total;
    private List<HotBroadCast> withdraw_hot_broadcast;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<HotBroadCast> getWithdraw_hot_broadcast() {
        return withdraw_hot_broadcast;
    }

    public void setWithdraw_hot_broadcast(List<HotBroadCast> withdraw_hot_broadcast) {
        this.withdraw_hot_broadcast = withdraw_hot_broadcast;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTitle_picture() {
        return title_picture;
    }

    public void setTitle_picture(String title_picture) {
        this.title_picture = title_picture;
    }

    public String getJump_address() {
        return jump_address;
    }

    public void setJump_address(String jump_address) {
        this.jump_address = jump_address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public class HotBroadCast{
        private String title;
        private String red_sign;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRed_sign() {
            return red_sign;
        }

        public void setRed_sign(String red_sign) {
            this.red_sign = red_sign;
        }
    }
}
