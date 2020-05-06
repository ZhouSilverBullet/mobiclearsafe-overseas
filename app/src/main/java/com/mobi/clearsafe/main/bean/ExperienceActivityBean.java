package com.mobi.clearsafe.main.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2020/2/11
 * desc: 集数字体验活动
 */
public class ExperienceActivityBean implements Serializable {

    private List<ExperienceActivityItemBean> list;

    public List<ExperienceActivityItemBean> getList() {
        return list;
    }

    public void setList(List<ExperienceActivityItemBean> list) {
        this.list = list;
    }

    public  class ExperienceActivityItemBean implements Serializable{
            private int class_id;
            private int activity_id;
            private String icon;//标题图片
            private String title;
            private int jump_type; //跳转类型 1-原生 2-h5 3-tab页
            private String jump_url;
            private String params;
            private int state;//状态 1立即参与 2立即领取 3己完成
            private int total_times;//总次数
            private int curr_times;//己完成次数

        public int getClass_id() {
            return class_id;
        }

        public void setClass_id(int class_id) {
            this.class_id = class_id;
        }

        public int getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(int activity_id) {
            this.activity_id = activity_id;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getTotal_times() {
            return total_times;
        }

        public void setTotal_times(int total_times) {
            this.total_times = total_times;
        }

        public int getCurr_times() {
            return curr_times;
        }

        public void setCurr_times(int curr_times) {
            this.curr_times = curr_times;
        }
    }
}
