package com.mobi.clearsafe.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.base.adapter.CommonAdapter;
import com.mobi.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.clearsafe.main.bean.HistoryItemBean;
import com.mobi.clearsafe.main.bean.StepRaceHistoryBean;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.wxapi.bean.UserEntity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2019-12-05  22:00
 */
public class StepRaceHistoryActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private int page = 1;
    private static final int row_number = 20;
    private RecyclerView rv_history;
    private int competition_type = 3000;
    private int activity_id = 1;
    private CommonAdapter<HistoryItemBean> adapter;
    private LinearLayout ll_back;
    private TextView tv_title;
    private TextView tv_allreward, tv_racenum, tv_hight_reward, tv_hight_step;
    private List<HistoryItemBean> list = new ArrayList<>();
    private RefreshLayout refresh;


    public static void IntoStepRaceHistory(Context context, int competition_type, int activity_id) {
        Intent intent = new Intent(context, StepRaceHistoryActivity.class);
        intent.putExtra("competition_type", competition_type);
        intent.putExtra("activity_id", activity_id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steprace_history_layout);
        Intent intent = getIntent();
        if (intent != null) {
            competition_type = intent.getIntExtra("competition_type", 3000);
            activity_id = intent.getIntExtra("activity_id", 1);
        }
        initView();
        requestHistory();
    }

    private void initView() {
        refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                list.clear();
                page = 1;
                requestHistory();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                requestHistory();
            }
        });
        tv_allreward = findViewById(R.id.tv_allreward);
        tv_racenum = findViewById(R.id.tv_racenum);
        tv_hight_reward = findViewById(R.id.tv_hight_reward);
        tv_hight_step = findViewById(R.id.tv_hight_step);
        rv_history = findViewById(R.id.rv_history);
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.step_history_title, String.valueOf(competition_type)));
        adapter = new CommonAdapter<HistoryItemBean>(this, R.layout.item_steprace_history_layout, list) {
            @Override
            public void convert(RecycleViewHolder holder, HistoryItemBean item) {
                TextView tv_title = holder.getView(R.id.tv_title);
                tv_title.setText(getResources().getString(R.string.race_history_item_title, item.stages_number));
                holder.setText(R.id.tv_jackpot_points, String.valueOf(item.jackpot_points));
                holder.setText(R.id.tv_qualified_persons, String.valueOf(item.qualified_persons));
                holder.setText(R.id.tv_points, String.valueOf(item.points));
                TextView tv_tip = holder.getView(R.id.tv_tip);
                if (item.is_opening == 0) {//未开赛
                    holder.setText(R.id.tv_jackpot_points, "--");
                    holder.setText(R.id.tv_qualified_persons, "--");
                    holder.setText(R.id.tv_points, "--");
                    tv_tip.setText(Html.fromHtml(getResources().getString(R.string.race_history_item_weikaisai)));
                } else {//已开赛
                    if (item.is_standard == 1) {//已达标
                        tv_tip.setText(Html.fromHtml(getResources().getString(R.string.race_history_item_yidabiao)));
                    } else {//未达标
                        tv_tip.setText(Html.fromHtml(getResources().getString(R.string.race_history_item_weidabiao)));
                    }
                }
            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_history.setLayoutManager(manager);
        rv_history.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    public void requestHistory() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .requestStepRaceHistory(UserEntity.getInstance().getUserId(), Const.SYSTEM_ID, activity_id, competition_type, page, row_number)
                .compose(CommonSchedulers.<BaseResponse<StepRaceHistoryBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<StepRaceHistoryBean>() {
                    @Override
                    public void onSuccess(StepRaceHistoryBean demo) {
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                        if (demo != null) {
                            tv_racenum.setText(String.valueOf(demo.attend_number));
                            tv_hight_reward.setText(String.valueOf(demo.max_points));
                            tv_hight_step.setText(String.valueOf(demo.max_step));
                            tv_allreward.setText(String.valueOf(demo.sum_points));
                            if (demo.list != null) {
                                List<HistoryItemBean> mlist = demo.list;
                                list.addAll(mlist);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {
                        refresh.finishLoadMore();
                        refresh.finishRefresh();
                        if (!TextUtils.isEmpty(errorMsg)) {
                            ToastUtils.showShort(errorMsg);
                        }
                    }
                });
    }
}
