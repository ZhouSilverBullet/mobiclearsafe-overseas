package com.mobi.clearsafe.ui.clear.control;

import android.support.v7.app.AppCompatActivity;

import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.ui.clear.entity.CleanRewardBean;
import com.mobi.clearsafe.utils.ToastUtils;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/24 20:05
 * @Dec 略
 */
public class ClearPresenter {
    public static final int CLEAR_QQ = 1;
    public static final int CLEAR_WECHAT = 2;
    public static final int CLEAR_GARBAGE = 3;
    public static final int CLEAR_POWER_COOL = 4;
    public static final int CLEAR_SPEED_PHONE = 5;

    public static final int CLEAR_APK = 6;
    public static final int CLEAR_BIG_FILE = 7;

    private IClearView clearView;

    public ClearPresenter(IClearView clearView) {
        this.clearView = clearView;
    }

    public void requestClearQQ(int cleanId, AppCompatActivity activity) {
        requestClear(CLEAR_QQ, cleanId, activity);
    }

    public void requestClearWechat(int cleanId, AppCompatActivity activity) {
        requestClear(CLEAR_WECHAT, cleanId, activity);
    }

    public void requestClearGarbage(int cleanId, AppCompatActivity activity) {
        requestClear(CLEAR_GARBAGE, cleanId, activity);
    }

    public void requestClearPowerCool(int cleanId, AppCompatActivity activity) {
        requestClear(CLEAR_POWER_COOL, cleanId, activity);
    }

    public void requestClearSpeedPhone(int cleanId, AppCompatActivity activity) {
        requestClear(CLEAR_SPEED_PHONE, cleanId, activity);
    }

    public void requestClearApk(int cleanId, AppCompatActivity activity) {
        requestClear(CLEAR_APK, cleanId, activity);
    }

    public void requestClearBigFile(int cleanId, AppCompatActivity activity) {
        requestClear(CLEAR_BIG_FILE, cleanId, activity);
    }

    public void requestClear(int type, int cleanId, AppCompatActivity activity) {
        if (cleanId <= 0) {
            return;
        }
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .postCleanReward(cleanId)
                .compose(CommonSchedulers.<BaseResponse<CleanRewardBean>>observableIO2Main(activity))
                .subscribe(new BaseObserver<CleanRewardBean>() {
                    @Override
                    public void onSuccess(CleanRewardBean data) {
                        if (data != null) {
                            handleTypeAndData(type, data);
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

    private void handleTypeAndData(int type, CleanRewardBean data) {
        switch (type) {
            case CLEAR_QQ:
                if (clearView != null) {
                    clearView.clearQQ(data);
                }
                break;
            case CLEAR_WECHAT:
                if (clearView != null) {
                    clearView.clearWechat(data);
                }
                break;
            case CLEAR_GARBAGE:
                if (clearView != null) {
                    clearView.clearGarbage(data);
                }
                break;
            case CLEAR_POWER_COOL:
                if (clearView != null) {
                    clearView.clearPowerCool(data);
                }
                break;
            case CLEAR_SPEED_PHONE:
                if (clearView != null) {
                    clearView.clearSpeedPhone(data);
                }
                break;
        }
    }

    public interface IClearView {
        default void clearQQ(CleanRewardBean data) {

        }

        default void clearWechat(CleanRewardBean data) {

        }

        default void clearGarbage(CleanRewardBean data) {

        }

        default void clearPowerCool(CleanRewardBean data) {

        }

        default void clearSpeedPhone(CleanRewardBean data) {

        }
    }

    public void detach() {
        if (clearView != null) {
            clearView = null;
        }
    }
}
