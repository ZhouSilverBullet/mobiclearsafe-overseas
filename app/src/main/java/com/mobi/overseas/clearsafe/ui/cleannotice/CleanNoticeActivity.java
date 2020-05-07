package com.mobi.overseas.clearsafe.ui.cleannotice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.ui.cleannotice.adapter.CleanNoticeAdapter;
import com.mobi.overseas.clearsafe.ui.cleannotice.control.CleanNoticeManager;
import com.mobi.overseas.clearsafe.ui.cleannotice.data.CleanNoticeBean;
import com.mobi.overseas.clearsafe.ui.cleannotice.util.CleanNoticeUtil;
import com.mobi.overseas.clearsafe.ui.clear.control.CountDownTimerWrap;
import com.mobi.overseas.clearsafe.ui.clear.control.SingleClickListener;
import com.mobi.overseas.clearsafe.ui.clear.dialog.CleanDialogWarp;
import com.mobi.overseas.clearsafe.ui.clear.fragment.ShowGoodFragment;
import com.mobi.overseas.clearsafe.ui.clear.util.GarbageTextAnimUtil;
import com.mobi.overseas.clearsafe.ui.common.Bugs;
import com.mobi.overseas.clearsafe.ui.common.base.BaseActivity;
import com.mobi.overseas.clearsafe.ui.common.util.TypefaceUtil;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.widget.NoPaddingTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class CleanNoticeActivity extends BaseActivity implements CleanNoticeManager.INoticeCallback, CountDownTimerWrap.ICountTimerListener {

    @BindView(R.id.flTop)
    FrameLayout flTop;
    @BindView(R.id.tvCount)
    NoPaddingTextView tvCount;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.tvPowerDuration)
    TextView tvPowerDuration;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    @BindView(R.id.flContainer)
    FrameLayout flContainer;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    private CleanNoticeAdapter mCleanNoticeAdapter;
    private CountDownTimerWrap countDownTimerWrap;
    private int mItemCount;
    /**
     * 如果正在清理，就不在接受新的通知了
     */
    private boolean isClearing;

    public static void start(Context context, int cleanId) {
        Intent intent = new Intent(context, CleanNoticeActivity.class);
        intent.putExtra("cleanId", cleanId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_clean_notice;
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

        mCleanNoticeAdapter = new CleanNoticeAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mCleanNoticeAdapter);

        SlideInLeftAnimator animator = new SlideInLeftAnimator();
        animator.setAddDuration(300);
        animator.setRemoveDuration(300);
        animator.setChangeDuration(300);
        animator.setMoveDuration(300);
        rv.setItemAnimator(animator);

        mCleanNoticeAdapter.setEmptyView(R.layout.clean_notice_empty_view, rv);

        //小于19的api不支持
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            ToastUtils.showShort(R.string.phoneNotSupportFeature);
        }

        if (!notificationListenerEnable()) {
            showConfirmDialog();
        } else {
            handleNoticeData();
        }

