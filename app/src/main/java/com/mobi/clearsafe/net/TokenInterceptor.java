package com.mobi.clearsafe.net;

import android.util.Log;

import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author:zhaijinlu
 * date: 2020/3/6
 * desc: token刷新  token过期 401
 */
public class TokenInterceptor implements Interceptor {
    private static final String TAG = "TokenInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        Log.d(TAG, "response.code=" + response.body().string());

        if (isTokenExpired(response)) {
            Log.d(TAG, "自动刷新Token,然后重新请求数据");
            //同步请求方式，获取最新的Token
            String newToken = getNewToken();
            Log.e(TAG, "intercept:新的请求头 "+newToken );
            //使用新的Token，创建新的请求
            Request newRequest = chain.request()
                    .newBuilder()
                    .header("Authorization", newToken)
                    .build();
            //重新请求
            return chain.proceed(newRequest);
        }
        return response;
    }

    //判断Token是否过期
    private boolean isTokenExpired(Response response) {
        if (response.code() == 401) {
            return true;
        }
        return false;
    }

    //用同步方法获取新的Token
    private String getNewToken() throws IOException {
        // 通过获取token的接口，同步请求接口
        String newToken="";
        FormBody formBody = new FormBody.Builder().build();
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .addHeader("X-Access-Token", UserEntity.getInstance().getToken())
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .addHeader("Device-ID", Const.deviceID)
                .addHeader("Platform-No","1")//平台号（1-安卓 2-ios）
                .addHeader("Version", AppUtil.packageName(MyApplication.getContext()))
                .addHeader("Channel", AppUtil.getChannelName(MyApplication.getContext()))
                .addHeader("Device-Type","mobile")
                .addHeader("Device-Model",AppUtil.getSystemModel())//手机型号
                .addHeader("Device-Brand",AppUtil.getDeviceBrand())
                .addHeader("Idfa","")
                .addHeader("language",AppUtil.getLocale(MyApplication.getContext())==null?"":AppUtil.getLocale(MyApplication.getContext()).getLanguage())
                .addHeader("Network-Type",AppUtil.getNetworkTypeName())
                .addHeader("Network-Operator",AppUtil.getSimOperatorInfo())
                .addHeader("Mac",AppUtil.getMac())
                .addHeader("Android-ID",AppUtil.getAndroidID())
                .addHeader("IMEI",AppUtil.getIMEI())
                .url(Const.getBaseUrl()+"/all-walking/v1/user/login")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200) {
                    String loginInfo = response.body().string();
                    Log.e("loginInfo",loginInfo);
//                    if(!TextUtils.isEmpty(loginInfo)){
//                        LoginBean loginBean = new Gson().fromJson(loginInfo, LoginBean.class);
//                    }

                }
            }
        });

        return newToken;
    }
}
