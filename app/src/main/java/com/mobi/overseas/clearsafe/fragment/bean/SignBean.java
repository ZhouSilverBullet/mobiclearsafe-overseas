package com.mobi.overseas.clearsafe.fragment.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * author : liangning
 * date : 2019-10-22  17:09
 */
public class SignBean {


    /**
     * continuity_signin_number : 3
     * is_today_signin : 1
     * tomorrow_points : 88
     * week_all : [{"number":1,"show":4,"points":18},{"number":2,"show":3,"points":28},{"number":3,"show":0,"points":88},{"number":4,"show":0,"points":38},{"number":5,"show":0,"points":48},{"number":6,"show":0,"points":88},{"number":7,"show":1,"points":128}]
     */

    public int continuity_signin_number;
    public int is_today_signin;
    public int tomorrow_points;
    public int week_day;//一周中的第几天
    public List<WeekAll> week_all;
    public List<WelfareList> today_welfare_list;

    public static SignBean objectFromData(String str) {

        return new Gson().fromJson(str, SignBean.class);
    }

    public static class WeekAll {
        /**
         * number : 1
         * show : 4
         * points : 18
         */

        public int number;
        public int show;
        public int points;
        public int points_plus;
        private int show_type;

        public static WeekAll objectFromData(String str) {

            return new Gson().fromJson(str, WeekAll.class);
        }
    }

    public static class WelfareList{

        public String name;
        public String jump_url;
        public int jump_type;
        public String params;
        public String butten_text;

        public static WelfareList objectFromData(String str) {

            return new Gson().fromJson(str, WelfareList.class);
        }

    }
}
