package com.mobi.overseas.clearsafe.ui.clear;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.mobi.overseas.clearsafe.ui.clear.adapter.ApkFileAdapter;
import com.mobi.overseas.clearsafe.ui.clear.control.ClearPresenter;
import com.mobi.overseas.clearsafe.ui.clear.control.SingleClickListener;
import com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanApkTask;
import com.mobi.overseas.clearsafe.ui.clear.control.scan.IScanCallback;
import com.mobi.overseas.clearsafe.ui.clear.data.ScanFileBean;
import com.mobi.overseas.clearsafe.ui.clear.dialog.CleanDialogWarp;
import com.mobi.overseas.clearsafe.ui.clear.entity.CleanRewardBean;
import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;
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

public class CleanApkActivity extends BaseActivity implements IScanCallback<ScanFileBean>, ClearPresenter.IClearView {
    public static final String TAG = "CleanApkActivity";

    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.llTop)
    LinearLayout llTop;
    @BindView(R.id.btnClear)
    Button btnClear;

    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.tvCheckAll)
    TextView tvCheckAll;

    @BindView(R.id.pb)
    ProgressBar mProgressBar;

    @BindView(R.id.fsLayout)
    FileScanLayout fsLayout;
    @BindView(R.id.tvPath)
    TextView tvPath;

    long mAllSize = 0L;
    long mSelectedSize = 0L;

    private ApkFileAdapter mApkFileAdapter;
    private CleanApkTask mCleanApkTask;

    private boolean isScanFinish;
    private TextView mTvEmpty;
    private ClearPresenter clearPresenter;
    private int cleanId;

    public static void start(Context context, int cleanId) {
        Intent intent = new Intent(context, CleanApkActivity.class);
        intent.putExtra("cleanId", cleanId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_clean_apk;
    }

    @Override
    public void initVariables() {
        cleanId = getIntent().getIntExtra("cleanId", 0);
    }

    @Override
    public void initView() {
        super.initView();
        setDarkStatusIcon(getWindow(), true);

        mToolBar.setTitle(getResources().getString(R.string.installationPackageManagement));
        mToolBar.setTitleTextColor(getResources().getColor(R.color.black_33));
        mToolBar.setNavigationIcon(R.drawable.return_black_left);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mApkFileAdapter = new ApkFileAdapter(new ArrayList<>());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mApkFileAdapter);

        mApkFileAdapter.setEmptyView(R.layout.scaning_layout, mRecyclerView);

        mTvEmpty = mApkFileAdapter.getEmptyView().findViewById(R.id.tvEmpty);

        fsLayout.setShowText(getResources().getString(R.string.scanningInstallationPackage));
    }

    @Override
    public void initEvent() {
        super.initEvent();
        mApkFileAdapter.setCheckedListener((allSize, data) -> {
            judgeClearBtnStatus(allSize);
        });

        tvCheckAll.setOnClickListener(new SingleClickListener(1200) {
            @Override
            public void onSingleClick(View v) {
                long allSize = mApkFileAdapter.checkAll();
                judgeClearBtnStatus(allSize);
            }

            @Override
            public void onFastClick() {
                ToastUtils.showShort(getResources().getString(R.string.clickTooFast));
            }
        });

        btnClear.setOnClickListener(new SingleClickListener(2000) {
            @Override
            public void onSingleClick(View v) {
                showClearDialog();
            }
        });
    }

    private void showClearDialog() {
        new CleanDialogWarp.Builder(this)
                .setTitle(getResources().getString(R.string.dialogIsDelete))
                .setContent(getResources().getString(R.string.dialogIsDeleteSelected))
                .setRightButton(getResources().getString(R.string.dialogDelete))
                .setLeftButton(getResources().getString(R.string.dialogCancel))
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
        ButtonStatistical.pKGClearButtonClick();

        mTvEmpty.setText(getResources().getString(R.string.installationPackageCleaned));

        List<File> pathList = mApkFileAdapter.findClearFile();
        showProgressDialog(false, getResources().getString(R.string.cleanUp));


        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {

            for (File file : pathList) {
                String path = file.getPath();
                Log.e(TAG, "path: " + path);
                if (file.isFile()) {
                    FileUtil.deleteFileOrDirectory(FileUtil.getLenFile(file, 0L));
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()) {
                        return;
                    }

                    List<MultiItemEntity> newDataList = new ArrayList<>();
                    mAllSize = 0L;
                    mSelectedSize = 0L;

                    for (MultiItemEntity datum : mApkFileAdapter.getData()) {
                        if (datum instanceof ScanFileBean) {
                            if (!((ScanFileBean) datum).isCheck) {
                                newDataList.add(datum);
                                mAllSize += ((ScanFileBean) datum).fileSize;
                            }
                        }
                    }
                    //刷新一下删除后的界面
                    refreshUIStatus(newDataList);

//                    mApkFileAdapter.replaceData(newDataList);

                    ToastUtils.showLong(getResources().getString(R.string.delSuccess));
                    closeProgressDialog();

                    requestClearGarbage();
                }
            });

        });
    }

    @Override
    public void initData() {
        super.initData();
        mCleanApkTask = new CleanApkTask(this);
        mCleanApkTask.setCallback(this);
        mCleanApkTask.execTask();
    }

    @Override
    public void onBegin() {

    }

    @Override
    public void onProgress(ScanFileBean info) {
        Log.i(TAG, info.fileList.get(0).getPath());
    }

    @Override
    public void onFinish() {
        List<ScanFileBean> garbageListCopy = mCleanApkTask.getGarbageList();

        for (ScanFileBean garbageBean : garbageListCopy) {
            garbageBean.itemType = 0;
            //大文件没有全部选中
            mAllSize += garbageBean.fileSize;
            if (garbageBean.isCheck) {
                mSelectedSize += garbageBean.fileSize;
            }
        }
        //ArrayCopy一下，有些 mi5s不支持 CopyOnWriteArrayList 直接sort
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
        String[] fileSize0 = FileUtil.getFileSize0(mAllSize);
        tvContent.setText(SpannableStringBuilderUtil.getRedStr(String.format(getResources().getString(R.string.installationPackagesTakeUp), garbageList.size()), getResources().getString(R.string.space), fileSize0[0] + fileSize0[1]));
        mApkFileAdapter.setSize(mAllSize);
        mApkFileAdapter.setSelectedSize(mSelectedSize);
        //大文件默认是0
        judgeClearBtnStatus(mSelectedSize);

        mApkFileAdapter.replaceData(garbageList);
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

    private void judgeClearBtnStatus(long clearSize) {
        if (clearSize > 0) {
            btnClear.setEnabled(true);
            String[] fileSize0 = FileUtil.getFileSize0(clearSize);
            btnClear.setText(String.format(getResources().getString(R.string.immediatelyCleanUpFormat), fileSize0[0] + fileSize0[1]));
        } else {
            btnClear.setEnabled(false);
            btnClear.setText(getResources().getString(R.string.immediatelyCleanUp));
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
