package com.mobi.overseas.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;

import com.example.adtest.manager.ScenarioEnum;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.base.adapter.CommonAdapter;
import com.mobi.overseas.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.overseas.clearsafe.eventbean.CheckTabEvent;
import com.mobi.overseas.clearsafe.fragment.bean.DialogCoinsBean;
import com.mobi.overseas.clearsafe.fragment.bean.TodayTaskBean;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2020/2/7
 * desc:
 */
public class TodayTaskDialog extends BaseDialog implements View.OnClickListener {

    private Context mContent;
    private ImageView iv_close;
    private RecyclerView recycler;
    private CommonAdapter<TodayTaskBean> adapter;
    private List<TodayTaskBean> list = new ArrayList<>();


    public TodayTaskDialog(Builder builder) {
        super(builder.mContent);
        this.mContent = builder.mContent;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todaytask_dialog_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initWindow();
        initView();
        getDate();
    }

    private void getDate() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getTodayTask(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<List<TodayTaskBean>>>observableIO2Main(mContent))
                .subscribe(new BaseObserver<List<TodayTaskBean>>() {
                    @Override
                    public void onSuccess(List<TodayTaskBean> demo) {
                        if (demo.size() > 0) {
                            if(AppUtil.isXiaomi()){
                                list.clear();
                                TodayTaskBean bean=new TodayTaskBean();
                                bean.setJump_type(1);
                                bean.setJump_url("steprace");
                                bean.setName("步数挑战赛");
                                bean.setStatus(1);
                                list.add(bean);
                                adapter.notifyDataSetChanged();
                            }else {
                                list = demo;
                                adapter.replaceList(list);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }



    private void initView() {
        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        recycler = findViewById(R.id.recycler);

        adapter = new CommonAdapter<TodayTaskBean>(mContent, R.layout.todaytask_layout_item, list) {
            @Override
            public void convert(RecycleViewHolder holder, final TodayTaskBean bean) {
                TextView tv_title = holder.getView(R.id.tv_title);
                ProgressBar progress_bar=holder.getView(R.id.progress_bar);
                TextView tv_tips=holder.getView(R.id.tv_tips);
                TextView receive_num = holder.getView(R.id.receive_num);
                TextView tv_btn = holder.getView(R.id.tv_btn);
                tv_title.setText(bean.getName());
                progress_bar.setMax(bean.getTotal_num());
                progress_bar.setProgress(bean.getFinished_num());
                tv_tips.setText(bean.getFinished_num()+"/"+bean.getTotal_num());
                receive_num.setText("+"+bean.getPoints());
                if (bean.getStatus() == 3) {
                    tv_btn.setBackgroundResource(R.drawable.welfare_btn_gray_shape);
                    tv_btn.setText(mContent.getResources().getString(R.string.finished_string));
                    tv_btn.setTextColor(mContent.getResources().getColor(R.color.black_99));
                } else if (bean.getStatus() == 2) {
                    tv_btn.setBackgroundResource(R.drawable.welfare_receive_shape_btn);
                    tv_btn.setText(mContent.getResources().getString(R.string.once_receive));
                    tv_btn.setTextColor(mContent.getResources().getColor(R.color.white));
                }else if(bean.getStatus()==1){
                    tv_btn.setBackgroundResource(R.drawable.todaytask_btn_shape);
                    tv_btn.setText(mContent.getResources().getString(R.string.string_gofinish));
                    tv_btn.setTextColor(mContent.getResources().getColor(R.color.c_FF9E2B));
                }
                tv_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if(bean.getStatus()==2){//立即领取
                            receive(bean.getId());
                        }else {
                           dismiss();
                            if (bean.getJump_type() == 1) {//原生
                                AppUtil.startActivityFromAction(getContext(), bean.getJump_url(), bean.getParams());
                            } else  if (bean.getJump_type() == 2) {//H5
                                String url = bean.getJump_url()
                                        + "?token=" + UserEntity.getInstance().getToken()
                                        + "&user_id=" + UserEntity.getInstance().getUserId()
                                        +"&version="+AppUtil.packageName(MyApplication.getContext());
                                LoadWebViewActivity.IntoLoadWebView(getContext(), url);
                            } else if (bean.getJump_type() == 3) {
                                EventBus.getDefault().post(new CheckTabEvent(bean.getJump_url()));
                            }
                        }
                    }
                });

            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);

    }

    //领取金币
    private void receive(final int id) {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .todayTaskReceive(UserEntity.getInstance().getUserId(),id)
                .compose(CommonSchedulers.<BaseResponse<DialogCoinsBean>>observableIO2Main(mContent))
                .subscribe(new BaseObserver<DialogCoinsBean>() {
                    @Override
                    public void onSuccess(DialogCoinsBean demo) {
                        if(demo!=null){
                            if(id==186){//体验大转盘
                                ButtonStatistical.taskLuckweel();
                            }else if(id==187){//体验刮刮卡
                                ButtonStatistical.taskScratchCard();
                            }
                            getDate();
                            UserEntity.getInstance().setPoints(demo.getTotal_points());
                            UserEntity.getInstance().setCash(demo.getCash());
                            GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(getContext())
                                    .isVisible(false)
                                    .setShowAgain(false)
                                    .setlableIsVisible(false)
                                    .setFanbeiTitle(false)
                                    .setIsShowAd(true)
                                    .setGold(demo.getPoints())
                                    .setScenario(ScenarioEnum.gold_bubble_native)
                                    .setDialogClick(new GetGoldNewDialog.GetGoldDialogClick() {
                                        @Override
                                        public void doubleBtnClick(Dialog dialog) {

                                        }

                                        @Override
                                        public void closeBtnClick(Dialog dialog) {
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void bottomBtnClick(Dialog dialog) {

                                        }
                                    }).build();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    private void initWindow() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    @Override
    protected Context getActivityContext() {
        return mContent;
    }

    public static class Builder {

        private Context mContent;

        public Builder(Context context) {
            this.mContent = context;
        }


        public TodayTaskDialog build() {
            return new TodayTaskDialog(this);
        }

    }
}
