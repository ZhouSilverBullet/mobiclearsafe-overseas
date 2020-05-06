package com.mobi.clearsafe.main.fragment;

import android.Manifest;
import android.content.Context;
import android.support.v4.app.ActivityCompat;

import com.mobi.clearsafe.MainActivity;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.download.MyDownLoadManager;
import com.mobi.clearsafe.eventbean.CheckTabEvent;
import com.mobi.clearsafe.main.BannerBean;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.ui.common.util.NotificationsUtils;
import com.mobi.clearsafe.ui.repair.util.PermissionUtil;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.widget.LoadWebViewActivity;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/10 16:51
 * @Dec 略
 */
public class HomeFragPresenter {

    public static final String[] PERMISSION_NEED =
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE};

    private IView mView;

    public HomeFragPresenter(IView view) {
        this.mView = view;
    }

    public void jump(int jump_type, String jump_url, String params, Object o) {
        if (jump_type == 1) {//原生
            AppUtil.startActivityFromAction(mView.getContext(), jump_url, params);
        } else if (jump_type == 2) {//H5
            if (jump_url.contains("turn3") && !jump_url.contains("scratchList")) {
                ButtonStatistical.bubbleToTurn();
            } else if (jump_url.contains("scratchList")) {
                ButtonStatistical.bubbleToScratch();
            }
            String url = jump_url
                    + "?token=" + UserEntity.getInstance().getToken()
                    + "&user_id=" + UserEntity.getInstance().getUserId()
                    + "&version=" + AppUtil.packageName(MyApplication.getContext());
            LoadWebViewActivity.IntoLoadWebView(mView.getContext(), url);
        } else if (jump_type == 3) {
            EventBus.getDefault().post(new CheckTabEvent(jump_url));
        }
    }

    public void findNeedOpenPermission() {
        //默认是开启的，不开启是用不了app
//        ActivityCompat.requestPermissions(((MainActivity) mView.getContext()),
//                ,
//                100);

        if (mView != null) {
            mView.showRightSkip(
                    PermissionUtil.enablePermission(mView.getContext(), PERMISSION_NEED) &&
                            NotificationsUtils.isNotificationEnabled());
        }
    }

    public interface IView {

        void handleBannerData(int totalDays, List<BannerBean.HotActivityList> hotActivityList, BannerBean.RedEnvelope envelope);

        Context getContext();

        /**
         * 权限是否全部打开
         *
         * @param permissionAllEnabled
         */
        void showRightSkip(boolean permissionAllEnabled);
    }

    public void detach() {
        mView = null;
    }

    public void getBanner() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getBanner(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<BannerBean>>observableIO2Main(mView.getContext()))
                .subscribe(new BaseObserver<BannerBean>() {
                    @Override
                    public void onSuccess(final BannerBean demo) {
                        if (demo != null) {

                            if (demo.hot_activity_list != null && demo.hot_activity_list.size() > 0) {
                                if (mView != null) {
                                    mView.handleBannerData(demo.total_days, demo.hot_activity_list, demo.red_envelope);
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
//                        ToastUtils.showLong(errorMsg + "（" + code + "）" + " " + e);
                    }

                });
    }
}
