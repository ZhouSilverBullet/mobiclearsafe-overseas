package com.mobi.overseas.clearsafe.ui.clear.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.mobi.overseas.clearsafe.R;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/23 10:06
 * @Dec 略
 */
public class GoodChangeView extends View {
    Paint paint;
    private Bitmap bitmap;

    public GoodChangeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        paint = new Paint();
        bitmap = getBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2.0f, 0, paint);
    }


    private Bitmap getBitmap() {
        Drawable drawable = getResources().getDrawable(R.mipmap.exit_icon);
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
}
