package com.mobi.overseas.clearsafe.me.bean;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * author : liangning
 * date : 2020-02-11  21:14
 */
public class MyCardBean implements Serializable {


    /**
     * type : 100
     * icon : http://
     * title : 标题
     * number : 10
     * content : 内容
     * deadline : 过期时间
     * jump_url : xx
     * jump_type : 1
     * params : 参数
     * status : 2
     */

    public int type;
    public String icon="";
    public String title="";
    public int number;
    public String content="";
    public String deadline="";
    public String jump_url="";
    public int jump_type;
    public String params="";
    public int status;
    public boolean showbottom = false;
    public String introduction = "";
    public String activity_name= "";

    public static MyCardBean objectFromData(String str) {

        return new Gson().fromJson(str, MyCardBean.class);
    }
}
