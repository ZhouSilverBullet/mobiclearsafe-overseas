package com.mobi.overseas.clearsafe.ui.clear.widget.dustbinview;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;

/**
 * Created by panruijie on 2017/3/6.
 * Email : zquprj@gmail.com
 */

public class DustbinDialog extends BaseDialog {

    private DustbinView mDustbinView;
    private View mContentView;

    //这个动画是否已经执行到这个地方了
    private int shakeExec;

    private ValueAnimator shakeAnimator;
    private Context mContext;

    public DustbinDialog(@NonNull Context context) {
        this(context, R.style.junk_finish_dialog);
    }

    public DustbinDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

        mContext = context;
        mContentView = LayoutInflater.from(context)
                .inflate(R.layout.layout_dustbin, null);
        mDustbinView = (DustbinView) mContentView.findViewById(R.id.dustbinView);
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);
        setContentView(mContentView);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mDustbinView.setShakeCount(100000);
        mDustbinView.startAnimation();
        mDustbinView.setAnimationListener(new DustbinView.IAnimationListener() {
            @Override
            public void onDustbinFadeIn() {

            }

            @Override
            public void onProcessRecycler() {

            }

            @Override
            public void onShakeDustbin(ValueAnimator shakeAnimator) {
                DustbinDialog.this.shakeAnimator = shakeAnimator;
                if (shakeDustbinCallback != null) {
                    shakeDustbinCallback.onShakeDustbin();
                }
            }

            @Override
            public void onDustbinFadeOut() {

            }

            @Override
            public void onAnimationFinish() {
                if (isShowing()) {
                    dismiss();
                }

            }
        });
    }


    public void cancelShakeAnimator(int duration) {
        mContentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (shakeAnimator != null) {
                    shakeAnimator.cancel();
                }
            }
        }, duration);

    }

    private IShakeDustbinCallback shakeDustbinCallback;

    public void setShakeDustbinCallback(IShakeDustbinCallback shakeDustbinCallback) {
        this.shakeDustbinCallback = shakeDustbinCallback;
    }

    public interface IShakeDustbinCallback {
        void onShakeDustbin();
    }

}
