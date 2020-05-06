package com.mobi.overseas.clearsafe.ui.appwiget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CleanAppWidgetGuideActivity extends BaseActivity {
    public static final String TAG = "CleanAppWidgetGuide";

    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.btnOpen)
    Button btnOpen;
    @BindView(R.id.tvDec1)
    TextView tvDec1;
    @BindView(R.id.tvDec2)
    TextView tvDec2;

    @BindView(R.id.rlLayout)
    RelativeLayout rlLayout;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    public static void start(Context context, int cleanId) {
        Intent intent = new Intent(context, CleanAppWidgetGuideActivity.class);
        intent.putExtra("cleanId", cleanId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_clean_app_widget_guide;
    }

    @Override
    public void initView() {
        super.initView();
        toolBar.setTitle("清理小工具设置攻略");
        toolBar.setTitleTextColor(getResources().getColor(R.color.black_33));
        toolBar.setNavigationIcon(R.drawable.return_black_left);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (isShowOpenBtn()) {
            rlLayout.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            rlLayout.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);
        }
    }

    private boolean isShowOpenBtn() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                (Build.MANUFACTURER.toLowerCase().contains("huawei")
                        || Build.BRAND.equals("huawei")
                        || Build.BRAND.equals("honor"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @OnClick({R.id.btnOpen, R.id.tvDec1, R.id.tvDec2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnOpen:
                setWidgetShortcut(getApplicationContext());
                break;
            case R.id.tvDec1:
                break;
            case R.id.tvDec2:
                break;
        }
    }

    public static void setWidgetShortcut(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppWidgetManager appWidgetManager = context.getSystemService(AppWidgetManager.class);
                if (appWidgetManager != null && appWidgetManager.isRequestPinAppWidgetSupported()) {
                    ComponentName widgetProvider = new ComponentName(context, CleanAppWidget.class);
                    appWidgetManager.requestPinAppWidget(widgetProvider, null, null);
                }
            }

        } catch (Exception ex) {
            Log.e(TAG, "setWidgetShortcut:" + ex.getMessage().toString());
        }

    }

}
