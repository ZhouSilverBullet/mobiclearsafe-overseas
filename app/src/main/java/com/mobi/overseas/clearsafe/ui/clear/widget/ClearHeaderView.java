package com.mobi.overseas.clearsafe.ui.clear.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.clear.control.ScanAnimatorContainer;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/20 10:33
 * @Dec 略
 */
public class ClearHeaderView extends FrameLayout {

    private TextView tvNum;
    private TextView tvUnit;
    private View vScan;
    private View clRoot;
    private ScanAnimatorContainer scanAnimatorContainer;

    public ClearHeaderView(@NonNull Context context) {
        super(context);
        init();
    }

    public ClearHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_clear_layout, null);
        tvNum = findViewById(R.id.tvNum);
        tvUnit = findViewById(R.id.tvUnit);
        //扫描动画
        vScan = findViewById(R.id.vScan);
        //变色布局
        clRoot = findViewById(R.id.clRoot);

        //执行扫描动画
        scanAnimatorContainer = new ScanAnimatorContainer(vScan);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (scanAnimatorContainer != null) {
            scanAnimatorContainer.startAnimator();
        }
    }

    public void setData(String tvNumValue, String tvUnitValue) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimal();
    }

    public void stopAnimal() {
        if (scanAnimatorContainer != null) {
            scanAnimatorContainer.stop();
        }
    }
}
