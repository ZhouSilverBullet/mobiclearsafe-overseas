package com.mobi.clearsafe.base;

import android.app.Activity;
import android.os.Bundle;

import com.mobi.clearsafe.manager.AppManager;

@Deprecated
public class BaseActivity extends Activity {

    protected AppManager appManager = AppManager.getInstance();
    private String contextString;//类名
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appManager.addActivity(this);
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
    protected Activity getActivity(){
        return this;
    }
}
