package com.mobi.clearsafe.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import android.support.annotation.NonNull;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.base.adapter.CommonAdapter;
import com.mobi.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.clearsafe.me.bean.PointsRecord;
import com.mobi.clearsafe.me.bean.RecordBean;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.utils.DateUtils;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.utils.UiUtils;
import com.mobi.clearsafe.widget.LoadingTips;
import com.mobi.clearsafe.wxapi.bean.UserEntity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 金币收益记录
 * author : liangning
 * date : 2019-10-23  14:27
 */
public class GoldHistoryActivity extends BaseAppCompatActivity {


    public static void IntoGoldHistory(Activity activity) {
        Intent intent = new Intent(activity, GoldHistoryActivity.class);
        activity.startActivity(intent);
    }


    private RecyclerView rv_history;
    private CommonAdapter<RecordBean> adapter;
    private List<RecordBean> mList = new ArrayList<>();
    private RefreshLayout refresh;
    private int page = 1;
    private LoadingTips loadTips;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goldhistory_layout);
        ButtonStatistical.coinsRecordPage();
        UiUtils.setTitleBar(this, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getResources().getString(R.string.money_record), null);
        initView();
        getData();
    }


    private void getData() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getPointsRecord(UserEntity.getInstance().getUserId(), page, 10)
                .compose(CommonSchedulers.<BaseResponse<PointsRecord>>observableIO2Main(this))
                .subscribe(new BaseObserver<PointsRecord>() {
                    @Override
                    public void onSuccess(PointsRecord demo) {
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                        loadTips.setLoadingTip(LoadingTips.LoadStatus.finish);
                        if (demo != null) {
                            List<RecordBean> resList = demo.getRes_list();
                            mList.addAll(resList);
                            adapter.notifyDataSetChanged();
                            if (mList.size() <= 0) {
                                loadTips.setLoadingTip(LoadingTips.LoadStatus.empty);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
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

    private void initView() {
        loadTips = findViewById(R.id.loadTips);
        rv_history = findViewById(R.id.rv_history);
        refresh = findViewById(R.id.refresh);
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

        adapter = new CommonAdapter<RecordBean>(this, R.layout.item_goldhistory_layout, mList) {
            @Override
            public void convert(RecycleViewHolder holder, RecordBean goldHistoryBean) {
                TextView tv_title = holder.getView(R.id.tv_title);
                TextView tv_time = holder.getView(R.id.tv_time);
                TextView tv_gold_num = holder.getView(R.id.tv_gold_num);
                tv_title.setText(goldHistoryBean.getActivity_name());
                String dateFormat = DateUtils.dateFormat(goldHistoryBean.getTime(), "yyyy/MM/dd HH:mm");
                tv_time.setText(dateFormat);
                tv_gold_num.setText(goldHistoryBean.getPoints() + "");
            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_history.setLayoutManager(manager);
        rv_history.setAdapter(adapter);
    }
}
