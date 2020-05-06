package com.mobi.clearsafe.ui.common.base;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import com.mobi.clearsafe.base.BaseAppCompatActivity;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/19 19:43
 * @Dec 略
 * 来源一个bug
 * https://juejin.im/entry/58636864128fe10069efc4b5
 */
public abstract class FragmentStateLossActivity extends BaseAppCompatActivity {
    private static final String TAG = "FragmentStateLoss";
    private boolean mStateSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStateSaved = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Not call super won't help us, still get crash
        super.onSaveInstanceState(outState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mStateSaved = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStateSaved = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mStateSaved = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mStateSaved = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!mStateSaved) {
            return super.onKeyDown(keyCode, event);
        } else {
            // State already saved, so ignore the event
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (!mStateSaved) {
            super.onBackPressed();
        }
    }
}

