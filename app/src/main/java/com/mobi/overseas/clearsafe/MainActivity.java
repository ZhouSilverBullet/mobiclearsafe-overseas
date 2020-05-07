package com.mobi.overseas.clearsafe;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.adtest.cache.NativeCacheGroup;
import com.example.adtest.cache.NativeExpressCache;
import com.example.adtest.manager.SDKManager;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.example.adtest.rewardvideo.RewardVideoLoadListener;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.download.MyDownLoadManager;
import com.mobi.overseas.clearsafe.eventbean.CheckTabEvent;
import com.mobi.overseas.clearsafe.eventbean.PleasanEvent;
import com.mobi.overseas.clearsafe.eventbean.RequestStep;
import com.mobi.overseas.clearsafe.eventbean.ScrathTabEvent;
import com.mobi.overseas.clearsafe.eventbean.StepEvent;
import com.mobi.overseas.clearsafe.eventbean.TabTipEvent;
import com.mobi.overseas.clearsafe.eventbean.ViewpagerEvent;
import com.mobi.overseas.clearsafe.fragment.AggregationFragment;
import com.mobi.overseas.clearsafe.fragment.activityFragment;
import com.mobi.overseas.clearsafe.fragment.mainFragment;
import com.mobi.overseas.clearsafe.fragment.meFragment;
import com.mobi.overseas.clearsafe.main.PleasantlyBean;
import com.mobi.overseas.clearsafe.main.PleasantlyReceiveBean;
import com.mobi.overseas.clearsafe.main.UpdateBean;
import com.mobi.overseas.clearsafe.main.bean.ExitDialogBean;
import com.mobi.overseas.clearsafe.main.bean.RedBean;
import com.mobi.overseas.clearsafe.main.fragment.HomeFragment;
import com.mobi.overseas.clearsafe.main.fragment.ToolBoxFragment;
import com.mobi.overseas.clearsafe.me.bean.UploadNikeName;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.service.TodayStepManager;
import com.mobi.overseas.clearsafe.statistical.AlarmReceiver;
import com.mobi.overseas.clearsafe.statistical.reyun.ReyunUtil;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.ui.common.base.FragmentStateLossActivity;
import com.mobi.overseas.clearsafe.utils.AppInfo;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.GetDeviceId;
import com.mobi.overseas.clearsafe.utils.LogUtils;
import com.mobi.overseas.clearsafe.utils.SPUtil;
import com.mobi.overseas.clearsafe.widget.BadgeView;
import com.mobi.overseas.clearsafe.widget.ExitDialog;
import com.mobi.overseas.clearsafe.widget.GoldDialog;
import com.mobi.overseas.clearsafe.widget.NoScrollViewPager;
import com.mobi.overseas.clearsafe.widget.PermissionDialog;
import com.mobi.overseas.clearsafe.widget.PrivacyDialog;
import com.mobi.overseas.clearsafe.widget.RedPacketsDiaglog;
import com.mobi.overseas.clearsafe.widget.SurpriseNewDialog;
import com.mobi.overseas.clearsafe.widget.VersionUpdateDialog;
import com.mobi.overseas.clearsafe.wxapi.bean.LoginBean;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;
import com.reyun.tracking.sdk.Tracking;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends FragmentStateLossActivity {
    private NoScrollViewPager mViewpager;
    private RadioGroup mTabRadioGroup;
    private List<Fragment> mFragmentList;
    private MyFragmentPageAdapter mAdapter;
    private RadioButton tabbar_main, tabbar_activity, tabbar_me;
    private boolean isActive = true;
    private Button btn_msg;

    public static final String INTENT_ALARM_LOG = "intent_alarm_log";

    MsgReceiver msgReceiver;
    AlarmManager am;
    private BadgeView badge;
    private long waitTime = 2000;
    private long touchTime = 0;
    private int mMemoryValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.main_activity);
        EventBus.getDefault().register(this);
        MyDownLoadManager.registerBroadCast(this);
        ButtonStatistical.mainControllerCount();
        ButtonStatistical.intoMain();
        initView();
        boolean isShow = (boolean) SPUtil.getParam(this, "isShow", false);
        if (!isShow) {
            showDialog();
        } else {
            getPermissions();
        }

        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.communication.RECEIVER");
        registerReceiver(msgReceiver, intentFilter);
        TodayStepManager.init(this);

        MyApplication.getApplication().setRun(true);
        // initAlerManager();
    }


    //半小时上报服务器判断app是否被杀死
    private void initAlerManager() {
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_ALARM_LOG);
        registerReceiver(alarmReceiver, filter);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Intent intent = new Intent(INTENT_ALARM_LOG);
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pi);

    }


    //新用户隐私弹框
    private void showDialog() {
        PrivacyDialog dialog = new PrivacyDialog.Builder(this)
                .setButtonClick(new PrivacyDialog.onButtonClick() {
                    @Override
                    public void onConfirmClick(Dialog dialog) {
                        SPUtil.setParam(MainActivity.this, "isShow", true);
                        getPermissions();
                        dialog.dismiss();
                    }
                })
                .build();
        dialog.show();
    }

    private void getInstallList() {
        List<AppInfo> appList = AppUtil.getAppList(this);
        JSONObject jb = new JSONObject();
        jb.put("content", JSONArray.parseArray(JSON.toJSONString(appList)));
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .updateInstallList(jb)
                .compose(CommonSchedulers.<BaseResponse<UploadNikeName>>observableIO2Main(this))
                .subscribe(new BaseObserver<UploadNikeName>() {
                    @Override
                    public void onSuccess(UploadNikeName demo) {
                        LogUtils.e("安装列表上传成功");
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        LogUtils.e("安装列表上传失败");
                    }
                });
    }

    //版本更新
    private void checkVersion() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .checkVersioin()
                .compose(CommonSchedulers.<BaseResponse<UpdateBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<UpdateBean>() {
                    @Override
                    public void onSuccess(UpdateBean demo) {
                        if (demo.isNeed_update()) {
                            VersionUpdateDialog dialog = new VersionUpdateDialog.Builder(MainActivity.this)
                                    .setVersionName(demo.getVersion())
                                    .setDownLoadUrl(demo.getDownload_url())
                                    .setVersionContent(demo.getContent())
                                    .setIsForce(demo.isMust_update())
                                    .build();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        LogUtils.e(errorMsg);
                    }
                });
    }


    /**
     * 动态申请权限
     */
    private void getPermissions() {
        XXPermissions.with(this)
                .permission(Permission.ACCESS_COARSE_LOCATION,
                        Permission.ACCESS_FINE_LOCATION,
                        Permission.READ_EXTERNAL_STORAGE,
                        Permission.READ_PHONE_STATE,
                        Permission.WRITE_EXTERNAL_STORAGE
                )
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        //已经取得的权限
                        Log.e("已经取得的权限", granted.toString());
                        if (isAll) {
                            ReyunUtil.initSDK((Application) MyApplication.getContext());
                            getDeviceID();

                            //获取权限后扫描本地文件
                            for (Fragment fragment : mFragmentList) {
                                if (fragment instanceof HomeFragment) {
                                    ((HomeFragment) fragment).findCache();
                                    //再请求一下网络
                                    ((HomeFragment) fragment).getCleanList();
                                }
                            }
                            try {
                                NativeExpressCache.loadGroupNative(NativeCacheGroup.MainNative, getActivity());
//                                SinceRenderCache.loadGroupSinceRender(new String[]{},getActivity());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, final boolean quick) {
                        //拒绝的权限
                        //判断是第一次进入APP 并且拒绝了权限，则弹出提示授权弹窗
                        Log.e("拒绝的权限", denied.toString());
                        if (denied.size() > 0) {
                             ReyunUtil.initSDK((Application) MyApplication.getContext());
                            PermissionDialog dialog = new PermissionDialog.Builder(MainActivity.this)
                                    .setOnButtonClick(new PermissionDialog.onButtonClick() {
                                        @Override
                                        public void onConfirmClick(Dialog dialog) {
                                            dialog.dismiss();
                                            if (quick) {//有永久拒绝权限跳到设置
                                                XXPermissions.gotoPermissionSettings(MainActivity.this);
                                            } else {
                                                getPermissions();
                                            }

                                        }
                                    })
                                    .build();
                            dialog.show();
                        }
                    }
                });

    }

    private void getDeviceID() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取保存在sd中的 设备唯一标识符
                    String readDeviceID = GetDeviceId.readDeviceID(MainActivity.this);
                    Log.e("设备标识", "readDeviceID" + readDeviceID);
                    //获取缓存在  sharepreference 里面的 设备唯一标识
                    String string = (String) SPUtil.getParam(MainActivity.this, "device_id", "");
                    Log.e("设备标识", "string" + readDeviceID);
                    if (!TextUtils.isEmpty(string)) {
                        Const.deviceID = string;
                    } else if (!TextUtils.isEmpty(readDeviceID)) {
                        Const.deviceID = readDeviceID;
                    }
                    //判断 app 内部是否已经缓存,  若已经缓存则使用app 缓存的 设备id
                    if (string != null) {
                        //app 缓存的和SD卡中保存的不相同 以app 保存的为准, 同时更新SD卡中保存的 唯一标识符
                        if (TextUtils.isEmpty(readDeviceID) && !string.equals(readDeviceID)) {
                            // 取有效地 app缓存 进行更新操作
                            if (TextUtils.isEmpty(readDeviceID) && !TextUtils.isEmpty(string)) {
                                readDeviceID = string;
                                GetDeviceId.saveDeviceID(readDeviceID, MainActivity.this);
                            }
                        }
                    }
                    // app 没有缓存 (这种情况只会发生在第一次启动的时候)
                    if (TextUtils.isEmpty(readDeviceID)) {
                        //保存设备id
                        readDeviceID = GetDeviceId.getDeviceId(MainActivity.this);
                        Const.deviceID = readDeviceID;
                    }
                    //左后再次更新app 的缓存
                    GetDeviceId.saveDeviceID(readDeviceID, MainActivity.this);
                    SPUtil.setParam(MainActivity.this, "device_id", readDeviceID);
                    Log.e("最终设备标识", "string" + readDeviceID);
                    PushAgent mPushAgent = PushAgent.getInstance(MainActivity.this);
                    mPushAgent.addAlias(Const.deviceID, "deviceId", new UTrack.ICallBack() {
                        @Override
                        public void onMessage(boolean isSuccess, String message) {
                            LogUtils.e(isSuccess + "----" + message);
                        }
                    });
                    if (TextUtils.isEmpty(UserEntity.getInstance().getToken())) {
                        login();//获取权限后登陆
                    } else {
                        getSurprised();
                        getRed();
                        getExitDialogInfo();
                    }
                    SDKManager.setDeviceId(Const.deviceID, MyApplication.getContext());
                    checkVersion();
                    if (UserEntity.getInstance().getConfigEntity().isIs_Install_list()) {
                        getInstallList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //获取退出app时弹框信息
    private void getExitDialogInfo() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getExitDialogInfo(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<ExitDialogBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<ExitDialogBean>() {
                    @Override
                    public void onSuccess(ExitDialogBean demo) {
                        // if (demo != null) {
                        Const.exitBean = demo;
                        // }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    //请求红包
    private void getRed() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getRedPack(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<RedBean>>observableIO2Main(MainActivity.this))
                .subscribe(new BaseObserver<RedBean>() {
                    @Override
                    public void onSuccess(RedBean demo) {
                        if (demo != null) {
                            ButtonStatistical.showRedpacket();
                            if (demo.isIs_red_envelope()) {
                                if (demo.getType() == 3) {//激励红包自动发放
                                    UserEntity.getInstance().setCash(demo.getToday_reward().getCash());
                                    UserEntity.getInstance().setPoints(demo.getToday_reward().getTotal_points());
                                }
                                RedPacketsDiaglog diaglog = new RedPacketsDiaglog.Builder(MainActivity.this)
                                        .setCash(demo)
                                        .build();
                                diaglog.show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    /**
     * 获取惊喜奖励弹窗在哪个tab显示
     */
    private void getSurprised() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .pleasantlyPage(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, Const.ACTIVITY_PLEASANTLY_id)
                .compose(CommonSchedulers.<BaseResponse<PleasantlyBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<PleasantlyBean>() {
                    @Override
                    public void onSuccess(PleasantlyBean demo) {
                        if (demo != null) {
                            LogUtils.e("惊喜奖励弹窗请求成");
                            Const.pBean = demo;
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });

    }

    private void login() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .login()
                .compose(CommonSchedulers.<BaseResponse<LoginBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<LoginBean>() {
                    @Override
                    public void onSuccess(LoginBean demo) {
                        if (demo != null) {
                            //保存token user_id
                            UserEntity.getInstance().setToken(demo.getToken());
                            UserEntity.getInstance().setUserId(demo.getUser_id());
                            UserEntity.getInstance().setNickname(demo.getNickname());
                            UserEntity.getInstance().setPoints(demo.getTotal_points());
                            UserEntity.getInstance().setCash(demo.getCash());
                            ButtonStatistical.loginSuccess();
                            //别名增加，将某一类型的别名ID绑定至某设备，老的绑定设备信息还在，别名ID和device_token是一对多的映射关系
                            PushAgent mPushAgent = PushAgent.getInstance(MainActivity.this);
                            mPushAgent.addAlias(demo.getUser_id(), "userID", new UTrack.ICallBack() {
                                @Override
                                public void onMessage(boolean isSuccess, String message) {
                                    LogUtils.e(isSuccess + "----" + message);
                                }
                            });
                            EventBus.getDefault().post(new RequestStep());
                            if (demo.isIs_red_envelope()) {
//                                RedPacketsDiaglog diaglog = new RedPacketsDiaglog.Builder(MainActivity.this)
//                                        .setCash(demo.getReward_cash())
//                                        .build();
//                                diaglog.show();
                            }
                            getSurprised();
                            getRed();
                            getExitDialogInfo();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });

    }

    public void setMemoryValue(int memoryValue) {
        mMemoryValue = memoryValue;
    }

    public int getMemoryValue() {
        return mMemoryValue;
    }

    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            //拿到进度，更新UI
            final int step = intent.getIntExtra("step", 0);
            EventBus.getDefault().post(new StepEvent(step));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mViewpager.removeOnPageChangeListener(mChangeListener);
        unregisterReceiver(msgReceiver);
        //退出热云SDK
        Tracking.exitSdk();
        //通过BaiduXAdSDKContext.exit()来告知AdSDK，以便AdSDK能够释放资源.
//        com.baidu.mobads.production.BaiduXAdSDKContext.exit();
        MyDownLoadManager.unRegisterBroadCast(this);
    }

    //接收更多任务，显示任务页
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(ViewpagerEvent event) {
        mViewpager.setCurrentItem(2);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(CheckTabEvent event) {
        if (event != null) {
            if (!TextUtils.isEmpty(event.getTab_name())) {
                switch (event.getTab_name()) {
                    case "main":
                        mViewpager.setCurrentItem(0);
                        break;

                    case "toolbox":
                        mViewpager.setCurrentItem(1);
                        break;

                    case "activity":
                        mViewpager.setCurrentItem(2);
                        break;


//                    case "mission":
//                        mViewpager.setCurrentItem(3);
//                        break;
//                    case "sport":
//                        mViewpager.setCurrentItem(3);
//                        break;
                    case "me":
                        mViewpager.setCurrentItem(3);
                        break;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(PleasanEvent event) {
        Log.e("惊喜回调", "回调");
        if (event != null) {
            PleasantlyReceiveBean bean = Const.pBean;
            if (bean.pop_type == 1000 || bean.pop_type == 1001 || bean.pop_type == 1005) {
                receivePleasan();
            } else {
                checkDialog(bean);
            }
            Const.pBean = null;
        }
    }

    //tab提示
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(TabTipEvent event) {
        if (event != null) {
            if (event.getPoins() > 0) {
                if (badge == null) {
                    badge = new BadgeView(MainActivity.this, btn_msg);
                    badge.setText(event.getPoins() + "");
                    badge.setTextSize(10);
                    badge.setGravity(Gravity.CENTER);
                    // 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
                    badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                    // 文本颜色
                    badge.setTextColor(Color.WHITE);
                    // 提醒信息的背景颜色，自己设置
                    // badge1.setBadgeBackgroundColor(Color.RED);
                    badge.setBackgroundResource(R.mipmap.msg_icon);
                    badge.show();
                }
            }
        }
    }

    //隐藏Tab
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(ScrathTabEvent sc) {
        if (sc != null) {
            if (sc.getS().equals("1")) {//隐藏Tab
                mTabRadioGroup.setVisibility(View.INVISIBLE);
                if (badge != null) {
                    badge.hide();
                }
            } else {//显示Tab
                mTabRadioGroup.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 领取惊喜奖励
     */
    private void receivePleasan() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .receivePleasant(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, Const.ACTIVITY_PLEASANTLY_id)
                .compose(CommonSchedulers.<BaseResponse<PleasantlyReceiveBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<PleasantlyReceiveBean>() {
                    @Override
                    public void onSuccess(PleasantlyReceiveBean demo) {
                        if (demo != null) {
                            checkDialog(demo);
                            UserEntity.getInstance().setPoints(demo.total_points);
                            UserEntity.getInstance().setCash(demo.cash);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    /**
     * 领取惊喜奖励 翻倍
     */
    private void receiveDoublePleasan() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .receiveDoublePleasant(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, Const.ACTIVITY_PLEASANTLY_id)
                .compose(CommonSchedulers.<BaseResponse<PleasantlyReceiveBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<PleasantlyReceiveBean>() {
                    @Override
                    public void onSuccess(PleasantlyReceiveBean demo) {
                        if (demo != null) {
                            checkDialog(demo);
                            UserEntity.getInstance().setPoints(demo.total_points);
                            UserEntity.getInstance().setCash(demo.cash);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    private void checkDialog(PleasantlyReceiveBean demo) {
        switch (demo.pop_type) {
            case 1000:
                //惊喜奖励弹窗
                if (demo.is_ad) {
                    GoldDialog dialog = new GoldDialog.Builder(getActivity())
                            .setGold(demo.points)
                            .setScenario(ScenarioEnum.low_plaque)
                            .build();
                    dialog.show();
                } else {
                    SurpriseNewDialog dialog = new SurpriseNewDialog.Builder(getActivity())
                            .isVisible(false)
                            .setGold(demo.points)
                            .setIsShowAd(false)
                            .setScenario(ScenarioEnum.pleasantly_native)
                            .setDialogClick(new SurpriseNewDialog.SurpriseDialogClick() {
                                @Override
                                public void doubleBtnClick(Dialog dialog) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void closeBtnClick(Dialog dialog) {
                                    dialog.dismiss();
                                }

                            }).build();
                    dialog.show();
                }
                break;
            case 1001:
                SurpriseNewDialog dialog1 = new SurpriseNewDialog.Builder(getActivity())
                        .isVisible(true)
                        .setBtnText(demo.pop_up_message)
                        .setGold(demo.points)
                        .setIsShowAd(false)
                        .setScenario(ScenarioEnum.pleasantly_native)
                        .setDialogClick(new SurpriseNewDialog.SurpriseDialogClick() {
                            @Override
                            public void doubleBtnClick(Dialog dialog) {
                                //金币翻倍跳转广告
                                new RewardVideoAd.Builder(getActivity())
                                        .setSupportDeepLink(true)
                                        .setScenario(ScenarioEnum.pleasantly_double_video)
                                        .setRewardVideoLoadListener(new RewardVideoLoadListener() {
                                            @Override
                                            public void onAdClick(String channel) {

                                            }

                                            @Override
                                            public void onVideoComplete(String channel) {

                                            }

                                            @Override
                                            public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                                            }

                                            @Override
                                            public void onAdClose(String channel) {
                                                receiveDoublePleasan();
                                            }

                                            @Override
                                            public void onAdShow(String channel) {

                                            }
                                        }).build();
                                dialog.dismiss();
                            }

                            @Override
                            public void closeBtnClick(Dialog dialog) {
                                dialog.dismiss();
                            }

                        }).build();
                dialog1.show();
                break;
            case 1002:
            case 1003:
                SurpriseNewDialog newDialog = new SurpriseNewDialog.Builder(getActivity())
                        .isVisible(true)
                        .setBtnText(demo.pop_up_message)
                        .setGold(demo.points)
                        .setIsShowAd(false)
                        .setScenario(ScenarioEnum.pleasantly_native)
                        .setDialogClick(new SurpriseNewDialog.SurpriseDialogClick() {
                            @Override
                            public void doubleBtnClick(Dialog dialog) {
                                new RewardVideoAd.Builder(getActivity())
                                        .setSupportDeepLink(true)
                                        .setScenario(ScenarioEnum.pleasantly_double_video)
                                        .setRewardVideoLoadListener(new RewardVideoLoadListener() {
                                            @Override
                                            public void onAdClick(String channel) {

                                            }

                                            @Override
                                            public void onVideoComplete(String channel) {

                                            }

                                            @Override
                                            public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                                            }

                                            @Override
                                            public void onAdClose(String channel) {
                                                receivePleasan();
                                            }

                                            @Override
                                            public void onAdShow(String channel) {

                                            }
                                        }).build();
                                dialog.dismiss();
                            }

                            @Override
                            public void closeBtnClick(Dialog dialog) {
                                dialog.dismiss();
                            }

                        }).build();
                newDialog.show();
                break;
            case 1005:
                SurpriseNewDialog dialog2 = new SurpriseNewDialog.Builder(getActivity())
                        .isVisible(true)
                        .setlableIsVisible(true)
                        .setIsShowAd(false)
                        .setBtnText(demo.pop_up_message)
                        .setGold(demo.points)
                        .setScenario(ScenarioEnum.pleasantly_native)
                        .setDialogClick(new SurpriseNewDialog.SurpriseDialogClick() {
                            @Override
                            public void doubleBtnClick(Dialog dialog) {
                                dialog.dismiss();
                                new RewardVideoAd.Builder(getActivity())
                                        .setSupportDeepLink(true)
                                        .setScenario(ScenarioEnum.pleasantly_double_video)
                                        .setRewardVideoLoadListener(new RewardVideoLoadListener() {
                                            @Override
                                            public void onAdClick(String channel) {

                                            }

                                            @Override
                                            public void onVideoComplete(String channel) {

                                            }

                                            @Override
                                            public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                                            }

                                            @Override
                                            public void onAdClose(String channel) {
                                                receiveDoublePleasan();
                                            }

                                            @Override
                                            public void onAdShow(String channel) {

                                            }
                                        }).build();
                            }

                            @Override
                            public void closeBtnClick(Dialog dialog) {
                                dialog.dismiss();
                            }

                        }).build();
                dialog2.show();
                break;
            case 1006:
                if (demo.is_ad) {
                    GoldDialog dialog = new GoldDialog.Builder(getActivity())
                            .setGold(demo.points)
                            .setScenario(ScenarioEnum.low_plaque)
                            .build();
                    dialog.show();
                } else {
                    SurpriseNewDialog dialog3 = new SurpriseNewDialog.Builder(getActivity())
                            .isVisible(false)
                            .setFanbeiTitle(true)
                            .setIsShowAd(false)
                            .setGold(demo.points)
                            .setScenario(ScenarioEnum.pleasantly_native)
                            .setDialogClick(new SurpriseNewDialog.SurpriseDialogClick() {
                                @Override
                                public void doubleBtnClick(Dialog dialog) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void closeBtnClick(Dialog dialog) {
                                    dialog.dismiss();
                                }

                            }).build();
                    dialog3.show();
                }
                break;
        }
    }

    private void initView() {
        tabbar_main = findViewById(R.id.tabbar_main);
        tabbar_activity = findViewById(R.id.tabbar_activity);
        tabbar_me = findViewById(R.id.tabbar_me);
        StateListDrawable listDrawable = new StateListDrawable();
        tabbar_main.setButtonDrawable(listDrawable);
        tabbar_activity.setButtonDrawable(listDrawable);
        tabbar_me.setButtonDrawable(listDrawable);
        mViewpager = findViewById(R.id.vp);
        mTabRadioGroup = findViewById(R.id.tabs_rg);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new ToolBoxFragment());
//        mFragmentList.add(new activityFragment());
//       mFragmentList.add(new AggregationFragment());
        // mFragmentList.add(new PlayFragment());
//        mFragmentList.add(new meFragment());
        mAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), mFragmentList);
        mViewpager.setAdapter(mAdapter);
        mViewpager.addOnPageChangeListener(mChangeListener);
        mViewpager.setOffscreenPageLimit(10);
        mTabRadioGroup.setOnCheckedChangeListener(mOnCheckChangeListener);
        btn_msg = findViewById(R.id.btn_msg);


//        ((RadioButton) mTabRadioGroup.getChildAt(1)).setChecked(true);
//        mViewpager.setCurrentItem(1);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Const.exitBean != null && !TextUtils.isEmpty(Const.exitBean.getContent())) {
                ButtonStatistical.newuserWindow();
                ExitDialog dialog = new ExitDialog.Builder(this)
                        .setContent(Const.exitBean.getContent())
                        .setCancelText(Const.exitBean.getButton2())
                        .setConfirmText(Const.exitBean.getButton1())
                        .setButtonClick(new ExitDialog.onButtonClick() {
                            @Override
                            public void onConfirmClick(Dialog dialog) {
                                ButtonStatistical.continueBtn();
                                dialog.dismiss();
                            }

                            @Override
                            public void onCancelClick(Dialog dialog) {
                                ButtonStatistical.cancelBtn();
                                dialog.dismiss();
                                moveTaskToBack(false);
//                                appManager.finishAllActivity();
                            }
                        })
                        .build();
                dialog.show();
                //   Const.exitBean = null;
            } else {
                moveTaskToBack(false);
//                appManager.finishAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private ViewPager.OnPageChangeListener mChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) mTabRadioGroup.getChildAt(position);
            radioButton.setChecked(true);
            HashMap<String, Integer> map = new HashMap<>();
            map.put("position", position);
            ButtonStatistical.tabChange(map);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    private RadioGroup.OnCheckedChangeListener mOnCheckChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {

                    if (i == 2 && badge != null) {
                        badge.hide();
                    }
                    mViewpager.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    private class MyFragmentPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> mList;

        public MyFragmentPageAdapter(@NonNull FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mList == null ? null : mList.get(position);
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }
    }

    /**
     * APP是否处于前台唤醒状态
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    private long onForegroundTime = 0;

    @Override
    protected void onStop() {
        if (!isAppOnForeground()) {
            //app 进入后台
            isActive = false;//记录当前已经进入后台
//            Log.i("ACTIVITY", "程序进入后台");
            onForegroundTime = System.currentTimeMillis();
//            WebViewActivity.IntoWebView(MainActivity.this);
//            openUrls();
        }
        super.onStop();
    }


    @Override
    protected void onResume() {
        if (!isActive) {
            //app 从后台唤醒，进入前台
            isActive = true;
//            Log.i("ACTIVITY", "程序从后台唤醒");
            if (onForegroundTime > 0 && System.currentTimeMillis() - onForegroundTime > 5000) {
                if (AppUtil.HWIsShowAd()) {
                    SplashActivity.IntoSplash(this);
                }
            }
            if (onForegroundTime > 0 && System.currentTimeMillis() - onForegroundTime > 50000) {
                getRed();
            }
        }
        super.onResume();
    }

}
