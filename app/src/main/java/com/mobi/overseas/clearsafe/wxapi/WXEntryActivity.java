package com.mobi.overseas.clearsafe.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.eventbean.UserInfoEvent;
import com.mobi.overseas.clearsafe.me.bean.UploadNikeName;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.reyun.ReyunUtil;
import com.mobi.overseas.clearsafe.utils.LogUtils;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.wxapi.bean.AccessTokenBean;
import com.mobi.overseas.clearsafe.wxapi.bean.LoginBean;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;
import com.mobi.overseas.clearsafe.wxapi.bean.UserInfo;
import com.mobi.overseas.clearsafe.wxapi.bean.WechatUserInfo;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI iwxapi;
    public static final String  APPID = "wx7dd57a328f8b5684";
    private String secret = "8c1baf357646768f0c38b518d8e2c469";
    private String baseUrl = "https://api.weixin.qq.com/sns/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        iwxapi = WeixinHandler.getInstance().createWXApiInstance();
        iwxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
//        if (baseResp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
//            WXLaunchMiniProgram.Resp lu = (WXLaunchMiniProgram.Resp) baseResp;
//            String extraData = lu.extMsg;//微信返回的步数
//            Log.e("小程序返回", extraData);
//            finish();
//        }
        //登录回调
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (baseResp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
                    WXLaunchMiniProgram.Resp lu = (WXLaunchMiniProgram.Resp) baseResp;
                    String extraData = lu.extMsg;//微信返回的步数
                    Log.e("小程序返回", extraData);
                    finish();
                }else if(baseResp.getType()==ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){
                    ToastUtils.showShort("分享成功");
                    settingShareState();
                    finish();
                } else {
                    String code = ((SendAuth.Resp) baseResp).code;
                    getAccessToken(code);
                    LogUtils.e(code + "");
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                finish();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                finish();
                break;
            default:
                finish();
                break;
        }

    }

    //设置分享状态
    private void settingShareState() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .settingShareState(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<UploadNikeName>>observableIO2Main(this))
                .subscribe(new BaseObserver<UploadNikeName>() {
                    @Override
                    public void onSuccess(UploadNikeName demo) {
                        EventBus.getDefault().post(new UserInfoEvent(new UserInfo()));
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    /**
     * 通过 code 获取 access_token
     *
     * @param code
     */
    private void getAccessToken(String code) {
        Observable<AccessTokenBean> bodyObservable = OkHttpClientManager.getInstance().getApiService(baseUrl)
                .getAccessToken(APPID, secret, code, "authorization_code");
        bodyObservable.compose(CommonSchedulers.<AccessTokenBean>observableIO2Main(this))
                .subscribe(new Observer<AccessTokenBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AccessTokenBean accessTokenBean) {
                        if (accessTokenBean != null) {
                            String access_token = accessTokenBean.getAccess_token();
                            String openid = accessTokenBean.getOpenid();
                            getUserInfo(access_token, openid);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        finish();
                        Log.e("WXEntryActivity", "获取access_token失败");

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    /**
     * 获取用户信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserInfo(String access_token, String openid) {
        Observable<WechatUserInfo> infoObservable = OkHttpClientManager.getInstance().getApiService(baseUrl).getWechatUserInfo(access_token, openid);
        infoObservable.compose(CommonSchedulers.<WechatUserInfo>observableIO2Main(this))
                .subscribe(new Observer<WechatUserInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WechatUserInfo wechatUserInfo) {
                        if (wechatUserInfo != null) {
                            OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                                    .bindWechat(wechatUserInfo.getOpenid(),UserEntity.getInstance().getUserId(),wechatUserInfo.getNickname(),wechatUserInfo.getHeadimgurl(),String.valueOf(wechatUserInfo.getSex()),wechatUserInfo.getUnionid())
                                    .compose(CommonSchedulers.<BaseResponse<LoginBean>>observableIO2Main(WXEntryActivity.this))
                                    .subscribe(new BaseObserver<LoginBean>() {
                                        @Override
                                        public void onSuccess(LoginBean demo) {
                                            if(demo!=null){
                                                //微信登录注册热云登录
                                                ReyunUtil.setLoginSuccessBusiness(Const.deviceID);
                                                if(demo.isIs_new_user()){
                                                    //首次登录注册热云注册
                                                    ReyunUtil.setRegisterWithAccountID(Const.deviceID);
                                                }
                                                UserEntity.getInstance().setToken(demo.getToken());
                                                UserEntity.getInstance().setUserId(demo.getUser_id());
                                                UserEntity.getInstance().setPoints(demo.getTotal_points());
                                                UserEntity.getInstance().setCash(demo.getCash());
                                                getServiceUserInfo(demo.getToken(),demo.getUser_id());
                                                PushAgent mPushAgent = PushAgent.getInstance(WXEntryActivity.this);
                                                mPushAgent.addAlias(demo.getUser_id(), "userID", new UTrack.ICallBack() {
                                                    @Override
                                                    public void onMessage(boolean isSuccess, String message) {
                                                        LogUtils.e(isSuccess+"----"+message);
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onFailure(Throwable e, String errorMsg, String code) {
                                            ToastUtils.showShort(errorMsg);
                                            finish();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 从服务器获取用户信息
     *
     * @param token
     * @param user_id
     */
    private void getServiceUserInfo(String token, String user_id) {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getUserInfo(token, user_id)
                .compose(CommonSchedulers.<BaseResponse<UserInfo>>observableIO2Main(this))
                .subscribe(new BaseObserver<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo demo) {
                        if (demo != null) {
                            UserEntity.getInstance().setUserInfo(demo);
                            ToastUtils.showShort("绑定微信成功");
                            EventBus.getDefault().post(new UserInfoEvent(demo));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                            ToastUtils.showShort(errorMsg);
                            finish();
                    }

                });
    }


}
