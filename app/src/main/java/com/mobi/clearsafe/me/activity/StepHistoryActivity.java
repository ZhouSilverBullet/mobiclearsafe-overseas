package com.mobi.clearsafe.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android.support.annotation.Nullable;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.me.bean.StepHistoryBean;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.utils.DateUtils;
import com.mobi.clearsafe.utils.UiUtils;
import com.mobi.clearsafe.widget.CircleIndicatorView;
import com.mobi.clearsafe.widget.CircularProgressView;
import com.mobi.clearsafe.widget.NoPaddingTextView;
import com.mobi.clearsafe.widget.ScrollChartView;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * author : liangning
 * date : 2019-10-28  14:23
 */
public class StepHistoryActivity extends BaseAppCompatActivity {

    public static void IntoStepHistory(Activity activity) {
        Intent intent = new Intent(activity, StepHistoryActivity.class);
        activity.startActivity(intent);
    }

    private CircularProgressView circle_progress;//圆形进度条
    private NoPaddingTextView tv_step_num;//当前步数
    private TextView tv_target_percentage, tv_consumption, tv_paifang;//完成百分比/目标步数
    private ScrollChartView scroll_chart_main;//曲线图
    private CircleIndicatorView civ_main;//圆形指示器
    private List<StepHistoryBean> mList = new ArrayList<>();
    private TextView tv_mileage, tv_hour, tv_min, tv_kll;//公里数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stephistory_layout);
        ButtonStatistical.movementRecordPage();
        UiUtils.setTitleBar(this, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getString(R.string.report_record), null);
        initView();
        getRecord();
    }

    private void initView() {
        circle_progress = findViewById(R.id.circle_progress);
        tv_step_num = findViewById(R.id.tv_step_num);
        tv_target_percentage = findViewById(R.id.tv_target_percentage);
        tv_consumption = findViewById(R.id.tv_consumption);
        tv_paifang = findViewById(R.id.tv_paifang);
        tv_mileage = findViewById(R.id.tv_mileage);
        tv_hour = findViewById(R.id.tv_hour);
        tv_min = findViewById(R.id.tv_min);
        tv_kll = findViewById(R.id.tv_kll);
        scroll_chart_main = findViewById(R.id.scroll_chart_main);
        civ_main = findViewById(R.id.civ_main);
    }

    private void initData() {
        List<String> xList = new ArrayList<>();//X轴底部显示数据
        final List<Integer> cList = new ArrayList<>();//曲线显示数据
        int size = mList.size();
        for (int i = 0; i < size; i++) {
            xList.add(mList.get(i).time);
            cList.add(mList.get(i).step);
        }
        scroll_chart_main.setData(xList, cList);
        scroll_chart_main.setOnScaleListener(new ScrollChartView.OnScaleListener() {
            @Override
            public void onScaleChanged(int position) {
                StepHistoryBean bean = mList.get(position);
                tv_step_num.setText(bean.step + "");

                int progress = (int) (((float) bean.step / (float) bean.target_step) * 100);

                circle_progress.setProgress(progress, 800);
                tv_target_percentage.setText(String.format(getString(R.string.target_percentage),
                        progress, bean.target_step));
                tv_consumption.setText(String.format(getString(R.string.consumption_text), bean.heat + ""));
                tv_paifang.setText(String.format(getString(R.string.reduce_co2), bean.co2 + ""));
                tv_kll.setText(bean.kilometer + "");
                HashMap<String, String> times = DateUtils.Min2Hours(bean.minute);
                tv_hour.setText(times.get("hour"));
                tv_min.setText(times.get("min"));
                tv_kll.setText(String.valueOf(bean.calories));
                ScrollChartView.Point point = scroll_chart_main.getList().get(position);
                civ_main.setCircleY(point.y);
            }

            @Override
            public void onScaleChangeing() {
                civ_main.setCircleY(0);
            }
        });
        //滚动到目标position
        scroll_chart_main.smoothScrollTo(mList.size() - 1);
    }

    private void getRecord() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getStepRecord( UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<List<StepHistoryBean>>>observableIO2Main(this))
                .subscribe(new BaseObserver<List<StepHistoryBean>>() {
                    @Override
                    public void onSuccess(List<StepHistoryBean> demo) {
                        Log.e("数据", "");
                        if (demo != null) {
                            mList = demo;
                            initData();
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
    }
}
