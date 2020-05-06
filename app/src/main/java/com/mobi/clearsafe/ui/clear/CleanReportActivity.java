package com.mobi.clearsafe.ui.clear;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.ui.clear.ad.ADRelativeLayout;
import com.mobi.clearsafe.ui.clear.control.ClearPresenter;
import com.mobi.clearsafe.ui.clear.control.ClearQQWrap;
import com.mobi.clearsafe.ui.clear.control.ScanAnimatorContainer;
import com.mobi.clearsafe.ui.clear.control.SingleClickListener;
import com.mobi.clearsafe.ui.clear.control.scan.CleanWechatTask;
import com.mobi.clearsafe.ui.clear.control.scan.IScanCallback;
import com.mobi.clearsafe.ui.clear.data.LenFile;
import com.mobi.clearsafe.ui.clear.data.WechatBean;
import com.mobi.clearsafe.ui.clear.entity.CleanEvent;
import com.mobi.clearsafe.ui.clear.entity.CleanRewardBean;
import com.mobi.clearsafe.ui.clear.util.FileUtil;
import com.mobi.clearsafe.ui.clear.util.WechatClearUtil;
import com.mobi.clearsafe.ui.clear.widget.ClearItemView;
import com.mobi.clearsafe.ui.clear.widget.dustbinview.DustbinDialog;
import com.mobi.clearsafe.ui.clear.widget.wave.MultiWaveHeader;
import com.mobi.clearsafe.ui.clear.widget.wave.WaveHelper;
import com.mobi.clearsafe.ui.clear.widget.wave.WaveView;
import com.mobi.clearsafe.ui.common.global.AppGlobalConfig;
import com.mobi.clearsafe.ui.common.util.SpUtil;
import com.mobi.clearsafe.widget.CleanGoldDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.mobi.clearsafe.ui.clear.GarbageActivity.setDarkStatusIcon;

public class CleanReportActivity extends BaseAppCompatActivity implements ClearPresenter.IClearView {

    private Toolbar mToolBar;
    private TextView tvText;
    private TextView tvNum;
    private TextView tvUnit;
    private ClearItemView civAd;
    private ClearItemView civFriend;
    private ClearItemView civWechatGarbage;
    private Button btnClear;
    //    private ClearWechatWrap clearWechatWrap;
    private View vScan;

    private View vLine;
    private MultiWaveHeader waveHeader;

    public static final String TAG = "CleanReportActivity";
    private ScanAnimatorContainer scanAnimatorContainer;
    private ClearPresenter clearPresenter;
    private int cleanId;
    private View iOkLayout;
    private View iTabLayout;
    private WaveView mWaveView;

    private TextView tvClearText;
    private String clearValue = "";
    private CleanWechatTask cleanWechatTask;

    private AtomicLong allSize = new AtomicLong(0);

    private LinearLayout llAd;
    private LinearLayout llClear;
    private ADRelativeLayout adRLayout;

    private DustbinDialog dustbinDialog;
    private NestedScrollView nsvScroll;

    public static void start(Context context, int cleanId) {
        Intent intent = new Intent(context, CleanReportActivity.class);
        intent.putExtra("cleanId", cleanId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_clean_report);

        setDarkStatusIcon(getWindow(), true);

        cleanId = getIntent().getIntExtra("cleanId", 0);
        clearPresenter = new ClearPresenter(this);

//        tvText = findViewById(R.id.tvText);
        initToolBar();
        initContent();
        initEvent();

        findWechatClear();

        //启动扫描
//        ClearBiz clearBiz = new ClearBiz(this, handler);
//        clearBiz.scan();
//        clearBiz.updataTotalDataSiz();

//        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//        startActivity(intent);
        startScanAnimal();
    }

    private void startScanAnimal() {
//        vScan = findViewById(R.id.vScan);
//        scanAnimatorContainer = new ScanAnimatorContainer(vScan);
//        scanAnimatorContainer.startAnimator();
    }

