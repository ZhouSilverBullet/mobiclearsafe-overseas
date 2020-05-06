package com.mobi.clearsafe.ui.clear;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.ui.clear.ad.ADRelativeLayout;
import com.mobi.clearsafe.ui.clear.control.ClearPresenter;
import com.mobi.clearsafe.ui.clear.control.ProcessManager;
import com.mobi.clearsafe.ui.clear.entity.CleanEvent;
import com.mobi.clearsafe.ui.clear.entity.CleanRewardBean;
import com.mobi.clearsafe.ui.clear.widget.GoodChangeLayout;
import com.mobi.clearsafe.ui.clear.widget.SpeedLayout;
import com.mobi.clearsafe.ui.clear.widget.SpeedScanLayout;
import com.mobi.clearsafe.ui.clear.widget.wave.WaveSpeedHelper;
import com.mobi.clearsafe.ui.clear.widget.wave.WaveView;
import com.mobi.clearsafe.ui.common.util.SpUtil;
import com.mobi.clearsafe.widget.CleanGoldDialog;
import com.mobi.clearsafe.widget.GoldDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;

import static com.mobi.clearsafe.ui.common.base.BaseActivity.setDarkStatusIcon;

public class SpeedPhoneActivity extends BaseAppCompatActivity implements ClearPresenter.IClearView {

    private RecyclerView rv;
    private GoodChangeLayout gclLayout;
    private SpeedScanLayout slLayout;
    private View rlRoot;
    private Toolbar mToolBar;

    private ClearPresenter clearPresenter;
    private int cleanId;
    private int removeMemoryValue;

    private WaveView waveView;

    private LinearLayout llAd;

    public static void start(Context context, int cleanId, int removeMemoryValue) {
        Intent intent = new Intent(context, SpeedPhoneActivity.class);
        intent.putExtra("cleanId", cleanId);
        intent.putExtra("removeMemoryValue", removeMemoryValue);
        context.startActivity(intent);
    }

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
        setContentView(R.layout.activity_speed_phone);

        setDarkStatusIcon(getWindow(), true);

        removeMemoryValue = getIntent().getIntExtra("removeMemoryValue", 0);
        //去sp中获取一次
        if (removeMemoryValue == 0) {
            removeMemoryValue = SpUtil.getInt(Const.SPEED_TEMP_VALUE, 0);
        }
        if (removeMemoryValue == 0) {
            removeMemoryValue = 70 + new Random().nextInt(10);
        }

        cleanId = getIntent().getIntExtra("cleanId", 0);

        initRv();
        initToolBar();
        initOtherIds();

        clearPresenter = new ClearPresenter(this);
    }

    @Override
    public void clearSpeedPhone(CleanRewardBean data) {
        CleanGoldDialog dialog = new CleanGoldDialog.Builder(this)
                .setGold(data.getPoints())
                .setDialogClick(new CleanGoldDialog.GetGoldDialogClick() {
                    @Override
                    public void closeBtnClick(Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build();
        dialog.show();

    }

    private void initOtherIds() {
        rlRoot = findViewById(R.id.rlRoot);
        gclLayout = findViewById(R.id.gclLayout);

        waveView = findViewById(R.id.waveView);
        WaveSpeedHelper waveSpeedHelper = new WaveSpeedHelper(waveView);
        waveSpeedHelper.start();
        slLayout = findViewById(R.id.slLayout);
        slLayout.setSpeedValue(removeMemoryValue);

        slLayout.setSpeedChangeListener((percent) -> {
            if (percent == 1) {
                gclLayout.setTvDec("已成功加速");
                gclLayout.setTvDec2("手机速度优化成功");
                slLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waveView.setVisibility(View.GONE);
                        slLayout.setVisibility(View.GONE);
                        gclLayout.startAnim();
                    }
                }, 300);
            }
            if (percent <= 0.7f || percent >= 0.69f) {
//                rlRoot.setBackgroundResource(R.drawable.clear_blue_selector);
            }
        });

        gclLayout.setGoodChangeListener(() -> {
            clearPresenter.requestClearSpeedPhone(cleanId, this);
            ProcessManager.getInstance().setRandomNewTime(true);
            addAdLocation();

            CleanEvent event = new CleanEvent();
            event.setType(1);
            EventBus.getDefault().post(event);

            SpUtil.putLongCommit(Const.SPEED_CAN_CLEAR, System.currentTimeMillis());
        });

        gclLayout.setAdRootView(findViewById(R.id.adslContainer));

        llAd = findViewById(R.id.llAd);
    }

    private void initToolBar() {
        mToolBar = findViewById(R.id.toolBar);
        mToolBar.setTitle("");
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setNavigationIcon(R.drawable.white_return);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initRv() {
//        rv = findViewById(R.id.rv);
    }

    /**
     * 添加广告位
     */
    private void addAdLocation() {
        if (llAd != null) {
            llAd.setVisibility(View.VISIBLE);
            for (int i = 0; i < llAd.getChildCount(); i++) {
                View childAt = llAd.getChildAt(i);
                if (childAt instanceof ADRelativeLayout) {
                    ((ADRelativeLayout) childAt).showAdLayout();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消动画

    }
}
