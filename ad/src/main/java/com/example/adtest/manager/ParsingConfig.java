package com.example.adtest.manager;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.adtest.bean.AdBean;
import com.example.adtest.bean.ConfigBean;
import com.example.adtest.bean.ConfigItemBean;
import com.example.adtest.bean.InfoItem;
import com.example.adtest.bean.ParameterBean;
import com.example.adtest.bean.SdkInfoItem;


import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2019-11-09  13:53
 */
public class ParsingConfig {


    public static ConfigBean ParsConfig(JSONObject json) {
        ConfigBean bean = new ConfigBean();
        try {
            long timeout = json.getLong("timeout");
            bean.setTimeout(timeout);
            long ad_adk_req_timeout = json.getLong("ad_adk_req_timeout");
            bean.setAd_adk_req_timeout(ad_adk_req_timeout);
            JSONArray sdkinfo = json.getJSONArray("sdk_info");
            List<SdkInfoItem> sdkinfoList = ParsingSdkInfo(sdkinfo);
            bean.setSdk_info(sdkinfoList);
            JSONArray jsonList = json.getJSONArray("list");
            List<ConfigItemBean> itemList = new ArrayList<>();
            if (jsonList != null) {
                int size = jsonList.size();
                for (int i = 0; i < size; i++) {
                    JSONObject listitem = jsonList.getJSONObject(i);
                    ConfigItemBean itembean = new ConfigItemBean();
                    String posid = listitem.getString("posid");
                    itembean.setPosid(posid);
                    int sort_type = listitem.getIntValue("sort_type");
                    itembean.setSort_type(sort_type);
                    JSONArray parameter = listitem.getJSONArray("sort_parameter");
                    if (parameter!=null){
                        List<String> sort_parameter = parameter.toJavaList(String.class);
                        itembean.setSort_parameter(sort_parameter);
                    }
                    JSONArray networkArray = listitem.getJSONArray("network");
                    List<AdBean> networkList = new ArrayList<>();
                    if (networkArray != null) {
                        int netSize = networkArray.size();
                        for (int k = 0; k < netSize; k++) {
                            JSONObject networkItem = networkArray.getJSONObject(k);
                            AdBean adBean = new AdBean();
                            String name = networkItem.getString("name");
                            adBean.setName(name);
                            String sdk = networkItem.getString("sdk");
                            adBean.setSdk(sdk);
                            int order = networkItem.getIntValue("order");
                            adBean.setOrder(order);
                            JSONObject parameterObj = networkItem.getJSONObject("parameter");
                            ParameterBean pBean = new ParameterBean();
                            if (parameterObj != null) {
                                String appid = parameterObj.getString("appid");
                                pBean.setAppid(appid);
                                String appname = parameterObj.getString("appname");
                                pBean.setAppname(appname);
                                String Pposid = parameterObj.getString("posid");
                                pBean.setPosid(Pposid);
                            }
                            adBean.setParameterBean(pBean);
                            networkList.add(adBean);
                        }
                    }
                    itembean.setNetwork(networkList);
                    itemList.add(itembean);
                }
            }
            bean.setList(itemList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bean;
    }

    private static List<SdkInfoItem> ParsingSdkInfo(JSONArray json) {
        List<SdkInfoItem> mList = new ArrayList<>();
        if (json != null) {
            int size = json.size();
            for (int i = 0; i < size; i++) {
                JSONObject obj = json.getJSONObject(i);
                SdkInfoItem sdkItem = new SdkInfoItem();
                String mid = obj.getString("mid");
                if (!TextUtils.isEmpty(mid)) {
                    sdkItem.setMid(mid);
                }
                JSONArray info = obj.getJSONArray("info");
                List<InfoItem> infoList = new ArrayList<>();
                if (info != null) {
                    int infoSize = info.size();
                    for (int j = 0; j < infoSize; j++) {
                        JSONObject jsonObject = info.getJSONObject(j);
                        InfoItem infoItem = ParsingInfoItem(jsonObject);
                        infoList.add(infoItem);
                    }
                }
                sdkItem.setInfo(infoList);
                mList.add(sdkItem);
            }
        }


        return mList;
    }

    private static InfoItem ParsingInfoItem(JSONObject json) {
        InfoItem item = new InfoItem();
        if (json != null) {
            String appid = json.getString("appid");
            String appname = json.getString("appname");
            String sdk = json.getString("sdk");
            if (!TextUtils.isEmpty(appid)) {
                item.setAppid(appid);
            }
            if (!TextUtils.isEmpty(appname)) {
                item.setAppname(appname);
            }
            if (!TextUtils.isEmpty(sdk)) {
                item.setSdk(sdk);
            }
        }
        return item;
    }

}
