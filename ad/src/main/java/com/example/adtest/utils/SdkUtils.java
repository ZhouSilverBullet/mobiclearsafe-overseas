package com.example.adtest.utils;

import com.example.adtest.bean.AdBean;
import com.example.adtest.bean.ConfigItemBean;

import java.util.List;

/**
 * author : liangning
 * date : 2019-12-17  17:40
 */
public class SdkUtils {


    /**
     * 通过平台类型 得到对应的加载信息
     * @param bean
     * @param plant
     * @return
     */
    public static AdBean getShowAdBean(ConfigItemBean bean, String plant) {
        List<AdBean> list = bean.getNetwork();
        AdBean adBean = null;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i).getSdk().equals(plant)) {
                adBean = list.get(i);
                break;
            }
        }
        return adBean;
    }
}
