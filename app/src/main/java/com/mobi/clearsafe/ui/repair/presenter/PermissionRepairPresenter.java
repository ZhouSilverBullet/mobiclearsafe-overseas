package com.mobi.clearsafe.ui.repair.presenter;

import android.app.Activity;
import android.content.Context;

import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.ui.clear.entity.CleanRewardBean;
import com.mobi.clearsafe.ui.common.mvp.BasePresenter;
import com.mobi.clearsafe.ui.common.mvp.IMvpView;
import com.mobi.clearsafe.ui.repair.data.CleanAuthBean;
import com.mobi.clearsafe.ui.repair.data.CleanAuthOpenBean;
import com.mobi.clearsafe.ui.repair.data.CleanAuthOutBean;
import com.mobi.clearsafe.ui.repair.data.PermissionRepairBean;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/17 11:47
 * @Dec 略
 */
public class PermissionRepairPresenter extends BasePresenter<PermissionRepairPresenter.IView> {


    public void transformBean(PermissionRepairBean permissionRepairBean, CleanAuthBean cleanAuthBean) {
        if (permissionRepairBean == null || cleanAuthBean == null) {
            return;
        }
        permissionRepairBean.setPoints(cleanAuthBean.getPoints());
        permissionRepairBean.setState(cleanAuthBean.getState());
        permissionRepairBean.setPopUpMsg(cleanAuthBean.getPopUpMsg());
        permissionRepairBean.setPopType(cleanAuthBean.getPopType());
        permissionRepairBean.setLoaded(true);
        permissionRepairBean.setId(cleanAuthBean.getId());
    }

    public interface IView extends IMvpView {

        /**
         * 显示 金币
         *
         * @param bean
         */
        void showCleanOutAuth(CleanAuthOutBean bean);

        /**
         * 领取金币奖励
         *
         * @param bean
         */
        void showCleanAuthOpen(CleanAuthOpenBean bean);

    }

    public void getCleanAuthList() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getCleanAuthList(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<CleanAuthOutBean>>observableIO2Main(mView.getContext()))
                .subscribe(new BaseObserver<CleanAuthOutBean>() {
                    @Override
                    public void onSuccess(CleanAuthOutBean data) {
                        if (data != null) {
                            if (mView != null) {
                                mView.showCleanOutAuth(data);
                            }
//                            handleTypeAndData(type, data);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        if ("1016".equals(code)) {
                            return;
                        }
                        ToastUtils.showShort(errorMsg);
                    }
                });
    }

    public void postCleanAuthOpen(int id) {
        if (mView == null) {
            return;
        }
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .postCleanAuthOpen(UserEntity.getInstance().getUserId(), id)
                .compose(CommonSchedulers.<BaseResponse<CleanAuthOpenBean>>observableIO2Main(mView.getContext()))
                .subscribe(new BaseObserver<CleanAuthOpenBean>() {
                    @Override
                    public void onSuccess(CleanAuthOpenBean data) {
                        if (data != null) {
                            if (mView != null) {
                                mView.showCleanAuthOpen(data);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        if ("1016".equals(code)) {
                            return;
                        }
                        ToastUtils.showShort(errorMsg);
                    }
                });
    }
}
