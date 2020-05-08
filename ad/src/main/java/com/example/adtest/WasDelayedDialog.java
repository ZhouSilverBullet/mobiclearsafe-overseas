package com.example.adtest;

/**A
 * author : liangning
 * date : 2020-01-02  17:44
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * author : liangning
 * date : 2020-01-02  14:49
 */
public class WasDelayedDialog  {

    private Context mContext;
    private PopupWindow popupWindow;
    private View view;
    private WasDelayedClick delayedClick;
    private Handler handler = new Handler();

    public WasDelayedDialog(Builder builder) {
        this.mContext = builder.mContext;
        this.view = builder.view;
        this.delayedClick=builder.delayedClick;
        initPop();
    }

    private void initPop() {
        popupWindow = new PopupWindow(mContext);
        popupWindow.setContentView(LayoutInflater.from(mContext).inflate(R.layout.dialog_wasdelayed_layout, null));
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));
       // popupWindow.setFocusable(false);
        popupWindow.setTouchable(false);
        popupWindow.setOutsideTouchable(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(view, Gravity.RIGHT|Gravity.TOP, 0,dpToPx(32) );
            }
        }, 200);

//        startShakeByViewAnim(popupWindow.getContentView().findViewById(R.id.tv_btn),20f,1000);
        TextView close= popupWindow.getContentView().findViewById(R.id.tv_btn);

//        close.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.e("xxxx","点击");
//                delayedClick.btnClick(WasDelayedDialog.this);
//                return true;
//            }
//        });
//        close.setTouchClick(new ClickTextView.TouchClick() {
//            @Override
//            public void click() {
//                Log.e("xxxx","点击");
//                delayedClick.btnClick(WasDelayedDialog.this);
//            }
//        });
    }


    public static int dpToPx(float v) {
        Resources resource = Resources.getSystem();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resource.getDisplayMetrics());
    }
    private void startShakeByViewAnim(View view,float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性

        //由小变大
        //从左向右
        Animation rotateAnim = new RotateAnimation(-shakeDegrees, shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(duration / 10);
        rotateAnim.setRepeatMode(Animation.REVERSE);
        rotateAnim.setRepeatCount(10);

        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(rotateAnim);

        view.startAnimation(smallAnimationSet);
    }



    public static class Builder {
        private Context mContext;
        private View view;
        private WasDelayedClick delayedClick;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        public WasDelayedDialog build() {
            return new WasDelayedDialog(this);
        }
        public Builder setDelayedClick(WasDelayedClick delayedClick) {
            this.delayedClick = delayedClick;
            return this;
        }
    }

    public interface WasDelayedClick {
        void btnClick(WasDelayedDialog dialog);
    }
}
