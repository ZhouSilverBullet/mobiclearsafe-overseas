package com.mobi.overseas.clearsafe.me.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.annotation.Nullable;

import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.nativeexpress.NativeExpressAd;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.WeChatLoginActivity;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.eventbean.UserInfoEvent;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.utils.UiUtils;
import com.mobi.overseas.clearsafe.widget.CommonDialog;
import com.mobi.overseas.clearsafe.widget.LoadWebViewActivity;
import com.mobi.overseas.clearsafe.widget.LoadingDialog;
import com.mobi.overseas.clearsafe.wxapi.bean.LoginBean;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;
import com.mobi.overseas.clearsafe.wxapi.bean.UserInfo;

import org.greenrobot.eventbus.EventBus;

/**
 * author : liangning
 * date : 2019-10-18  16:25
 */
public class SettingsActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static void IntoSettings(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    private RelativeLayout rl_service, rl_privacy, rl_about;
    private TextView exit_login,tv_qq;
    private RelativeLayout  pact, privacy_policy,editor_info;
    private RelativeLayout fl_ad;
    private NativeExpressAd ad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_layout);
        ButtonStatistical.settingPage();
        editor_info=findViewById(R.id.editor_info);
        editor_info.setOnClickListener(this);
        pact = findViewById(R.id.pact);
        privacy_policy = findViewById(R.id.privacy_policy);
        rl_about = findViewById(R.id.rl_about);
        exit_login=findViewById(R.id.exit_login);
        exit_login.setOnClickListener(this);
        pact.setOnClickListener(this);
        privacy_policy.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        rl_service = findViewById(R.id.rl_service);
        rl_service.setOnClickListener(this);
        tv_qq = findViewById(R.id.tv_qq);
        tv_qq.setText(getResources().getString(R.string.add_qq_group,UserEntity.getInstance().getConfigEntity().getQqqun()));
        UiUtils.setTitleBar(this, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getString(R.string.settings), null);
        UserInfo userInfo = UserEntity.getInstance().getUserInfo();
        if(userInfo==null){
            exit_login.setTextColor(getResources().getColor(R.color.gray_d7d7));
        }
        fl_ad = findViewById(R.id.fl_ad);
        if(AppUtil.HWIsShowAd()){
            Log.e("rrr","走");
            ad = new NativeExpressAd.Builder(SettingsActivity.this)
                    .setSupportDeepLink(true)
                    .setHeightAuto(true)
                    .setADViewSize(375,0)
                    .setAdCount(1)
                    .setScenario(ScenarioEnum.setting_page_native)
                    .setBearingView(fl_ad)
                    .build();
        }

//        showDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ad!=null){
            ad.destory();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_service:
                if (!UiUtils.joinQQGroup(getActivity(), UserEntity.getInstance().getConfigEntity().getQqqunkey())) {
                    if (AppUtil.copy(getActivity(), UserEntity.getInstance().getConfigEntity().getQqqun())) {
                        ToastUtils.showShort(R.string.qqgroup_copy);
                    }
                }
                break;
            case R.id.pact:
                LoadWebViewActivity.IntoServiceAgreement(getActivity(),UserEntity.getInstance().getConfigEntity().getPact());
                break;
            case R.id.privacy_policy:
                LoadWebViewActivity.IntoPrivacyAgreement(getActivity(),UserEntity.getInstance().getConfigEntity().getPrivacy_policy());
                break;
            case R.id.rl_about:
                AboutUsActivity.IntoAboutUs(SettingsActivity.this);
                break;
            case R.id.exit_login:
                if(UserEntity.getInstance().getUserInfo()==null){
                    return;
                }
                CommonDialog dialog=new CommonDialog.Builder(this)
                        .setCancelText(getResources().getString(R.string.cancel))
                        .setConfirmText(getResources().getString(R.string.confirm))
                        .setContent(getResources().getString(R.string.exit_content))
                        .setButtonClick(new CommonDialog.onButtonClick() {
                            @Override
                            public void onConfirmClick(Dialog dialog) {

                                login();

                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelClick(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build();
                dialog.show();
                break;
            case R.id.editor_info:
                if (UserEntity.getInstance().getUserInfo() == null) {
                    WeChatLoginActivity.IntoSettings(getActivity());
                    return;
                }
                ButtonStatistical.editorInfoBtn();
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
        }
    }

    private void login() {
        Log.e("重新登录","退出登录");
        //清空用户信息，同时请求游客登录接口
        final Dialog dialog = LoadingDialog.showDialogForLoading(getActivity(),"退出登录...",false);
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .login()
                .compose(CommonSchedulers.<BaseResponse<LoginBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<LoginBean>() {
                    @Override
                    public void onSuccess(LoginBean demo) {
                        if (demo != null) {
                            UserEntity.getInstance().clearInfo();
                            //保存token user_id
                            UserEntity.getInstance().setToken(demo.getToken());
                            UserEntity.getInstance().setUserId(demo.getUser_id());
                            UserEntity.getInstance().setNickname(demo.getNickname());
                            EventBus.getDefault().post(new UserInfoEvent(null));

                            exit_login.setTextColor(getResources().getColor(R.color.gray_d7d7));
                            dialog.dismiss();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        dialog.dismiss();
                        ToastUtils.showShort("退出登录失败，请稍后再试!");
                    }
                });
    }

    private void showDialog(){
//        StepCeilingDialog dialog = new StepCeilingDialog.Builder(SettingsActivity.this)
//                .setContent("兑换步数上线实打实的阿斯顿阿斯顿阿斯顿阿斯顿")
//                .setButtonText("更多赚钱任务")
//                .setAllGold(121)
//                .setMoney(0.45f)
//                .build();
//        dialog.show();

//        SignDialog dialog = new SignDialog.Builder(SettingsActivity.this)
//                .isVisible(true)
//                .setAllGold(12)
//                .setGold(1212)
//                .setMoney(1f)
//                .setSignDay(12)
//                .build();
//        dialog.show();
//        GetGoldDialog dialog = new GetGoldDialog.Builder(SettingsActivity.this)
//                .setMoney(1212)
//                .setGold(12)
//                .setBottomBtnVisible(true)
//                .setAllGold(12)
//                .isVisible(true)
//                .build();
//        dialog.show();
    }
}
