package com.mobi.overseas.clearsafe.ui.clear;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.ui.clear.adapter.GarbageAdapter;
import com.mobi.overseas.clearsafe.ui.clear.control.ClearPresenter;
import com.mobi.overseas.clearsafe.ui.clear.control.CountDownTimerWrap;
import com.mobi.overseas.clearsafe.ui.clear.control.GarbagePresenter;
import com.mobi.overseas.clearsafe.ui.clear.control.SingleClickListener;
import com.mobi.overseas.clearsafe.ui.clear.data.GarbageBean;
import com.mobi.overseas.clearsafe.ui.clear.data.GarbageHeaderBean;
import com.mobi.overseas.clearsafe.ui.clear.data.LenFile;
import com.mobi.overseas.clearsafe.ui.clear.dialog.CleanDialogWarp;
import com.mobi.overseas.clearsafe.ui.clear.entity.CleanEvent;
import com.mobi.overseas.clearsafe.ui.clear.entity.CleanRewardBean;
import com.mobi.overseas.clearsafe.ui.clear.fragment.ShowGoodFragment;
import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;
import com.mobi.overseas.clearsafe.ui.clear.util.GarbageTextAnimUtil;
import com.mobi.overseas.clearsafe.ui.clear.widget.wave.MultiWaveHeader;
import com.mobi.overseas.clearsafe.ui.clear.widget.wave.WaveGarbageHelper;
import com.mobi.overseas.clearsafe.ui.clear.widget.wave.WaveView;
import com.mobi.overseas.clearsafe.ui.common.base.BaseActivity;
import com.mobi.overseas.clearsafe.ui.common.global.AppGlobalConfig;
import com.mobi.overseas.clearsafe.ui.common.widget.LockableNestedScrollView;
import com.mobi.overseas.clearsafe.widget.CleanGoldDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class GarbageActivity extends BaseActivity implements ClearPresenter.IClearView, GarbagePresenter.IView {

    public static final String TAG = "GarbageActivity";

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;

    @BindView(R.id.tvScan)
    TextView tvScan;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.waveHeader)
    MultiWaveHeader waveHeader;
    @BindView(R.id.iOkLayout)
    View iOkLayout;
    @BindView(R.id.iTabLayout)
    View iTabLayout;
    @BindView(R.id.waveView)
    WaveView mWaveView;
    @BindView(R.id.tvClearText)
    TextView tvClearText;
    @BindView(R.id.nsvScroll)
    LockableNestedScrollView nsvScroll;

    private GarbageAdapter garbageAdapter;
//    private ClearPresenter clearPresenter;

    private GarbagePresenter garbagePresenter;
