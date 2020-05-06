package com.mobi.clearsafe.ui.clear.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.utils.UiUtils;

import java.util.Random;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/23 11:20
 * @Dec 略
 */
public class PowerCoolLayout extends View {

    private Paint paint;
    private Bitmap bitmap;
    private int dp250;
    private int dp100;
    private Rect rect;
    private LinearGradient greenShader;
    private LinearGradient redShader;
    private Paint gradientPaint;

    private ValueAnimator valueAnimator;
    private Path snowPath;
    private Path snowPath2;
    private Path snowPath3;
    private PathMeasure pathMeasure;
    private PathMeasure pathMeasure2;
    private PathMeasure pathMeasure3;
    private float[] mCurrentPosition = new float[2];
    private float[] mCurrentPosition2 = new float[2];
    private float[] mCurrentPosition3 = new float[2];
    private Bitmap snowBitmap;
    private Bitmap snowBitmap2;
    private Rect bitmapRect2;
    private Rect desBitmapRect2;
    private Rect bitmapRect3;
    private Rect desBitmapRect3;

    //摄氏度的值
    private String drawCelsiusText;
    public double currentCelsius;

    public PowerCoolLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(UiUtils.dp2px(getContext(), 40));

        gradientPaint = new Paint();

        bitmap = getBitmap(R.drawable.ic_report_mobile_cool, false);
        dp250 = UiUtils.dp2px(getContext(), 150);
        dp100 = UiUtils.dp2px(getContext(), 100);

        snowPath = new Path();
        snowPath2 = new Path();
        snowPath3 = new Path();

        snowBitmap = getBitmap(R.drawable.ic_snow, false);
        snowBitmap2 = getBitmap(R.drawable.ic_snow, true);


        drawCelsiusText = String.valueOf(MyApplication.PHONE_CELSIUS) + "°";
        currentCelsius = getRandomCool();
    }

    /**
     * 来回降5度多一点
     *
     * @return
     */
    private double getRandomCool() {
        return (((int) (new Random().nextDouble() * 100)) / 100.0 + new Random(1).nextInt(5) + 6);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (valueAnimator == null) {

            bitmapRect2 = new Rect(0, 0, snowBitmap.getWidth(), snowBitmap.getHeight());
            desBitmapRect2 = new Rect(0, 0, snowBitmap.getWidth() / 2, snowBitmap.getHeight() / 2);
            bitmapRect3 = new Rect();
            desBitmapRect3 = new Rect();

            handlePathMeasure();

            rect = new Rect(0, 0, getWidth(), getHeight());
            greenShader = new LinearGradient(0, 0, getWidth(), getHeight(), new int[]{getResources().getColor(R.color.c_0043ff), getResources().getColor(R.color.c_008dff)}, null, Shader.TileMode.REPEAT);
            redShader = new LinearGradient(0, 0, getWidth(), getHeight(), new int[]{getResources().getColor(R.color.c_FF4A00), getResources().getColor(R.color.c_F49F1F)}, null, Shader.TileMode.REPEAT);
            gradientPaint.setShader(redShader);
        }
    }

    private void handlePathMeasure() {
        int centerX = (int) (getWidth() / 2.0);
        int centerY = (int) (dp250 + bitmap.getHeight() / 2.0f);
        snowPath.moveTo(0, 0);
        snowPath.lineTo(centerX, centerY);

        pathMeasure = new PathMeasure(snowPath, false);

        snowPath2.moveTo(getWidth(), 0);
        snowPath2.lineTo(centerX, centerY);

        pathMeasure2 = new PathMeasure(snowPath2, false);

        snowPath3.moveTo(getWidth(), getHeight() - dp250);
        snowPath3.lineTo(centerX, centerY);

        pathMeasure3 = new PathMeasure(snowPath3, false);

        initValueAnim(1, pathMeasure);
        initValueAnim(2, pathMeasure2);
        initValueAnim(3, pathMeasure3);

    }


    private void initValueAnim(int type, PathMeasure pathMeasure) {

        float length = pathMeasure.getLength();
        valueAnimator = ValueAnimator.ofFloat(0, length);
        valueAnimator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            //mCurrentPosition 传进去，就被复制了
            if (type == 1) {
                pathMeasure.getPosTan(animatedValue, mCurrentPosition, null);
            } else if (type == 2) {
                pathMeasure.getPosTan(animatedValue, mCurrentPosition2, null);
            } else {
                pathMeasure.getPosTan(animatedValue, mCurrentPosition3, null);
            }
            onPercent(animatedValue / length);

            invalidate();
        });
        valueAnimator.setDuration(5000);
        valueAnimator.start();
    }

    private void onPercent(float percent) {
        drawCelsiusText = getLastDoubleValue(MyApplication.PHONE_CELSIUS - ((currentCelsius) * percent)) + "°";
        if (percent > 0.8f) {
            gradientPaint.setShader(greenShader);
        }

        if (coolChangeListener != null) {
            coolChangeListener.onChangePercent(percent);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(snowPath, paint);

        canvas.drawRect(rect, gradientPaint);
        canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2.0f, dp250, paint);

//        String text = "35.1°";

        //画摄氏度的变化
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(drawCelsiusText, (getWidth()) / 2.0f, dp250 + dp100 + bitmap.getHeight(), paint);

        canvas.drawBitmap(snowBitmap, mCurrentPosition[0], mCurrentPosition[1], paint);
        canvas.drawBitmap(snowBitmap2, mCurrentPosition2[0], mCurrentPosition2[1], paint);
        canvas.drawBitmap(snowBitmap2, mCurrentPosition3[0], mCurrentPosition3[1], paint);
    }

    private Bitmap getBitmap(int drawableRes, boolean isHalf) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        return drawableToBitmap(drawable, isHalf);
    }

    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable, boolean isHalf) {
        Bitmap bitmap = null;
        if (isHalf) {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2,
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        }

        Canvas canvas = new Canvas(bitmap);
        if (isHalf) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2, drawable.getIntrinsicHeight() / 2);
        } else {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        drawable.draw(canvas);
        return bitmap;
    }

    private double getLastDoubleValue(double value) {
        return ((int) (value * 10)) / 10.0;
    }

    private PowerCoolChangeListener coolChangeListener;

    public void setCoolChangeListener(PowerCoolChangeListener coolChangeListener) {
        this.coolChangeListener = coolChangeListener;
    }

    public interface PowerCoolChangeListener {
        void onChangePercent(float percent);
    }
}
