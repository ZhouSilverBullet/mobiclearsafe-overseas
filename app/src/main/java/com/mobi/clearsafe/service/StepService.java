package com.mobi.clearsafe.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;


import com.mobi.clearsafe.MainActivity;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.greendao.TodayStepData;
import com.mobi.clearsafe.greendao.TodayStepSessionUtils;
import com.mobi.clearsafe.ui.cleannotice.GuideDialogActivity;
import com.mobi.clearsafe.utils.DateUtils;
import com.mobi.clearsafe.utils.LogUtils;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import java.util.List;

public class StepService extends Service {

    private static final String TAG = "StepService";
    public static final String INTENT_NAME_0_SEPARATE = "intent_name_0_separate";//0点广播
    public static final String INTENT_NAME_BOOT = "intent_name_boot";//开机广播


    //保存数据库频率
    private static final int DB_SAVE_COUNTER = 30;
    private int mDbSaveCount = 0;
    //当前步数
    public static int CURRENT_SETP = 0;
    private boolean mSeparate = false;//是否0点
    private boolean mBoot = false;//是否开机

    //传感器的采样周期，这里使用SensorManager.SENSOR_DELAY_FASTEST，如果使用SENSOR_DELAY_UI会导致部分手机后台清理内存之后传感器不记步
    private static final int SAMPLING_PERIOD_US = SensorManager.SENSOR_DELAY_FASTEST;

    private TodayStepCounter stepCounter;

    // 通知栏相关
    public static int NOTI_ID = 20191014;//通知栏ID
    Notification notification;
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;
    RemoteViews remoteViews;
    private int index;

    //广播接收者
    private BroadcastReceiver broadcastReceiver;

    private SensorManager sensorManager;//传感器实例

    private Intent intent = new Intent("com.example.communication.RECEIVER");
    private Handler mHandler;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // 开始一个前台的通知栏
        initNotification(CURRENT_SETP);
//           initBroadcastReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.e("onStartCommand:" + CURRENT_SETP);
        if (null != intent) {
            mSeparate = intent.getBooleanExtra(INTENT_NAME_0_SEPARATE, false);
            mBoot = intent.getBooleanExtra(INTENT_NAME_BOOT, false);
        }
        updateNotification(CURRENT_SETP);
        //注册传感器
        startStepDetector();
        return START_STICKY;
    }

    private void initNotification(int currentStep) {
        builder = new NotificationCompat.Builder(this, "notification_id");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 【适配Android8.0】给NotificationManager对象设置NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.clean_notification_layout);
        builder.setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher);
        builder.setContent(remoteViews);
        builder.setContentIntent(contentIntent);
//        String km = getDistanceByStep(currentStep);
//        String calorie = getCalorieByStep(currentStep);
//        remoteViews.setTextViewText(R.id.tv_stepnum,currentStep+"");
//        remoteViews.setTextViewText(R.id.tv_distance,km+"公里");
//        remoteViews.setTextViewText(R.id.tv_kaluli,calorie+"千卡");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR; // 不允许删除通知栏

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id");
        }
        startForeground(NOTI_ID, notification);

