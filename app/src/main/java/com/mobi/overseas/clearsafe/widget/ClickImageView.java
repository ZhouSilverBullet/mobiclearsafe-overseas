package com.mobi.overseas.clearsafe.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * author : liangning
 * date : 2020-01-14  16:04
 */
public class ClickImageView extends AppCompatImageView {

    private ImageTouchClick mClick;

    public ClickImageView(Context context) {
        super(context);
    }

    public ClickImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mClick!=null){
            mClick.ImageClick();
        }
        return super.onTouchEvent(event);
    }

    public void setTouchClick(ImageTouchClick click){
        this.mClick = click;
    }

    public interface ImageTouchClick{
        void ImageClick();
    }
}
