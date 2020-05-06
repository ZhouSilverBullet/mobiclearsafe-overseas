package com.mobi.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.base.adapter.CommonAdapter;
import com.mobi.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.clearsafe.eventbean.CheckTabEvent;
import com.mobi.clearsafe.fragment.bean.TodayWelfareBean;
import com.mobi.clearsafe.fragment.bean.WelfareReceiveBean;
import com.mobi.clearsafe.main.activity.SetNumberActivity;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.ui.common.dialog.BaseDialog;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2020/2/7
 * desc: 今日福利弹框
 */
public class TodayWelfareDialog extends BaseDialog implements View.OnClickListener {

    private Context mContent;
    private ImageView iv_close;
    private RecyclerView recycler;
    private TextView tv_tip;
    private CommonAdapter<TodayWelfareBean> adapter;
    private List<TodayWelfareBean> list=new ArrayList<>();


    public TodayWelfareDialog(Builder builder) {
        super(builder.mContent);
        this.mContent = builder.mContent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welfare_dialog_layout);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initWindow();
        initView();
        getDate();
    }

    private void getDate() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getTodayWelfare(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<List<TodayWelfareBean>>>observableIO2Main(mContent))
                .subscribe(new BaseObserver<List<TodayWelfareBean>>() {
                    @Override
                    public void onSuccess(List<TodayWelfareBean> demo) {
                        if(demo.size()>0){
                            list=demo;
                            adapter.replaceList(list);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    private void initView() {
        iv_close=findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        recycler=findViewById(R.id.recycler);
        tv_tip=findViewById(R.id.tv_tip);
        tv_tip.setText(Html.fromHtml(mContent.getResources().getString(R.string.string_welfare_tips)));

        adapter=new CommonAdapter<TodayWelfareBean>(mContent,R.layout.welfare_layout_item,list) {
            @Override
            public void convert(RecycleViewHolder holder, final TodayWelfareBean bean) {
                TextView tv_title=holder.getView(R.id.tv_title);
                TextView tv_tips=holder.getView(R.id.tv_tips);
                TextView receive_num=holder.getView(R.id.receive_num);
                TextView tv_btn=holder.getView(R.id.tv_btn);
                tv_title.setText(bean.getName());
                tv_tips.setText(bean.getTitle());
                if(bean.getStatus()==3){
                    tv_btn.setBackgroundResource(R.drawable.welfare_btn_gray_shape);
                    tv_btn.setText(mContent.getResources().getString(R.string.string_received));
                    tv_btn.setTextColor(mContent.getResources().getColor(R.color.black_99));
                }else if(bean.getStatus()==2){
                    tv_btn.setBackgroundResource(R.drawable.welfare_receive_shape_btn);
                    tv_btn.setText(mContent.getResources().getString(R.string.once_receive));
                    tv_btn.setTextColor(mContent.getResources().getColor(R.color.white));
                }
                tv_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(bean.getStatus()==2){//立即领取
                            receive(bean.getId());
                        } else {
                            if (bean.getJump_type() == 1) {//原生
                                AppUtil.startActivityFromAction(getContext(), bean.getJump_url(), bean.getParams());
                            } else  if (bean.getJump_type() == 2) {//H5
                                String url = bean.getJump_url()
                                        + "?token=" + UserEntity.getInstance().getToken()
                                        + "&user_id=" + UserEntity.getInstance().getUserId()
                                        +"&version="+AppUtil.packageName(MyApplication.getContext());
                                LoadWebViewActivity.IntoLoadWebView(getContext(), url);
                            } else if (bean.getJump_type() == 3) {
                                EventBus.getDefault().post(new CheckTabEvent(bean.getBg_url()));
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

    private void receive(int id) {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .todaywelfareReceive(UserEntity.getInstance().getUserId(),id)
                .compose(CommonSchedulers.<BaseResponse<WelfareReceiveBean>>observableIO2Main(getContext()))
                .subscribe(new BaseObserver<WelfareReceiveBean>() {
                    @Override
                    public void onSuccess(final WelfareReceiveBean demo) {
                        if(demo!=null){
                            getDate();
                            switch (demo.getType()){
                                case 100://翻倍卡
                                    ButtonStatistical.welfareDoublecard();
                                    showDialog(demo);
                                    break;
                                case 101://步数卡
                                    ButtonStatistical.welfareRoadcard();
                                    showDialog(demo);
                                    break;
                                case 102://收益卡
                                    showDialog(demo);
                                    break;
                                case 200://数字
                                    NumberCardDialog dialog=new NumberCardDialog.Builder(mContent)
                                            .setNumber(demo.getNumber())
                                            .setDesContent(demo.getNumber_tips())
                                            .setBtnText(demo.getButton_msg())
                                            .setNeedNumStr(demo.getLack_number_msg())
                                            .setIsComplete(demo.isIs_complete())
                                            .setButtonClick(new NumberCardDialog.onButtonClick() {
                                                @Override
                                                public void onConfirmClick(Dialog dialog) {
                                                    dialog.dismiss();
                                                    if(demo.isIs_complete()){
                                                        mContent.startActivity(new Intent(mContent, SetNumberActivity.class));
                                                    }
                                                }
                                            })
                                            .build();
                                    dialog.show();
                                    break;
                                case 10://金币
                                    break;
                            }
                        }

                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });


//        CompleteNumDialog dialog1=new CompleteNumDialog.Builder(getContext())
//                .setButtonClick(new CompleteNumDialog.onButtonClick() {
//                    @Override
//                    public void onConfirmClick(Dialog dialog) {
//                        dialog.dismiss();
//                    }
//                })
//                .build();
//        dialog1.show();

//        ReceiveDialog dialog=new ReceiveDialog.Builder(mContent)
//                .setButtonClick(new ReceiveDialog.onButtonClick() {
//                    @Override
//                    public void onConfirmClick(Dialog dialog) {
//                        dialog.dismiss();
//                    }
//                })
//                .build();
//        dialog.show();
    }

    private void showDialog(WelfareReceiveBean bean) {
                        CardDialog cardDialog=new CardDialog.Builder(getContext())
                                .setTitleContent(bean.getTitle())
                                .setImageUrl(bean.getIcon())
                                .setDesContent(bean.getContent())
                                .setType(bean.getType())
                                .setBtnText(mContent.getResources().getString(R.string.i_know))
                                .setButtonClick(new CardDialog.onButtonClick() {
                                    @Override
                                    public void onConfirmClick(Dialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .build();
                        cardDialog.show();
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



        public TodayWelfareDialog build() {
            return new TodayWelfareDialog(this);
        }

    }


}
