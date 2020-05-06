package com.mobi.clearsafe.ui.clear.ad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/24 15:26
 * @Dec 略
 */
public class ADSplashLayout extends FrameLayout {
    public ADSplashLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public ADSplashLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ADSplashLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }
}
