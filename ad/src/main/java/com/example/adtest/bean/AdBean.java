package com.example.adtest.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 广告数据类 对应network中的item
 * author : liangning
 * date : 2019-11-09  14:02
 */
public class AdBean implements Serializable {

    private String name;
    private String sdk;
    private int order;
    private ParameterBean parameterBean;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public ParameterBean getParameterBean() {
        return parameterBean;
    }

    public void setParameterBean(ParameterBean parameterBean) {
        this.parameterBean = parameterBean;
    }
}
