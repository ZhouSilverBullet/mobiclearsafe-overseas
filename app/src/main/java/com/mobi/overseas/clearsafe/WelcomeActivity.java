package com.mobi.overseas.clearsafe;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adtest.manager.SDKManager;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.splash.SplashViewAD;
import com.example.adtest.splash.SplashViewADLoadListener;
import com.example.adtest.statistical.AdStatistical;
import com.google.gson.Gson;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.main.ConfigBean;
import com.mobi.overseas.clearsafe.main.ConfigEntity;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class WelcomeActivity extends BaseAppCompatActivity {

    private FrameLayout fl_ad;
    //    private RelativeLayout rl_ad;
    private ImageView iv_welcome;
    private TextView tv_countdown;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.activity_welcome_layout);
        ButtonStatistical.startPage();
        initView();
        getConfig();
    }

    private void initView() {
        fl_ad = findViewById(R.id.fl_ad);
//        rl_ad = findViewById(R.id.rl_ad);
//        iv_welcome = findViewById(R.id.iv_welcome);
        tv_countdown = findViewById(R.id.tv_countdown);
    }


    private void getConfig() {
        OkHttpClientManager.getInstance().getApiService(Const.DEBUG ? Const.DEBUGUEL : Const.RELEASEURL)
                .getConfig()
                .compose(CommonSchedulers.<BaseResponse<ConfigEntity>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<ConfigEntity>() {
                    @Override
                    public void onSuccess(ConfigEntity demo) {
                        if (demo != null) {
                            ConfigBean configBean = new Gson().fromJson(demo.getConfig(), ConfigBean.class);
                            UserEntity.getInstance().setConfigEntity(configBean);//配置信息保存本地
                            ButtonStatistical.openApp();
                            AdStatistical.init(getApplicationContext(), configBean.getAdlog_upload_url(), configBean.isIs_upload_log());
                            SDKManager.setChannel(AppUtil.getChannelName(WelcomeActivity.this), MyApplication.getContext());
                            SDKManager.init(MyApplication.getContext(), configBean.getAd_request(),
                                    new SDKManager.InitListener() {
                                        @Override
                                        public void Success() {
                                            //切换到主线程初始化广告SDK
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    SDKManager.InitializeSDK(WelcomeActivity.this);
                                                    if (AppUtil.HWIsShowAd()) {
                                                        new SplashViewAD.Builder(WelcomeActivity.this)
                                                                .setImageAcceptedSize(1080, 1920)
                                                                .setScenario(ScenarioEnum.splash_ad)
                                                                .setSupportDeepLink(true)
                                                                .setBearingView(fl_ad)
                                                                .setLoadListener(new SplashViewADLoadListener() {
                                                                    @Override
                                                                    public void LoadError(String channel, int errorCode, String errorMsg) {
                                                                        //配置信息获取成功，跳转主页
                                                                        toMainActivity();
                                                                    }

                                                                    @Override
                                                                    public void onTimeout(String channel) {
                                                                        //配置信息获取成功，跳转主页
                                                                        toMainActivity();
                                                                    }

                                                                    @Override
                                                                    public void onAdClicked(String channel) {

                                                                    }

                                                                    @Override
                                                                    public void onAdShow(String channel) {
                                                                        tv_countdown.setVisibility(View.VISIBLE);
                                                                        ButtonStatistical.intoSplash();
                                                                        countdown();
                                                                    }

                                                                    @Override
                                                                    public void onSplashAdLoad(String channel) {

                                                                    }
                                                                }).build();
                                                    } else {
                                                        toMainActivity();
                                                    }

                                                }
                                            });


                                        }

                                        @Override
                                        public void Failure(String errorMsg) {
                                            //切换到主线程初始化广告SDK
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    SDKManager.InitializeSDK(WelcomeActivity.this);
                                                    //配置信息获取成功，跳转主页
                                                    toMainActivity();
                                                }
                                            });

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    private void toMainActivity() {
        //配置信息获取成功，跳转主页
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void countdown() {
        Observable.intervalRange(0, 4, 0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return 4 - aLong - 1;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        tv_countdown.setText(getResources().getString(R.string.countdown_time, String.valueOf(aLong)));
                    }

                    @Override
                    public void onError(Throwable e) {
                        toMainActivity();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        toMainActivity();
                    }
                });
    }

}