//        mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ToastUtils.showLong("adfafdafdadfafdafdadfafdafdadfafdafdadfafdafdadfafdafdadfafdafd");
//                Intent intent2 = new Intent(StepService.this, GuideDialogActivity.class);
//                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                StepService.this.startActivity(intent2);
//            }
//        } ,10000);
    }

    /**
     * 获取传感器实例
     */
    private void startStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        //android 4.4  以后可以使用记步传感器
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isStepCounter()) {
            addCountStepListener();
        }
    }

    private boolean isStepCounter() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);
    }

    /**
     * 添加传感器监听
     * 1。 TYPE_STEP_COUNTER  API解释说返回从开机被激活后统计的步数，当重启手机后该数据归零
     * 该传感器是一个硬件传感器  低功耗
     * 为了能持续记步 不要解绑事件监听，手机在休眠状态依然能记步，适合长时间记步需求
     * <p>
     * 2。TYPE_STEP_DETECTOR 走路检测
     * API解释 用来检测走步，每次返回值为1.0 产生步数后+1的 可以用这个返回自己累加步数
     */
    private void addCountStepListener() {
        Log.e(TAG, "addStepCounterListener");
        if (null != stepCounter) {
            LogUtils.e("已经注册TYPE_STEP_COUNTER:");
            WakeLockUtils.getLock(this);
            CURRENT_SETP = stepCounter.getCurrentStep();
            updateNotification(CURRENT_SETP);
            return;
        }
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (null == countSensor) {
            return;
        }
        stepCounter = new TodayStepCounter(getApplicationContext(), mOnStepCounterListener, mSeparate, mBoot);
        sensorManager.registerListener(stepCounter, countSensor, SAMPLING_PERIOD_US);
    }

    /**
     * 更新通知
     */
    private void updateNotification(int stepCount) {
        if (true) {
            return;
        }
        try {
            index++;
            if (index % 100 == 0) {
                //当到第一百次的时候重新init一个，之前的丢弃
                initNotification(stepCount);
                index = 0;
                return;
            }
            if (null == builder) {
                return;
            }
            builder.setContentTitle(getString(R.string.app_name));
            String km = getDistanceByStep(stepCount);
            String calorie = getCalorieByStep(stepCount);
//           remoteViews.setTextViewText(R.id.tv_stepnum,stepCount+"");
//           remoteViews.setTextViewText(R.id.tv_distance,km+"公里");
            notification = builder.build();
            notificationManager.notify(NOTI_ID, notification);
        } catch (Exception e) {

        }

    }

    // 公里计算公式
    static String getDistanceByStep(long steps) {
        return String.format("%.2f", steps * 0.6f / 1000);
    }

    // 千卡路里计算公式
    static String getCalorieByStep(long steps) {
        return String.format("%.1f", steps * 0.6f * 60 * 1.036f / 1000);
    }

    private OnStepCounterListener mOnStepCounterListener = new OnStepCounterListener() {
        @Override
        public void onChangeStepCounter(int step) {
            saveDb(step);
        }

        @Override
        public void onStepCounterClean() {
            CURRENT_SETP = 0;
            updateNotification(CURRENT_SETP);
            cleanDb();
        }

    };

    private void cleanDb() {

        LogUtils.e("cleanDb");
        mDbSaveCount = 0;
    }


    private void saveDb(int currentStep) {
        List<TodayStepData> query = TodayStepSessionUtils.query(this, DateUtils.getStringDateDay());
        if (query != null && query.size() > 0) {
            //计算当前步数 =微信步数+（本次系统步数-上次系统步数）
            int weChat_step = 0;
            int current_data = 0;
            int realCurrent = 0;
            if (!TextUtils.isEmpty(query.get(0).getWeChat_data())) {
                weChat_step = Integer.valueOf(query.get(0).getWeChat_data());
            }
            if (!TextUtils.isEmpty(query.get(0).getCurrent_data())) {
                current_data = Integer.valueOf(query.get(0).getCurrent_data());
            }
            if (weChat_step == 0) {//当数据库微信步数为0说明之前步数尚未上传服务器，系统回调步数即为当前步数
                realCurrent = currentStep;
            } else {//已将上次步数上传服务器，目前步数=微信步数+（本次-上次系统步数）
                realCurrent = weChat_step + (currentStep - current_data);
            }
            //  LogUtils.e(currentStep+"===="+weChat_step+"==="+current_data+"===="+realCurrent);
            CURRENT_SETP = realCurrent;
            intent.putExtra("step", realCurrent);
            sendBroadcast(intent);
            updateNotification(CURRENT_SETP);
            //将每次获取到的系统步数存入数据库
            TodayStepData stepData = query.get(0);
            stepData.setWeChat_data(String.valueOf(realCurrent));
            stepData.setCurrent_data(String.valueOf(currentStep));
            stepData.setDate(DateUtils.getStringDateDay());
            stepData.setTime(String.valueOf(System.currentTimeMillis()));
            TodayStepSessionUtils.insertDbBean(this, stepData);
        } else {
            CURRENT_SETP = currentStep;
            intent.putExtra("step", currentStep);
            sendBroadcast(intent);
            updateNotification(CURRENT_SETP);
            String userId = UserEntity.getInstance().getUserId();
            TodayStepData stepData = new TodayStepData();
            if (!TextUtils.isEmpty(userId)) {
                stepData.setUserId(userId);
            }
            stepData.setCurrent_data(String.valueOf(currentStep));
            stepData.setDate(DateUtils.getStringDateDay());
            stepData.setTime(String.valueOf(System.currentTimeMillis()));
            //   LogUtils.e("saveDb currentStep : " + currentStep);
            TodayStepSessionUtils.insertDbBean(this, stepData);
        }
        List<TodayStepData> todayStepData = TodayStepSessionUtils.queryAll(this);
        for (TodayStepData bean : todayStepData) {
            // LogUtils.e("步数为"+bean.getCurrent_data());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消前台进程
        stopForeground(true);
    }


}
