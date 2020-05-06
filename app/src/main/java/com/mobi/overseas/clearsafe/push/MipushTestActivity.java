package com.mobi.overseas.clearsafe.push;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.WelcomeActivity;
import com.umeng.message.UmengNotifyClickActivity;

import org.android.agoo.common.AgooConstants;

/**
 * author:zhaijinlu
 * date: 2019/12/24
 * desc:
 */
public class MipushTestActivity extends UmengNotifyClickActivity {
    private static String TAG = "MipushTestActivity";


    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String body = (String) msg.obj;
            try {
                Intent intent = new Intent(MipushTestActivity.this, WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mipush);
    }
    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Log.i(TAG, body);
        Message message = Message.obtain();
        message.obj = body;
        Log.i("TAG",body);
        handler.sendMessage(message);
    }
}
