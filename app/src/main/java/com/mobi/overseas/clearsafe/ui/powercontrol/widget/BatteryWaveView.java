package com.mobi.overseas.clearsafe.ui.powercontrol.widget;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class BatteryWaveView extends View {
    public AnimatorSet animatorSet;
    public int b;
    public int mHeight;
    public int d;
    public int e;
    public float f = 0.4F;
    public float g = 0.1F;
    public boolean h = false;
    private Paint paint;
    private Paint paint1;
    private Path path;
    private Path path1;
    private int mWidth;
    private int n;
    public int o = 0;
    public int p = 0;
    private ICallback callback;

    public BatteryWaveView(Context paramContext) {
        super(paramContext);
        init();
    }

    public BatteryWaveView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public BatteryWaveView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private void init() {
        this.paint = new Paint();
        this.paint.setColor(Color.parseColor("#bbffffff"));
        this.paint.setDither(true);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint1 = new Paint(this.paint);
        this.paint1.setColor(Color.parseColor("#55ffffff"));
        this.path = new Path();
        this.path1 = new Path();
    }

    public final void cancelAnim() {
        if (this.animatorSet != null) {
            if (this.animatorSet.isRunning()) {
                this.animatorSet.cancel();
            }
            this.animatorSet = null;
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAnim();
    }

    protected void onDraw(Canvas paramCanvas) {
        super.onDraw(paramCanvas);
        this.path.reset();
        this.path1.reset();
        int i2 = -this.d + this.o;
        int i3 = this.b - this.p;
        if (this.callback != null) {
            this.callback.startDraw(i3);
        }
        Path localPath = this.path;
        float f2 = i2;
        float f1 = i3;
        localPath.moveTo(f2, f1);
        this.path1.moveTo(f2, f1);
        int i1 = 0;
        while (i1 < this.n) {
            f2 = this.d / 4 + i2 + this.d * i1;
            float f3 = i3 + 15;
            float f4 = this.d / 2 + i2 + this.d * i1;
            this.path.quadTo(f2, f3, f4, f1);
            float f5 = this.d * 3 / 4 + i2 + this.d * i1;
            float f6 = i3 - 15;
            float f7 = this.d + i2 + this.d * i1;
            this.path.quadTo(f5, f6, f7, f1);
            this.path1.quadTo(f2, f6, f4, f1);
            this.path1.quadTo(f5, f3, f7, f1);
            i1 += 1;
        }
        this.path.lineTo(this.mWidth, this.mHeight - 20);
        this.path.quadTo(this.mWidth, this.mHeight, this.mWidth - 20, this.mHeight);
        this.path.lineTo(20.0F, this.mHeight);
        this.path.quadTo(0.0F, this.mHeight, 0.0F, this.mHeight - 20);
        this.path.close();
        this.path1.lineTo(this.mWidth, this.mHeight - 20);
        this.path1.quadTo(this.mWidth, this.mHeight, this.mWidth - 20, this.mHeight);
        this.path1.lineTo(20.0F, this.mHeight);
        this.path1.quadTo(0.0F, this.mHeight, 0.0F, this.mHeight - 20);
        this.path1.close();
        paramCanvas.drawPath(this.path1, this.paint1);
        paramCanvas.drawPath(this.path, this.paint);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        this.d = this.mWidth;
        this.n = (this.mWidth / this.d + 1);
        this.b = ((int) (this.mHeight * (1.0F - this.g)));
        this.e = ((int) (this.mHeight * (this.f - this.g) - 15.0F));
        if (this.callback != null) {
            this.callback.startBack();
        }
    }

    public ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = animation -> {
        o = (int) animation.getAnimatedValue();
        invalidate();
    };

    public ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener2 = animation -> {
        p = (int) animation.getAnimatedValue();
    };

    public void startBgAnim(View view) {
        if (view != null && view.getTag() != null) {
            return;
        }

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0xFFFF7400, 0xFF0062FF);
        valueAnimator.setEvaluator(new ArgbEvaluator());
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            if (view != null) {
                view.setBackgroundColor(animatedValue);
            }
        });

        if (view != null) {
            view.setTag(valueAnimator);
        }
        valueAnimator.setStartDelay(3000);
        valueAnimator.start();
    }

    public void setCallback(ICallback paramICallback) {
        this.callback = paramICallback;
    }

    public static abstract interface ICallback {
        public abstract void startBack();

        public abstract void startDraw(int paramInt);
    }
}


/* Location:              /Users/zhousaito/Desktop/nixiang/dex2jar-2.0/classes-dex2jar.jar!/com/sogou/interestclean/battery/view/BatteryWaveView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */