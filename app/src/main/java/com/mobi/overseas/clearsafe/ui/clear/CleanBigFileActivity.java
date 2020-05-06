package com.mobi.overseas.clearsafe.ui.clear;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.ui.clear.adapter.BigFileAdapter;
import com.mobi.overseas.clearsafe.ui.clear.control.ClearPresenter;
import com.mobi.overseas.clearsafe.ui.clear.control.SingleClickListener;
import com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanBigFileTask;
import com.mobi.overseas.clearsafe.ui.clear.control.scan.IScanCallback;
import com.mobi.overseas.clearsafe.ui.clear.data.ScanFileBean;
import com.mobi.overseas.clearsafe.ui.clear.dialog.CleanDialogWarp;
import com.mobi.overseas.clearsafe.ui.clear.entity.CleanRewardBean;
import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;
import com.mobi.overseas.clearsafe.ui.clear.util.StorageUtil;
import com.mobi.overseas.clearsafe.ui.clear.widget.FileScanLayout;
import com.mobi.overseas.clearsafe.ui.common.base.BaseActivity;
import com.mobi.overseas.clearsafe.ui.common.global.AppGlobalConfig;
import com.mobi.overseas.clearsafe.ui.common.util.SpannableStringBuilderUtil;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.widget.CleanGoldDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CleanBigFileActivity extends BaseActivity implements IScanCallback<ScanFileBean>, ClearPresenter.IClearView {
    public static final String TAG = "CleanBigFileActivity";

    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.llTop)
    LinearLayout llTop;
    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.tvFileCount)
    TextView tvFileCount;
    @BindView(R.id.tvFileSize)
    TextView tvFileSize;
    @BindView(R.id.pb)
    ProgressBar mProgressBar;

    @BindView(R.id.fsLayout)
    FileScanLayout fsLayout;
    @BindView(R.id.tvPath)
    TextView tvPath;

    long mAllSize = 0L;
    long mSelectedSize = 0L;

    private BigFileAdapter mBigFileAdapter;
    private CleanBigFileTask mCleanBigFileTask;

    private boolean isScanFinish;
    private TextView mTvEmpty;

    private ClearPresenter clearPresenter;
    private int cleanId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_clean_big_file;
    }

    public static void start(Context context, int cleanId) {
        Intent intent = new Intent(context, CleanBigFileActivity.class);
        intent.putExtra("cleanId", cleanId);
        context.startActivity(intent);
    }

    @Override
    public void initVariables() {
        cleanId = getIntent().getIntExtra("cleanId", 0);
    }

    @Override
    public void initView() {
        super.initView();
        setDarkStatusIcon(getWindow(), true);

        mToolBar.setTitle("大文件清理");
        mToolBar.setTitleTextColor(getResources().getColor(R.color.black_33));
        mToolBar.setNavigationIcon(R.drawable.return_black_left);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mBigFileAdapter = new BigFileAdapter(new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mBigFileAdapter);

//        TextView textView = new TextView(this);
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
//        textView.setText("扫描中...");
//
//        View.inflate()

        mBigFileAdapter.setEmptyView(R.layout.scaning_layout, mRecyclerView);

        mTvEmpty = mBigFileAdapter.getEmptyView().findViewById(R.id.tvEmpty);

        fsLayout.setShowText("正在扫描大文件");
    }

    @Override
    public void initEvent() {
        mBigFileAdapter.setCheckedListener((allSize, data) -> {
            judgeClearBtnStatus(allSize);
        });

        btnClear.setOnClickListener(new SingleClickListener(1200) {
            @Override
            public void onSingleClick(View v) {
                showClearDialog();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        mCleanBigFileTask = new CleanBigFileTask(this);
        mCleanBigFileTask.setCallback(this);
        mCleanBigFileTask.execTask();
    }

    @Override
    public void onBegin() {

    }

    @Override
    public void onProgress(ScanFileBean info) {

    }

    @Override
    public void onPath(String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
//                fsLayout.setPathValue(path);
                tvPath.setText(path);
            }
        });
    }

    @Override
    public void onFinish() {
        List<ScanFileBean> garbageListCopy = mCleanBigFileTask.getGarbageList();

        for (ScanFileBean garbageBean : garbageListCopy) {
            garbageBean.itemType = 0;
            garbageBean.dec = "可以删除";
            //大文件没有全部选中
            garbageBean.isCheck = false;
            mAllSize += garbageBean.fileSize;
        }
        List<ScanFileBean> garbageList = new ArrayList<>(garbageListCopy);
        Collections.sort(garbageList);

        runOnUiThread(() -> {

            if (isFinishing()) {
                return;
            }

            //更新一下扫描状态
            isScanFinish = true;

            llTop.setVisibility(View.VISIBLE);

            refreshUIStatus(garbageList);

            tvPath.setVisibility(View.GONE);
            fsLayout.setVisibility(View.GONE);
        });
    }

    /**
     * 更新界面
     *
     * @param garbageList
     */
    private <T extends MultiItemEntity> void refreshUIStatus(Collection<T> garbageList) {
        tvFileCount.setText(SpannableStringBuilderUtil.getRedStr("共", "个文件", garbageList.size()));
        String[] fileSize0 = FileUtil.getFileSize0(mAllSize);
        tvFileSize.setText(SpannableStringBuilderUtil.getRedStr("占用", "空间", fileSize0[0] + fileSize0[1]));
        mBigFileAdapter.setSize(mAllSize);
        mBigFileAdapter.setSelectedSize(mSelectedSize);

        //大文件默认是0
        judgeClearBtnStatus(0L);
        mBigFileAdapter.replaceData(garbageList);
    }


    private void judgeClearBtnStatus(long clearSize) {
        if (clearSize > 0) {
            btnClear.setEnabled(true);
            String[] fileSize0 = FileUtil.getFileSize0(clearSize);
            btnClear.setText("立即清理（" + fileSize0[0] + fileSize0[1] + "）");
        } else {
            btnClear.setEnabled(false);
            btnClear.setText("立即清理");
        }

    }

    @Override
    public void onBackPressed() {
        if (!isScanFinish) {
            CleanDialogWarp dialogWarp = new CleanDialogWarp.Builder(this)
                    .setRightListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setLeftListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .build();
            dialogWarp.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void showClearDialog() {
        new CleanDialogWarp.Builder(this)
                .setTitle("是否删除")
                .setContent("是否删除选中项")
                .setRightButton("删除")
                .setLeftButton("取消")
                .setRightListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearFile();
                    }
                })
                .build()
                .show();
    }

    private void clearFile() {
        ButtonStatistical.bigFileClearButtonClick();
        mTvEmpty.setText("大文件清理完毕");

        List<File> pathList = mBigFileAdapter.findClearFile();
        showProgressDialog(false, "清理中...");

        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {

            for (File file : pathList) {
                if (file.isFile()) {
                    FileUtil.deleteFile(file);
                    Log.e(TAG, "file path " + file.getPath());
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    List<MultiItemEntity> newDataList = new ArrayList<>();

                    mAllSize = 0L;
                    mSelectedSize = 0L;

                    for (MultiItemEntity datum : mBigFileAdapter.getData()) {
                        if (datum instanceof ScanFileBean) {
                            if (!((ScanFileBean) datum).isCheck) {
                                newDataList.add(datum);
                                mAllSize += ((ScanFileBean) datum).fileSize;
                            }
                        }
                    }
                    //刷新一下删除后的界面
                    refreshUIStatus(newDataList);

                    ToastUtils.showLong("删除成功");
                    closeProgressDialog();

                    requestClearGarbage();
                }
            });

        });
    }

    private void requestClearGarbage() {
        clearPresenter = new ClearPresenter(this);
        clearPresenter.requestClearGarbage(cleanId, this);
    }

    @Override
    public void clearGarbage(CleanRewardBean data) {
        if (isFinishing()) {
            return;
        }

        CleanGoldDialog dialog = new CleanGoldDialog.Builder(this)
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
}
