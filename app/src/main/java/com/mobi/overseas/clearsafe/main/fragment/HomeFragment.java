package com.mobi.overseas.clearsafe.main.fragment;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.MainActivity;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.eventbean.CheckTabEvent;
import com.mobi.overseas.clearsafe.main.BannerBean;
import com.mobi.overseas.clearsafe.main.adapter.HomeAdapter;
import com.mobi.overseas.clearsafe.main.adapter.data.ClearBean;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.ui.appwiget.CleanAppWidget;
import com.mobi.overseas.clearsafe.ui.cleannotice.CleanNoticeActivity;
import com.mobi.overseas.clearsafe.ui.clear.CleanBigFileActivity;
import com.mobi.overseas.clearsafe.ui.clear.CleanQReportActivity;
import com.mobi.overseas.clearsafe.ui.clear.GarbageActivity;
import com.mobi.overseas.clearsafe.ui.clear.control.ProcessManager;
import com.mobi.overseas.clearsafe.ui.clear.control.SingleClickListener;
import com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanQQTask;
import com.mobi.overseas.clearsafe.ui.clear.control.scan.CleanWechatTask;
import com.mobi.overseas.clearsafe.ui.clear.control.scan.IScanCallback;
import com.mobi.overseas.clearsafe.ui.clear.control.scan.MemoryTask;
import com.mobi.overseas.clearsafe.ui.clear.data.WechatBean;
import com.mobi.overseas.clearsafe.ui.clear.entity.CleanEvent;
import com.mobi.overseas.clearsafe.ui.clear.entity.CleanListBean;
import com.mobi.overseas.clearsafe.ui.clear.util.FileUtil;
import com.mobi.overseas.clearsafe.ui.common.Bugs;
import com.mobi.overseas.clearsafe.ui.common.global.AppGlobalConfig;
import com.mobi.overseas.clearsafe.ui.common.util.SpUtil;
import com.mobi.overseas.clearsafe.ui.common.util.SpannableStringBuilderUtil;
import com.mobi.overseas.clearsafe.ui.common.util.StatusBarPaddingUtil;
import com.mobi.overseas.clearsafe.ui.common.util.TypefaceUtil;
import com.mobi.overseas.clearsafe.ui.repair.PermissionRepairActivity;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.imageloader.ImageLoader;
import com.mobi.overseas.clearsafe.widget.LazyLoadFragment;
import com.mobi.overseas.clearsafe.widget.LoadWebViewActivity;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static com.mobi.overseas.clearsafe.ui.appwiget.CleanSpeedService.REFRESH_APP_WIDGET;

public class HomeFragment extends LazyLoadFragment implements Handler.Callback, HomeFragPresenter.IView {
    public static final String TAG = "HomeFragment";

    public static final int H_WX_CACHE = 10;
    public static final int H_QQ_CACHE = 11;

    public static final int HOME_RIGHT_IMAGE_ANIM = 101;
    public static final int HOME_RIGHT_IMAGE_ANIM_DURATION = 5000;

    private RecyclerView rv;
    private HomeAdapter homeAdapter;
    private Button btnSkipClear;
    private Handler handler;
    private View vLine;
    private TextView tvPoint;
    private TextView tvStatus;
    private HomeFragmentWrap homeFragmentWrap;

    private AtomicLong wechatAllSize = new AtomicLong(0);
    private AtomicLong qqAllSize = new AtomicLong(0);

    private volatile boolean isFindCache = false;
    private volatile boolean isWxTaskStarted = false;
    private volatile boolean isQQTaskStarted = false;
    private ProcessManager processManager;
    private NestedScrollView nsvScroll;
    private Toolbar mToolBar;

    public static final int BlueColor = 0xFF0062FF;
    public static final int OrengeColor = 0xFFFF4A00;

    //toolbar的颜色
    private int mToolbarColor = BlueColor;
    private int mCurrentScrollY = 0;
    private HomeFragPresenter mHomeFragPresenter;
    private Context mContext;
    private ImageView mIbRightSkip;
    private ImageView ivFloat;
    private TextView tvRightHint;

