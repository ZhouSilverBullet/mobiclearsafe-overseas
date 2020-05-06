package com.mobi.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.annotation.NonNull;

import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.nativeexpress.NativeExpressAd;
import com.example.adtest.nativeexpress.NativeInteractionTypeListener;
import com.example.adtest.nativeexpress.NativeLoadListener;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;
import com.mobi.clearsafe.utils.UiUtils;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 信息流伪装插屏
 * author : liangning
 * date : 2019-12-30  14:55
 */
public class InterActionDialog extends BaseDialog {

    private Context mContext;
    private RelativeLayout rl_ad;
    private NativeExpressAd ad;
    private ClickTextView time;
    private ClickImageView close;
    private ImageView iv_light;
    private Handler handler = new Handler();
    private boolean h1fg = false;//false no true is
    private TextView tv_times;
    private String POSID;
    private ScenarioEnum scenarioEnum;
    private InterActionDialogListener mListener;
    private int InteractionType = -1;//穿山甲广告类型 2在浏览器内打开 （普通类型）3落地页（普通类型），4:应用下载，5:拨打电话 -1 未知类型
    private boolean percenStatus = false;
    private  ImageView iv_close;

    public InterActionDialog(@NonNull Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.h1fg = builder.h1fg;
        this.POSID = builder.POSID;
        this.scenarioEnum = builder.scenarioEnum;
        this.mListener = builder.mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_interaction_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initWindow();
        initView();
    }

    private void initView() {
        rl_ad = findViewById(R.id.rl_ad);
        tv_times = findViewById(R.id.tv_times);
        iv_light = findViewById(R.id.iv_light);
        iv_close=findViewById(R.id.iv_close);
        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_light);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        iv_light.startAnimation(operatingAnim);
        ad = new NativeExpressAd.Builder(mContext)
                .setSupportDeepLink(true)
                .setBearingView(rl_ad)
                .setHeightAuto(true)
                .setADViewSize(300, 0)
                .setNativeLoadListener(new NativeLoadListener() {
                    @Override
                    public void onAdClick(String channel) {

                    }

                    @Override
                    public void onLoadFaild(String channel, int faildCode, String faildMsg) {
                        if (mListener != null) {
                            mListener.onFailure();
                        }
                    }

                    @Override
                    public void onAdDismissed(String channel) {

                    }

                    @Override
                    public void onAdRenderSuccess(String channel) {

                    }

                    @Override
                    public void onAdShow(String channel) {

                    }
                })
                .setInteractionTypeListener(new NativeInteractionTypeListener() {
                    @Override
                    public void InteractionType(int type) {
                        InteractionType = type;
                        getPerce(InteractionType);
                        setAddViewClick();
                    }

                    @Override
                    public void error() {
                        iv_close.setVisibility(View.VISIBLE);
                        iv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dismiss();
                            }
                        });
                    }
                })
                .setScenario(scenarioEnum)
                .setPosID(POSID)
                .build();
        countdown();
//        time = new ClickTextView(mContext);
//        time.setTouchClick(new ClickTextView.TouchClick() {
//            @Override
//            public void click() {
////                dismiss();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        dismiss();
//                    }
//                }, 300);
//            }
//        });
//        time.setText("倒计时3秒");
//        time.setTextColor(mContext.getResources().getColor(R.color.white));
//        time.setBackgroundResource(R.drawable.shape_inter_circle);
//        time.setPadding(12, 8, 12, 8);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        //lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);//与父容器的左侧对齐
//        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);//与父容器的上侧对齐
//
////        lp.leftMargin=30;
////
//        lp.topMargin = 30;
//
////        time.setId(R.id.text_view_1);//设置这个View 的id
//
//        time.setLayoutParams(lp);//设置布局参数

//        time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//        int size = rl_ad.getChildCount();
//        for (int i = 0;i<size;i++){
//            View view = rl_ad.getChildAt(i);
//            Log.e("ViewID",String.valueOf(view.getId()));
//        }
    }

    private void getPerce(int interType) {
        if (interType == 4) {//下载类型 不做处理
            percenStatus = true;
        } else {
            int num = (int) (100 * Math.random());
            if (num < UserEntity.getInstance().getConfigEntity().getHc1g()) {
                percenStatus = true;
            }else {
                percenStatus = false;
            }
        }
    }

    private void addViews() {
        close = new ClickImageView(mContext);
        close.setImageResource(R.mipmap.interaction_dialog_close);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(UiUtils.dp2px(mContext, 22), UiUtils.dp2px(mContext, 22));
        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.leftMargin = UiUtils.dp2px(mContext, 8);
        lp.topMargin = UiUtils.dp2px(mContext, 8);
        close.setLayoutParams(lp);
        rl_ad.addView(close);
        getPerce(InteractionType);
        setAddViewClick();
//        if (h1fg) {
//            close.setTouchClick(new ClickImageView.ImageTouchClick() {
//                @Override
//                public void ImageClick() {
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mListener != null) {
//                                mListener.close();
//                            }
//                            dismiss();
//                        }
//                    }, 600);
//                }
//            });
//        } else {
//            close.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//                        mListener.close();
//                    }
//                    dismiss();
//                }
//            });
//        }
    }

    private void setAddViewClick() {
        if (close != null) {
            if (h1fg && percenStatus) {
                close.setTouchClick(new ClickImageView.ImageTouchClick() {
                    @Override
                    public void ImageClick() {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mListener != null) {
                                    mListener.close();
                                }
                                dismiss();
                            }
                        }, 600);
                    }
                });
            } else {
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.close();
                        }
                        dismiss();
                    }
                });
            }
        }
    }


    private void countdown() {
        Observable.intervalRange(0, 4, 0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return 4 - aLong - 1;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        tv_times.setText(Html.fromHtml(mContext.getResources().getString(R.string.interaction_times, String.valueOf(aLong))));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        tv_times.setVisibility(View.GONE);
                        addViews();
                    }
                });
    }

    private void initWindow() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.c_D9000000);
        WindowManager.LayoutParams lps = dialogWindow.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels > dm.heightPixels ? dm.heightPixels
                : dm.widthPixels;
        int h = dm.widthPixels > dm.heightPixels ? dm.widthPixels
                : dm.heightPixels;
        lps.width = w;
        lps.height = h;
//        lps.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lps);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (ad != null) {
            ad.destory();
        }
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }

    public static class Builder {

        private Context mContext;
        private boolean h1fg;
        private String POSID;
        private ScenarioEnum scenarioEnum;
        private InterActionDialogListener mListener;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setH1fg(boolean h1fg) {
            this.h1fg = h1fg;
            return this;
        }

        public Builder setPOSID(String POSID) {
            this.POSID = POSID;
            return this;
        }

        public Builder setEnum(ScenarioEnum scenarioEnum) {
            this.scenarioEnum = scenarioEnum;
            return this;
        }

        public Builder setListener(InterActionDialogListener listener) {
            this.mListener = listener;
            return this;
        }

        public InterActionDialog build() {
            return new InterActionDialog(this);
        }
    }

    public interface InterActionDialogListener {
        void close();

        /**
         * 垃圾清理的时候，串行执行使用
         */
        default void onFailure() {

        }
    }

}
