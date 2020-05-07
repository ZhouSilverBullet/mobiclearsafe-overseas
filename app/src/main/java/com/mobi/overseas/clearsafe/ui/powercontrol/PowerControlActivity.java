package com.mobi.overseas.clearsafe.ui.powercontrol;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.clear.control.SingleClickListener;
import com.mobi.overseas.clearsafe.ui.clear.fragment.ShowGoodFragment;
import com.mobi.overseas.clearsafe.ui.common.base.BaseActivity;
import com.mobi.overseas.clearsafe.ui.common.util.SpannableStringBuilderUtil;
import com.mobi.overseas.clearsafe.ui.powercontrol.adapter.PowerControlAdapter;
import com.mobi.overseas.clearsafe.ui.powercontrol.adapter.PowerControlPropertyAdapter;
import com.mobi.overseas.clearsafe.ui.powercontrol.data.BatteryBean;
import com.mobi.overseas.clearsafe.ui.powercontrol.data.BatteryInfo;
import com.mobi.overseas.clearsafe.ui.powercontrol.data.BatteryPropertyBean;
import com.mobi.overseas.clearsafe.ui.powercontrol.fragment.PowerControlFragment;
import com.mobi.overseas.clearsafe.ui.powercontrol.receiver.BatteryBroadcastReceiver;
import com.mobi.overseas.clearsafe.ui.powercontrol.widget.BatteryView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

import static android.os.BatteryManager.BATTERY_STATUS_CHARGING;
import static android.os.BatteryManager.BATTERY_STATUS_FULL;