    private void findWechatClear() {
//        clearWechatWrap.findWechatClear(handler, tvNum, tvUnit, civAd, civFriend, civWechatGarbage);

        cleanWechatTask = new CleanWechatTask(new IScanCallback<WechatBean>() {
            @Override
            public void onBegin() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishing()) {
                            return;
                        }

                        btnClear.setEnabled(false);
                        btnClear.setText("扫描中...");
                    }
                });
            }

            @Override
            public void onProgress(WechatBean info) {
                allSize.set(allSize.get() + info.fileSize);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishing()) {
                            return;
                        }

                        computeAllSize(allSize.get(), true, false);
                        int size = (int) (allSize.get() >= Integer.MAX_VALUE ? Integer.MAX_VALUE : allSize.get());
                        handleBgStatus(size);
                    }
                });
            }

            @Override
            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishing()) {
                            return;
                        }


                        List<WechatBean> mWechatGabargeList = cleanWechatTask.mWechatGabargeList;
                        for (WechatBean wechatBean : mWechatGabargeList) {
                            civWechatGarbage.setData(wechatBean);
                        }
                        List<WechatBean> mWechatAdList = cleanWechatTask.mWechatAdList;
                        for (WechatBean wechatBean : mWechatAdList) {
                            civAd.setData(wechatBean);
                        }
                        List<WechatBean> mWechatFriendCacheList = cleanWechatTask.mWechatFriendCacheList;
                        for (WechatBean wechatBean : mWechatFriendCacheList) {
                            civFriend.setData(wechatBean);
                        }

                        handleClick(civWechatGarbage);
                        handleClick(civAd);
                        handleClick(civFriend);

                        boolean enabled = !(allSize.get() == 0) && iOkLayout.getVisibility() == View.GONE;
                        if (!enabled) {
                            btnClear.setText(getResources().getString(R.string.clear_to_complete));
                        }
                        btnClear.setEnabled(enabled);

                        computeAllSize(allSize.get(), false, true);
                        if (allSize.get() == 0) {
                            tvNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                            tvNum.setText("无需清理");
                            tvUnit.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        cleanWechatTask.execTask();
    }

    private void handleClick(ClearItemView civ) {
        civ.getCbMemory().setOnCheckedChangeListener(null);
        civ.getCbMemory().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                long size = civ.getSize();
                allSize.set(allSize.get() + size);
            } else {
                long size = civ.getSize();
                allSize.set(allSize.get() - size);
            }

            computeAllSize(allSize.get(), false, true);
            int size = (int) (allSize.get() >= Integer.MAX_VALUE ? Integer.MAX_VALUE : allSize.get());
            handleBgStatus(size);
        });
    }

    private void initEvent() {
        dustbinDialog = new DustbinDialog(this);

        btnClear.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                btnClear.setEnabled(false);
                btnClear.setText("清理中...");
                dustbinDialog.setShakeDustbinCallback(new DustbinDialog.IShakeDustbinCallback() {
                    @Override
                    public void onShakeDustbin() {
                        startClean();
                    }
                });

                dustbinDialog.show();
            }
        });

//        clearWechatWrap.setOnCheckedListener(size -> {
//            String[] fileSize0 = FileUtil.getFileSize0(size);
//            tvNum.setText(fileSize0[0]);
//            tvUnit.setText(fileSize0[1]);
//            btnClear.setText("放心清理（" + fileSize0[0] + fileSize0[1] + "）");
//        });

        nsvScroll = findViewById(R.id.nsvScroll);
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
    }

    private void execClearForWrap() {
        btnClear(() -> {

//            if (clearPresenter != null) {
//                clearPresenter.requestClearWechat(cleanId, this);
//            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (dustbinDialog != null) {
                        dustbinDialog.cancelShakeAnimator(600);
                        dustbinDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                //当这个dialog隐藏了再请求接口
                                requestClearGarbage();

                                iOkLayout.setVisibility(View.VISIBLE);
                                iTabLayout.setVisibility(View.INVISIBLE);

                                allSize.set(0);
                                btnClear.setText("清理完成");
                                btnClear.setEnabled(false);
                                tvClearText.setText("已清理" + clearValue + "微信垃圾");
                            }
                        });
                    }

                    btnClear.setEnabled(false);
                    tvClearText.setText("已清理" + clearValue + "微信垃圾");

