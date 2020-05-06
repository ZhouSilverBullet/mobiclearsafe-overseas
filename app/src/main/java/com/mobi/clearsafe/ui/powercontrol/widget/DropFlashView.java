package com.mobi.clearsafe.ui.powercontrol.widget;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.utils.UiUtils;

import java.util.Random;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/23 01:16
 * @Dec 略
 */
public class DropFlashView extends FrameLayout implements Handler.Callback {
    private int dp35;
    private int dp25;
    private int mCurFlashHeight;
    private int dp10;
    private Rect rect;
    private Random random;
    private int mWidth;
    private int mHeight;

    private Handler mHandler = new Handler(this);

    private boolean isSend = false;


    public DropFlashView(Context paramContext) {
        super(paramContext);
        init();
    }

    public DropFlashView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public DropFlashView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private void init() {
        dp35 = ((int) TypedValue.applyDimension(1, 35.0F, getResources().getDisplayMetrics()));
        this.dp25 = ((int) TypedValue.applyDimension(1, 25.0F, getResources().getDisplayMetrics()));
        this.random = new Random();
        this.rect = new Rect();
        this.dp10 = UiUtils.dp2px(getContext(), 10);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeMessages(1);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    public void setCurFlashHeight(int paramInt) {
        this.mCurFlashHeight = (paramInt + this.dp10);
        if (!isSend) {
            isSend = true;
            mHandler.sendEmptyMessage(1);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(R.drawable.ic_flash_move);
        int width = random.nextInt(mWidth);
        //最左边
        if (width < 100) {
            width = 100;
            //最右边
        } else if (width > mWidth - 100) {
            width = mWidth - 100;
        }
        imageView.setX(width);
        //往上走
//            float y = mCurFlashHeight;
        //往下走
        float y = 0;
        imageView.setY(y);


        LayoutParams layoutParams = new LayoutParams(dp25, dp25);
        addView(imageView, layoutParams);

        imageView.animate()
                .alpha(0)
                .translationY(mCurFlashHeight + dp25)
//                    .rotation(360)
                .setDuration(1000)
                .start();

        mHandler.sendEmptyMessageDelayed(1, 400);
        return true;
    }
}
