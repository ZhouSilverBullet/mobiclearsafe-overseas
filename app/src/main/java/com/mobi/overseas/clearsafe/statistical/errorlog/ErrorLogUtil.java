package com.mobi.overseas.clearsafe.statistical.errorlog;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.me.bean.UploadNikeName;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.LogFileUtil;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.DateUtils;
import com.mobi.overseas.clearsafe.utils.LogUtils;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2019/11/19
 * desc: 上报错误日志
 */
public class ErrorLogUtil {
    private static List<ErrorLogEntity> mList = new ArrayList<>();
    private static String FILE_NAME="errolog";

    public static void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String info= LogFileUtil.readFile(MyApplication.getContext(),FILE_NAME);
                    List<ErrorLogEntity> list = null;
                    if (!TextUtils.isEmpty(info)) {
                        list = JSONObject.parseArray(info, ErrorLogEntity.class);
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

    //上传服务器
    public static void upDateService(String code){
        ErrorLogEntity parm=new ErrorLogEntity();
        parm.setDay( DateUtils.getStringDateDay());
        parm.setTime(DateUtils.getStringDateMin());
        parm.setDeviceid(Const.deviceID);
        parm.setPlatform("android");
        parm.setSdkv(AppUtil.packageName(MyApplication.getContext()));
        parm.setChannel_no(AppUtil.getChannelName(MyApplication.getContext()));
        parm.setErrorCode(code);
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.add(parm);
        if (mList.size() % 5==0&& UserEntity.getInstance().getConfigEntity().isIs_upload_log()) {//每5条发送一次给服务器
            upLoadData();
        } else if (mList.size() % 2== 0) {//每2条存一次本地
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
                .updateErrLog(jb)
                .compose(CommonSchedulers.<BaseResponse<UploadNikeName>>observableIO2Main(MyApplication.getContext()))
                .subscribe(new BaseObserver<UploadNikeName>() {
                    @Override
                    public void onSuccess(UploadNikeName demo) {
                        LogUtils.e("错误日志上传成功");
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        List<ErrorLogEntity> list = new ArrayList<>();
                        JSONObject object = JSON.parseObject(postData);
                        list = JSONObject.parseArray(object.getJSONArray("content").toJSONString(), ErrorLogEntity.class);
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