    //用来存储服务端请求回来的数据
    private List<CleanListBean.ListBean> mCleanListBeanList;

    public HomeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void lazyLoad() {
//        findCache();
        StatusBarPaddingUtil.statusBar(getActivity(), false);

        if (homeFragmentWrap != null) {
            homeFragmentWrap.handleBubble();
        }

        getCleanList();

        //检测一下权限
        if (mHomeFragPresenter != null) {
            mHomeFragPresenter.findNeedOpenPermission();
        }
    }

    @Override
    protected void firstIn() {
        StatusBarPaddingUtil.statusBar(getActivity(), false);
        initView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        findViewById(R.id.hcivBigfile).setOnClickListener(v -> {
            ButtonStatistical.homeFeatureClick("CleanBigFile");
            CleanBigFileActivity.start(v.getContext(), homeAdapter.getSkipId(5));
        });
        findViewById(R.id.hcivQQ).setOnClickListener(v -> {
            ButtonStatistical.homeFeatureClick("CleanQReport");
            CleanQReportActivity.start(v.getContext(), 0);
        });
        findViewById(R.id.hcivNotice).setOnClickListener(v -> {
            ButtonStatistical.homeFeatureClick("CleanNotice");
            CleanNoticeActivity.start(v.getContext(), homeAdapter.getSkipId(4));
        });

        startShakeByViewAnim();
    }

    private void initView() {
        if (handler == null) {
            handler = new Handler(this);
        }

        handler.sendEmptyMessageDelayed(100, Const.CURRENT_MINUTE);
        handler.sendEmptyMessageDelayed(HOME_RIGHT_IMAGE_ANIM, HOME_RIGHT_IMAGE_ANIM_DURATION);

        vLine = findViewById(R.id.vLine);
        tvPoint = findViewById(R.id.tvPoint);

        if (getActivity() != null) {
            Typeface mtypeface = TypefaceUtil.getTypeFace(getActivity(), "hyt.ttf");
            tvPoint.setTypeface(mtypeface);
        }

        tvStatus = findViewById(R.id.tvStatus);
        ivFloat = findViewById(R.id.iv_float);
        tvRightHint = findViewById(R.id.tvRightHint);

        mIbRightSkip = findViewById(R.id.ibRightSkip);
        mIbRightSkip.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                PermissionRepairActivity.start(v.getContext(), 0);
            }
        });

        tvRightHint.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                PermissionRepairActivity.start(v.getContext(), 0);
            }
        });

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
//        rv.addItemDecoration(new GridDividerItemDecoration(0));
        rv.setNestedScrollingEnabled(false);
        homeAdapter = new HomeAdapter();
        rv.setAdapter(homeAdapter);

        List<ClearBean> list = ClearBean.getRvGridData();
        homeAdapter.addData(list);

        btnSkipClear = findViewById(R.id.btnSkipClear);

        if (mHomeFragPresenter == null) {
            mHomeFragPresenter = new HomeFragPresenter(this);
        }

        findCache();
