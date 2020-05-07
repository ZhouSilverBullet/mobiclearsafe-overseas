package com.mobi.overseas.clearsafe.ui.clear.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.utils.UiUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/22 20:52
 * @Dec 略
 */
public class SpeedScanLayout extends View {
    public static final String TAG = "SpeedLayout";
    Paint paint;
    Paint mMemoryPaint;
    Paint mMemoryPaint2;
    Paint mMemoryPaint3;
    private ValueAnimator valueAnimator;

    //用来执行动画的
    private float mDrawHeight = -1;
    private Bitmap bitmap;
    private Paint gradientPaint;
    private Shader redShader;
    private Shader greenShader;
    private Rect rect;
    private ValueAnimator nextAnimator;
    private int removeMemoryValue;
    private int mChangeMemoryValue;
    private int dp350;
    private int dp300;
    private int baseline;
    private int dp15;

    public SpeedScanLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        paint = new Paint();
        bitmap = getBitmap();

        //定义一个Paint
        gradientPaint = new Paint();
        dp350 = MyApplication.sHeight * 7 / 8;
        dp300 = UiUtils.dp2px(getContext(), 200);
        dp15 = UiUtils.dp2px(getContext(), 15);

        mMemoryPaint = new Paint();
        mMemoryPaint.setColor(Color.WHITE);
        mMemoryPaint.setTextSize(UiUtils.dp2px(getContext(), 40));
        mMemoryPaint.setTextAlign(Paint.Align.CENTER);

        mMemoryPaint2 = new Paint();
        mMemoryPaint2.setColor(Color.WHITE);
        mMemoryPaint2.setTextSize(UiUtils.dp2px(getContext(), 20));
        mMemoryPaint2.setTextAlign(Paint.Align.CENTER);

        mMemoryPaint3 = new Paint();
        mMemoryPaint3.setColor(Color.WHITE);
        mMemoryPaint3.setTextSize(UiUtils.dp2px(getContext(), 40));
        mMemoryPaint3.setTextAlign(Paint.Align.CENTER);


        //获取文字所在区域的长方形,textContent是文字内容
        Rect lRect = new Rect();
        Paint.FontMetrics fontMetrics = mMemoryPaint.getFontMetrics();
        String textContent = "%";
        mMemoryPaint.getTextBounds(textContent, 0, textContent.length(), lRect);
        //用控件所占区域的长方形的宽度，减去文字所在的长方形的跨度，一半的位置就是文字开始的X坐标
//        int baseLineX = getMeasuredWidth() / 2 - lRect.width() / 2;
        baseline = (int) ((getMeasuredHeight() - (-fontMetrics.ascent + fontMetrics.descent)) /
                2 + fontMetrics.descent - fontMetrics.ascent + 6);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (valueAnimator == null) {
            initValueAnim(getHeight());

            rect = new Rect(0, 0, getWidth(), getHeight());
            greenShader = new LinearGradient(0, 0, getWidth(), getHeight(), new int[]{getResources().getColor(R.color.c_0043ff), getResources().getColor(R.color.c_008dff)}, null, Shader.TileMode.REPEAT);
            redShader = new LinearGradient(0, 0, getWidth(), getHeight(), new int[]{getResources().getColor(R.color.c_FF4A00), getResources().getColor(R.color.c_F49F1F)}, null, Shader.TileMode.REPEAT);
            gradientPaint.setShader(redShader);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(rect, gradientPaint);

        drawShowText(canvas);

        canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2.0f, mDrawHeight, paint);
    }

    /**
     * 绘制 相关 text
     *
     * @param canvas
     */
    private void drawShowText(Canvas canvas) {

        canvas.drawText(mChangeMemoryValue + "%", (getWidth()) / 2.0f, dp350 + baseline + dp15, mMemoryPaint);
        canvas.drawText(MyApplication.getResString(R.string.memoryOverrun), (getWidth()) / 2.0f, dp350, mMemoryPaint2);
        canvas.drawText(MyApplication.getResString(R.string.speedScanning), (getWidth()) / 2.0f, getHeight() / 2.0f, mMemoryPaint3);

    }

    private void initValueAnim(int value) {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(value - dp300, dp300);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator.setRepeatCount(6);
            valueAnimator.setDuration(800);
            valueAnimator.addUpdateListener(animation -> {
                float animatedValue = (float) animation.getAnimatedValue();
                mDrawHeight = animatedValue;

                invalidate();
            });
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (speedChangeListener != null) {
                        speedChangeListener.onChangePercent(1);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            valueAnimator.start();
        }
//        if (valueAnimator == null) {
//            valueAnimator = ValueAnimator.ofFloat(value, bitmapHeight);
//            valueAnimator.setDuration(5000);
//            valueAnimator.setInterpolator(new DecelerateInterpolator());
//            valueAnimator.addUpdateListener(animation -> {
//                float animatedValue = (float) animation.getAnimatedValue();
//                mDrawHeight = animatedValue;
//
//                float percent = (value - animatedValue) / (value - bitmapHeight);
//
//                //这个动画执行完成，执行下一个动画
////                if (percent == 1) {
////                    createNextAnimator(bitmapHeight);
////                }
//                mChangeMemoryValue = (int) (removeMemoryValue - percent * removeMemoryValue);
//
//                Log.e("SpeedLayout", " mDrawHeight " + mDrawHeight + " p: " + percent + " removeMemoryValue " + removeMemoryValue);
//                invalidate();
//            });
//            valueAnimator.start();
//        }
    }

    private void createArgAnimator() {
//        ValueAnimator animator =  ValueAnimator.ofInt(0xff0043ff, 0xffff6a00);
//        animator.setEvaluator(new ArgbEvaluator());//设置Evaluator
//        animator.setDuration(3000);
//
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int curValue = (Integer) animation.getAnimatedValue();
//                tv_text.setBackgroundColor(curValue);
//            }
//        });
//        animator.start();
    }

    private void createNextAnimator(int bitmapHeight) {
        nextAnimator = ValueAnimator.ofFloat(bitmapHeight, -bitmapHeight);
        nextAnimator.setDuration(500);
        nextAnimator.setInterpolator(new AccelerateInterpolator());
        nextAnimator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            mDrawHeight = animatedValue;
            if (animatedValue == -bitmapHeight) {
                if (speedChangeListener != null) {
                    speedChangeListener.onChangePercent(1);
                }
            }
            invalidate();
        });
        postDelayed(new Runnable() {
            @Override
            public void run() {
                gradientPaint.setShader(greenShader);
                nextAnimator.start();
            }
        }, 500);

    }

    private Bitmap getBitmap() {
        Drawable drawable = getResources().getDrawable(R.drawable.speed_scan_selector);
        return drawableToBitmap(drawable);
    }

    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("PowerCoolActivity", "onDetachedFromWindow");
        //取消动画
        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
        }

        if (nextAnimator != null) {
            nextAnimator.removeAllUpdateListeners();
            nextAnimator.cancel();
        }
    }

    private SpeedChangeListener speedChangeListener;

    public void setSpeedChangeListener(SpeedChangeListener speedChangeListener) {
        this.speedChangeListener = speedChangeListener;
    }

    public void setSpeedValue(int removeMemoryValue) {
        this.removeMemoryValue = removeMemoryValue;
        mChangeMemoryValue = removeMemoryValue;
    }

    public interface SpeedChangeListener {
        void onChangePercent(float percent);
    }
}