//    private int cleanId;

    private String clearValue = "";

    private AtomicLong allSize = new AtomicLong(0);

    //是否已经扫描到path了
    private volatile boolean mIsPathScanned;

    //    private GarbageTask mGarbageTask;
    private ValueAnimator valueAnimator;
    private CountDownTimerWrap countDownTimerWrap;
    private WaveGarbageHelper waveHelper;
    private ShowGoodFragment mFragment;

    public static void start(Context context, int cleanId) {
        Intent intent = new Intent(context, GarbageActivity.class);
        intent.putExtra("cleanId", cleanId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_garbage;
    }

    @Override
    public void initVariables() {

//        cleanId = getIntent().getIntExtra("cleanId", 0);
    }

    @Override
    public void initView() {

        initToolBar();
        initRv();

        mWaveView.setWaveColor(0x00ffffff, WaveView.DEFAULT_FRONT_WAVE_COLOR2);
        waveHelper = new WaveGarbageHelper(mWaveView, 5000, 0.9f);
        waveHelper.start();

    }

    @Override
    protected boolean isFitWindow() {
        return true;
    }

    @Override
    public void initData() {
//        clearPresenter = new ClearPresenter(this);
        garbagePresenter = new GarbagePresenter(this);

        //可以进行清理的
        if (garbagePresenter.isCanClear()) {
            garbagePresenter.execTaskGarbage();

            List<String> packageNameList = garbagePresenter.getPackageNameList();
            valueAnimator = ValueAnimator.ofFloat(0.00f, 2.89f);
            valueAnimator.setDuration(5000);
            valueAnimator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                int currentValue = (int) (value * 100);
                float animatedValue = (float) (currentValue / 100.0);
                tvNum.setText(String.valueOf(animatedValue));
                tvUnit.setText("B");

                if (packageNameList.size() > 0 && currentValue % 50 == 0 && !mIsPathScanned) {
//                    tvScan.setText("正在扫描：" + packageNameList.remove(0));
                    tvScan.setText(String.format(getResources().getString(R.string.scanning), packageNameList.remove(0)));
                }

            });
            valueAnimator.start();
        } else { //不需要进行清理
            garbageAdapter.isLoadFinish = true;
            showClearGoodLayout(true);
        }

    }

    @Override
    public void initEvent() {
        btnClear.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                btnClear.setEnabled(false);
                btnClear.setText(getResources().getString(R.string.cleanUp));
                garbagePresenter.calculateClearPercent(garbageAdapter.dataAllSize.get(), allSize.get());

                handleCleanAnim();
            }
        });

        nsvScroll.setOnScrollChangeListener((NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) -> {
            Log.e(TAG, "scrollX: " + scrollX + " scrollY: " + scrollY + " oldScrollX: " + oldScrollX + " oldScrollY: " + oldScrollY);

            if (scrollY < 300) {
                mToolBar.setBackgroundColor(getResources().getColor(R.color.transparent));
                mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
                mToolBar.setNavigationIcon(R.drawable.white_return);
                setDarkStatusIcon(getWindow(), false);
            } else {
                mToolBar.setBackgroundColor(getResources().getColor(R.color.white));
                mToolBar.setTitleTextColor(getResources().getColor(R.color.c_3D3F5C));
                mToolBar.setNavigationIcon(R.mipmap.back_icon);
                setDarkStatusIcon(getWindow(), true);
            }
        });

        garbageAdapter.setCheckedListener((allSize, data) -> {
            computeAllSize(allSize, false, true);
        });
    }

    private void handleCleanAnim() {
        //scrollView布局变化
        List<LenFile> pathList = garbageAdapter.changeToRemoveDataIsEmpty();
        if (pathList.isEmpty()) {
            showClearGoodLayout(true);
        } else {
            nsvScroll.setPadding(0, 0, 0, 0);
            llBottomAnim();

            startClean2(pathList);
        }
    }

    private void llBottomAnim() {
        llBottom.post(() -> {
            ViewCompat.animate(llBottom)
                    .translationY(llBottom.getHeight())
                    .setDuration(500)
                    .start();
        });
    }

    @Override
    public void onBegin() {
        if (isFinishing()) {
            return;
        }

        btnClear.setEnabled(false);
        btnClear.setText(getResources().getString(R.string.scan));
    }

    @Override
    public void onProgress(GarbageBean info) {
        if (isFinishing()) {
            return;
        }

        allSize.set(allSize.get() + info.fileSize);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                computeAllSize(allSize.get(), true, false);
                int size = (int) (allSize.get() >= Integer.MAX_VALUE ? Integer.MAX_VALUE : allSize.get());
                handleBgStatus(size);
            }
        });
    }

    @Override
    public void onPath(String path) {
        if (isFinishing()) {
            return;
        }

        //扫描到了，就用当前的这个
        //不然用假的包名显示扫描
        mIsPathScanned = true;

        tvScan.setText(String.format(getResources().getString(R.string.scanning), path));
    }

    @Override
    public void onFinish(long pAllSize, List<MultiItemEntity> garbageHeaders) {

        if (isFinishing()) {
            return;
        }

        if (waveHelper != null) {
            waveHelper.transparentAnim();
        }

        //加载完成
        tvScan.setText("");
        allSize.set(pAllSize);
        garbageAdapter.dataAllSize.set(pAllSize);

        boolean enabled = !(allSize.get() == 0) && iOkLayout.getVisibility() == View.GONE;
        if (!enabled) {
            btnClear.setText(getResources().getString(R.string.clear_to_complete));
        }
        btnClear.setEnabled(enabled);
        computeAllSize(allSize.get(), false, true);

        if (allSize.get() == 0) {
            tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            tvNum.setText(getResources().getString(R.string.clear_to_complete));
            tvUnit.setVisibility(View.GONE);
        }

        garbageAdapter.replaceData(garbageHeaders);
        garbageAdapter.expandAll();
        garbageAdapter.isLoadFinish = true;
        //加载完毕，隐藏一些垃圾
//        boolean isEmpty = garbageAdapter.removeEmptyAndReplaceData(garbageHeaders);
//        if (isEmpty) {
//            //清理完毕，非常干净
//            showClearGoodLayout();
//        }

    }

    private void startClean2(List<LenFile> pathList) {
        clearValue = tvNum.getText().toString() + tvUnit.getText().toString();

        rv.scrollToPosition(0);
        //不能滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv.setLayoutManager(linearLayoutManager);
        //滑动到顶部
//        nsvScroll.fullScroll(View.FOCUS_UP);
        nsvScroll.pageScroll(View.FOCUS_UP);
        //禁止滑动
        nsvScroll.setScrollingEnabled(false);

        if (countDownTimerWrap != null) {
            countDownTimerWrap.cancel();
        }
        countDownTimerWrap = new CountDownTimerWrap(System.currentTimeMillis(), 800);
        countDownTimerWrap.setCountTimerListener(new CountDownTimerWrap.ICountTimerListener() {
            @Override
            public void onTime(String hour, String minute, String second) {
                //删到最后一个等待删除，不然就没有了
                if (garbageAdapter != null && garbageAdapter.getData().size() > 1) {
                    garbageAdapter.getData().remove(0);
                    garbageAdapter.notifyItemRemoved(0);
                } else if (garbageAdapter != null) {
                    garbageAdapter.replaceData(new ArrayList<>());
                }
            }

            @Override
            public void onTimeFinish() {

            }
        });
        countDownTimerWrap.start();


        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {

            for (LenFile file : pathList) {

                String path = file.getPath();
                Log.e(TAG, "path: " + path);

                //删除文件或者文件夹，并返回长度
                final long length = FileUtil.deleteFileOrDirectory(file);

                //计算删除文件的长度
                runOnUiThread(() -> {
                    garbageAdapter.dataAllSize.set(garbageAdapter.dataAllSize.get() - length);
                    long l = garbageAdapter.dataAllSize.get() < 0 ? 0L : garbageAdapter.dataAllSize.get();

                    computeAllSize(garbageAdapter.dataAllSize.get(), true, false);
                    int size = (int) (l >= Integer.MAX_VALUE ? Integer.MAX_VALUE : l);
                    handleBgStatus(size);

//                        iterator.remove();

                });

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()) {
                        return;
                    }

                    if (countDownTimerWrap != null) {
                        countDownTimerWrap.setCancel(true);
                        countDownTimerWrap.cancel();
                    }

                    if (garbageAdapter != null && garbageAdapter.getData().size() > 0) {
                        garbageAdapter.getData().clear();
                        garbageAdapter.notifyItemRangeRemoved(0, garbageAdapter.getData().size() - 1);

                    }

                    if (garbagePresenter != null) {
                        garbagePresenter.saveClearStatus();
                    }

                    if (tvNum == null || isFinishing()) {
                        return;
                    }

                    //延迟执行showClearGoodLayout的显示
                    tvNum.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (isFinishing()) {
                                return;
                            }

                            garbageAdapter.replaceData(new ArrayList<>());

                            showClearGoodLayout(false);

                            btnClear.setEnabled(false);
                            btnClear.setText(getResources().getString(R.string.cleanUpTheComplete));
//                            tvClearText.setText("已清理" + clearValue + "系统垃圾");
                            tvClearText.setText(String.format(getResources().getString(R.string.systemGarbageHasBeenCleared), clearValue));


                        }
                    }, 500);
                }
            });

            ButtonStatistical.garbageClearButtonClick();
            EventBus.getDefault().post(new CleanEvent());

        });
    }