//        StatusBarUtil.setStatusBarColor(getActivity(), R.color.c_008dff);
        getCleanList();

        RelativeLayout bubble1 = findViewById(R.id.bubble1);
        TextView bubble1Text = findViewById(R.id.bubble1_text);
        TextView bubble1Content = findViewById(R.id.bubble1_content);

        RelativeLayout bubble2 = findViewById(R.id.bubble2);
        TextView bubble2Text = findViewById(R.id.bubble2_text);
        TextView bubble2Content = findViewById(R.id.bubble2_content);

        RelativeLayout bubble3 = findViewById(R.id.bubble3);
        TextView bubble3Text = findViewById(R.id.bubble3_text);
        TextView bubble3Content = findViewById(R.id.bubble3_content);

        RelativeLayout bubble4 = findViewById(R.id.bubble4);
        TextView bubble4Text = findViewById(R.id.bubble4_text);


        homeFragmentWrap = new HomeFragmentWrap(bubble1, bubble1Text, bubble2, bubble2Text, bubble3, bubble3Text,
                bubble4, bubble4Text, bubble1Content, bubble2Content, bubble3Content);
        homeFragmentWrap.setActivity(getActivity());
        homeFragmentWrap.handleBubble();


        mToolBar = findViewById(R.id.toolBar);
        //bar设置上
        StatusBarPaddingUtil.topViewPadding(mToolBar);

        nsvScroll = findViewById(R.id.nsvScroll);
        nsvScroll.setOnScrollChangeListener((NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) -> {
            Log.e(TAG, "scrollX: " + scrollX + " scrollY: " + scrollY + " oldScrollX: " + oldScrollX + " oldScrollY: " + oldScrollY);

            handleScrollY(scrollY);
        });


        btnSkipClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonStatistical.homeFeatureClick("Garbage");
                GarbageActivity.start(v.getContext(), 0);
            }
        });

    }

    /**
     * 默认scrollY<300左右比较合适，不过首页先这样用0
     * 就不透明了
     *
     * @param scrollY
     */
    private void handleScrollY(int scrollY) {
        if (!isAdded()) {
            return;
        }
        mCurrentScrollY = scrollY;
        if (scrollY < 0) {
            mToolBar.setBackgroundColor(getResources().getColor(R.color.transparent));
        } else {
            mToolBar.setBackgroundColor(mToolbarColor);
        }
    }

    public void findCache() {
        //做一下权限判断
        if (getActivity() == null) {
            return;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (isFindCache) {
            return;
        }

        if (isWxTaskStarted || isQQTaskStarted) {
            return;
        }

        qqAllSize.set(0);
        wechatAllSize.set(0);

        new CleanWechatTask(new IScanCallback<WechatBean>() {
            @Override
            public void onBegin() {
                isWxTaskStarted = true;
            }

            @Override
            public void onProgress(WechatBean info) {
                wechatAllSize.set(wechatAllSize.get() + info.fileSize);
                if (mContext instanceof Activity && isAdded() && !isDetached()) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String[] fileSizeStr = FileUtil.getFileSize0(wechatAllSize.get());
                            handleFindData(fileSizeStr[0], fileSizeStr[0] + fileSizeStr[1], 1);
//                        computeAllSize(allSize.get());
                            int size = (int) (qqAllSize.get() + wechatAllSize.get() >= Integer.MAX_VALUE ? Integer.MAX_VALUE : wechatAllSize.get() + qqAllSize.get());
                            handleBgStatus(size);
                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                isWxTaskStarted = false;
                if (mContext instanceof Activity && isAdded() && !isDetached()) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            String[] fileSizeStr = FileUtil.getFileSize0(wechatAllSize.get());
                            handleFindData(fileSizeStr[0], fileSizeStr[0] + fileSizeStr[1], 1);
//                        computeAllSize(allSize.get());
                            int size = (int) (qqAllSize.get() + wechatAllSize.get() >= Integer.MAX_VALUE ? Integer.MAX_VALUE : wechatAllSize.get() + qqAllSize.get());
                            handleBgStatus(size);

                            if (!isQQTaskStarted && !isWxTaskStarted) {
                                //刷一下接口
                                getCleanList();
                                if (homeFragmentWrap != null) {
                                    homeFragmentWrap.handleBubble();
                                }
                            }
                        }
                    });
                }
            }
        }).execute();

        new CleanQQTask(new IScanCallback<WechatBean>() {
            @Override
            public void onBegin() {
                isQQTaskStarted = true;
            }

            @Override
            public void onProgress(WechatBean info) {
                qqAllSize.set(qqAllSize.get() + info.fileSize);
                if (mContext instanceof Activity && isAdded()) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String[] fileSizeStr = FileUtil.getFileSize0(qqAllSize.get());
                            handleFindData(fileSizeStr[0], fileSizeStr[0] + fileSizeStr[1], 2);
//                        computeAllSize(allSize.get());
                            int size = (int) (qqAllSize.get() + wechatAllSize.get() >= Integer.MAX_VALUE ? Integer.MAX_VALUE : wechatAllSize.get() + qqAllSize.get());
                            handleBgStatus(size);
                        }
                    });
                }
            }

            @Override
            public void onFinish() {
                isQQTaskStarted = false;
                if (mContext instanceof Activity && isAdded()) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String[] fileSizeStr = FileUtil.getFileSize0(qqAllSize.get());
                            handleFindData(fileSizeStr[0], fileSizeStr[0] + fileSizeStr[1], 2);
//                        computeAllSize(allSize.get());
                            int size = (int) (qqAllSize.get() + wechatAllSize.get() >= Integer.MAX_VALUE ? Integer.MAX_VALUE : wechatAllSize.get() + qqAllSize.get());
                            handleBgStatus(size);

                            if (!isQQTaskStarted && !isWxTaskStarted) {
                                //刷一下接口
                                getCleanList();
                                if (homeFragmentWrap != null) {
                                    homeFragmentWrap.handleBubble();
                                }
                            }
                        }
                    });
                }
            }
        }).execute();

        isFindCache = true;

        findMemoryCache();

        processManager = ProcessManager.getInstance();
        processManager.setProcessMemoryListener(memoryValue -> {
//            ClearBean clearBean = homeAdapter.getData().get(0);
//            clearBean.memoryValue = memoryValue;
//            if (memoryValue == 0) {
//                clearBean.dec = "一键清理，释放空间";
//                clearBean.color = R.color.c_999999;
//                homeAdapter.notifyDataSetChanged();
//            } else {
//                clearBean.dec = "一键清理" + memoryValue + "M，释放空间";
//
//                if (memoryValue > 150) {
//                    clearBean.color = R.color.c_F49F1F;
//                } else {
//                    clearBean.color = R.color.c_999999;
//                }
//
//                homeAdapter.notifyDataSetChanged();
//            }
        });


        //检测一下权限
        if (mHomeFragPresenter != null) {
            mHomeFragPresenter.findNeedOpenPermission();
        }

    }

    private void findMemoryCache() {
        new MemoryTask(new MemoryTask.IMemoryCallback() {
            @Override
            public void onPercent(long percent) {
                if (mContext instanceof Activity && isAdded() && !isDetached()) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                        Log.i(TAG, "内存为" + percent + "%");
//                        ToastUtils.showLong("内存为" + percent + "%");
                            long speedClearTime = SpUtil.getLong(Const.SPEED_CAN_CLEAR, 0L);
                            ClearBean clearBean = homeAdapter.getData().get(0);
                            //如果清理时间超过了5分钟了，就继续显示内存使用了多少
                            if (System.currentTimeMillis() - speedClearTime > Const.CURRENT_MINUTE) {
                                clearBean.isClear = false;
                                clearBean.color = R.color.c_F49F1F;
                            } else {
                                clearBean.isClear = true;
                                clearBean.color = R.color.black_33;
                            }

                            clearBean.memoryValue = (int) percent;
//                            clearBean.dec = "已用" + percent + "%内存"
                            clearBean.dec = String.format(getResources().getString(R.string.memoryIsUsed), (int) percent);

                            ((MainActivity) mContext).setMemoryValue(clearBean.memoryValue);

                            homeAdapter.notifyDataSetChanged();

                            SpUtil.putInt(Const.SPEED_TEMP_VALUE, (int) percent);
                        }
                    });
                }
            }
        }).executeOnExecutor(AppGlobalConfig.APP_THREAD_POOL_EXECUTOR);
    }

    public void getCleanList() {
        if (getActivity() == null) {
            //当确认权限后访问的时候出现的问题
            return;
        }

        if (mHomeFragPresenter != null) {
            mHomeFragPresenter.getBanner();
        }

        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getCleanList()
                .compose(CommonSchedulers.<BaseResponse<CleanListBean>>observableIO2Main(getActivity()))
                .subscribe(new BaseObserver<CleanListBean>() {
                    @Override
                    public void onSuccess(CleanListBean data) {
                        if (data != null) {
                            handleData(data);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
//                        ToastUtils.showShort(errorMsg);
                    }
                });
    }

    private void handleData(CleanListBean data) {
        mCleanListBeanList = data.getList();
        if (mCleanListBeanList.size() != 7) {
            return;
        }

        List<ClearBean> dataList = homeAdapter.getData();

        for (int i = 0; i < dataList.size(); i++) {
            CleanListBean.ListBean listBean = mCleanListBeanList.get(i + 1);
            ClearBean clearBean = dataList.get(i);
            clearBean.id = listBean.getId();
            clearBean.points = listBean.getPoints();
        }

        homeAdapter.notifyDataSetChanged();

        CleanListBean.ListBean listBean = mCleanListBeanList.get(0);
        btnSkipClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GarbageActivity.start(v.getContext(), listBean.getId());
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case 100: {
                //刷新内存使用
//              findMemoryCache();
                //每次都延迟5分钟刷新一下homeFragment的数据
                findCache();
                //继续发5分钟的刷新
                handler.sendEmptyMessageDelayed(100, Const.CURRENT_MINUTE);
            }
            break;
            case HOME_RIGHT_IMAGE_ANIM: {
                startShakeByViewAnim();
                handler.sendEmptyMessageDelayed(HOME_RIGHT_IMAGE_ANIM, HOME_RIGHT_IMAGE_ANIM_DURATION);
            }
        }


        return false;
    }

    @Bugs("java.lang.IllegalStateException: Fragment HomeFragment{767ad8e} not attached to a context")
    private void handleBgStatus(int size) {
        if (!isAdded()) {
            return;
        }
        //加一个很健康的逻辑
        long garbageCanTime = SpUtil.getLong(Const.GARBAGE_CAN_CLEAR, 0L);
        if (System.currentTimeMillis() - garbageCanTime < Const.CURRENT_MINUTE) {
            vLine.setBackgroundResource(R.drawable.clear_blue_selector);
            int point = 100;
            tvPoint.setText(String.valueOf(point));
            btnSkipClear.setTextColor(getResources().getColor(R.color.c_2B86FF));
            btnSkipClear.setBackgroundResource(R.drawable.btn_home_clear_selector);
            tvStatus.setText(getResources().getString(R.string.goodHealthy));
            mToolbarColor = BlueColor;
            handleScrollY(mCurrentScrollY);
            sendWidgetBroadcastNotify(point);
            return;
        }

        int randomValue = new Random().nextInt(16);
        if (size < 30000000) {
            vLine.setBackgroundResource(R.drawable.clear_orange_shape);
            int point = 70 + randomValue;
            tvPoint.setText(String.valueOf(point));
            btnSkipClear.setTextColor(getResources().getColor(R.color.c_FF4A00));
            btnSkipClear.setBackgroundResource(R.drawable.btn_home_clear_orange_selector);
            tvStatus.setText(getResources().getString(R.string.subHealthy));
            mToolbarColor = OrengeColor;
            handleScrollY(mCurrentScrollY);

            sendWidgetBroadcastNotify(point);
        }
//        else if (size < 50000000) {
//            vLine.setBackgroundResource(R.drawable.clear_orange_shape);
//            int point = 60 + randomValue;
//            tvPoint.setText(String.valueOf(point));
//            btnSkipClear.setTextColor(getResources().getColor(R.color.c_FF4A00));
//            btnSkipClear.setBackgroundResource(R.drawable.btn_home_clear_orange_selector);
//            tvStatus.setText("不健康");
//
//            mToolbarColor = R.color.c_FF7400;
//        } else if (size < 100000000) {
//            vLine.setBackgroundResource(R.drawable.clear_orange_shape);
//            int point = 40 + randomValue;
//            tvPoint.setText(String.valueOf(point));
//            btnSkipClear.setTextColor(getResources().getColor(R.color.c_FF4A00));
//            btnSkipClear.setBackgroundResource(R.drawable.btn_home_clear_orange_selector);
//            tvStatus.setText("超负荷");
//
//            mToolbarColor = R.color.c_FF7400;
//        }
        else {
            randomValue = new Random().nextInt(10);
//            vLine.setBackgroundResource(R.drawable.clear_orange_shape);
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

                    mToolbarColor = animatedValue;
                    handleScrollY(mCurrentScrollY);
                }
            });

            vLine.setTag(valueAnimator);
            valueAnimator.start();

            int point = 60 + randomValue;
            tvPoint.setText(String.valueOf(point));
            btnSkipClear.setTextColor(getResources().getColor(R.color.c_FF4A00));
            btnSkipClear.setBackgroundResource(R.drawable.btn_home_clear_orange_selector);
            tvStatus.setText(getResources().getString(R.string.overloadHealthy));

            sendWidgetBroadcastNotify(point);
