package com.mobi.overseas.clearsafe.ui.clear.control;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Looper;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.example.adtest.rewardvideo.RewardVideoLoadListener;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.ui.clear.control.scan.GarbageTask;
import com.mobi.overseas.clearsafe.ui.clear.control.scan.IScanCallback;
import com.mobi.overseas.clearsafe.ui.clear.data.GarbageBean;
import com.mobi.overseas.clearsafe.ui.clear.data.GarbageHeaderBean;
import com.mobi.overseas.clearsafe.ui.clear.util.GarbageClearUtil;
import com.mobi.overseas.clearsafe.ui.common.util.SpUtil;

import java.util.ArrayList;
import java.util.List;

import static com.mobi.overseas.clearsafe.ui.clear.data.GarbageHeaderBean.TYPE_AD_GARBAGE;
import static com.mobi.overseas.clearsafe.ui.clear.data.GarbageHeaderBean.TYPE_CACHE_GARBAGE;
import static com.mobi.overseas.clearsafe.ui.clear.data.GarbageHeaderBean.TYPE_INVALID_PACKAGE;
import static com.mobi.overseas.clearsafe.ui.clear.data.GarbageHeaderBean.TYPE_SYSTEM_GARBAGE;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/8 20:31
 * @Dec 略
 */
public class GarbagePresenter {
    private final Handler mHandler;
    private IView mView;
    private GarbageTask mGarbageTask;

    private long mAllSize;


    private double percent;

    public GarbagePresenter(IView mView) {
        this.mView = mView;
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 计算一个清理的比例
     *
     * @param clearSize
     * @param allSize
     */
    public void calculateClearPercent(long clearSize, long allSize) {
        percent = clearSize / (allSize * 1.0);
    }

    /**
     * 判断是否下次进来的时候直接显示清理完毕
     * 上次清理大于50%表示清理请完毕
     */
    public void saveClearStatus() {
        if (percent >= 0.5d) {
            SpUtil.putLongCommit(Const.GARBAGE_CAN_CLEAR, System.currentTimeMillis());
        }
    }

    /**
     * @return
     */
    public boolean isCanClear() {
        return System.currentTimeMillis() - SpUtil.getLong(Const.GARBAGE_CAN_CLEAR, 0L) - Const.CURRENT_MINUTE > 0;
    }

    public void showRewardVideoAd(Activity activity) {
        new RewardVideoAd.Builder(activity)
                .setSupportDeepLink(true)
                .setScenario(ScenarioEnum.garbage_clear_reward_ad)
                .setRewardVideoLoadListener(new RewardVideoLoadListener() {
                    @Override
                    public void onAdClick(String channel) {

                    }

                    @Override
                    public void onVideoComplete(String channel) {

                    }

                    @Override
                    public void onLoadFaild(String channel, int faildCode, String faildMsg) {
                        if (mView != null) {
                            mView.showRewardVideoAdFinish();
                        }
                    }

                    @Override
                    public void onAdClose(String channel) {
                        if (mView != null) {
                            mView.showRewardVideoAdFinish();
                        }
                    }

                    @Override
                    public void onAdShow(String channel) {

                    }
                }).build();
    }

    public interface IView {
        /**
         * 开始
         */
        void onBegin();

        /**
         * 扫描到的数据
         *
         * @param info
         */
        void onProgress(GarbageBean info);

        /**
         * 扫描完成
         */
        void onFinish(long allSize, List<MultiItemEntity> garbageHeaders);

        /**
         * 扫描的path显示
         *
         * @param path
         */
        void onPath(String path);


        /**
         * 视频隐藏回调
         */
        void showRewardVideoAdFinish();
    }

    public List<String> getPackageNameList() {
        List<String> packageNameList = new ArrayList<>();
        List<ApplicationInfo> installedApps = MyApplication.INSTALLED_APPS;
        for (ApplicationInfo installedApp : installedApps) {
            packageNameList.add(installedApp.packageName);
        }
        return packageNameList;
    }

    public void execTaskGarbage() {
        mGarbageTask = new GarbageTask(new IScanCallback<GarbageBean>() {
            @Override
            public void onBegin() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.onBegin();
                        }
                    }
                });
            }

            @Override
            public void onProgress(GarbageBean info) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.onProgress(info);
                        }
                    }
                });

            }

            @Override
            public void onPath(String path) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.onPath(path);
                        }
                    }
                });
            }

            @Override
            public void onFinish() {
                mAllSize = 0;
                final List<MultiItemEntity> garbageHeaders = GarbageHeaderBean.createGarbageHeaders();
                for (MultiItemEntity garbageHeader : garbageHeaders) {
                    if (garbageHeader instanceof GarbageHeaderBean) {
                        GarbageHeaderBean headerBean = (GarbageHeaderBean) garbageHeader;
                        headerBean.isLoading = false;
                        if (headerBean.headerType == TYPE_CACHE_GARBAGE) {
                            headerBean.allSize = GarbageClearUtil.getAllSize(mGarbageTask.mCacheList);
                            mAllSize += headerBean.allSize;
                            headerBean.setSubItems(mGarbageTask.mCacheList);
                        } else if (headerBean.headerType == TYPE_SYSTEM_GARBAGE) {
                            headerBean.allSize = GarbageClearUtil.getAllSize(mGarbageTask.mSystemGarbageList);
                            mAllSize += headerBean.allSize;
                            headerBean.setSubItems(mGarbageTask.mSystemGarbageList);
                        } else if (headerBean.headerType == TYPE_AD_GARBAGE) {
                            headerBean.allSize = GarbageClearUtil.getAllSize(mGarbageTask.mAdGarbageList);
                            mAllSize += headerBean.allSize;
                            headerBean.setSubItems(mGarbageTask.mAdGarbageList);
                        } else if (headerBean.headerType == TYPE_INVALID_PACKAGE) {
                            headerBean.allSize = GarbageClearUtil.getAllSize(mGarbageTask.mUninstallList);
                            mAllSize += headerBean.allSize;
                            headerBean.setSubItems(mGarbageTask.mUninstallList);
                        }
                    }
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mView != null) {
                            mView.onFinish(mAllSize, garbageHeaders);
                        }
                    }
                });
            }
        });

        mGarbageTask.execTask();
    }

    public void detach() {
        if (mGarbageTask != null) {
            mGarbageTask.shutNowDownExecutor();
            mGarbageTask.cancel(true);
        }

        if (mView != null) {
            mView = null;
        }
    }
}
