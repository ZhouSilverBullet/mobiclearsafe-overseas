package com.mobi.overseas.clearsafe.ui.clear;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.clear.adapter.BigFileAdapter;
import com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanBigFileTask;
import com.mobi.overseas.clearsafe.ui.clear.control.scan.IScanCallback;
import com.mobi.overseas.clearsafe.ui.clear.data.ScanFileBean;
import com.mobi.overseas.clearsafe.ui.clear.dialog.CleanDialogWarp;
import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;
import com.mobi.overseas.clearsafe.ui.clear.util.StorageUtil;
import com.mobi.overseas.clearsafe.ui.common.base.BaseActivity;
import com.mobi.overseas.clearsafe.ui.common.util.SpannableStringBuilderUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public abstract class BaseScanFileActivity extends BaseActivity implements IScanCallback<ScanFileBean> {
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

    long mAllSize = 0L;

    private BigFileAdapter mBigFileAdapter;
    private CleanBigFileTask mCleanBigFileTask;

    private boolean isScanFinish;

    @Override
    public int getLayoutId() {
        return R.layout.activity_base_scan_file;
    }

    @Override
    public void initView() {
        super.initView();

        mToolBar.setTitle("安装包管理");
        mToolBar.setTitleTextColor(getResources().getColor(R.color.black_33));
        mToolBar.setNavigationIcon(R.drawable.return_black_left);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBigFileAdapter = new BigFileAdapter(new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mBigFileAdapter);

        mBigFileAdapter.setEmptyView(R.layout.scaning_layout, mRecyclerView);

    }

    @Override
    public void initEvent() {
        mBigFileAdapter.setCheckedListener((allSize, data) -> {
            judgeClearBtnStatus(allSize);
        });

        btnClear.setOnClickListener(v -> {

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
    public void onFinish() {
        List<ScanFileBean> garbageListCopy = mCleanBigFileTask.getGarbageList();

        for (ScanFileBean garbageBean : garbageListCopy) {
            garbageBean.itemType = 0;
            garbageBean.dec = "可以删除";
            //大文件没有全部选中
            garbageBean.isCheck = false;
            mAllSize += garbageBean.fileSize;
        }

        List<ScanFileBean> garbageList =  new ArrayList<>(garbageListCopy);
        Collections.sort(garbageList);

        runOnUiThread(() -> {

            //更新一下扫描状态
            isScanFinish = true;

            llTop.setVisibility(View.VISIBLE);
            tvFileCount.setText(SpannableStringBuilderUtil.getRedStr("共", "个文件", garbageList.size()));
            String[] fileSize0 = FileUtil.getFileSize0(mAllSize);
            tvFileSize.setText(SpannableStringBuilderUtil.getRedStr("占用", "空间", fileSize0[0] + fileSize0[1]));
            mProgressBar.setProgress(StorageUtil.getStoragePercent(getApplication(), mAllSize));

            //大文件默认是0
            judgeClearBtnStatus(0L);
            mBigFileAdapter.replaceData(garbageList);
        });
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
        finish();
        return super.onSupportNavigateUp();
    }
}
