package com.mobi.clearsafe.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


/**
 * author : liangning
 * date : 2019-12-30  17:08
 */
public class TheTailLayout extends RelativeLayout {
    public TheTailLayout(Context context) {
        super(context);
    }

    public TheTailLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TheTailLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TheTailLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
