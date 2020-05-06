package com.mobi.clearsafe.statistical;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobi.clearsafe.MainActivity;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.me.bean.UploadNikeName;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.statistical.umeng.CommonParm;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.DateUtils;
import com.mobi.clearsafe.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2019/12/9
 * desc: 闹钟
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static List<CommonParm> mList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == MainActivity.INTENT_ALARM_LOG) {
            LogUtils.e("log log log");
            CommonParm parm=new CommonParm();
            parm.setDay( DateUtils.getStringDateDay());
            parm.setTime(DateUtils.getStringDateMin());
            parm.setDeviceid(Const.deviceID);
            parm.setPlatform("android");
            parm.setSdkv(AppUtil.packageName(MyApplication.getContext()));
            parm.setChannel_no(AppUtil.getChannelName(MyApplication.getContext()));
            parm.setEventId("unKill");
            if (mList == null) {
                mList = new ArrayList<>();
            }
            mList.add(parm);
            JSONObject jb = new JSONObject();
            jb.put("content", JSONArray.parseArray(JSON.toJSONString(mList)));
            mList = new ArrayList<>();
            OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                    .updateBehavior(jb)
                    .compose(CommonSchedulers.<BaseResponse<UploadNikeName>>observableIO2Main(MyApplication.getContext()))
                    .subscribe(new BaseObserver<UploadNikeName>() {
                        @Override
                        public void onSuccess(UploadNikeName demo) {
                            LogUtils.e("一小时上报成功");
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg, String code) {

                        }
                    });
        }

    }
}
