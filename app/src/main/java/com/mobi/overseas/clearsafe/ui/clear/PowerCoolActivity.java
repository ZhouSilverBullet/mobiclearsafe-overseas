package com.mobi.overseas.clearsafe.ui.clear;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.ui.clear.ad.ADRelativeLayout;
import com.mobi.overseas.clearsafe.ui.clear.control.ClearPresenter;
import com.mobi.overseas.clearsafe.ui.clear.entity.CleanEvent;
import com.mobi.overseas.clearsafe.ui.clear.entity.CleanRewardBean;
import com.mobi.overseas.clearsafe.ui.clear.widget.GoodChangeLayout;
import com.mobi.overseas.clearsafe.ui.clear.widget.PowerCoolLayout;
import com.mobi.overseas.clearsafe.ui.common.util.SpUtil;
import com.mobi.overseas.clearsafe.widget.CleanGoldDialog;
import com.mobi.overseas.clearsafe.widget.GoldDialog;

import org.greenrobot.eventbus.EventBus;

import static com.mobi.overseas.clearsafe.ui.common.base.BaseActivity.setDarkStatusIcon;

/**
 * 降温
 */
public class PowerCoolActivity extends BaseAppCompatActivity implements ClearPresenter.IClearView {
    public static final String TAG = "PowerCoolActivity";
    private PowerCoolLayout pclLayout;
    private GoodChangeLayout gclLayout;
    private Toolbar mToolBar;

    private ClearPresenter clearPresenter;
    private int cleanId;
    private LinearLayout llAd;

    public static void start(Context context, int cleanId) {
        Intent intent = new Intent(context, PowerCoolActivity.class);
        intent.putExtra("cleanId", cleanId);
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
        setContentView(R.layout.activity_power_cool);

        setDarkStatusIcon(getWindow(), true);

        initToolBar();
        initOtherIds();

        cleanId = getIntent().getIntExtra("cleanId", 0);
        clearPresenter = new ClearPresenter(this);
    }

    @Override
    public void clearPowerCool(CleanRewardBean data) {
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

        EventBus.getDefault().post(new CleanEvent());
    }

    private void initOtherIds() {
        gclLayout = findViewById(R.id.gclLayout);
        pclLayout = findViewById(R.id.pclLayout);
        llAd = findViewById(R.id.llAd);

//        if (MyApplication.PHONE_CELSIUS < 25) {
//            pclLayout.setVisibility(View.GONE);
//            gclLayout.startAnim();
//        } else  {
        pclLayout.setCoolChangeListener(percent -> {
            if (percent == 1) {
                String formatStr = getResources().getString(R.string.clear_power_cool_format);
                gclLayout.setTvDec(String.format(formatStr, pclLayout.currentCelsius));
                gclLayout.setTvDec2(getResources().getString(R.string.clear_power_cool_status));
                pclLayout.setVisibility(View.GONE);
                gclLayout.startAnim();
            }
        });
//        }

        gclLayout.setGoodChangeListener(() -> {
            clearPresenter.requestClearPowerCool(cleanId, this);
            addAdLocation();

            SpUtil.putLong(Const.POWER_CAN_CLEAR, System.currentTimeMillis());
        });

        gclLayout.setAdRootView(findViewById(R.id.adslContainer));
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

}
