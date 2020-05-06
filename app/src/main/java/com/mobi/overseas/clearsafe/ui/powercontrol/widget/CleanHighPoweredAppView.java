package com.mobi.overseas.clearsafe.ui.powercontrol.widget;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.ui.powersaving.data.PowerSavingBean;
import com.mobi.overseas.clearsafe.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/23 01:16
 * @Dec 略
 */
public class CleanHighPoweredAppView extends FrameLayout implements Handler.Callback {
    private int dp35;
    private int dp25;
    private int mCurFlashHeight;
    private int dp10;
    private Rect rect;
    private Random random;
    private int mWidth;
    private int mHeight;

    private int index = 0;

    private Handler mHandler = new Handler(this);

    private boolean isSend = false;
    private List<Drawable> mList;


    public CleanHighPoweredAppView(Context paramContext) {
        super(paramContext);
        init();
    }

    public CleanHighPoweredAppView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        init();
    }

    public CleanHighPoweredAppView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        init();
    }

    private void init() {
        dp35 = ((int) TypedValue.applyDimension(1, 35.0F, getResources().getDisplayMetrics()));
        this.dp25 = ((int) TypedValue.applyDimension(1, 25.0F, getResources().getDisplayMetrics()));
        this.random = new Random();
        this.rect = new Rect();
        addData();
        this.dp10 = UiUtils.dp2px(getContext(), 10);

    }

    private void addData() {
        int size = MyApplication.INSTALLED_APPS.size();
        //计算前面两个drawable给显示上
        int k = 0;
        mList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ApplicationInfo applicationInfo = MyApplication.INSTALLED_APPS.get(i);

            //系统app不进行获取了
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                continue;
            }

            Drawable imageDrawable = applicationInfo.loadIcon(MyApplication.PM);
            mList.add(imageDrawable);
        }

        if (mList.size() == 0) {
            mList.add(getResources().getDrawable(R.drawable.type_apk));
            mList.add(getResources().getDrawable(R.drawable.type_folder));
            mList.add(getResources().getDrawable(R.drawable.type_note));
            mList.add(getResources().getDrawable(R.drawable.type_package));
        }
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
        imageView.setImageDrawable(mList.get(index % mList.size()));
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
        float y = mCurFlashHeight;
        //往下走
//            float y = mHeigh-  mCurFlashHeight;
        imageView.setY(y);


        LayoutParams layoutParams = new LayoutParams(dp25, dp25);
        addView(imageView, layoutParams);

        imageView.animate()
                .alpha(0)
                .translationY(0)
                .rotation(360)
                .setDuration(1000)
                .start();

        index++;
        mHandler.sendEmptyMessageDelayed(1, 1000);
        return true;
    }
}
