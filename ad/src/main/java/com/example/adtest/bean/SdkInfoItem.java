package com.example.adtest.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2019-11-25  17:00
 */
public class SdkInfoItem implements Serializable {

    private String mid = "";
    private List<InfoItem> info = new ArrayList<>();

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public List<InfoItem> getInfo() {
        return info;
    }

    public void setInfo(List<InfoItem> info) {
        this.info = info;
    }
}
