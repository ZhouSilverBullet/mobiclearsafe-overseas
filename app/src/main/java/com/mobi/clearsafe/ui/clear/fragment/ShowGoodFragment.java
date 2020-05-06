package com.mobi.clearsafe.ui.clear.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.clear.ad.ADRelativeLayout;
import com.mobi.clearsafe.ui.clear.control.ClearPresenter;
import com.mobi.clearsafe.ui.clear.entity.CleanEvent;
import com.mobi.clearsafe.ui.clear.entity.CleanRewardBean;
import com.mobi.clearsafe.ui.clear.widget.GoodChangeLayout;
import com.mobi.clearsafe.ui.common.base.BaseFragment;
import com.mobi.clearsafe.widget.CleanGoldDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/8 16:45
 * @Dec 略
 */
public class ShowGoodFragment extends BaseFragment implements ClearPresenter.IClearView {

    @BindView(R.id.gclLayout)
    GoodChangeLayout gclLayout;
    @BindView(R.id.adRLayout1)
    ADRelativeLayout adRLayout1;
    @BindView(R.id.adRLayout2)
    ADRelativeLayout adRLayout2;
    @BindView(R.id.adRLayout3)
    ADRelativeLayout adRLayout3;
    @BindView(R.id.adRLayout4)
    ADRelativeLayout adRLayout4;
    @BindView(R.id.llAd)
    LinearLayout llAd;

    private ClearPresenter clearPresenter;
    private int cleanId;
    private boolean mIsFirstGood;
    private Handler mHandler;
    private String dec;
    private String dec2;
    private boolean isGarbageGoodShowAd;

    public static ShowGoodFragment newInstance(boolean mIsFirstGood) {

        Bundle args = new Bundle();
        args.putBoolean("isFirstGood", mIsFirstGood);
        ShowGoodFragment fragment = new ShowGoodFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getFragmentView() {
        return R.layout.fragment_show_good;
    }

    @Override
    protected void initVariables() {
        if (getArguments() != null) {
            mIsFirstGood = getArguments().getBoolean("isFirstGood");
        }
        if (getActivity() != null) {
            cleanId = getActivity().getIntent().getIntExtra("cleanId", 0);
            dec = getActivity().getIntent().getStringExtra("dec");
            dec2 = getActivity().getIntent().getStringExtra("dec2");
            mIsFirstGood = getActivity().getIntent().getBooleanExtra("isFirstGood", false);

            isGarbageGoodShowAd = getActivity().getIntent().getBooleanExtra("isGarbageGoodShowAd", false);
        }
    }

    @Override
    protected void initView() {
        if (TextUtils.isEmpty(dec) && TextUtils.isEmpty(dec2)) {
            gclLayout.setTvDec("已清理");
            gclLayout.setTvDec2("磁盘性能良好");
        } else {
            gclLayout.setTvDec(dec);
            gclLayout.setTvDec2(dec2);
        }

        gclLayout.setVisibility(View.VISIBLE);

//        gclLayout.post(() -> {
////            if (getArguments() != null) {
//
////            }
//
//        });
        gclLayout.setGarbageGoodShowAd(isGarbageGoodShowAd);
        gclLayout.setFirstGood(mIsFirstGood);
        gclLayout.post(() -> {
//            gclLayout.initAnimator();
            gclLayout.startAnim();
        });

        gclLayout.setGoodChangeListener(() -> {
            requestClearGarbage();
            addAdLocation(llAd);

            EventBus.getDefault().post(new CleanEvent());
        });

        gclLayout.setAdRootView(mRootView.findViewById(R.id.adslContainer));
    }

    @Override
    protected void initData() {
        super.initData();
        clearPresenter = new ClearPresenter(this);
    }

    private void requestClearGarbage() {
        if (clearPresenter != null) {
            clearPresenter.requestClearGarbage(cleanId, (AppCompatActivity) getActivity());
        }
    }

    @Override
    public void clearGarbage(CleanRewardBean data) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        CleanGoldDialog dialog = new CleanGoldDialog.Builder(getActivity())
                .setGold(data.getPoints())
                .setDialogClick(new CleanGoldDialog.GetGoldDialogClick() {
                    @Override
                    public void closeBtnClick(Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build();
        dialog.show();
    }

    /**
     * 添加广告位
     */
    private void addAdLocation(LinearLayout llAd) {
        if (llAd != null) {
            llAd.setVisibility(View.VISIBLE);
            for (int i = 0; i < llAd.getChildCount(); i++) {
                View childAt = llAd.getChildAt(i);
                if (childAt instanceof ADRelativeLayout) {
                    ((ADRelativeLayout) childAt).showAdLayout();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (clearPresenter != null) {
            clearPresenter.detach();
        }
    }

    public boolean isFirstGood() {
        return mIsFirstGood;
    }

    public void setFirstGood(boolean isFirstGood) {
        mIsFirstGood = isFirstGood;
    }

}