//    private void requestClearGarbage() {
//        if (clearPresenter != null) {
//            clearPresenter.requestClearGarbage(cleanId, this);
//        }
//    }

    @Override
    public void clearGarbage(CleanRewardBean data) {
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

        tvClearText.setText(String.format(getResources().getString(R.string.systemGarbageHasBeenCleared), clearValue));

        iOkLayout.setVisibility(View.VISIBLE);
        iTabLayout.setVisibility(View.INVISIBLE);

        EventBus.getDefault().post(new CleanEvent());
    }

    private void initToolBar() {
        mToolBar.setTitle(getResources().getString(R.string.garbageClean));
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));
        mToolBar.setNavigationIcon(R.drawable.white_return);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showShortSafe("aaaaaa");
//                StatusBarUtil.setStatusBarMode(CleanReportActivity.this, true, R.color.white);
//                FileManager.scaFile(handler);
            }
        });

    }

    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        garbageAdapter = new GarbageAdapter(new ArrayList<>());
        rv.setNestedScrollingEnabled(false);
        rv.setAdapter(garbageAdapter);
        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setAddDuration(0);
        animator.setRemoveDuration(300);
        animator.setChangeDuration(300);
        animator.setMoveDuration(300);
        rv.setItemAnimator(animator);

        List<MultiItemEntity> headerBeans = GarbageHeaderBean.createGarbageHeaders();

        garbageAdapter.addData(headerBeans);

    }

    private void computeAllSize(long allSize, boolean isLoading, boolean isHandleClearBtn) {

        if (valueAnimator != null) {
            valueAnimator.cancel();
        }

        String[] fileSize0 = FileUtil.getFileSize0(allSize);

//        tvNum.setText(fileSize0[0]);
        if (tvNum == null) {
            return;
        }

        GarbageTextAnimUtil.addTextViewAddAnim(tvNum, Float.parseFloat(tvNum.getText().toString()), Float.parseFloat(fileSize0[0]));

        tvUnit.setText(fileSize0[1]);
        Log.e(TAG, "allSizeAic : " + allSize);

        if (isHandleClearBtn) {
            handleClearBtn(allSize, isLoading);
        }
    }

    public void handleClearBtn(long allSize, boolean isLoading) {
        String[] fileSize0 = FileUtil.getFileSize0(allSize);
        boolean enabled = !(allSize <= 0) && iOkLayout.getVisibility() == View.GONE;
        if (!enabled) {
            btnClear.setText(getResources().getString(R.string.clear_to_complete));
            btnClear.setEnabled(false);
            return;
        }

        if (allSize <= 0) {
            allSize = 0;
//            btnClear.setEnabled(false);
        }
        //为了选中的时候，产生不能点击的情况
        btnClear.setEnabled(allSize > 0 && iOkLayout.getVisibility() == View.GONE && !isLoading);
//        btnClear.setText("放心清理（" + fileSize0[0] + fileSize0[1] + "）");
        btnClear.setText(String.format(getResources().getString(R.string.restAssuredToCleanUp), fileSize0[0] + fileSize0[1]));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                finish();
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!garbageAdapter.isLoadFinish) {
            CleanDialogWarp dialogWarp = new CleanDialogWarp.Builder(this)
                    .setRightListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setLeftListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openSplashActivity(GarbageActivity.this);
                            finish();
                        }
                    })
                    .build();
            dialogWarp.show();
        }
        //显示了清理，就是清理完了，加个激励视频
        else if (mFragment != null && !mFragment.isFirstGood()) {
            if (garbagePresenter != null) {
                garbagePresenter.showRewardVideoAd(this);
            }
        } else {
            openSplashActivity(this);
            super.onBackPressed();
        }
    }

    @Override
    public void showRewardVideoAdFinish() {
        openSplashActivity(this);
        finish();
    }

    private void handleBgStatus(int size) {
        if (isFinishing()) {
            return;
        }

        if (size < 30000000) {
            vLine.setBackgroundResource(R.drawable.clear_blue_selector);

            waveHeader.setStartColor(getResources().getColor(R.color.c_0043ff));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_008dff));

        }
