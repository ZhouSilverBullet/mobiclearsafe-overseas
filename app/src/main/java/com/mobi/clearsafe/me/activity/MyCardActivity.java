package com.mobi.clearsafe.me.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.nativeexpress.NativeExpressAd;
import com.mobi.clearsafe.MainActivity;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.base.adapter.CommonAdapter;
import com.mobi.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.clearsafe.eventbean.CheckTabEvent;
import com.mobi.clearsafe.manager.AppManager;
import com.mobi.clearsafe.me.bean.MyCardBean;
import com.mobi.clearsafe.me.bean.ReceiveeErningsBean;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.UiUtils;
import com.mobi.clearsafe.utils.imageloader.ImageLoader;
import com.mobi.clearsafe.widget.GetGoldNewDialog;
import com.mobi.clearsafe.widget.LoadWebViewActivity;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * author : liangning
 * date : 2020-02-11  21:06
 */
public class MyCardActivity extends BaseAppCompatActivity {

    public static void intoMyCard(Context context) {
        Intent intent = new Intent(context, MyCardActivity.class);
        context.startActivity(intent);
    }

    private RecyclerView rv_card;
    private CommonAdapter<MyCardBean> adapter;
    private List<MyCardBean> mList = new ArrayList<>();
    private RelativeLayout fl_ad;
    private NativeExpressAd ad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycard_layout);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void initView() {
        rv_card = findViewById(R.id.rv_card);
        fl_ad = findViewById(R.id.fl_ad);

        UiUtils.setTitleBar(this, "", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, getString(R.string.string_my_coupon), null);
        adapter = new CommonAdapter<MyCardBean>(this, R.layout.item_mycard_layout, mList) {
            @Override
            public void convert(final RecycleViewHolder holder, final MyCardBean item) {
                ImageView iv_icon = holder.getView(R.id.iv_icon);
                ImageView iv_jiantou = holder.getView(R.id.iv_jiantou);
                TextView tv_content = holder.getView(R.id.tv_content);
                TextView tv_neirong = holder.getView(R.id.tv_neirong);
                TextView tv_use = holder.getView(R.id.tv_use);
                LinearLayout ll_jianrou = holder.getView(R.id.ll_jianrou);
                ImageLoader.loadImage(getActivity(), iv_icon, item.icon);
                holder.setText(R.id.tv_title, item.title);
                TextView tv_endtime = holder.getView(R.id.tv_endtime);
                if (TextUtils.isEmpty(item.deadline)) {
                    tv_endtime.setVisibility(View.INVISIBLE);
                }
                tv_endtime.setText(getResources().getString(R.string.mycard_item_time, item.deadline));
                holder.setText(R.id.tv_cardnum, getResources().getString(R.string.mycard_item_cardnum, String.valueOf(item.number)));

                if (item.type == 102) {
                    ll_jianrou.setVisibility(View.VISIBLE);
                    tv_content.setText(Html.fromHtml(item.content));
                    tv_neirong.setText(Html.fromHtml(item.introduction));
                    if (item.status == 1) {
                        tv_use.setText(getResources().getString(R.string.once_receive));
                        tv_use.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                receiveErnings();
                            }
                        });
                    } else if (item.status == 2) {
                        tv_use.setText(getResources().getString(R.string.mycard_item_using));
                    } else if (item.status == 3) {
                        tv_use.setText(getResources().getString(R.string.string_received));
                    }
                } else {
                    SpannableString ss = AppUtil.matcherSearchText(getResources().getColor(R.color.colorLineStart), item.content, item.activity_name);
                    tv_content.setText(ss);
                    tv_use.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (item.jump_type == 1) {//原生
                                AppUtil.startActivityFromAction(MyCardActivity.this, item.jump_url, item.params);
                            } else if (item.jump_type == 2) {//H5
                                String jump_url = item.jump_url
                                        + "?token=" + UserEntity.getInstance().getToken()
                                        + "&user_id=" + UserEntity.getInstance().getUserId()
                                        +"&version="+AppUtil.packageName(MyApplication.getContext());
                                LoadWebViewActivity.IntoLoadWebView(MyCardActivity.this, jump_url);
                            } else if (item.jump_type == 3) {
                                AppManager.getInstance().returnToActivity(MainActivity.class);
                                EventBus.getDefault().post(new CheckTabEvent(item.jump_url));
                            }
                        }
                    });
                }
                ll_jianrou.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mList.get(holder.getAdapterPosition()).showbottom = !mList.get(holder.getAdapterPosition()).showbottom;
                        adapter.notifyDataSetChanged();
                    }
                });
                if (item.showbottom) {
                    tv_neirong.setVisibility(View.VISIBLE);
                    iv_jiantou.setImageResource(R.mipmap.car_item_top_jiantou);
                } else {
                    tv_neirong.setVisibility(View.GONE);
                    iv_jiantou.setImageResource(R.mipmap.car_item_bottom_jiantou);
                }
            }
        };
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv_card.setLayoutManager(lm);
        rv_card.setAdapter(adapter);

        if (fl_ad != null) {
            if(AppUtil.HWIsShowAd()){
                ad = new NativeExpressAd.Builder(this)
                        .setAdCount(1)
                        .setADViewSize(350, 0)
                        .setHeightAuto(true)
                        .setSupportDeepLink(true)
                        .setPosID("1028055")
                        .setBearingView(fl_ad)
                        .setScenario(ScenarioEnum.me_page_native)
                        .build();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ad != null) {
            ad.destory();
        }
    }

    private void requestData() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getMyCardList(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<List<MyCardBean>>>observableIO2Main(this))
                .subscribe(new BaseObserver<List<MyCardBean>>() {
                    @Override
                    public void onSuccess(List<MyCardBean> demo) {
                        if (demo != null) {
                            if (adapter != null) {
                                adapter.replaceList(demo);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    /**
     * 领取收益奖励
     */
    private void receiveErnings() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .receiveErnings(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<ReceiveeErningsBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<ReceiveeErningsBean>() {
                    @Override
                    public void onSuccess(ReceiveeErningsBean demo) {
                        if (demo != null) {
                            GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(MyCardActivity.this)
                                    .isVisible(false)
                                    .setIsShowAd(false)
                                    .setBtnText(demo.pop_up_message)
                                    .setGold(demo.points)
                                    .setTitle("")
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
                                            dialog.dismiss();
                                        }
                                    }).build();
                            dialog.show();
                            requestData();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }
}
