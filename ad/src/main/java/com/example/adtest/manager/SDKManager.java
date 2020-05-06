package com.example.adtest.manager;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.adtest.bean.AdBean;
import com.example.adtest.bean.ConfigBean;
import com.example.adtest.bean.ConfigItemBean;
import com.example.adtest.bean.InfoItem;
import com.example.adtest.bean.ParameterBean;
import com.example.adtest.bean.SdkInfoItem;
import com.example.adtest.config.TTAdManagerHolder;
import com.example.adtest.utils.HttpClientUtils;
import com.example.adtest.utils.PhoneUtils;
import com.example.adtest.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author : liangning
 * date : 2019-11-07  14:33
 */
public class SDKManager {

    /**
     * 建议在applocation中调用此方法
     * 广告SDK初始化 穿山甲和广点通
     *
     * @param context 建议传入application的context
     */
    public static void init(final Context context, String configUrl, final InitListener listener) {
//        configUrl = "https://mex-cdn-cn-beijing.oss-cn-beijing.aliyuncs.com/mediation.merge.txt";
        String channel_no = (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "channel", "");
        String deviceid = (String) SharedPreferencesUtils.getParam(context.getApplicationContext(), "deviceid", "");
        Constants.CHANNEL = channel_no;
        Constants.DEVICEID = deviceid;
        String realRequest = configUrl + "?sdkv=" + Constants.SDK_VERSION
                + "&imei=" + PhoneUtils.getIMEI(context.getApplicationContext())
                + "&platform=" + Constants.PLATFORM
                + "&media_id=" + Constants.MEDIA_ID
                + "androidid=" + SharedPreferencesUtils.getParam(context.getApplicationContext(), "deviceid", "");
        Constants.REQUESR_URL = realRequest;//将请求地址放在公共参数文件中 不用重复拼接

        ConfigBean configBean = (ConfigBean) SharedPreferencesUtils.readObject(context.getApplicationContext(), Constants.STORAGE_KEY);
        if (configBean != null) {
            Constants.bean = configBean;
            ToDeal(configBean);
        }
//        Log.e("请求地址", realRequest);

        HttpClientUtils.get(realRequest, new HttpClientUtils.OnRequestCallBack() {
            @Override
            public void onSuccess(String json) {
                try {

                    JSONObject jsonObject = JSON.parseObject(json);

                    ConfigBean bean = ParsingConfig.ParsConfig(jsonObject);
//                    InitializeSDK(bean.getSdk_info(), context.getApplicationContext());
                    ToDeal(bean);
                    if (bean != null) {
                        bean.setStorage_time(System.currentTimeMillis());
                        SharedPreferencesUtils.saveObject(context.getApplicationContext(), Constants.STORAGE_KEY, bean);
                        Constants.bean = bean;
                    }
                    if (listener != null) {
                        listener.Success();
                    }
//                    TTAdManagerHolder.init(context);
                    //此处应按服务器传回来的数据初始化SDK
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorMsg) {
                if (listener != null) {
                    listener.Failure(errorMsg);
                }
                Map<String, ConfigItemBean> admap = new HashMap<>();
                admap.put(Constants.default_native_posid, DefaultNativeConfigItem());
                admap.put(Constants.default_video_posid, DefaultVideoConfigItem());

                Constants.adMap = admap;
            }
        });
    }

    public static void InitializeSDK(Context context) {
        if (Constants.bean != null && Constants.bean.getSdk_info() != null) {
            List<SdkInfoItem> list = Constants.bean.getSdk_info();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                SdkInfoItem item = list.get(i);
                if (item.getMid().equals(Constants.MEDIA_ID)) {
                    List<InfoItem> infoList = item.getInfo();
                    if (infoList != null) {
                        int infoSize = infoList.size();
                        for (int j = 0; j < infoSize; j++) {
                            InfoItem infoItem = infoList.get(j);
                            if (infoItem.getSdk().equals(Constants.TT_KEY)) {
                                TTAdManagerHolder.singleInit(context.getApplicationContext(), infoItem.getAppid(), infoItem.getAppname());
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 将数据转换成适合本地使用的数据结构
     *
     * @param bean
     */
    public static void ToDeal(ConfigBean bean) {
        if (bean == null) return;
        List<ConfigItemBean> mList = bean.getList();
        int size = mList.size();

        Map<String, ConfigItemBean> admap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            ConfigItemBean item = mList.get(i);
            if (item.getSort_type() == Constants.SORT_TYPE_ORDER) {//如果是按顺序 将list内容进行排序
                sortList(item.getNetwork());
            }
            if (item.getSort_type() == Constants.SORT_TYPE_SERVICE_ORDER) {
                Constants.SortParameterMap.put(item.getPosid(), 0);
            }

            if (admap.get(item.getPosid()) == null) {
                admap.put(item.getPosid(), item);
            }
        }
        admap.put(Constants.default_native_posid, DefaultNativeConfigItem());
        admap.put(Constants.default_video_posid, DefaultVideoConfigItem());
        admap.put(Constants.default_inter_posid, DefaultInterActionConfigItem());
        Constants.adMap = admap;
    }

    /**
     * 排序广告sdk列表
     *
     * @return
     */
    private static void sortList(List<AdBean> list) {
        Comparator<AdBean> netTypeComparator = new Comparator<AdBean>() {
            @Override
            public int compare(AdBean o1, AdBean o2) {
                return o1.getOrder() - o2.getOrder();
            }
        };
        Collections.sort(list, netTypeComparator);

    }

    //创建默认信息流广告配置
    private static ConfigItemBean DefaultNativeConfigItem() {
        ConfigItemBean videoItem = new ConfigItemBean();
        videoItem.setSort_type(Constants.SORT_TYPE_ORDER);
        videoItem.setPosid(Constants.default_video_posid);
        List<AdBean> adBeanList = new ArrayList<>();

        //广点通
        AdBean gdt_ad = new AdBean();
        gdt_ad.setOrder(1);
        gdt_ad.setSdk("gdt");
        gdt_ad.setName("gdt_falt信息流");
        ParameterBean parameter_gdt = new ParameterBean();
        parameter_gdt.setPosid("8060796141491537");
        parameter_gdt.setAppid("1109966989");
        parameter_gdt.setAppname("快清理_android");
        gdt_ad.setParameterBean(parameter_gdt);
        adBeanList.add(gdt_ad);
        videoItem.setNetwork(adBeanList);
        //穿山甲
        AdBean tt_ad = new AdBean();//创建默认 信息流广告
        tt_ad.setOrder(2);
        tt_ad.setSdk("tt");
        tt_ad.setName("tt_falt信息流");
        ParameterBean parameter_tt = new ParameterBean();
        parameter_tt.setPosid("936888052");
        parameter_tt.setAppname(Constants.TT_APPNAME);
        parameter_tt.setAppid(Constants.TT_APPID);
        tt_ad.setParameterBean(parameter_tt);
        adBeanList.add(tt_ad);

        return videoItem;
    }

    //创建默认激励视频广告配置
    private static ConfigItemBean DefaultVideoConfigItem() {
        ConfigItemBean bean = new ConfigItemBean();
        bean.setPosid(Constants.default_video_posid);
        bean.setSort_type(Constants.SORT_TYPE_ORDER);
        List<AdBean> adList = new ArrayList<>();
        AdBean adBean = new AdBean();
        adBean.setName("tt_fault激励视频");
        adBean.setSdk("tt");
        adBean.setOrder(1);
        ParameterBean parameter_tt = new ParameterBean();
        parameter_tt.setAppid(Constants.TT_APPID);
        parameter_tt.setAppname(Constants.TT_APPNAME);
        parameter_tt.setPosid("936888329");
        adBean.setParameterBean(parameter_tt);
        adList.add(adBean);
        bean.setNetwork(adList);
        return bean;
    }

    //创建默认插屏广告配置
    private static ConfigItemBean DefaultInterActionConfigItem() {
        ConfigItemBean videoItem = new ConfigItemBean();
        videoItem.setSort_type(Constants.SORT_TYPE_ORDER);
        videoItem.setPosid(Constants.default_inter_posid);
        List<AdBean> adBeanList = new ArrayList<>();
        //穿山甲
        AdBean tt_ad = new AdBean();//创建默认 信息流广告
        tt_ad.setOrder(1);
        tt_ad.setSdk("tt");
        tt_ad.setName("tt_falt插屏");
        ParameterBean parameter_tt = new ParameterBean();
        parameter_tt.setPosid("941395450");
        parameter_tt.setAppname(Constants.TT_APPNAME);
        parameter_tt.setAppid(Constants.TT_APPID);
        tt_ad.setParameterBean(parameter_tt);
        adBeanList.add(tt_ad);
        //广点通
        AdBean gdt_ad = new AdBean();
        gdt_ad.setOrder(2);
        gdt_ad.setSdk("gdt");
        gdt_ad.setName("gdt_falt插屏");
        ParameterBean parameter_gdt = new ParameterBean();
        parameter_gdt.setPosid("8060998441514288");
        parameter_gdt.setAppid("1109966989");
        parameter_gdt.setAppname("快清理_android");
        gdt_ad.setParameterBean(parameter_gdt);
        adBeanList.add(gdt_ad);
        videoItem.setNetwork(adBeanList);
        return videoItem;
    }

    public static void setDeviceId(String deviceId, Context context) {
        SharedPreferencesUtils.setParam(context.getApplicationContext(), "deviceid", deviceId);
        Constants.DEVICEID = deviceId;
    }

    public static void setChannel(String channel, Context context) {
        SharedPreferencesUtils.setParam(context.getApplicationContext(), "channel", channel);
        Constants.CHANNEL = channel;
    }

    //ValPub 初始化
    public static void InitValPub(Context context){
      //  ValPubSDK.init(context);
    }
    public interface InitListener {
        void Success();

        void Failure(String errorMsg);
    }

}
