package com.mobi.clearsafe.ui.repair;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.nativeexpress.NativeExpressAd;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.example.adtest.rewardvideo.RewardVideoLoadListener;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.ad.RewardVideoLoadListenerImpl;
import com.mobi.clearsafe.ui.common.base.BaseActivity;
import com.mobi.clearsafe.ui.common.util.TypefaceUtil;
import com.mobi.clearsafe.ui.repair.adapter.PermissionRepairAdapter;
import com.mobi.clearsafe.ui.repair.data.CleanAuthBean;
import com.mobi.clearsafe.ui.repair.data.CleanAuthOpenBean;
import com.mobi.clearsafe.ui.repair.data.CleanAuthOutBean;
import com.mobi.clearsafe.ui.repair.data.PermissionRepairBean;
import com.mobi.clearsafe.ui.repair.presenter.PermissionRepairPresenter;
import com.mobi.clearsafe.utils.UiUtils;
import com.mobi.clearsafe.widget.CleanGoldDialog;
import com.mobi.clearsafe.widget.GoldDialog;
import com.mobi.clearsafe.widget.NoPaddingTextView;

import java.util.List;

import butterknife.BindView;

public class PermissionRepairActivity extends BaseActivity implements PermissionRepairPresenter.IView {

    @BindView(R.id.flTop)
    FrameLayout flTop;
    @BindView(R.id.tvCount)
    NoPaddingTextView tvCount;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.tvCleanDec)
    TextView tvCleanDec;
    @BindView(R.id.tvDec)
    TextView tvDec;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.ivTop)
    ImageView ivTop;

    private PermissionRepairAdapter mAdapter;
    private PermissionRepairPresenter mPresenter;

    private boolean isFirst = true;

    public static void start(Context context, int cleanId) {
        Intent intent = new Intent(context, PermissionRepairActivity.class);
        intent.putExtra("cleanId", cleanId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_permission_repair;
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

        mAdapter = new PermissionRepairAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);

        mAdapter.replaceData(PermissionRepairBean.createList());

        mPresenter = new PermissionRepairPresenter();
        mPresenter.attach(this);

        Typeface typeFace = TypefaceUtil.getTypeFace(this, "hyt.ttf");
        tvCount.setTypeface(typeFace);

    }

    @Override
    public void initEvent() {
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            List<PermissionRepairBean> data = mAdapter.getData();
            int id = data.get(position).getId();
            //看激励视频
            new RewardVideoAd.Builder(getContext())
                    .setSupportDeepLink(true)
                    .setScenario(ScenarioEnum.task_permission)
                    .setRewardVideoLoadListener(new RewardVideoLoadListenerImpl() {

                        @Override
                        public void onAdClose(String channel) {

                            //激励视频完成之后，请求接口
                            mPresenter.postCleanAuthOpen(id);
                        }

                    }).build();

        });

    }

    @Override
    public void initData() {
        mPresenter.getCleanAuthList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //下次是onResume的时候，就要回来进行刷新
        if (!isFirst) {
            refreshData();
        }
        isFirst = false;
    }

    @Override
    public void showCleanOutAuth(CleanAuthOutBean bean) {
        List<CleanAuthBean> list = bean.getList();

        //计算权限count的数据
        int count = 0;
        PermissionRepairBean permissionRepairBean0 = mAdapter.getData().get(0);
        if (!permissionRepairBean0.isHasPermission()) {
            count++;
        }

        PermissionRepairBean permissionRepairBean1 = mAdapter.getData().get(1);
        if (!permissionRepairBean1.isHasPermission()) {
            count++;
        }

        PermissionRepairBean permissionRepairBean2 = mAdapter.getData().get(2);
        if (!permissionRepairBean2.isHasPermission()) {
            count++;
        }

        PermissionRepairBean permissionRepairBean3 = mAdapter.getData().get(3);
        if (!permissionRepairBean3.isHasPermission()) {
            count++;
        }

        PermissionRepairBean permissionRepairBean4 = mAdapter.getData().get(4);
        if (!permissionRepairBean4.isHasPermission()) {
            count++;
        }

        tvCount.setText(String.valueOf(count));
        if (count == 0) {
            ivTop.setVisibility(View.GONE);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) flTop.getLayoutParams();
            layoutParams.height = UiUtils.dp2px(getContext(), 200);
            flTop.setLayoutParams(layoutParams);
//
            ConstraintLayout.LayoutParams tvLp = (ConstraintLayout.LayoutParams) tvCount.getLayoutParams();
            tvLp.topMargin = UiUtils.dp2px(getContext(), 86);
            tvCount.setLayoutParams(tvLp);

            tvCleanDec.setVisibility(View.GONE);
            tvDec.setVisibility(View.GONE);
        } else {
            ivTop.setVisibility(View.VISIBLE);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) flTop.getLayoutParams();
            layoutParams.height = UiUtils.dp2px(getContext(), 287);
            flTop.setLayoutParams(layoutParams);
//
            ConstraintLayout.LayoutParams tvLp = (ConstraintLayout.LayoutParams) tvCount.getLayoutParams();
            tvLp.topMargin = UiUtils.dp2px(getContext(), 109);
            tvCount.setLayoutParams(tvLp);

            tvCleanDec.setVisibility(View.VISIBLE);
            tvDec.setVisibility(View.VISIBLE);
        }

        //计算网络回来的数据
        if (list.size() >= 4) {
            mPresenter.transformBean(permissionRepairBean0, list.get(0));
            mPresenter.transformBean(permissionRepairBean1, list.get(1));
            mPresenter.transformBean(permissionRepairBean2, list.get(2));
            mPresenter.transformBean(permissionRepairBean3, list.get(3));
            if (list.size() >= 5) {
                mPresenter.transformBean(permissionRepairBean4, list.get(4));
            } else {
                mPresenter.transformBean(permissionRepairBean4, list.get(3));
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showCleanAuthOpen(CleanAuthOpenBean bean) {
        CleanGoldDialog goldDialog = new CleanGoldDialog.Builder(this)
                .setGold(bean.getPoints())
                .setDialogClick(dialog -> {
                    dialog.dismiss();
                })
                .build();
        goldDialog.show();

        refreshData();
    }

    private void refreshData() {
        //刷新本地权限控制，获取
        List<PermissionRepairBean> list = PermissionRepairBean.createList();
        mAdapter.replaceData(list);

        //获取服务器的数据
        if (mPresenter != null) {
            mPresenter.getCleanAuthList();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.detach();
        }
    }
}