//                    findWechatClear();
                    addAdLocation();


                }
            });

            ButtonStatistical.wxClearButtonClick();
            EventBus.getDefault().post(new CleanEvent());

        }, civAd, civFriend, civWechatGarbage);
    }

    /**
     * 添加广告位
     */
    private void addAdLocation() {
        if (llAd != null) {
            llAd.setVisibility(View.VISIBLE);
            for (int i = 0; i < llAd.getChildCount(); i++) {
                View childAt = llAd.getChildAt(i);
                if (childAt instanceof ADRelativeLayout) {
                    ((ADRelativeLayout) childAt).showAdLayout();
                }
            }
            llClear.setVisibility(View.GONE);
            adRLayout.setVisibility(View.GONE);
        }
    }

    private void requestClearGarbage() {
        if (clearPresenter != null) {
            clearPresenter.requestClearWechat(cleanId, this);
        }
    }

    private void startClean() {
        clearValue = tvNum.getText().toString() + tvUnit.getText().toString();
        execClearForWrap();
    }

    public void btnClear(ClearQQWrap.IClearCallback callback, ClearItemView... civ) {
        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(() -> {
            for (ClearItemView clearItemView : civ) {
                boolean check = clearItemView.isCheck();
                if (check) {
                    for (File file : clearItemView.getFile()) {
                        //计算删除文件的长度
                        long currentSize = clearItemView.getSize() / clearItemView.getFile().size();
                        //获取带有len的file
                        LenFile lenFile = FileUtil.getLenFile(file, currentSize);
                        //删除的文件长度
                        final long deleteLen = FileUtil.deleteFileOrDirectory(lenFile);

                        runOnUiThread(() -> {
                            allSize.set(allSize.get() - deleteLen);
                            long l = allSize.get() < 0 ? 0L : allSize.get();

                            computeAllSize(l, true, false);
                            int size = (int) (allSize.get() >= Integer.MAX_VALUE ? Integer.MAX_VALUE : allSize.get());
                            handleBgStatus(size);
                        });

                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                clearItemView.release();
            }

            if (callback != null) {
                callback.onFinish();
            }

            SpUtil.putLong(Const.WECHAT_CAN_CLEAR, System.currentTimeMillis());
        });
    }


    @Override
    public void clearWechat(CleanRewardBean data) {

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

        tvClearText.setText("已清理" + clearValue + "微信垃圾");
        iOkLayout.setVisibility(View.VISIBLE);
        iTabLayout.setVisibility(View.INVISIBLE);

        EventBus.getDefault().post(new CleanEvent());
    }

    private void initContent() {
        tvNum = findViewById(R.id.tvNum);
        tvUnit = findViewById(R.id.tvUnit);
        civAd = findViewById(R.id.civAd);
        civFriend = findViewById(R.id.civFriend);
        civWechatGarbage = findViewById(R.id.civWechatGarbage);
        btnClear = findViewById(R.id.btnClear);

        civAd.initData(WechatClearUtil.createWechatBean(
                getString(R.string.clear_wechat_ad_title),
                R.drawable.clean_clean_wechat_ad,
                getString(R.string.clear_wechat_ad_dec)));

        civFriend.initData(WechatClearUtil.createWechatBean(
                getString(R.string.clear_wechat_friend_title),
                R.drawable.clean_clean_wechat_friend,
                getString(R.string.clear_wechat_friend_dec)));

        civWechatGarbage.initData(WechatClearUtil.createWechatBean(
                getString(R.string.clear_wechat_title),
                R.drawable.clean_clean_wechat,
                getString(R.string.clear_wechat_dec)));


        vLine = findViewById(R.id.vLine);
        waveHeader = findViewById(R.id.waveHeader);

        iOkLayout = findViewById(R.id.iOkLayout);
        iTabLayout = findViewById(R.id.iTabLayout);

        mWaveView = findViewById(R.id.waveView);

        WaveHelper waveHelper = new WaveHelper(mWaveView);
        waveHelper.start();

        tvClearText = findViewById(R.id.tvClearText);

        llAd = findViewById(R.id.llAd);
        llClear = findViewById(R.id.llClear);
        adRLayout = findViewById(R.id.adRLayout);
    }

    private void initToolBar() {
        mToolBar = findViewById(R.id.toolBar);
        mToolBar.setTitle("微信清理");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (cleanWechatTask != null) {
            cleanWechatTask.shutNowDownExecutor();
            cleanWechatTask.cancel(true);
        }
    }

    private void handleBgStatus(int size) {

        if (size < 30000000) {
            vLine.setBackgroundResource(R.drawable.clear_blue_selector);

            waveHeader.setStartColor(getResources().getColor(R.color.c_0043ff));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_008dff));

        } else if (size < 50000000) {
            vLine.setBackgroundResource(R.drawable.clear_orange_shape);

            waveHeader.setStartColor(getResources().getColor(R.color.c_FFE700));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_CA7A03));
        } else if (size < 100000000) {
            vLine.setBackgroundResource(R.drawable.clear_orange_shape);

            waveHeader.setStartColor(getResources().getColor(R.color.c_FFE700));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_CA7A03));
        } else {
            vLine.setBackgroundResource(R.drawable.clear_orange_shape);

            waveHeader.setStartColor(getResources().getColor(R.color.c_FFE700));
            waveHeader.setCloseColor(getResources().getColor(R.color.c_CA7A03));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        clearWechatWrap = null;
        if (clearPresenter != null) {
            clearPresenter.detach();
        }

//        scanAnimatorContainer.stop();
    }

    private void computeAllSize(long allSize, boolean isLoading, boolean isHandleClearBtn) {
        String[] fileSize0 = FileUtil.getFileSize0(allSize);
        tvNum.setText(fileSize0[0]);
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
        btnClear.setText("放心清理（" + fileSize0[0] + fileSize0[1] + "）");
    }

    //    @Override
//    public boolean onSupportNavigateUp() {
//        finish();
//        return super.onSupportNavigateUp();
//    }
}
