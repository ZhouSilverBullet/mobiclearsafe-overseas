package com.mobi.overseas.clearsafe.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;

import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.Const;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.base.adapter.CommonAdapter;
import com.mobi.overseas.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.overseas.clearsafe.eventbean.CheckTabEvent;
import com.mobi.overseas.clearsafe.eventbean.PleasanEvent;
import com.mobi.overseas.clearsafe.fragment.bean.ActivityListBean;
import com.mobi.overseas.clearsafe.fragment.bean.AggregationBean;
import com.mobi.overseas.clearsafe.fragment.bean.TopBean;
import com.mobi.overseas.clearsafe.main.activity.SetNumberActivity;
import com.mobi.overseas.clearsafe.net.BaseObserver;
import com.mobi.overseas.clearsafe.net.BaseResponse;
import com.mobi.overseas.clearsafe.net.CommonSchedulers;
import com.mobi.overseas.clearsafe.net.OkHttpClientManager;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.StatusBarUtil;
import com.mobi.overseas.clearsafe.utils.imageloader.ImageLoader;
import com.mobi.overseas.clearsafe.widget.LazyLoadFragment;
import com.mobi.overseas.clearsafe.widget.LoadWebViewActivity;
import com.mobi.overseas.clearsafe.widget.TodayTaskDialog;
import com.mobi.overseas.clearsafe.widget.TodayWelfareDialog;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2020/2/7
 * desc: 聚合活动页
 */
public class AggregationFragment extends LazyLoadFragment {


    private RecyclerView top_recycler,activity_recycler;
    private CommonAdapter<TopBean> topAdapter;
    private CommonAdapter<ActivityListBean> activityAdapter;
    private List<TopBean> topList=new ArrayList<>();
    private List<ActivityListBean> activityList=new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AggregationFragment");

    }

    @Override
    protected int setContentView() {
        return R.layout.aggregation_fragment;
    }

    @Override
    protected void lazyLoad() {
//        StatusBarUtil.setStatusBarMode(getActivity(), true, R.color.white);
        ButtonStatistical.bdCount();
        if (Const.pBean != null) {
            if (Const.pBean != null && Const.pBean.show_page == 2) {
                EventBus.getDefault().post(new PleasanEvent(Const.pBean.show_page));
            }
        }
        getDate();
    }

    private void getDate() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getActivityDate(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<AggregationBean>>observableIO2Main(getContext()))
                .subscribe(new BaseObserver<AggregationBean>() {
                    @Override
                    public void onSuccess(AggregationBean demo) {
                        if(demo!=null){
                            if(demo.getTop_list().size()>0){
                                topList=demo.getTop_list();
                            }
                            topAdapter.replaceList(topList);
                            if(AppUtil.isXiaomi()){
                                activityList.clear();
                                ActivityListBean bean=new ActivityListBean();
                                bean.setButton_content("去看看");
                                bean.setJump_type(1);
                                bean.setJump_url("steprace");
                                bean.setName("步数挑战赛");
                                bean.setTitle("报名步数挑战赛");
                                bean.setTitle_picture("http://cdn.findwxapp.com/activity_page_202002/activity_list/stepRace.png");
                                bean.setActivity_bg("#FFECE9");
                                activityList.add(bean);
                                activityAdapter.notifyDataSetChanged();
                            }else {
                                if(demo.getActivity_list().size()>0){
                                    activityList=demo.getActivity_list();
                                }
                                activityAdapter.replaceList(activityList);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    @Override
    protected void firstIn() {
        top_recycler=findViewById(R.id.top_list);
        activity_recycler=findViewById(R.id.rv_recylcer);
        topAdapter=new CommonAdapter<TopBean>(getContext(),R.layout.top_list_item, topList) {
            @Override
            public void convert(RecycleViewHolder holder, final TopBean topBean) {
                RelativeLayout layout=holder.getView(R.id.layout);
                ImageView image=holder.getView(R.id.image);
                LinearLayout layout_icon=holder.getView(R.id.layout_icon);
                TextView tv_name=holder.getView(R.id.tv_name);
                TextView tv_state=holder.getView(R.id.tv_state);
                TextView red_dot=holder.getView(R.id.red_dot);
                if(!TextUtils.isEmpty(topBean.getIcon())){
                    ImageLoader.loadImage(getContext(),image,topBean.getIcon());
                }
                if(topBean.getStatus()==100){
                    layout_icon.setVisibility(View.VISIBLE);
                }else {
                    layout_icon.setVisibility(View.GONE);
                }
                tv_name.setText(topBean.getName());
                if(topBean.getName().contains("福利")){
                    if(topBean.getStatus()==2){
                        tv_state.setText(getContext().getResources().getString(R.string.string_unreceive));
                    }else {
                        tv_state.setText(getContext().getResources().getString(R.string.string_received));
                    }
                }else {
                    tv_state.setText(topBean.getTitle());
                }
                if(topBean.getStatus()!=3){
                    red_dot.setVisibility(View.VISIBLE);
                }else {
                    red_dot.setVisibility(View.GONE);
                }

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(topBean.getName().contains("福利")){
                            ButtonStatistical.dailyWelfare();
                            TodayWelfareDialog dialog=new TodayWelfareDialog.Builder(getContext())
                                    .build();
                            dialog.show();
                        }else if(topBean.getName().contains("任务")){
                            ButtonStatistical.todayTask();
                            TodayTaskDialog taskDialog=new TodayTaskDialog.Builder(getContext())
                                    .build();
                            taskDialog.show();
                        }else {
                            ButtonStatistical.collectionNumber();
                            getActivity().startActivity(new Intent(getActivity(), SetNumberActivity.class));
                        }
                    }
                });

            }
        };
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        top_recycler.setLayoutManager(manager);
        top_recycler.setAdapter(topAdapter);

        activityAdapter=new CommonAdapter<ActivityListBean>(getContext(),R.layout.activity_item_layout, activityList) {
            @Override
            public void convert(RecycleViewHolder holder, final ActivityListBean bean) {
                RelativeLayout layout=holder.getView(R.id.layout);
                TextView name=holder.getView(R.id.tv_title);
                TextView tv_tips=holder.getView(R.id.tv_tips);
                TextView tv_next=holder.getView(R.id.tv_next);
                ImageView image_icon=holder.getView(R.id.image_icon);
                ImageLoader.loadImage(getContext(),image_icon,bean.getTitle_picture());

                GradientDrawable drawable=new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setCornerRadius(AppUtil.dpToPx(6));
                drawable.setColor(Color.parseColor(bean.getActivity_bg()));
                layout.setBackground(drawable);
                tv_next.setText(bean.getButton_content());
                name.setText(bean.getName());
                tv_tips.setText(bean.getTitle());


                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        jump(bean.getJump_type(),bean.getJump_url(),bean.getParams());
                    }
                });
            }
        };
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        activity_recycler.setLayoutManager(manager2);
        activity_recycler.setAdapter(activityAdapter);

    }

    private void jump(int jump_type, String jump_url,String parm) {
        if (jump_type == 1) {//原生
            AppUtil.startActivityFromAction(getContext(), jump_url, parm);
        } else if (jump_type == 2) {//H5
            String url = jump_url
                    + "?token=" + UserEntity.getInstance().getToken()
                    + "&user_id=" + UserEntity.getInstance().getUserId()
                    +"&version="+AppUtil.packageName(MyApplication.getContext());
            LoadWebViewActivity.IntoLoadWebView(getContext(), url);
        } else if (jump_type == 3) {
            EventBus.getDefault().post(new CheckTabEvent(jump_url));
        }

    }


}
