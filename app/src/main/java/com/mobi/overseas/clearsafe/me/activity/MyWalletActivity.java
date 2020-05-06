package com.mobi.overseas.clearsafe.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.WeChatLoginActivity;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.base.BaseAppCompatActivity;
import com.mobi.overseas.clearsafe.base.adapter.CommonAdapter;
import com.mobi.overseas.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.overseas.clearsafe.eventbean.UserInfoEvent;
import com.mobi.overseas.clearsafe.me.bean.WalletBean;
import com.mobi.overseas.clearsafe.me.bean.WalletItemBean;
import com.mobi.overseas.clearsafe.me.bean.WalletItemRecordBean;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.utils.LogUtils;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2020-02-09  21:18
 */
public class MyWalletActivity extends BaseAppCompatActivity implements View.OnClickListener {

    public static void IntoMyWallet(Context context) {
        Intent intent = new Intent(context, MyWalletActivity.class);
        context.startActivity(intent);
    }

    private TextView tv_gold_num, tv_tixian, tv_today_gold, tv_all_gold, tv_huilv;
    private RecyclerView rv_history;
    private CommonAdapter<WalletItemBean> adapter;
    private int page = 1;
    private int limit = 10;
    private List<WalletItemBean> itemList = new ArrayList<>();
    private SmartRefreshLayout refresh;
    private LinearLayout ll_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet_layout);
        init();
        EventBus.getDefault().register(this);
    }

    private void init() {
        tv_gold_num = findViewById(R.id.tv_gold_num);
        tv_tixian = findViewById(R.id.tv_tixian);
        tv_tixian.setOnClickListener(this);
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        tv_today_gold = findViewById(R.id.tv_today_gold);
        tv_all_gold = findViewById(R.id.tv_all_gold);
        tv_huilv = findViewById(R.id.tv_huilv);
        rv_history = findViewById(R.id.rv_history);
        refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                itemList.clear();
                page = 1;
                requestData();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                requestData();
            }
        });
        adapter = new CommonAdapter<WalletItemBean>(this, R.layout.item_wallet_recycler_layout, itemList) {
            @Override
            public void convert(RecycleViewHolder holder, WalletItemBean wallet) {
                holder.setText(R.id.tv_date_str, wallet.date);
                LinearLayout ll_date = holder.getView(R.id.ll_date);
                if (wallet.isShow) {
                    ll_date.setVisibility(View.VISIBLE);
                } else {
                    ll_date.setVisibility(View.GONE);
                }
                RecyclerView rv = holder.getView(R.id.rv_item);
                CommonAdapter<WalletItemRecordBean> itemAdapter = new CommonAdapter<WalletItemRecordBean>(MyWalletActivity.this, R.layout.item_wallet_record_layout, wallet.record) {
                    @Override
                    public void convert(RecycleViewHolder holder, WalletItemRecordBean record) {
                        holder.setText(R.id.tv_time_hour, record.time);
                        holder.setText(R.id.tv_tujing, record.activity_name);
                        TextView tv_gold_nums = holder.getView(R.id.tv_gold_nums);
                        tv_gold_nums.setText(Html.fromHtml(getResources().getString(R.string.wall_add_gold_str, String.valueOf(record.points))));
                    }
                };
                LinearLayoutManager ll = new LinearLayoutManager(MyWalletActivity.this);
                rv.setLayoutManager(ll);
                rv.setAdapter(itemAdapter);
            }
        };
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv_history.setLayoutManager(lm);
        rv_history.setAdapter(adapter);
        requestData();
    }

    //登录之后会收到通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(UserInfoEvent info) {
        if (info != null && info.getUserInfo() != null) {
            itemList.clear();
            page = 1;
            requestData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tixian:
                //跳转到提现页
                if (UserEntity.getInstance().getUserInfo() == null) {
                    WeChatLoginActivity.IntoSettings(getActivity());
                    return;
                }
                Intent intent = WithdrawalActivity.IntoWithdrawal(getActivity());
                startActivity(intent);
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }


    private void requestData() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getWalletData(UserEntity.getInstance().getUserId(), page, limit)
                .compose(CommonSchedulers.<BaseResponse<WalletBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<WalletBean>() {
                    @Override
                    public void onSuccess(WalletBean demo) {
                        LogUtils.e(String.valueOf(demo.points));
                        if (demo != null) {
                            refresh.finishLoadMore();
                            refresh.finishRefresh();
                            tv_gold_num.setText(String.valueOf(demo.points));
                            tv_today_gold.setText(String.valueOf(demo.today_points));
                            tv_all_gold.setText(String.valueOf(demo.total_points));
                            tv_huilv.setText(getResources().getString(R.string.gold_huilv, String.valueOf(demo.rate)));
                            List<WalletItemBean> list = adapter.getList();
                            if (list.size() > 0) {
                                if (demo.res_list != null && demo.res_list.size() > 0) {
                                    WalletItemBean Fbean = list.get(list.size() - 1);
                                    WalletItemBean Lbean = demo.res_list.get(0);
                                    if (Fbean.date.equals(Lbean.date)) {
                                        demo.res_list.get(0).isShow = false;
                                    }
                                    adapter.addList(demo.res_list);
                                }
                            } else {
                                adapter.addList(demo.res_list);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
