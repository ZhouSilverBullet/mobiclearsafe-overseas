package com.mobi.overseas.clearsafe.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import android.support.annotation.Nullable;


/**
 * author : liangning
 * date : 2019-12-30  16:14
 */
public class ClickTextView extends AppCompatTextView {

    private TouchClick click;
    public ClickTextView(Context context) {
        super(context);
    }

    public ClickTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (click!=null){
            click.click();
        }
        return super.onTouchEvent(event);
    }

    public void setTouchClick(TouchClick click1){
        click = click1;
    }

    public interface TouchClick{
        void click();
    }

}
