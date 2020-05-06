package com.mobi.clearsafe.main.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.mobi.clearsafe.utils.UiUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/20 07:41
 * @Dec 略
 */
public class MyRecyclerView extends RecyclerView {
    public static final String TAG = "MyRecyclerView";
    private final int dp120;

    private boolean isUpToDown;

    private boolean isAutoScrolling;
    private boolean isDownToUp;

    private int currentScroll;

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        dp120 = UiUtils.dp2px(context, 300);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
//        Log.e(TAG, "onScrollStateChanged " + state + " " + getScrollY());

        if (isUpToDown && state == RecyclerView.SCROLL_STATE_IDLE && currentScroll < dp120) {
            isUpToDown = false;
            isAutoScrolling = true;
            smoothScrollBy(0, dp120 - currentScroll);
        }

        if (isDownToUp && state == RecyclerView.SCROLL_STATE_IDLE && currentScroll < dp120) {
            isDownToUp = false;
            isAutoScrolling = true;
            smoothScrollBy(0, -currentScroll);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        isAutoScrolling = false;
//        ToastUtils.showShortSafe("afdaf");
        return super.onTouchEvent(e);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        currentScroll += dy;

        RecyclerView.LayoutManager manager = getLayoutManager();
        if (manager instanceof LinearLayoutManager) {

            Log.e(TAG, "onScrolled " + dx + " " + dy + " currentScroll: " + currentScroll);
            if (dy > 0 && currentScroll < dp120 && !isAutoScrolling) { //从上往下滑
                isUpToDown = true;
            }
            if (dy < 0 && currentScroll < dp120 && !isAutoScrolling) {
                isDownToUp = true;
            }

            calculateScrollPercent(currentScroll);
        }
    }

    /**
     * 计算百分比，然后让外面去实现透明度，等一些操作
     * @param currentScroll
     */
    private void calculateScrollPercent(int currentScroll) {
        if (onScrollPercentListener != null) {
            float percent = currentScroll / (float) dp120;
            if (percent >= 1) {
                percent = 1.0f;
            } else if(percent <=0){
                percent = 0f;
            }
            Log.e(TAG, "percent = " + percent);
            onScrollPercentListener.onPercent(percent);
        }
    }

    private OnScrollPercentListener onScrollPercentListener;

    public void setOnScrollPercentListener(OnScrollPercentListener onScrollPercentListener) {
        this.onScrollPercentListener = onScrollPercentListener;
    }

    public interface OnScrollPercentListener {
        void onPercent(float percent);
    }
}