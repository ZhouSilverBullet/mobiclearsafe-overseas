package com.mobi.clearsafe.base;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;


import com.mobi.clearsafe.manager.AppManager;
import me.jessyan.autosize.AutoSizeCompat;

public class BaseFragmentActivity extends FragmentActivity {
    protected AppManager appManager = AppManager.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appManager.addActivity(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        appManager.finishActivity(this);
    }

    public Activity getActivity(){
        return this;
    }

    @Override
    public Resources getResources() {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources());
        return super.getResources();
    }
}
