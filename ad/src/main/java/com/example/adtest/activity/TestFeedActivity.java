package com.example.adtest.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.example.adtest.R;

/**
 * author : liangning
 * date : 2020-01-07  15:59
 */
public class TestFeedActivity extends AppCompatActivity {

    public static void IntoTestFeed(Context context) {
        Intent intent = new Intent(context, TestFeedActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testfeed_layout);
        findViewById(R.id.btn_feed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedNative2FullScreenActivity.IntoFeedActivity(TestFeedActivity.this);
            }
        });

    }
}
