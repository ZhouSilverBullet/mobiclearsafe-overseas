package com.mobi.clearsafe.net;

import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.ui.common.global.AppGlobalConfig;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.LogUtils;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OkHttpClientManager {


    private static OkHttpClientManager okHttpClientManager;

    OkHttpClient mOkHttpClient;

    Retrofit retrofit;


    public final static int CONNECT_TIMEOUT = 50000;
    public final static int READ_TIMEOUT = 50000;
    public final static int WRITE_TIMEOUT = 50000;


    private OkHttpClientManager() {

    }


    /**
     * 单例模式
     */
    public static OkHttpClientManager getInstance() {
        if (okHttpClientManager == null) {
            synchronized (OkHttpClientManager.class) {
                if (okHttpClientManager == null) {
                    okHttpClientManager = new OkHttpClientManager();
                }
            }
        }
        return okHttpClientManager;
    }


    private Retrofit getOkHttpClient(String baseUrl) {
        //这里修改baseUrl
//        if (mOkHttpClient != null && retrofit != null) {
//            HttpUrlHelper httpUrlHelper = new HttpUrlHelper(retrofit.baseUrl());
//            if (baseUrl.startsWith("https")) {
//                httpUrlHelper.setSchemeField("https");
//            } else {
//                httpUrlHelper.setSchemeField("http");
//            }
//            httpUrlHelper.setHost(baseUrl);
//            return retrofit;
//        }

        mOkHttpClient = getOkHttpClient();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .callFactory(mOkHttpClient)
                .build();

        return retrofit;
    }

    public OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (OkHttpClientManager.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = initOkHttpClient();
                }
            }

        }
        return mOkHttpClient;
    }

    private OkHttpClient initOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.e(message.toString());
                // Log.e("xxxxx",message.toString());
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .dispatcher(new Dispatcher(AppGlobalConfig.APP_THREAD_POOL_EXECUTOR))
                .addInterceptor(new Interceptor() {//拦截器添加公共请求头
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("X-Access-Token", UserEntity.getInstance().getToken())
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("Device-ID", Const.deviceID)
                                .addHeader("package-name", MyApplication.getContext().getPackageName())
                                .addHeader("Platform-No", "1")//平台号（1-安卓 2-ios）
                                .addHeader("Version", AppUtil.packageName(MyApplication.getContext()))
                                .addHeader("Channel", AppUtil.getChannelName(MyApplication.getContext()))
                                .addHeader("Device-Type", "mobile")
                                .addHeader("Device-Model", AppUtil.getSystemModel())//手机型号
                                .addHeader("Device-Brand", AppUtil.getDeviceBrand())
                                .addHeader("Idfa", "")
                                .addHeader("language", AppUtil.getLocale(MyApplication.getContext()) == null ? "" : AppUtil.getLocale(MyApplication.getContext()).getLanguage())
                                .addHeader("Network-Type", AppUtil.getNetworkTypeName())
                                .addHeader("Network-Operator", AppUtil.getSimOperatorInfo())
                                .addHeader("Mac", AppUtil.getMac())
                                .addHeader("Android-ID", AppUtil.getAndroidID())
                                .addHeader("IMEI", AppUtil.getIMEI())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                // .addInterceptor(new TokenInterceptor())
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())//配置
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())//配置
                .build();

    }

    public HttpApi getApiService(String baseUrl) {
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        Retrofit retrofit = getOkHttpClient(baseUrl);
        return retrofit.create(HttpApi.class);
    }
}