//            mToolbarColor = R.color.c_FF7400;
        }

    }

    private void sendWidgetBroadcastNotify(int point) {
        if (!isAdded() || tvStatus == null) {
            return;
        }

        Intent intent = new Intent(REFRESH_APP_WIDGET);
        intent.putExtra("progress", (int) point);
        String status = tvStatus.getText().toString();
        intent.putExtra("status", status);
        intent.setComponent(new ComponentName(MyApplication.getContext(), CleanAppWidget.class));
        MyApplication.getContext().sendBroadcast(intent);

        SpUtil.putInt(Const.HOME_POINT_TEMP_VALUE, point);
        SpUtil.putString(Const.HOME_POINT_TEMP_STATUS, status);
    }

    private void handleFindData(String strSize, String msg, int index) {
        double dSize = parseInteger(strSize);

        if (homeAdapter != null) {
            ClearBean clearBean = homeAdapter.getData().get(index);

            if (dSize == 0) {
                clearBean.dec = "一键清理，释放垃圾";
                clearBean.color = R.color.black_33;
                //是微信
                if (index == 1) {
//                    clearBean.icon = R.drawable.clean_icon_home_wechat;
                }
                clearBean.isClear = true;
                homeAdapter.notifyDataSetChanged();
            } else {
                //qq的不进行处理了，改成了
                if (index == 2) {
                    return;
                }
                //是微信
                if (index == 1) {//orenge
//                    clearBean.icon = R.drawable.clean_icon_home_wechat;
                }
                clearBean.isClear = false;

                clearBean.dec = "释放" + msg + "";
                clearBean.color = R.color.c_F49F1F;
                homeAdapter.notifyDataSetChanged();
            }

        }
    }

    public double parseInteger(String strSize) {
        try {
            return Double.parseDouble(strSize);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Subscribe
    public void clickBtnClear(CleanEvent event) {
        if (isFindCache) {
            isFindCache = false;
        }
        findCache();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (processManager != null) {
            processManager.removeMemoryListener();
        }
        handler.removeMessages(100);

        if (mHomeFragPresenter != null) {
            mHomeFragPresenter.detach();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("HomeFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("HomeFragment");

        if (mHomeFragPresenter != null) {
            mHomeFragPresenter.findNeedOpenPermission();
        }
    }

    //    private TextView title1;
//    private TextView title2;
//    private TextView des1;
//    private TextView des2;
    private TextView tvSafeDay;
    private ImageView imageOne;
    private ImageView imageTwo;

    @Override
    public void handleBannerData(int totalDays, List<BannerBean.HotActivityList> hotActivityListList, BannerBean.RedEnvelope envelope) {
        if (hotActivityListList.size() < 2) {
            return;
        }
        BannerBean.HotActivityList hotActivityList1 = hotActivityListList.get(0);
        BannerBean.HotActivityList hotActivityList2 = hotActivityListList.get(1);

        if (imageOne == null) {
//            title1 = findViewById(R.id.tv_title);
//            title2 = findViewById(R.id.tv_title2);
//            des1 = findViewById(R.id.tv_des);
//            des2 = findViewById(R.id.tv_des2);
            imageOne = findViewById(R.id.image_one);
            imageTwo = findViewById(R.id.image_two);
            tvSafeDay = findViewById(R.id.tvSafeDay);

            imageOne.setOnClickListener(v -> {
                mHomeFragPresenter.jump(hotActivityList1.judge_type, hotActivityList1.jump_url, hotActivityList1.params, null);
            });
            imageTwo.setOnClickListener(v -> {
                mHomeFragPresenter.jump(hotActivityList2.judge_type, hotActivityList2.jump_url, hotActivityList2.params, null);

            });

            tvSafeDay.setText(SpannableStringBuilderUtil.getSafeDayStr(totalDays));
            tvSafeDay.setVisibility(View.VISIBLE);
        }

        ImageLoader.loadImage(getContext(), imageOne, hotActivityList1.url);
//        title1.setText(hotActivityList1.name);
//        des1.setText(hotActivityList1.content);

        ImageLoader.loadImage(getContext(), imageTwo, hotActivityList2.url);
//        title2.setText(hotActivityList2.name);
//        des2.setText(hotActivityList2.content);

        if (envelope != null) {
            if (!TextUtils.isEmpty(envelope.url) && !TextUtils.isEmpty(envelope.jump_url)) {
                ivFloat.setVisibility(View.GONE);
                com.mobi.overseas.clearsafe.utils.imageloader.ImageLoader.loadImage(getContext(), ivFloat, envelope.url);
                ivFloat.setOnClickListener(v -> {
                    int jump_type = envelope.judge_type;
                    if (jump_type == 1) {//原生
                        AppUtil.startActivityFromAction(getContext(),
                                envelope.jump_url,
                                envelope.params);
                    } else if (jump_type == 2) {//H5
                        String jump_url = envelope.jump_url
                                + "?token=" + UserEntity.getInstance().getToken()
                                + "&user_id=" + UserEntity.getInstance().getUserId()
                                + "&version=" + AppUtil.packageName(MyApplication.getContext());
                        LoadWebViewActivity.IntoLoadWebView(getContext(), jump_url);
                    } else if (jump_type == 3) {
                        EventBus.getDefault().post(new CheckTabEvent(envelope.jump_url));
                    }
                });
            }
        }

    }

    @Override
    public void showRightSkip(boolean permissionAllEnabled) {
        mIbRightSkip.setVisibility(View.GONE);
        if (permissionAllEnabled) {
            tvRightHint.setVisibility(View.GONE);
            //清除动画和动画的handle
            tvRightHint.clearAnimation();
            handler.removeMessages(HOME_RIGHT_IMAGE_ANIM);
        } else {
            tvRightHint.setVisibility(View.VISIBLE);
        }
    }

    private void startShakeByViewAnim() {
        startShakeByViewAnim(mIbRightSkip, 1f, 1.2f, 10, 1000);
    }

    private void startShakeByViewAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性

        //由小变大
        Animation scaleAnim = new ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge);
        //从左向右
        Animation rotateAnim = new RotateAnimation(-shakeDegrees, shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnim.setDuration(duration);
        scaleAnim.setRepeatMode(Animation.REVERSE);

        rotateAnim.setDuration(duration / 10);
        rotateAnim.setRepeatMode(Animation.REVERSE);
        rotateAnim.setRepeatCount(10);

        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        smallAnimationSet.addAnimation(rotateAnim);
        smallAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (getActivity() == null || getActivity().isFinishing()) {
                    return;
                }
                if (!isAdded()) {
                    return;
                }

                startShakeByViewAnimRever(view, scaleSmall, scaleLarge, shakeDegrees, duration);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(smallAnimationSet);
    }

    private void startShakeByViewAnimRever(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }

        //由大到小
        Animation scaleAnim2 = new ScaleAnimation(scaleLarge, scaleSmall, scaleLarge, scaleSmall);
        //从左向右
        Animation rotateAnim2 = new RotateAnimation(shakeDegrees, -shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnim2.setDuration(duration);
        scaleAnim2.setRepeatMode(Animation.REVERSE);

        rotateAnim2.setDuration(duration / 10);
        rotateAnim2.setRepeatMode(Animation.REVERSE);
        rotateAnim2.setRepeatCount(10);

        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim2);
        smallAnimationSet.addAnimation(rotateAnim2);

        view.startAnimation(smallAnimationSet);
    }
}
