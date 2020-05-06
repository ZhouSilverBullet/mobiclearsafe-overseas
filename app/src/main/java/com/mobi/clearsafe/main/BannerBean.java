package com.mobi.clearsafe.main;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2019/11/1
 * desc:
 */
public class BannerBean implements Serializable {

    /**
     * slide_show : [{"name":"幸运大转盘","url":"http://cdn.findwxapp.com/slideshow/turntable.gif","jump_url":"http://dev.findwxapp.com/","platform":"ios","judge_type":2,"params":""}]
     * red_envelope : {"name":"首页红包入口","url":"http://cdn.findwxapp.com/app_icon/firstPageRedEnvelope.gif","jump_url":"xx","platform":"ios","judge_type":2,"params":""}
     * hot_activity_list : [{"name":"热门活动1","url":"xx","jump_url":"xx","platform":"ios","judge_type":2,"params":""},{"name":"热门活动2","url":"xx","jump_url":"xx","platform":"ios","judge_type":2,"params":""}]
     * insert_screen : [{"name":"插屏","url":"xx","jump_url":"xx","platform":"ios","judge_type":2,"params":""}]
     */

    public RedEnvelope red_envelope;
    public List<SlideShow> slide_show;
    public List<HotActivityList> hot_activity_list;
    public List<InsertScreen> insert_screen;

    public int total_days;

    public static BannerBean objectFromData(String str) {

        return new Gson().fromJson(str, BannerBean.class);
    }

    public static class RedEnvelope {
        /**
         * name : 首页红包入口
         * url : http://cdn.findwxapp.com/app_icon/firstPageRedEnvelope.gif
         * jump_url : xx
         * platform : ios
         * judge_type : 2
         * params :
         */

        public String name;
        public String url;
        public String jump_url;
        public String platform;
        public int judge_type;
        public String params;

        public static RedEnvelope objectFromData(String str) {

            return new Gson().fromJson(str, RedEnvelope.class);
        }
    }

    public static class SlideShow {
        /**
         * name : 幸运大转盘
         * url : http://cdn.findwxapp.com/slideshow/turntable.gif
         * jump_url : http://dev.findwxapp.com/
         * platform : ios
         * judge_type : 2
         * params :
         */

        public String name;
        public String url;
        public String jump_url;
        public String platform;
        public int judge_type;
        public String params;

        public static SlideShow objectFromData(String str) {

            return new Gson().fromJson(str, SlideShow.class);
        }
    }

    public static class HotActivityList {
        /**
         * name : 热门活动1
         * url : xx
         * jump_url : xx
         * platform : ios
         * judge_type : 2
         * params :
         */

        public String name;
        public String url;
        public String icon;
        public String content;
        public String jump_url;
        public String platform;
        public int judge_type;
        public String params;
        public String version;

        public static HotActivityList objectFromData(String str) {

            return new Gson().fromJson(str, HotActivityList.class);
        }
    }

    public static class InsertScreen {
        /**
         * name : 插屏
         * url : xx
         * jump_url : xx
         * platform : ios
         * judge_type : 2
         * params :
         */

        public String name;
        public String url;
        public String jump_url;
        public String platform;
        public int judge_type;
        public String params;
        public boolean is_ad;

        public static InsertScreen objectFromData(String str) {

            return new Gson().fromJson(str, InsertScreen.class);
        }
    }
}
