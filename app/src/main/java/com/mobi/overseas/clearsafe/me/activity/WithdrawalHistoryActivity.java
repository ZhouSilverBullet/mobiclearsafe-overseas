package com.mobi.overseas.clearsafe.me.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.base.adapter.CommonAdapter;
import com.mobi.overseas.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.overseas.clearsafe.me.bean.MoneyRecord;
import com.mobi.overseas.clearsafe.me.bean.MoneyRecordBean;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.utils.DateUtils;
import com.mobi.overseas.clearsafe.utils.ToastUtils;
import com.mobi.overseas.clearsafe.utils.UiUtils;
import com.mobi.overseas.clearsafe.widget.LoadingTips;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2019-10-25  16:50
 */
public class WithdrawalHistoryActivity extends BaseAppCompatActivity {

    public static void IntoWithdrawalHistory(Activity activity) {
        Intent intent = new Intent(activity, WithdrawalHistoryActivity.class);
        activity.startActivity(intent);
    }

    private RefreshLayout refresh;
    private RecyclerView rv_history;
    private List<MoneyRecord> mList = new ArrayList<>();
    private CommonAdapter<MoneyRecord> adapter;
    private LoadingTips loadTips;

    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawalhistory_layout);
        ButtonStatistical.withdrawalRecordPage();
        UiUtils.setTitleBar(this, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getString(R.string.wothdrawal_record), null);
        initView();
        getData();
    }

    private void getData() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getMoneyRecord(UserEntity.getInstance().getUserId(), page, 10)
                .compose(CommonSchedulers.<BaseResponse<MoneyRecordBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<MoneyRecordBean>() {
                    @Override
                    public void onSuccess(MoneyRecordBean demo) {
                        refresh.finishRefresh();
                        refresh.finishLoadMore();
                        loadTips.setLoadingTip(LoadingTips.LoadStatus.finish);
                        if (demo != null) {
                            List<MoneyRecord> res_list = demo.getRes_list();
                            mList.addAll(res_list);
                            adapter.notifyDataSetChanged();
                            if (mList.size() <= 0) {
                                loadTips.setLoadingTip(LoadingTips.LoadStatus.empty);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        refresh.finishRefresh();
                        refresh.finishLoadMore();
                        loadTips.setLoadingTip(LoadingTips.LoadStatus.finish);
                        if (TextUtils.isEmpty(code) && mList.size() <= 0) {
                            loadTips.setLoadingTip(LoadingTips.LoadStatus.netError);
                            loadTips.setOnReloadListener(new LoadingTips.onReloadListener() {
                                @Override
                                public void reload() {
                                    loadTips.setLoadingTip(LoadingTips.LoadStatus.loading);
                                    getData();
                                }
                            });
                        } else {
                            ToastUtils.showShort(errorMsg);
                        }
                    }

                });
    }

    @SuppressLint("WrongConstant")
    private void initView() {
        loadTips = findViewById(R.id.loadTips);
        refresh = findViewById(R.id.refresh);
        rv_history = findViewById(R.id.rv_history);

        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mList.clear();
                page = 1;
                getData();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getData();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);//设置滚动方向，横向滚动
        adapter = new CommonAdapter<MoneyRecord>(this, R.layout.item_withdrawalhistory_layout, mList) {
            @Override
            public void convert(RecycleViewHolder holder, MoneyRecord bean) {
                TextView tv_title = holder.getView(R.id.tv_title);
                TextView tv_time = holder.getView(R.id.tv_time);
                TextView tv_money = holder.getView(R.id.tv_money);
                tv_title.setText(bean.getActivity_name());
                tv_time.setText(DateUtils.dateFormat(bean.getTime(), "yyyy/MM/dd HH:mm"));
                tv_money.setText("+" + bean.getCash());

            }
        };
        rv_history.setLayoutManager(manager);
        rv_history.setAdapter(adapter);
    }
}

