package com.mobi.clearsafe.main.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2020/2/11
 * desc: 热门播报
 */
public class HotNoticeBean implements Serializable {

    private List<HotNotice> list;

    public List<HotNotice> getList() {
        return list;
    }

    public void setList(List<HotNotice> list) {
        this.list = list;
    }

    public class HotNotice implements Serializable{
        private String title;
        private int red_sign;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getRed_sign() {
            return red_sign;
        }

        public void setRed_sign(int red_sign) {
            this.red_sign = red_sign;
        }
    }
}
