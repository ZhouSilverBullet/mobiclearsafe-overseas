package com.mobi.clearsafe.statistical.umeng;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobi.clearsafe.BuildConfig;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.me.bean.UploadNikeName;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.statistical.LogFileUtil;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.DateUtils;
import com.mobi.clearsafe.utils.LogUtils;
import com.mobi.clearsafe.wxapi.bean.UserEntity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:zhaijinlu
 * date: 2019/11/5
 * desc: 友盟统计
 */
public class UmDataUtil {

    private static List<CommonParm> mList = new ArrayList<>();
    private static String FILE_NAME="log";

    public  static void UmDataInit(){
        //友盟统计
       // UMConfigure.init(MyApplication.getContext() ,UMConfigure.DEVICE_TYPE_PHONE, FILE_NAME);
        UMConfigure.init(MyApplication.getContext() , "5e7b2982895ccafd9d0005e2", AppUtil.getChannelName(MyApplication.getContext()), UMConfigure.DEVICE_TYPE_PHONE, "c58d0f094697b12eea329c79d9dcb7da");
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
        UMConfigure.setLogEnabled(Const.UMENG_DEBUG);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String info= LogFileUtil.readFile(MyApplication.getContext(),FILE_NAME);
                    List<CommonParm> list = null;
                    if (!TextUtils.isEmpty(info)) {
                        list = JSONObject.parseArray(info, CommonParm.class);
                    }
                    if (mList == null) {
                        mList = new ArrayList<>();
                    }
                    if (list != null) {
                        mList.addAll(list);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


    public static void track(String eventId, Map map) {
        MobclickAgent.onEventObject(MyApplication.getContext(), eventId, map);
        upDateService(eventId);
    }

    //上传服务器
    public static void upDateService(String eventId){
        CommonParm parm=new CommonParm();
            parm.setDay( DateUtils.getStringDateDay());//日期
            parm.setTime(DateUtils.getStringDateMin());//时间
            parm.setDeviceid(Const.deviceID);
            parm.setPlatform("1");//1 android  2 ios
            parm.setSdkv(AppUtil.packageName(MyApplication.getContext()));
            parm.setChannel_no(AppUtil.getChannelName(MyApplication.getContext()));
            parm.setEventId(eventId);
            parm.setAppID(MyApplication.getContext().getPackageName());
            parm.setDevice_model(AppUtil.getSystemModel());
            parm.setUserID(UserEntity.getInstance().getUserId());
            if (mList == null) {
                mList = new ArrayList<>();
            }
            mList.add(parm);
            if (mList.size() % 50==0&& UserEntity.getInstance().getConfigEntity().isIs_upload_log()) {//每50条发送一次给服务器
                upLoadData();
            } else if (mList.size() % 5== 0) {//每5条存一次本地
                saveFile();
            }

    }

    private static void upLoadData(){
        LogFileUtil.delete(MyApplication.getContext(),FILE_NAME);
        JSONObject jb = new JSONObject();
        jb.put("content", JSONArray.parseArray(JSON.toJSONString(mList)));
        final String postData = jb.toJSONString();
        mList = new ArrayList<>();
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .updateBehavior(jb)
                .compose(CommonSchedulers.<BaseResponse<UploadNikeName>>observableIO2Main(MyApplication.getContext()))
                .subscribe(new BaseObserver<UploadNikeName>() {
                    @Override
                    public void onSuccess(UploadNikeName demo) {
                        LogUtils.e("用户行为日志上传成功");
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        List<CommonParm> list = new ArrayList<>();
                        JSONObject object = JSON.parseObject(postData);
                        list = JSONObject.parseArray(object.getJSONArray("content").toJSONString(), CommonParm.class);
                        if (list != null) {
                            mList.addAll(list);
                            saveFile();
                        }
                    }
                });

    }

    private static void saveFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String saveString = JSONArray.parseArray(JSON.toJSONString(mList)).toJSONString();
                    LogFileUtil.saveFile(saveString,MyApplication.getContext(),FILE_NAME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
