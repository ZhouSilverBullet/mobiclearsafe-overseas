package com.mobi.overseas.clearsafe.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import android.support.annotation.Nullable;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.manager.AppManager;
import com.mobi.overseas.clearsafe.utils.StatusBarUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.message.PushAgent;

import me.jessyan.autosize.AutoSizeCompat;

public abstract class BaseAppCompatActivity extends RxAppCompatActivity {
    protected AppManager appManager = AppManager.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appManager.addActivity(this);
        PushAgent.getInstance(this).onAppStart();
        StatusBarUtil.setStatusBarMode(this, true, R.color.white);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appManager.finishActivity(this);
    }

    protected Activity getActivity() {
        return this;
    }

    public void goBack(View view){
        onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        AutoSizeCompat.autoConvertDensityOfGlobal(res);
        return res;
    }
}
