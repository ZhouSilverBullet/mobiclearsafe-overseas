package com.mobi.overseas.clearsafe.main.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.base.adapter.CommonAdapter;
import com.mobi.overseas.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.overseas.clearsafe.download.MyDownLoadManager;
import com.mobi.overseas.clearsafe.main.bean.IntegralBean;
import com.mobi.overseas.clearsafe.main.bean.IntegralItemBean;
import com.mobi.overseas.clearsafe.main.bean.IntegralWallReceiveBean;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.FileUtils;
import com.mobi.overseas.clearsafe.utils.LogUtils;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.utils.imageloader.ImageLoader;
import com.mobi.overseas.clearsafe.widget.GetGoldDialog;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2019-12-26  14:34
 */
public class IntegralWallActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static void IntoIntegral(Context context) {
        Intent intent = new Intent(context, IntegralWallActivity.class);
        context.startActivity(intent);
    }

    private RecyclerView rv_install;
    private RecyclerView rv_start;

    private CommonAdapter<IntegralItemBean> installAdapter;
    private CommonAdapter<IntegralItemBean> startAdapter;

    private TextView tv_gold_num;
    private TextView tv_step_one, tv_step_two;
    private LinearLayout ll_back;
    private LinearLayout ll_jiangli;
    private List<IntegralItemBean> installList = new ArrayList<>();
    private List<IntegralItemBean> startList = new ArrayList<>();
    private int DLSelectIndex = 0;//下载选中的下标
    private IntegralItemBean jumpBean = null;
    private boolean isDownLoading = false;
    private long interval_long = 5000;//试玩多久算有效
    private long start_time = 0;
    private LinearLayout ll_test, ll_install;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integrallwall_layout);
        MyDownLoadManager.initInstalledAPP(this);
        ButtonStatistical.numberWall();
        initView();
        requestData();

    }

    private void initView() {
        ll_test = findViewById(R.id.ll_test);
        ll_install = findViewById(R.id.ll_install);
        rv_install = findViewById(R.id.rv_install);
        rv_start = findViewById(R.id.rv_start);
        tv_gold_num = findViewById(R.id.tv_gold_num);
        tv_step_one = findViewById(R.id.tv_step_one);
        tv_step_one.setOnClickListener(this);
        tv_step_two = findViewById(R.id.tv_step_two);
        tv_step_two.setOnClickListener(this);
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        ll_jiangli = findViewById(R.id.ll_jiangli);
        installAdapter = new CommonAdapter<IntegralItemBean>(this, R.layout.item_integral_install_layout, installList) {
            @Override
            public void convert(final RecycleViewHolder holder, final IntegralItemBean item) {
                ImageView iv_app_icon = holder.getView(R.id.iv_app_icon);
                ImageLoader.loadRoundImage(IntegralWallActivity.this, iv_app_icon, item.icon_src, 12);
                RelativeLayout rv_bg = holder.getView(R.id.rv_bg);
                holder.setText(R.id.tv_app_name, item.name);
                TextView tv_gold = holder.getView(R.id.tv_gold);
                tv_gold.setText(getResources().getString(R.string.how_gold, String.valueOf(item.points)));
                LinearLayout ll_item = holder.getView(R.id.ll_item);
                if (DLSelectIndex == holder.getAdapterPosition()) {
                    rv_bg.setBackgroundResource(R.drawable.integral_item_select);
                } else {
                    rv_bg.setBackgroundResource(R.drawable.integral_item_unselect);
                }
                ll_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DLSelectIndex = holder.getAdapterPosition();
                        tv_gold_num.setText(getResources().getString(R.string.how_gold, String.valueOf(item.points)));
                        CheckInstallTaskItem(installList.get(DLSelectIndex));
                        notifyDataSetChanged();
                    }
                });
            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_install.setLayoutManager(gridLayoutManager);
        rv_install.setAdapter(installAdapter);
        startAdapter = new CommonAdapter<IntegralItemBean>(this, R.layout.item_integral_start_layout, startList) {
            @Override
            public void convert(RecycleViewHolder holder, final IntegralItemBean item) {
                ImageView iv_app_icon = holder.getView(R.id.iv_app_icon);
                ImageLoader.loadRoundImage(IntegralWallActivity.this, iv_app_icon, item.icon_src, 12);
                holder.setText(R.id.tv_title, item.name);
                holder.setText(R.id.tv_content, item.introduce != null ? item.introduce : "");
                TextView tv_gold = holder.getView(R.id.tv_gold);
                tv_gold.setText(getResources().getString(R.string.how_gold, String.valueOf(item.points)));
                TextView tv_start = holder.getView(R.id.tv_start);
                LinearLayout ll_canget = holder.getView(R.id.ll_canget);
                TextView tv_finished = holder.getView(R.id.tv_finished);
                if (item.is_gain == 0) {
                    ll_canget.setVisibility(View.VISIBLE);
                    tv_finished.setVisibility(View.GONE);
                } else {
                    ll_canget.setVisibility(View.GONE);
                    tv_finished.setVisibility(View.VISIBLE);
                }
                tv_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppUtil.startAPPforDeepLink(getActivity(), item.deep_link_url)) {
                            if (item.is_gain == 0) {
                                jumpBean = item;
                            }
                        } else {

                        }
                    }
                });
            }
        };
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_start.setLayoutManager(linearLayoutManager);
        rv_start.setAdapter(startAdapter);
    }

    private void requestData() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getIntegralList(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, Const.INTEGRAL_ACTIVITY_ID)
                .compose(CommonSchedulers.<BaseResponse<IntegralBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<IntegralBean>() {
                    @Override
                    public void onSuccess(IntegralBean demo) {
                        if (demo != null) {
//                            installList = new ArrayList<>();
//                            startList = new ArrayList<>();
                            if (demo.download != null) {
                                installList.addAll(demo.download);

                            }
                            if (demo.play_test != null) {
                                ScreeningNoInstall(demo.play_test);
                            }
                            installAdapter.notifyDataSetChanged();
                            startAdapter.notifyDataSetChanged();
                            if (startList.size() > 0) {
                                ll_test.setVisibility(View.VISIBLE);
                            } else {
                                ll_test.setVisibility(View.GONE);
                            }
                            if (installList.size() > 0) {
                                ll_install.setVisibility(View.VISIBLE);
                            } else {
                                ll_install.setVisibility(View.GONE);
                            }
                            if (installList != null && installList.size() > 0) {
                                if (installList.size() - 1 >= DLSelectIndex) {

                                } else {
                                    DLSelectIndex = 0;
                                }
                                IntegralItemBean itemBean = installList.get(DLSelectIndex);
                                tv_gold_num.setText(getResources().getString(R.string.how_gold, String.valueOf(itemBean.points)));
                                CheckInstallTaskItem(itemBean);
                            }
                        }


                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        LogUtils.e("请求失败");
                    }
                });


    }

    /**
     * 试玩的任务 如果本地没有安装 则不显示
     */
    private void ScreeningNoInstall(List<IntegralItemBean> list) {
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                IntegralItemBean item = list.get(i);
                if (AppUtil.checkAppInstalled(IntegralWallActivity.this, item.package_name)) {
                    startList.add(item);
                }
            }
        }

        startAdapter.notifyDataSetChanged();
    }

    /**
     * 判断当前选中的下载任务 是否已安装 任务是否已完成
     *
     * @param item
     */
    private void CheckInstallTaskItem(final IntegralItemBean item) {
        if (item != null) {
            if (AppUtil.checkAppInstalled(this, item.package_name)) {
                //APP已安装
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUtils.deleteFile(getFilePath(item.package_name));
                    }
                }).start();

                tv_step_one.setText(getResources().getString(R.string.finished_string));
                tv_step_one.setBackgroundResource(R.drawable.shape_corner_14dp_d3d3d3);
                if (item.is_gain == 0) {
                    //未领取
                    tv_step_two.setBackgroundResource(R.drawable.shape_corner_14dp_4595ff);
                    ll_jiangli.setVisibility(View.VISIBLE);
                } else {
                    //已领取
                    tv_step_two.setBackgroundResource(R.drawable.shape_corner_14dp_4595ff);
                    ll_jiangli.setVisibility(View.INVISIBLE);
                }
            } else {
                //未安装
                if (checkDownloading(item)) {//当前正在下载
                    isDownLoading = true;
                    tv_step_one.setText(getResources().getString(R.string.downloading));
                } else {
                    if (checkDownLoad(item)) {//已下载 未安装
                        tv_step_one.setText(getResources().getString(R.string.install_string));
                    } else {
                        tv_step_one.setText(getResources().getString(R.string.download_string));
                    }
                }
                tv_step_one.setBackgroundResource(R.drawable.shape_corner_14dp_4595ff);
                tv_step_two.setBackgroundResource(R.drawable.shape_corner_14dp_d3d3d3);
                if (item.is_gain == 0) {
                    //未领取
                    ll_jiangli.setVisibility(View.VISIBLE);
                } else {
                    //已领取
                    ll_jiangli.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkRecive();
        refreshBtn();
    }

    @Override
    protected void onStop() {
        super.onStop();
        setStartTime();
    }

    /**
     * 设置按钮状态
     */
    private void refreshBtn() {
        if (isDownLoading) {
            if (installList != null && installList.size() - 1 >= DLSelectIndex) {
                IntegralItemBean item = installList.get(DLSelectIndex);
                if (checkDownloading(item)) {
                    //下载中
                } else {
                    CheckInstallTaskItem(item);
                }
            }
        }
    }

    /**
     * 判断当前选中的是否在下载中
     *
     * @return
     */
    private boolean checkDownloading(IntegralItemBean bean) {
        if (MyDownLoadManager.downloadMap != null) {
            return MyDownLoadManager.downloadMap.containsValue(bean.package_name);
        } else {
            return false;
        }
    }

    /**
     * 检查是否需要请求领取接口
     */
    private void checkRecive() {
        if (jumpBean != null) {
            if (start_time > 0 && System.currentTimeMillis() - start_time >= interval_long) {
//                LogUtils.e("时间符合");
                if (jumpBean.type == 1) {//下载
//                    LogUtils.e("是下载任务");
                    boolean installMe = false;
                    List<String> sList = MyDownLoadManager.getInstalledApp();
                    int size = sList.size();
//                    LogUtils.e("存储的列表长度"+size);
//                    LogUtils.e("存储的列表"+ JSON.toJSONString(sList));
                    for (int i = 0; i < size; i++) {
                        if (sList.get(i).equals(jumpBean.package_name)) {
                            installMe = true;
                            break;
                        }
                    }
                    if (installMe) {
//                        LogUtils.e("是在本APP下载");
                        requestReceive(jumpBean.app_id,jumpBean.type);
                    }else {
//                        LogUtils.e("非本APP下载");
                    }
                } else {//试玩
                    requestReceive(jumpBean.app_id,jumpBean.type);
                }
            }
        }
    }

    private void setStartTime() {
        start_time = System.currentTimeMillis();
    }

    /**
     * 检测当前选中的APP是否已下载
     */
    private boolean checkDownLoad(IntegralItemBean bean) {
        String path = getFilePath(bean.package_name);
        return FileUtils.checkFileExists(path);
    }

    /**
     * 获取文件存储地址
     *
     * @param pkgName
     * @return
     */
    private String getFilePath(String pkgName) {
        String filePath = "";
        if (pkgName == null) {
            return "";
        }
        try {
            filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath()
                    + "/" + pkgName.replace(".", "_") + ".apk";
        } catch (Exception e) {

        }
        return filePath;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        MyDownLoadManager.unRegisterBroadCast(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_step_one://下载按钮
                if (installList != null && installList.size() - 1 >= DLSelectIndex) {
                    IntegralItemBean bean = installList.get(DLSelectIndex);
                    if (!AppUtil.checkAppInstalled(this, bean.package_name)) {
                        //未安装
                        if (checkDownloading(bean)) {
                            //下载中，点击无效
                        } else {
                            //不在下载中的状态
                            if (checkDownLoad(bean)) {
                                //安装包已经下载过
                                isDownLoading = true;
                                AppUtil.installAPP(getActivity(), getFilePath(bean.package_name));
                            } else {
                                //未下载过
                                if (MyDownLoadManager.downLoadApk(IntegralWallActivity.this, bean.download_url, bean.package_name,bean.name)) {
                                    ToastUtils.showShort(getResources().getString(R.string.is_start_download));
                                    tv_step_one.setText(getResources().getString(R.string.downloading));
                                    isDownLoading = true;
                                }
                            }
                        }
                    }
                }
                break;
            case R.id.tv_step_two://打开按钮
                if (installList != null && installList.size() - 1 >= DLSelectIndex) {
                    IntegralItemBean bean = installList.get(DLSelectIndex);
                    if (AppUtil.checkAppInstalled(this, bean.package_name)) {
                        try {
                            Intent intent = getPackageManager().getLaunchIntentForPackage(bean.package_name);
                            startActivity(intent);
                            if (bean.is_gain == 0) {//如果是未领取状态
                                jumpBean = bean;
                            }
//                            requestReceive(bean.app_id);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showShort(getResources().getString(R.string.check_install, bean.name));
                        }

                    }
                }
                break;
        }
    }

    /**
     * 请求领取
     */
    private void requestReceive(int appID, final int type) {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .receiveIntegralWall(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, Const.INTEGRAL_ACTIVITY_ID, appID)
                .compose(CommonSchedulers.<BaseResponse<IntegralWallReceiveBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<IntegralWallReceiveBean>() {
                    @Override
                    public void onSuccess(IntegralWallReceiveBean demo) {
                        if (demo != null) {
                            jumpBean = null;
                            UserEntity.getInstance().setCash((float) demo.cash);
                            UserEntity.getInstance().setPoints(demo.total_points);
                            installList.clear();
                            startList.clear();
                            requestData();
                            if (type==1){
                                //下载
                                ButtonStatistical.installAndopen();
                            }else {
                                //试玩
                                ButtonStatistical.open();
                            }
//                            if (demo.pop_type == 1000) {
                                GetGoldDialog dialog = new GetGoldDialog.Builder(getActivity())
                                        .isVisible(false)
                                        .setBottomBtnVisible(false)
                                        .setBtnText("")
                                        .setAllGold(demo.total_points)
                                        .setGold(demo.points)
                                        .setMoney((float) demo.cash)
                                        .setPostID("1028047")
                                        .setDialogClick(new GetGoldDialog.GetGoldDialogClick() {
                                            @Override
                                            public void doubleBtnClick(Dialog dialog) {
                                                dialog.dismiss();
                                            }

                                            @Override
                                            public void closeBtnClick(Dialog dialog) {
                                                dialog.dismiss();
                                            }

                                            @Override
                                            public void bottomBtnClick(Dialog dialog) {
                                                dialog.dismiss();
                                            }
                                        }).build();
                                dialog.show();
//                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        if (!TextUtils.isEmpty(errorMsg)) {
                            ToastUtils.showShort(errorMsg);
                        }
                    }
                });
    }
}
