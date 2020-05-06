package com.mobi.clearsafe.moneyactivity.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/12/10
 * desc: 赚钱页面倒流入口
 */
public class ActivityEntity implements Serializable {
    private int id;
    private String name;
    private String url;//活动url
    private String jump_url; //跳转url
    private  int weight;
    private int status;
    private int type;  //类型（1-轮播图 2-插屏 3-首页红包 4-入口
    private String create_time;
    private String params;//跳转参数
    private int judge_type;//跳转类型 1-原生 2-h5 3-底部tab切换
    private String platform; //平台 1安卓 2ios



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public int getJudge_type() {
        return judge_type;
    }

    public void setJudge_type(int judge_type) {
        this.judge_type = judge_type;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "ActivityEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", jump_url='" + jump_url + '\'' +
                ", weight=" + weight +
                ", status=" + status +
                ", type=" + type +
                ", create_time='" + create_time + '\'' +
                ", params='" + params + '\'' +
                ", judge_type=" + judge_type +
                ", platform='" + platform + '\'' +
                '}';
    }
}
