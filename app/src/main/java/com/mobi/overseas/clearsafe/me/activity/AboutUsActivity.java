package com.mobi.overseas.clearsafe.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android.support.annotation.Nullable;

import com.example.adtest.activity.TestFeedActivity;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.LogUtils;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.utils.UiUtils;
import com.mobi.overseas.clearsafe.widget.InterActionDialog;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;

/**
 * author : liangning
 * date : 2019-10-23  19:58
 */
public class AboutUsActivity extends BaseAppCompatActivity {

    public static void IntoAboutUs(Activity activity) {
        Intent intent = new Intent(activity, AboutUsActivity.class);
        activity.startActivity(intent);
    }

    private TextView tv_version, tv_qq;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus_layout);
        ButtonStatistical.aboutPage();
        tv_version = findViewById(R.id.tv_version);
        tv_qq = findViewById(R.id.tv_qq);
        tv_qq.setText(getResources().getString(R.string.qq_group_tips, UserEntity.getInstance().getConfigEntity().getQqqun()));
        tv_version.setText(getResources().getString(R.string.app_name) + AppUtil.packageName(this));
        UiUtils.setTitleBar(this, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getString(R.string.about_us), null);
        tv_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UiUtils.joinQQGroup(getActivity(), UserEntity.getInstance().getConfigEntity().getQqqunkey())) {
                    if (AppUtil.copy(AboutUsActivity.this, UserEntity.getInstance().getConfigEntity().getQqqun())) {
                        ToastUtils.showShort(R.string.qqgroup_copy);
                    }
                }
            }
        });
//        TestFeedActivity.IntoTestFeed(this);
//        InterActionDialog dialog = new InterActionDialog.Builder(this)
//                .setH1fg(true)
//                .setEnum(ScenarioEnum.gold_bubble_native)
//                .build();
//        dialog.show();
//        int num = (int) (100 * Math.random());
//        Log.e("测试随机数","随机数为："+num);
//        Log.e("测试随机数","服务器返回范围："+UserEntity.getInstance().getConfigEntity().getHc1g());
//        if (num < UserEntity.getInstance().getConfigEntity().getHc1g()) {
//            Log.e("测试随机数","随机数在范围内");
//        }else {
//            Log.e("测试随机数","随机数不在范围内");
//        }
    }


}
