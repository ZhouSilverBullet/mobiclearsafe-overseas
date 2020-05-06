package com.mobi.clearsafe.widget;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;
import android.util.DisplayMetrics;


/**
 * author:zhaijinlu
 * date: 2019/12/15
 * desc:
 */
public class TopSmoothScroller extends LinearSmoothScroller {
    public TopSmoothScroller(Context context) {
        super(context);
    }
    @Override
    public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return 100f / displayMetrics.densityDpi;
    }

    @Override
    public int getHorizontalSnapPreference() {
        return SNAP_TO_START;//具体见源码注释
    }

    @Override
    public int getVerticalSnapPreference()
    {
        return SNAP_TO_START;//具体见源码注释
    }


}
