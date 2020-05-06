package com.example.adtest.manager;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.adtest.bean.ConfigBean;
import com.example.adtest.bean.ConfigItemBean;
import com.example.adtest.utils.HttpClientUtils;
import com.example.adtest.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * author : liangning
 * date : 2019-11-07  14:37
 */
public class Constants {

    public static String REQUESR_URL = "";//请求广告配置接口

    public static String UPLOAD_URL = "";//统计数据发送接口

    public static String MEDIA_ID = "1028";

    public static String PLATFORM = "android";

    public static String STORAGE_KEY = "storage_key";//存储的广告信息的key

    public static String SDK_VERSION = "1.0.0";

    public static String DEVICEID = "";//设备ID

    public static String CHANNEL = "";//渠道

    public static boolean DEBUG = false; //true 测试服地址 false 正式服地址

    public static final String default_native_posid = "1028018";

    public static final String default_video_posid = "1028020";

    public static final String default_inter_posid = "10249999";

    public static final int STATUS_CODE_TRUE = 1;
    public static final int STATUS_CODE_FALSE = 0;

    public static final String Statistical_ad = "statistical_ad";//统计数据存储在SP的key

    public static String TT_APPID = false ? "******" : "5057651";//穿山甲APPID
    public static String TT_APPNAME = false ? "******" :"快清理_android";//穿上甲APPName

    public static String TT_NATIVE_CODE_ID = "936888923";//穿山甲信息流广告ID
    public static String TT_REWARD_CODE_ID = "936888329";//穿山甲激励视频广告ID
    public static int TT_SORT = 1;

    public static String GDT_APPID = "1109966989";//广点通APPID
    public static String GDT_POSID = "8060796141491537";//广点通广告ID
    public static String GDT_VIDEO_POSID = "2090845242931421";//激励视频广告
    public static int GDT_SORT = 2;

    public static String SIGMOB_APPID = "3189";//sigmob APPID
    public static String SIGMOB_APPKEY = "2908fda1b21b2718";//sigmob APPKEY


    public static ConfigBean bean;//服务器返回的广告相关信息

    public static Map<String, ConfigItemBean> adMap = new HashMap<>();//服务器返回的广告信息 根据posID处理出来的hash表


    public static final String TT_KEY = "tt";//穿山甲广告对应的key
    public static final String GDT_KEY = "gdt";//广点通广告对应的key
    public static final String MOBI_KEY="mobi";
    public static final String DEFAULT_KEY = "default";
    public static final String SIGMOB = "sigmob";//sigmob广告对应的key
    public static final int SORT_TYPE_ORDER = 1;//广告展示顺序类型 按顺序
    public static final int SORT_TYPE_PRICE = 2;//广告展示顺序类型 按价格
    public static final int SORT_TYPE_ORDER__PRICE = 3;//广告展示顺序类型 优先顺序 顺序相同则按价格
    public static final int SORT_TYPE_SERVICE_ORDER = 4;//按照服务器返回的固定顺序加载对应平台的广告

    public static ConfigItemBean getAdItemNoContext(String key) {
        if (adMap != null) {
            return adMap.get(key);
        }
        return null;
    }

    public static ConfigItemBean getAdItem(String key, final Context context) {
        long NowTime = System.currentTimeMillis();
        if (bean == null) {
            bean = (ConfigBean) SharedPreferencesUtils.readObject(context, STORAGE_KEY);
            SDKManager.ToDeal(bean);
        }
        if (bean == null) return null;
        if (NowTime - bean.getStorage_time() > bean.getTimeout() * 1000) {
            HttpClientUtils.get(REQUESR_URL, new HttpClientUtils.OnRequestCallBack() {
                @Override
                public void onSuccess(String json) {
                    try {
                        JSONObject jsonObject = JSON.parseObject(json);
                        ConfigBean bean = ParsingConfig.ParsConfig(jsonObject);
                        SDKManager.ToDeal(bean);
                        if (bean != null) {
                            bean.setStorage_time(System.currentTimeMillis());
                            SharedPreferencesUtils.saveObject(context.getApplicationContext(), Constants.STORAGE_KEY, bean);
                            Constants.bean = bean;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String errorMsg) {
                }
            });
        }
        if (adMap != null) {
            return adMap.get(key);
        }
        return null;
    }

    /**
     * 按服务器控制加载顺序加载广告使用到的 key 自有POSID value 相应集合下标，每展示一次value 加1
     */
    public static Map<String, Integer> SortParameterMap = new HashMap<>();
}
