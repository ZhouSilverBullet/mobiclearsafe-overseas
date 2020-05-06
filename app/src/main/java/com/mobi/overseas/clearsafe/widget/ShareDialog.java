package com.mobi.overseas.clearsafe.widget;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.moneyactivity.EwmActivity;
import com.mobi.overseas.clearsafe.moneyactivity.bean.ShareContentBean;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.DialogUtils;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.wxapi.WeixinHandler;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;

/**
 * author:zhaijinlu
 * date: 2019/12/5
 * desc: 分享弹框
 */
public class ShareDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "ShareDialog";

    private ShareContentBean mShareEntity;
    private int width;
    private int height;
    private ClickListener listener;

    public static ShareDialog getInstance(ShareContentBean shareEntity,ClickListener listener) {
        ShareDialog shareFragment = new ShareDialog();
        shareFragment.mShareEntity = shareEntity;
        shareFragment.listener=listener;
        return shareFragment;
    }

    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(width, height);
        window.setGravity(Gravity.BOTTOM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.share_dialog_layout, container, false);
        view.findViewById(R.id.share_to_weixin_friends).setOnClickListener(this);
        view.findViewById(R.id.share_to_weixin_timeline).setOnClickListener(this);
        view.findViewById(R.id.share_to_code).setOnClickListener(this);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        width = MyApplication.getContext().getResources()
                .getDisplayMetrics().widthPixels;
        height = AppUtil.dpToPx(220);
        return view;
    }

    private void doShare(boolean toTimeline) {
        if (!WeixinHandler.getInstance().isWeixinInstalled()) {
            ToastUtils.showShort(R.string.wechat_login_tip);
            return;
        }
        WeixinHandler.getInstance().shareToWeixin(mShareEntity.getName(),mShareEntity.getIntroduction(),mShareEntity.getJump_address()+ "?code=" + mShareEntity.getCode()+ "?user_id=" + UserEntity.getInstance().getUserId(),R.mipmap.icon_round,toTimeline);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_to_weixin_friends:
                doShare(true);
                listener.onClick();
                finish();
                break;
            case R.id.share_to_weixin_timeline:
                doShare(false);
                listener.onClick();
                finish();
                break;
            case R.id.share_to_code:
//                if(mShareEntity!=null){
//                    Intent intent = new Intent(getActivity(), EwmActivity.class);
//                    intent.putExtra("url",mShareEntity.getJump_address()+ "?code=" + mShareEntity.getCode());
//                    startActivity(intent);
//                }
//                listener.onClick();
//                finish();
                break;
            case R.id.iv_close:
                listener.onClick();
                finish();
                break;

        }
    }


    private void finish() {
        DialogUtils.dismissDialog(getFragmentManager(), TAG);
    }

   public interface ClickListener{
        void onClick();
    }
}
