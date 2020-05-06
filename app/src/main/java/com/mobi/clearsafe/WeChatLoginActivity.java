package com.mobi.clearsafe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.utils.UiUtils;
import com.mobi.clearsafe.widget.LoadWebViewActivity;
import com.mobi.clearsafe.wxapi.WeixinHandler;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

public class WeChatLoginActivity extends BaseAppCompatActivity implements View.OnClickListener {


    private LinearLayout btn_login;
    private ImageView isRead;
    private TextView service_agreement;
    private TextView privacy_policy;
    private RelativeLayout layout_check;
    boolean isCheck=false;
    private LinearLayout ll_back;

    public static void IntoSettings(Context activity) {
        Intent intent = new Intent(activity, WeChatLoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat_login);
        ButtonStatistical.bindWechatPage();
        UiUtils.setTitleBar(this,null,null,null,null);

        initView();
    }

    private void initView() {
        ll_back=findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        btn_login=findViewById(R.id.btn_login);
        layout_check=findViewById(R.id.layout_check);
        isRead=findViewById(R.id.ck_ischeck);
        service_agreement=findViewById(R.id.tv_service_agreement);
        privacy_policy=findViewById(R.id.tv_privacy_policy);
        btn_login.setOnClickListener(this);
        service_agreement.setOnClickListener(this);
        privacy_policy.setOnClickListener(this);
        layout_check.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                ButtonStatistical.wechatLoginBtn();
                if(isCheck){
                    if (!WeixinHandler.getInstance().isWeixinInstalled()) {
                        ToastUtils.showShort(R.string.wechat_login_tip);
                        return;
                    }
                    WeixinHandler.getInstance().loginWeChat();
                    finish();
                }else {
                    Toast.makeText(this,getResources().getString(R.string.wechat_login_tips),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_service_agreement:
                LoadWebViewActivity.IntoServiceAgreement(getActivity(), UserEntity.getInstance().getConfigEntity().getPact());
                break;
            case R.id.tv_privacy_policy:
                LoadWebViewActivity.IntoPrivacyAgreement(getActivity(),UserEntity.getInstance().getConfigEntity().getPrivacy_policy());
                break;
            case R.id.layout_check:
                if(isCheck==false){
                    isCheck=true;
                    isRead.setBackgroundResource(R.mipmap.check_check);
                }else if(isCheck) {
                    isCheck=false;
                    isRead.setBackgroundResource(R.mipmap.check_uncheck);
                }
                break;
            case R.id.ll_back:
                finish();
                break;

        }
    }
}
