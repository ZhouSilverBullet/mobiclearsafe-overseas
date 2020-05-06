package com.mobi.overseas.clearsafe.ui.cleannotice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mobi.overseas.clearsafe.utils.ToastUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/20 15:45
 * @Dec 略
 */
public class SingleTaskBlankActivity extends AppCompatActivity {
    public static final String TAG = "SingleTaskBlankActivity";

    public static void start(Context context, Intent intent) {
        Intent localIntent = new Intent(context, SingleTaskBlankActivity.class);
        localIntent.putExtra("extraSkipIntent", intent);
        context.startActivity(localIntent);
    }
    private boolean isResume;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Intent extraSkipIntent = getIntent().getParcelableExtra("extraSkipIntent");
            startActivity(extraSkipIntent);
        } catch (Exception e) {
            Log.e(TAG, " --> " + e.getMessage());
        } finally {
            //立马结束容易不显示后面的提示框
//            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //打开其他页面回来的时候结束界面
        if (isResume) {
            finish();
        }
        isResume = true;
    }
}