//        else if (size < 50000000) {
//            vLine.setBackgroundResource(R.drawable.clear_orange_shape);
//
//            waveHeader.setStartColor(getResources().getColor(R.color.c_FFE700));
//            waveHeader.setCloseColor(getResources().getColor(R.color.c_CA7A03));
//        } else if (size < 100000000) {
//            vLine.setBackgroundResource(R.drawable.clear_orange_shape);
//
//            waveHeader.setStartColor(getResources().getColor(R.color.c_FFE700));
//            waveHeader.setCloseColor(getResources().getColor(R.color.c_CA7A03));
//        }
        else {
            if (vLine.getTag() != null) {
                return;
            }

            ValueAnimator valueAnimator = ValueAnimator.ofInt(0xFF0062FF, 0xFFFF7400);
            valueAnimator.setEvaluator(new ArgbEvaluator());
            valueAnimator.setDuration(500);
            valueAnimator.addUpdateListener(animation -> {
                int animatedValue = (int) animation.getAnimatedValue();
                if (vLine != null) {
                    vLine.setBackgroundColor(animatedValue);
                }
            });

            vLine.setTag(valueAnimator);
            valueAnimator.start();

//            vLine.setBackgroundResource(R.drawable.clear_orange_shape);

//            waveHeader.setStartColor(getResources().getColor(R.color.c_FFE700));
//            waveHeader.setCloseColor(getResources().getColor(R.color.c_CA7A03));
        }
    }

    public void showClearGoodLayout(boolean isFirstGood) {

        if (isFinishing()) {
            return;
        }

        nsvScroll.setPadding(0, 0, 0, 0);
        llBottomAnim();
        nsvScroll.setVisibility(View.GONE);

        getIntent().putExtra("isFirstGood", isFirstGood);
        getIntent().putExtra("isGarbageGoodShowAd", true);

        getIntent().putExtra("dec", getResources().getString(R.string.optimizingSuccess));
        getIntent().putExtra("dec2", getResources().getString(R.string.optimizingSuccessStatus));

        mFragment = ShowGoodFragment.newInstance(isFirstGood);
        mFragment.setFirstGood(isFirstGood);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flContainer, mFragment)
                .commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimerWrap != null) {
            countDownTimerWrap.setCancel(true);
            countDownTimerWrap.cancel();
        }

        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
        }

//        if (clearPresenter != null) {
//            clearPresenter.detach();
//        }

        if (garbagePresenter != null) {
            garbagePresenter.detach();
        }
    }
}
