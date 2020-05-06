package com.mobi.clearsafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import android.support.annotation.Nullable;

import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.splash.SplashViewAD;
import com.example.adtest.splash.SplashViewADLoadListener;
import com.mobi.clearsafe.base.BaseAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * author : liangning
 * date : 2019-11-30  19:05
 */
public class SplashActivity extends BaseAppCompatActivity {

    public static void IntoSplash(Activity activity) {
        Intent intent = new Intent(activity, SplashActivity.class);
        activity.startActivity(intent);
    }

    private FrameLayout fl_ad;
    private TextView tv_countdown;
    private SplashViewAD sAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);
        fl_ad = findViewById(R.id.fl_ad);
        tv_countdown = findViewById(R.id.tv_countdown);

        new SplashViewAD.Builder(SplashActivity.this)
                .setImageAcceptedSize(1080, 1920)
                .setScenario(ScenarioEnum.splash_ad)
                .setSupportDeepLink(true)
                .setBearingView(fl_ad)
                .setLoadListener(new SplashViewADLoadListener() {
                    @Override
                    public void LoadError(String channel, int errorCode, String errorMsg) {
                        //配置信息获取成功，跳转主页
//                        Log.e("加载失败",errorCode+errorMsg);
                        finish();
                    }

                    @Override
                    public void onTimeout(String channel) {
                        //配置信息获取成功，跳转主页
//                        Log.e("加载失败","超时");
                        finish();
                    }

                    @Override
                    public void onAdClicked(String channel) {

                    }

                    @Override
                    public void onAdShow(String channel) {
                        tv_countdown.setVisibility(View.VISIBLE);
                        tv_countdown.setText(getResources().getString(R.string.countdown_time, String.valueOf(4)));
                        countdown();
                    }

                    @Override
                    public void onSplashAdLoad(String channel) {

                    }
                }).build();
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
//                        Log.e("加载失败","倒计时错误");
                        finish();
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
//                        Log.e("加载失败","倒计时完成");
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
