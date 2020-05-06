package com.mobi.clearsafe.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.mobi.clearsafe.R;

/**
 * author : liangning
 * date : 2019-11-14  17:31
 */
public class LoadingTips extends LinearLayout {

    private ImageView iv_icon;
    private TextView tv_tips;
    private TextView tv_loading;
    private TextView tv_reloadbtn;
    private LinearLayout ll_reload,ll_loading;
    private ProgressBar loading;
    private onReloadListener mListener;
    private boolean isSetTips = false, isSetImg = false;

    public LoadingTips(Context context) {
        super(context);
        initView(context);
    }

    public LoadingTips(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingTips(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingTips(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    /**
     * netError 网络错误
     * empty 数据为空
     * loading 加载中
     * finish 结束所有状态
     */
    public static enum LoadStatus {
        netError, empty,loading,finish
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_loadingtips_layout, this);
        tv_reloadbtn = findViewById(R.id.tv_reloadbtn);
        tv_loading = findViewById(R.id.tv_loading);
        ll_loading = findViewById(R.id.ll_loading);
        iv_icon = findViewById(R.id.iv_icon);
        tv_tips = findViewById(R.id.tv_tips);
        ll_reload = findViewById(R.id.ll_reload);
        tv_reloadbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.reload();
                }
            }
        });

        setVisibility(GONE);
    }

    public void setTips(String tips) {
        if (tv_tips != null) {
            tv_tips.setText(tips);
            isSetTips = true;
        }
    }

    public void setImage(int id) {
        if (iv_icon != null) {
            iv_icon.setImageResource(id);
            isSetImg = true;
        }
    }

    public void setOnReloadListener(onReloadListener listener) {
        mListener = listener;
    }

    public void setLoadingTip(LoadStatus loadStatus) {
        switch (loadStatus) {
            case empty:
                setVisibility(VISIBLE);
                ll_loading.setVisibility(GONE);
                ll_reload.setVisibility(VISIBLE);
                tv_reloadbtn.setVisibility(GONE);
//                iv_icon.setVisibility(VISIBLE);
//                tv_tips.setVisibility(VISIBLE);
                if (!isSetTips) {
                    tv_tips.setText(getContext().getResources().getString(R.string.empty));
                }
                if (!isSetImg){
                    iv_icon.setImageResource(R.mipmap.empty_data_img);
                }
                break;
            case netError:
                setVisibility(VISIBLE);
                tv_reloadbtn.setVisibility(VISIBLE);
                ll_loading.setVisibility(GONE);
                ll_reload.setVisibility(VISIBLE);
//                iv_icon.setVisibility(VISIBLE);
//                tv_tips.setVisibility(VISIBLE);
                if (!isSetTips){
                    tv_tips.setText(getContext().getResources().getString(R.string.net_error));
                }
                if (!isSetImg){
                    iv_icon.setImageResource(R.mipmap.net_error_img);
                }
                break;
            case loading:
                setVisibility(VISIBLE);
                ll_loading.setVisibility(VISIBLE);
                ll_reload.setVisibility(GONE);
                mListener=null;
                break;
            case finish:
                setVisibility(GONE);
                break;
        }
    }

    public interface onReloadListener {
        void reload();
    }

}
