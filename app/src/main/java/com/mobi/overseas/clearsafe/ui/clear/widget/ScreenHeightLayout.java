package com.mobi.overseas.clearsafe.ui.clear.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.mobi.overseas.clearsafe.R;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/22 18:01
 * @Dec 略
 */
public class ScreenHeightLayout extends RelativeLayout {
    public ScreenHeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_screen_layout, null);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
