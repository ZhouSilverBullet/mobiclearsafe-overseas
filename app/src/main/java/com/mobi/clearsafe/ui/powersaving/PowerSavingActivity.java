package com.mobi.clearsafe.ui.powersaving;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.ui.clear.control.CountDownTimerWrap;
import com.mobi.clearsafe.ui.clear.control.ProcessManager;
import com.mobi.clearsafe.ui.clear.control.SingleClickListener;
import com.mobi.clearsafe.ui.clear.fragment.ShowGoodFragment;
import com.mobi.clearsafe.ui.clear.util.GarbageTextAnimUtil;
import com.mobi.clearsafe.ui.common.base.BaseActivity;
import com.mobi.clearsafe.ui.common.global.AppGlobalConfig;
import com.mobi.clearsafe.ui.common.util.TypefaceUtil;
import com.mobi.clearsafe.ui.powersaving.adapter.PowerSavingAdapter;
import com.mobi.clearsafe.ui.powersaving.data.PowerSavingBean;
import com.mobi.clearsafe.ui.powersaving.presenter.PowerPresenter;
import com.mobi.clearsafe.ui.powersaving.util.BatteryStatsUtil;
import com.mobi.clearsafe.ui.powersaving.widget.PowerScanLayout;
import com.mobi.clearsafe.widget.NoPaddingTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class PowerSavingActivity extends BaseActivity implements CountDownTimerWrap.ICountTimerListener, PowerPresenter.IView {

    public static final String TAG = "PowerSavingActivity";

    @BindView(R.id.tvCount)
    NoPaddingTextView tvCount;
    @BindView(R.id.tvSleep)
    TextView tvSleep;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.tvPowerDuration)
    TextView tvPowerDuration;
    @BindView(R.id.rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @BindView(R.id.pslLayout)
    PowerScanLayout pslLayout;
    @BindView(R.id.flContainer)
    FrameLayout flContainer;

    private PowerSavingAdapter mPowerSavingAdapter;
    private CountDownTimerWrap countDownTimerWrap;


    //用来保存执行的动画
    //防止动画内存泄漏
    private List<Animator> mSaveAnimator = new ArrayList<>();

    private PowerPresenter mPowerPresenter;
    private int mItemCount;

    public static void start(Context context, int cleanId) {
        Intent intent = new Intent(context, PowerSavingActivity.class);
        intent.putExtra("cleanId", cleanId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_power_saving;
    }

    @Override
    protected boolean isFitWindow() {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void initView() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Typeface mtypeface = TypefaceUtil.getTypeFace(this, "hyt.ttf");
        tvCount.setTypeface(mtypeface);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPowerSavingAdapter = new PowerSavingAdapter();
        mRecyclerView.setAdapter(mPowerSavingAdapter);

        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setAddDuration(300);
        animator.setRemoveDuration(300);
        animator.setChangeDuration(300);
        animator.setMoveDuration(300);
        mRecyclerView.setItemAnimator(animator);

        mPowerPresenter = new PowerPresenter();
        mPowerPresenter.attach(this);

    }

    @Override
    public void initEvent() {
        btnClear.setOnClickListener(new SingleClickListener(1200) {
            @Override
            public void onSingleClick(View v) {
                clearPower();
            }
        });
    }

    private void clearPower() {

        ButtonStatistical.batteryClearButtonClick();

        mPowerSavingAdapter.replaceToRemoveData();
        //执行动画的时候recyclerview滑动到顶部
        mRecyclerView.scrollToPosition(0);
        //不能滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //把下面clear按钮隐藏
        llBottomAnim();

        //下面执行countDown逻辑
        if (countDownTimerWrap != null) {
            countDownTimerWrap.cancel();
        }
        //这个在清理完成后需要获取显示
        mItemCount = mPowerSavingAdapter.getItemCount();
        long millisInFuture = mItemCount > 4 ? 4 * 800 : mItemCount * 800;

        countDownTimerWrap = new CountDownTimerWrap(millisInFuture, 800);
        countDownTimerWrap.setCountTimerListener(this);
        countDownTimerWrap.start();

        //执行文字的动画
        GarbageTextAnimUtil.addTextViewAddAnim(tvCount, mItemCount, 0, millisInFuture);
    }

    @Override
    public void initData() {

        if (mPowerPresenter != null) {
            mPowerPresenter.calculatePowerSaving();
        }

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            if (isFinishing()) {
                return;
            }
            pslLayout.setProgress(animatedValue);
        });
        valueAnimator.start();
    }

    @Override
    public void onPowerSavingList(long currentTime, List<PowerSavingBean> list) {
        runOnUiThread(() -> {

            if (isFinishing()) {
                return;
            }

            //做一个延迟显示
            long delay = 5000 - currentTime > 0 ? 5000 - currentTime : 100;
            pslLayout.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (isFinishing()) {
                        return;
                    }

                    pslLayout.setVisibility(View.GONE);
                    GarbageTextAnimUtil.addTextViewAddAnim(tvCount, 0, list.size(), 800);
                }
            }, delay);

            long l = BatteryStatsUtil.getInstance().computeChargeTimeRemaining();
            Log.i(TAG, "computeChargeTimeRemaining: " + l);

            long time = list.size() * 60 * 1000;

            SimpleDateFormat sdf = new SimpleDateFormat("优化后延长待机hh小时mm分钟", Locale.CHINA);
            String format = sdf.format(time);
            tvPowerDuration.setText(format);
            mPowerSavingAdapter.replaceData(list);
        });
    }

    @Override
    public void onTwoDrawable(Drawable drawable1, Drawable drawable2) {
        runOnUiThread(() -> {

            if (isFinishing()) {
                return;
            }
            //值设置上去
            pslLayout.setTwoDrawable(drawable1, drawable2);

        });
    }

    @Override
    public boolean isViewFinishing() {
        return isFinishing();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimerWrap != null) {
            countDownTimerWrap.setCancel(true);
            countDownTimerWrap.cancel();
        }

        if (mPowerPresenter != null) {
            mPowerPresenter.detach();
        }
    }

    private void llBottomAnim() {
        mRecyclerView.setPadding(0, 0, 0, 0);
        llBottom.post(() -> {
            ViewCompat.animate(llBottom)
                    .translationY(llBottom.getHeight())
                    .setDuration(500)
                    .start();
        });
    }

    @Override
    public void onTime(String hour, String minute, String second) {
        //删到最后一个等待删除，不然就没有了
        if (mPowerSavingAdapter != null && mPowerSavingAdapter.getData().size() > 1) {
            mPowerSavingAdapter.getData().remove(0);
            mPowerSavingAdapter.notifyItemRemoved(0);
        } else if (mPowerSavingAdapter != null) {
            mPowerSavingAdapter.replaceData(new ArrayList<>());
        }
    }

    @Override
    public void onTimeFinish() {
        //删到最后一个等待删除，不然就没有了
        if (mPowerSavingAdapter != null) {
            if (mPowerSavingAdapter.getData().size() > 0) {
                mPowerSavingAdapter.getData().clear();
                mPowerSavingAdapter.notifyItemRangeRemoved(0, mPowerSavingAdapter.getData().size() - 1);
            }

            mRecyclerView.postDelayed(() -> {

                mPowerSavingAdapter.replaceData(new ArrayList<>());

                if (isFinishing()) {
                    return;
                }

                showGoodFragment();

            }, 300);
        }
    }

    private void showGoodFragment() {
        flContainer.setVisibility(View.VISIBLE);
        getIntent().putExtra("dec", "已成功休眠" + mItemCount + "款应用");
        getIntent().putExtra("dec2", "电量已达最佳状态");
        ShowGoodFragment fragment = ShowGoodFragment.newInstance(false);
        fragment.setFirstGood(false);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flContainer, fragment)
                .commit();
    }
}
