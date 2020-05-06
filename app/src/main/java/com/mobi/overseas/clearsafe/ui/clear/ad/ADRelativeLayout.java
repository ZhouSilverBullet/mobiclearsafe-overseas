package com.mobi.overseas.clearsafe.ui.clear.ad;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.nativeexpress.NativeExpressAd;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.ToastUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/30 14:37
 * @Dec 略
 */
public class ADRelativeLayout extends RelativeLayout {
    public static final String TAG = "ADRelativeLayout";
    private final boolean isGoneView;
    private final boolean isAdrShadow;

    private ScenarioEnum mScenarioEnum;
    private String adrScenario;

    public ADRelativeLayout(Context context) {
        this(context, null);
    }

    public ADRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ADRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ADRelativeLayout, defStyleAttr, 0);
        try {
            adrScenario = a.getString(R.styleable.ADRelativeLayout_adrScenario);
            isGoneView = a.getBoolean(R.styleable.ADRelativeLayout_isGoneView, false);
            isAdrShadow = a.getBoolean(R.styleable.ADRelativeLayout_isAdrShadow, true);
        } finally {
            a.recycle();
        }

        if (mScenarioEnum == null && !TextUtils.isEmpty(adrScenario)) {
            mScenarioEnum = ScenarioEnum.valueOf(adrScenario);
        }

        if (!isGoneView) {
            showAdLayout();
        }

        if (isAdrShadow) {
//            setPadding(10, 10, 10, 10);
            setBackgroundResource(R.drawable.ad_shadow_bg);
        }
    }

    public void setAdrScenario(ScenarioEnum scenarioEnum) {
        mScenarioEnum = scenarioEnum;
    }

    public void showAdLayout() {
        if (mScenarioEnum == null) {
            //remove广告位
            removeAllViews();
            return;
        }
        if (AppUtil.HWIsShowAd()) {

            Log.e("ADRelativeLayout", mScenarioEnum.name());
            new NativeExpressAd.Builder(getContext())
                    .setAdCount(1)
                    .setADViewSize(350, 0)
                    .setHeightAuto(true)
                    .setSupportDeepLink(true)
                    .setBearingView(this)
//                    .setScenario(ScenarioEnum.me_page_native)
                    .setScenario(mScenarioEnum)
                    .build();
        }
    }

    public void removeAnim() {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged");
    }
}
