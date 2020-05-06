package com.mobi.clearsafe.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.nativeexpress.NativeExpressAd;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.eventbean.UserInfoEvent;
import com.mobi.clearsafe.moneyactivity.BindPhoneActivity;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.utils.UiUtils;
import com.mobi.clearsafe.utils.imageloader.ImageLoader;
import com.mobi.clearsafe.widget.RoundImageView;
import com.mobi.clearsafe.wxapi.bean.UserEntity;
import com.mobi.clearsafe.wxapi.bean.UserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UserInfoActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private RelativeLayout editor_nickname;
    private TextView tv_nickname;
    private RoundImageView head_photo;
    private RelativeLayout bind_phone;
    private RelativeLayout bind_weChat;
    private TextView phone_num;
    private TextView weChat_state;
    private UserInfo userInfo;
    private RelativeLayout fl_ad;
    private NativeExpressAd ad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButtonStatistical.bindInfoPage();
        EventBus.getDefault().register(this);

        UiUtils.setTitleBar(this, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getString(R.string.user_info), null);

        initView();
    }

    private void initView() {
        editor_nickname=findViewById(R.id.editor_nickname);
        editor_nickname.setOnClickListener(this);
        tv_nickname=findViewById(R.id.tv_nickname);
        head_photo=findViewById(R.id.head_photo);
        bind_phone=findViewById(R.id.bind_phone);
        bind_weChat=findViewById(R.id.wechat_num);
        phone_num=findViewById(R.id.phone_num);
        weChat_state=findViewById(R.id.wechat_state);
        bind_phone.setOnClickListener(this);
        bind_weChat.setOnClickListener(this);
        fl_ad = findViewById(R.id.fl_ad);


        userInfo = UserEntity.getInstance().getUserInfo();
        if(userInfo !=null){
            tv_nickname.setText(userInfo.getNickname());
            ImageLoader.loadImage(this,head_photo, userInfo.getHead_img_url());
            weChat_state.setText(getResources().getString(R.string.bind));
            if(!TextUtils.isEmpty(userInfo.getPhone())){
                phone_num.setText(userInfo.getPhone());
            }
        }
        if(AppUtil.HWIsShowAd()){
            ad = new  NativeExpressAd.Builder(UserInfoActivity.this)
                    .setAdCount(1)
                    .setADViewSize(350,0)
                    .setHeightAuto(true)
                    .setBearingView(fl_ad)
                    .setScenario(ScenarioEnum.userinfo_native)
                    .setSupportDeepLink(true)
                    .build();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editor_nickname:
                Intent intent = NickNameAcitivity.IntoSettings(this);
                intent.putExtra("nick_name",tv_nickname.getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.bind_phone:
                if(userInfo!=null){
                    if(!TextUtils.isEmpty(userInfo.getPhone())){
                        ToastUtils.showShort(getResources().getString(R.string.bind));
                        return;
                    }
                }
                startActivity(new Intent(this, BindPhoneActivity.class));
                break;
            case R.id.wechat_num:
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(UserInfoEvent info) {
        if(info!=null){
            UserInfo userInfo = info.getUserInfo();
            tv_nickname.setText(userInfo.getNickname());
            if(!TextUtils.isEmpty(userInfo.getPhone())){
                phone_num.setText(userInfo.getPhone());
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (ad!=null){
            ad.destory();
        }
    }
}