public class PowerControlActivity extends BaseActivity implements BatteryBroadcastReceiver.BatterChangeListener {
    public static final String TAG = "PowerControlActivity";
    public static final String POWER_CONTROL_NOTIFY = "POWER_CONTROL_NOTIFY";

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.flTop)
    FrameLayout flTop;
    @BindView(R.id.ivTop)
    ImageView ivTop;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.batteryView)
    BatteryView batteryView;
    @BindView(R.id.tvPowerDec)
    TextView tvPowerDec;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.rvProperty)
    RecyclerView rvProperty;
    @BindView(R.id.llPowerStatics)
    LinearLayout llPowerStatics;
    @BindView(R.id.llPowerRich)
    LinearLayout llPowerRich;
    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    @BindView(R.id.flContainer)
    FrameLayout flContainer;
    @BindView(R.id.nsvScroll)
    NestedScrollView nsvScroll;


    private BatteryBroadcastReceiver mReceiver;
    private PowerControlPropertyAdapter mPropertyAdapter;
    private PowerControlAdapter mAdapter;
    private InnerBroadcastReceiver mInnerReceiver;
    private Random mRandom;

    public static int chargingPower = 0;
    public static int jianShaoPower = 0;
    //一键优化多少
    public static int youHuaPower = 0;
    private boolean flTopAnimIsRunning;
    private boolean isHealthChange;
    private boolean isBadChange;
    private int mLevel;
    private int mStatus;


    public static void start(Context context, int cleanId) {
        Intent intent = new Intent(context, PowerControlActivity.class);
        intent.putExtra("cleanId", cleanId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_power_control;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected boolean isFitWindow() {
        return true;
    }

    @Override
    public void initView() {
        super.initView();
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new PowerControlAdapter();
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(mAdapter);

        rvProperty.setLayoutManager(new GridLayoutManager(this, 3));
        mPropertyAdapter = new PowerControlPropertyAdapter();
        rvProperty.setNestedScrollingEnabled(false);
        rvProperty.setAdapter(mPropertyAdapter);

        flTop.setTag(R.drawable.power_control_top_blue_bg);


        registerNeedReceiver();

    }

    @Override
    public void initEvent() {
        btnClear.setOnClickListener(new SingleClickListener(1200) {
            @Override
            public void onSingleClick(View v) {
                getIntent().putExtra("level", mLevel);
                getIntent().putExtra("status", mStatus);

                nsvScroll.setPadding(0, 0, 0, 0);
                llBottomAnim();

                flContainer.setVisibility(View.VISIBLE);
                PowerControlFragment powerControlFragment = new PowerControlFragment();
                //power的动画结束后，显示ShowGoodFragment
                powerControlFragment.setAnimalFinishCallback((isPowering) -> {
                    if (isFinishing()) {
                        return;
                    }

                    //充电状态
                    if (isPowering) {
                        getIntent().putExtra("dec", getResources().getString(R.string.quickChargingStarted));
                        getIntent().putExtra("dec2", getResources().getString(R.string.quickChargingStartedStatus));
                    } else {
                        getIntent().putExtra("dec", getResources().getString(R.string.quickChargingSpeed));
                        getIntent().putExtra("dec2", getResources().getString(R.string.quickChargingSpeedStatus));
                    }

                    ShowGoodFragment fragment = ShowGoodFragment.newInstance(false);
                    fragment.setFirstGood(false);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .hide(powerControlFragment)
                            .add(R.id.flContainer, fragment)
                            .commit();

                });

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flContainer, powerControlFragment)
                        .commit();
            }
        });
    }

    private void llBottomAnim() {
        llBottom.post(() -> {
            ViewCompat.animate(llBottom)
                    .translationY(llBottom.getHeight())
                    .setDuration(500)
                    .start();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void initData() {

        mAdapter.replaceData(BatteryBean.createList());
    }

    private void registerNeedReceiver() {

        mReceiver = new BatteryBroadcastReceiver();
        mReceiver.setBatterChangeListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mReceiver, filter);


        mInnerReceiver = new InnerBroadcastReceiver();
        filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(POWER_CONTROL_NOTIFY);

        registerReceiver(mInnerReceiver, filter);

//        BatteryManager batteryManager;
//        batteryManager.com  puteChargeTimeRemaining()
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.e(TAG, "-------onWindowFocusChanged--------");
        mAdapter.replaceData(BatteryBean.createList());
    }

    @Override
    public void onChange(BatteryInfo info) {
        if (isFinishing()) {
            return;
        }
        int level = info.getLevel();
        int status = info.getStatus();

        mLevel = level;
        mStatus = status;

        batteryView.setProgress(level);

        if (mRandom == null) {
            mRandom = new Random();
        }

        if (youHuaPower == 0) {
            youHuaPower = mRandom.nextInt(6) + 10;
        }

        batteryView.setChargeState(2);

        if (BATTERY_STATUS_FULL == status) {
            tvTime.setText(getResources().getString(R.string.batteryFull));
            batteryView.setChargeState(0);
            btnClear.setText(getResources().getString(R.string.startQuickCharging));
            tvPowerDec.setText("");

        } else if (BATTERY_STATUS_CHARGING == status) {
            //充电
            if (chargingPower == 0) {
                chargingPower = mRandom.nextInt(5) + 28;
            }
            //提升
            //        ak = paramView.nextInt(6) + 10;
            //  容量 * currentLevel[总值-现在的值]/ bx /100d + 1
            //充满
            int m = (int) (info.getBatteryCapacityInt() * (info.getScale() - level) / chargingPower / 100.0 + 1);
            if (m < 0) {
                tvTime.setText(getResources().getString(R.string.batteryFull));
            } else {
                SpannableStringBuilderUtil.handlePowerLast(tvTime, m);
            }
            batteryView.setChargeState(0);

            btnClear.setText(getResources().getString(R.string.startQuickCharging));
            tvPowerDec.setText(getResources().getString(R.string.beCharging));
        } else {
            //放电
            if (jianShaoPower == 0) {
                jianShaoPower = (int) ((mRandom.nextInt(21) + 40.0F) / 100.0F + 3.0F);
            }
            int m = (int) (info.getBatteryCapacityInt() * level / info.getScale());
            m = (int) (m / jianShaoPower + 1.0F);
            SpannableStringBuilderUtil.handlePowerUse(tvTime, m);

            btnClear.setText(String.format(getResources().getString(R.string.optimizePowerMinutes) , youHuaPower));
            tvPowerDec.setText(getResources().getString(R.string.powerIsPoor));
        }

        //设置颜色的状态
        handleTopBgAndDec(level);


        ArrayList<BatteryPropertyBean> list = new ArrayList<>();
        list.add(new BatteryPropertyBean(getResources().getString(R.string.batteryStatus), info.getHealth()));
        list.add(new BatteryPropertyBean(getResources().getString(R.string.batteryVoltage), info.getVoltage()));
        list.add(new BatteryPropertyBean(getResources().getString(R.string.batteryTemperature), info.getTemperature()));
        list.add(new BatteryPropertyBean(getResources().getString(R.string.currentCapacity), info.getLevelPercent()));
        list.add(new BatteryPropertyBean(getResources().getString(R.string.totalCapacity), info.getBatteryCapacity()));
        list.add(new BatteryPropertyBean(getResources().getString(R.string.batteryTechnology), info.getTechnology()));
        mPropertyAdapter.replaceData(list);


    }

    private void handleTopBgAndDec(int level) {
        if (level > 80) {
            if (isHealthChange) {
                isBadChange = true;
//                return;
            }
            isHealthChange = true;

            ivTop.setBackgroundResource(R.drawable.power_control_top_blue_bg);


        } else {
            if (isBadChange) {
                isHealthChange = true;
//                return;
            }
            isBadChange = true;
            ivTop.setBackgroundResource(R.drawable.power_control_top_bg);

        }

    }


    @OnClick({R.id.llPowerStatics, R.id.llPowerRich})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llPowerStatics:
                try {
                    Intent paramView = new Intent("android.intent.action.POWER_USAGE_SUMMARY");
                    if (getPackageManager().resolveActivity(paramView, 0) != null) {
                        startActivity(paramView);
                    }
                } catch (Exception paramView) {
                }
                break;
            case R.id.llPowerRich:
                startActivity(new Intent(getContext(), BatteryMaintainTipsActivity.class));
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        unregisterReceiver(mInnerReceiver);
    }

    class InnerBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //刷新一下界面
            if (mAdapter != null) {
                mAdapter.replaceData(BatteryBean.createList());
            }

        }
    }
}
