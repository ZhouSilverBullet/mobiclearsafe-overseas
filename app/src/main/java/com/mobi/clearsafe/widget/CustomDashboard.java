package com.mobi.clearsafe.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import android.support.annotation.Nullable;

/**
 * author:zhaijinlu
 * date: 2020/2/4
 * desc: 自定义仪表盘
 */
public class CustomDashboard extends View {
    public CustomDashboard(Context context) {
        this(context,null);
    }

    public CustomDashboard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomDashboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

   //控件宽
    private int mWidth;
    /**
     * 控件高
     */
    private int mHeight;

    /**
     * 刻度高 短针
     */
    private int mScaleHeight = dp2px(10);
    /**
     * 刻度高 长针
     */
    private int mScaleHeight1 = dp2px(15);
    /**
     * 刻度盘/
     */
    private Paint mDialPaint;


    /**
     * 当前步数
     */
    private int mTemperature = 0;
    //
    /**
     * 最少步数
     */
    private int mMinTemp = 0;
    /**
     * 最大步数
     */
    private int mMaxTemp = 20;


    /**
     * 步数每份的角度
     */
    private float mAngleOneTem = 9f;


    /**
     * 步数，刻度的半径
     */
    private int mTemDialRadius;


    /**
     * 未达到的步数
     */
    private String mDialBackGroundColor = "#20ffffff";
    /**
     * 已经达到的步数
     */
    private String mDialForegroundColor = "#ffffff";


    public void init(){
        //温度刻度盘
        mDialPaint = new Paint();
        mDialPaint.setAntiAlias(true);
        mDialPaint.setColor(Color.parseColor(mDialBackGroundColor));
        mDialPaint.setStrokeWidth(dp2px(2));
        mDialPaint.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        // 参考宽，处理成正方形
        setMeasuredDimension(specSize, specSize);
    }
    public void changeTempAnim(final int mTemperature) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,20,mTemperature);
        valueAnimator.setDuration(1000);
        valueAnimator.setStartDelay(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                CustomDashboard.this.mTemperature = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 控件宽、高
        mWidth = mHeight = Math.min(h, w);
        // 刻度的半径
        mTemDialRadius = mWidth / 2 - dp2px(70);
        mTemDialRadius = mTemDialRadius + mScaleHeight1 + dp2px(15);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画步数刻度盘和内的刻度
        drawTempDial(canvas);
    }

    /**
     * 画温度刻度
     */
    private void drawTempDial(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // 顺时针旋转90度
        canvas.rotate(-90);
        for (int i = mMinTemp; i <= mMaxTemp; i++) {
            if (i <= mTemperature) {
                mDialPaint.setColor(Color.parseColor(mDialForegroundColor));
            } else {
                mDialPaint.setColor(Color.parseColor(mDialBackGroundColor));
            }
            if (i % 5 == 0) {
                //从刻度的内圈开始，往外画
                canvas.drawLine(0, -mTemDialRadius, 0, -mTemDialRadius -mScaleHeight1, mDialPaint);
            } else {
                canvas.drawLine(0, -mTemDialRadius, 0, -mTemDialRadius -mScaleHeight, mDialPaint);
            }
            canvas.rotate(mAngleOneTem);
        }
        canvas.restore();
    }

    public void setTemp(int temp) {
        this.mTemperature = temp;
        changeTempAnim(mTemperature * 20 / 6000);
       // invalidate();
    }




    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
