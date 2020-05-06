package com.mobi.clearsafe.push;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.oppo.OppoRegister;
import org.android.agoo.vivo.VivoRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

/**
 * author:zhaijinlu
 * date: 2019/12/24
 * desc: 友盟推送
 */
public class UmPush {

    public static String TAG="UmPush";

    public static void init(Context mContext){
       // UMConfigure.init(mContext, "5dc23c8f0cafb2f915000345", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "c58d0f094697b12eea329c79d9dcb7da");
        PushAgent mPushAgent = PushAgent.getInstance(mContext);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i(TAG,"注册成功：deviceToken：-------->  " + deviceToken);
            }
            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG,"注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });
        mPushAgent.setDisplayNotificationNumber(10);
        HuaWeiRegister.register((Application) mContext);
        MiPushRegistar.register( mContext ,"2882303761518222465",  "5131822281465");
        OppoRegister.register(mContext, "68317acdc65a43198abaa06c0556a12a", "f4e5bb2e694e43e7ae2e1a745cf8d7e8");
        VivoRegister.register(mContext);
    }
}