//        NotificationManagerCompat.from(this).cancelAll();
        CleanNoticeManager.getInstance().setNoticeCallback(this);
    }

    @Override
    public void initEvent() {
        btnClear.setOnClickListener(new SingleClickListener(1200) {
            @Override
            public void onSingleClick(View v) {
                execCleanNotice();
            }
        });
    }

    @Bugs("java.lang.IndexOutOfBoundsException: Inconsistency detected")
    private void execCleanNotice() {

        isClearing = true;

        //执行动画的时候recyclerview滑动到顶部
        rv.scrollToPosition(0);
        //不能滑动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv.setLayoutManager(linearLayoutManager);
//        rv.setAdapter(mCleanNoticeAdapter);

        llBottomAnim();

        //下面执行countDown逻辑
        if (countDownTimerWrap != null) {
            countDownTimerWrap.cancel();
        }
        //这个在清理完成后需要获取显示
        mItemCount = mCleanNoticeAdapter.getItemCount();
        long millisInFuture = mItemCount > 4 ? 4 * 800 : mItemCount * 800;

        countDownTimerWrap = new CountDownTimerWrap(millisInFuture, 800);
        countDownTimerWrap.setCountTimerListener(this);
        countDownTimerWrap.start();

        //执行文字的动画
        GarbageTextAnimUtil.addTextViewAddAnim(tvCount, mItemCount, 0, millisInFuture);

        CleanNoticeManager.getInstance().clearAll();
    }

    private void llBottomAnim() {
        rv.setPadding(0, 0, 0, 0);
        llBottom.post(() -> {
            ViewCompat.animate(llBottom)
                    .translationY(llBottom.getHeight())
                    .setDuration(500)
                    .start();
        });
    }

    private void handleNoticeData() {
        if (isFinishing()) {
            return;
        }

        //已经点下了清理按钮
        if (isClearing) {
            return;
        }

        List<CleanNoticeBean> noticeBean = CleanNoticeManager.getInstance().getNoticeBean();
        mCleanNoticeAdapter.replaceData(noticeBean);

        if (mCleanNoticeAdapter.getData().isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            llBottom.setVisibility(View.VISIBLE);
        }

        //值显示上去
        int size = noticeBean.size();
        tvCount.setText(String.valueOf(size));
        tvPowerDuration.setVisibility(size == 0 ? View.GONE : View.VISIBLE);
    }

    private void showConfirmDialog() {
        new CleanDialogWarp
                .Builder(this)
                .setTitle(MyApplication.getResString(R.string.permissionsPrompt))
                .setLeftButton(MyApplication.getResString(R.string.cancel))
                .setRightButton(MyApplication.getResString(R.string.toOpen))
                .setContent(MyApplication.getResString(R.string.toOpenDec))
                .setRightListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CleanNoticeUtil.gotoNotificationAccessSetting(v.getContext());
                    }
                })
                .build()
                .show();

    }

    private boolean notificationListenerEnable() {
        boolean enable = false;
        String packageName = getPackageName();
        String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (null != flat) {
            enable = flat.contains(packageName);
        }
        return enable;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        CleanNoticeManager.getInstance().setNoticeCallback(null);

        if (countDownTimerWrap != null) {
            countDownTimerWrap.setCancel(true);
            countDownTimerWrap.cancel();
        }
    }

    @Override
    public void onNotificationNotify() {
        handleNoticeData();
    }

    @Override
    public void onTime(String hour, String minute, String second) {
        //删到最后一个等待删除，不然就没有了
        if (mCleanNoticeAdapter != null && mCleanNoticeAdapter.getData().size() > 1) {
            mCleanNoticeAdapter.getData().remove(0);
            mCleanNoticeAdapter.notifyItemRemoved(0);
        } else if (mCleanNoticeAdapter != null) {
            mCleanNoticeAdapter.replaceData(new ArrayList<>());
        }
    }

    @Override
    public void onTimeFinish() {
        //删到最后一个等待删除，不然就没有了
        if (mCleanNoticeAdapter != null) {

            if (mCleanNoticeAdapter.getData().size() > 0) {
                mCleanNoticeAdapter.getData().clear();
                mCleanNoticeAdapter.notifyItemRangeRemoved(0, mCleanNoticeAdapter.getData().size() - 1);
            }

            rv.postDelayed(() -> {

                mCleanNoticeAdapter.replaceData(new ArrayList<>());

                if (isFinishing()) {
                    return;
                }

                showGoodFragment();

            }, 300);
        }
    }

    private void showGoodFragment() {
        flContainer.setVisibility(View.VISIBLE);
        getIntent().putExtra("dec", String.format(MyApplication.getResString(R.string.cleanSuccessNotice), mItemCount));
        getIntent().putExtra("dec2",MyApplication.getResString(R.string.cleanSuccessNoticeStatus));
        ShowGoodFragment fragment = ShowGoodFragment.newInstance(false);
        fragment.setFirstGood(false);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flContainer, fragment)
                .commit();
    }
}
