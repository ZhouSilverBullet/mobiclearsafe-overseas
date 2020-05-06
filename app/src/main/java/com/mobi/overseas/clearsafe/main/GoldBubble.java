package com.mobi.overseas.clearsafe.main;

import com.mobi.overseas.clearsafe.main.bean.DownLoadBean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/7
 * desc:金币泡泡
 * */
public class GoldBubble implements Serializable {

    private String id;
    private int system_id;
    private int points;
    private int type;  ////1-金币泡泡 2-金币问号
    private int pop_type;//弹窗类型
    private String pop_up_message;//button文案
    private int close_ad_type;//关闭广告时是否出广告
    private boolean information_flow_ad;//是否显示信息流广告
    private int h1fg;//插屏广告样式ID    3000不弹出 3001插屏不带误点  3002插屏带误点  3003全屏不带误点  3004全屏误点 3005视频不误点 3006视频误点
    private int is_jump;//是否跳转
    private int jump_type;//跳转类型 跟首页相同
    private String jump_url;//跳转地址
    private DownLoadBean download;

    private String name;


    public boolean isInformation_flow_ad() {
        return information_flow_ad;
    }

    public void setInformation_flow_ad(boolean information_flow_ad) {
        this.information_flow_ad = information_flow_ad;
    }

    public int getClose_ad_type() {
        return close_ad_type;
    }

    public void setClose_ad_type(int close_ad_type) {
        this.close_ad_type = close_ad_type;
    }

    public int getPop_type() {
        return pop_type;
    }

    public void setPop_type(int pop_type) {
        this.pop_type = pop_type;
    }

    public String getPop_up_message() {
        return pop_up_message;
    }

    public void setPop_up_message(String pop_up_message) {
        this.pop_up_message = pop_up_message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSystem_id() {
        return system_id;
    }

    public void setSystem_id(int system_id) {
        this.system_id = system_id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getH1fg() {
        return h1fg;
    }

    public void setH1fg(int h1fg) {
        this.h1fg = h1fg;
    }

    public int getIs_jump() {
        return is_jump;
    }

    public void setIs_jump(int is_jump) {
        this.is_jump = is_jump;
    }

    public int getJump_type() {
        return jump_type;
    }

    public void setJump_type(int jump_type) {
        this.jump_type = jump_type;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public DownLoadBean getDownload() {
        return download;
    }

    public void setDownload(DownLoadBean download) {
        this.download = download;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

