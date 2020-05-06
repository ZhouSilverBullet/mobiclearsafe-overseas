package com.mobi.clearsafe.main.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;



import com.example.adtest.manager.ScenarioEnum;
import com.example.adtest.rewardvideo.RewardVideoAd;
import com.example.adtest.rewardvideo.RewardVideoLoadListener;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.app.Const;
import com.mobi.clearsafe.app.MyApplication;
import com.mobi.clearsafe.base.BaseAppCompatActivity;
import com.mobi.clearsafe.base.adapter.CommonAdapter;
import com.mobi.clearsafe.base.adapter.RecycleViewHolder;
import com.mobi.clearsafe.eventbean.CheckTabEvent;
import com.mobi.clearsafe.fragment.bean.DialogCoinsBean;
import com.mobi.clearsafe.main.bean.CollectNumBean;
import com.mobi.clearsafe.main.bean.EggBean;
import com.mobi.clearsafe.main.bean.ExperienceActivityBean;
import com.mobi.clearsafe.main.bean.HotNoticeBean;
import com.mobi.clearsafe.main.bean.LuckyNumBer;
import com.mobi.clearsafe.main.bean.NemberBean;
import com.mobi.clearsafe.me.bean.UploadNikeName;
import com.mobi.clearsafe.net.BaseObserver;
import com.mobi.clearsafe.net.BaseResponse;
import com.mobi.clearsafe.net.CommonSchedulers;
import com.mobi.clearsafe.net.OkHttpClientManager;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.utils.AppUtil;
import com.mobi.clearsafe.utils.ToastUtils;
import com.mobi.clearsafe.utils.imageloader.ImageLoader;
import com.mobi.clearsafe.widget.CompleteNumDialog;
import com.mobi.clearsafe.widget.GetGoldNewDialog;
import com.mobi.clearsafe.widget.LoadWebViewActivity;
import com.mobi.clearsafe.widget.NumberCardDialog;
import com.mobi.clearsafe.widget.NumberRulesDialog;
import com.mobi.clearsafe.widget.ReceiveDialog;
import com.mobi.clearsafe.widget.StepCeilingNewDialog;
import com.mobi.clearsafe.wxapi.bean.UserEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 集数字界面
 */
public class SetNumberActivity extends BaseAppCompatActivity implements View.OnClickListener {


    private ImageView iv_back;
    private TextView activity_rules;
    private TextView tv_info;
    private TextView num_title;
    private RecyclerView num_recycler;
    private ViewFlipper notice_view;
    private RecyclerView ty_recycler;
    private TextView hammer_num;
    private TextView copper_coins_interval,copperhas_hammer,need_copper;
    private TextView silver_coins_interval,silverhas_hammer,need_silver;
    private TextView gold_coins_interval,goldhas_hammer,need_gold;
    private TextView egg_btn;
    private LinearLayout layout_gold,layout_silver,layout_copper;
    private List<LuckyNumBer> numBerList=new ArrayList<>();
    private CommonAdapter<LuckyNumBer> numAdapter;

    private List<ExperienceActivityBean.ExperienceActivityItemBean> experienceList=new ArrayList<>();
    private CommonAdapter<ExperienceActivityBean.ExperienceActivityItemBean> experienceAdapter;

    private boolean isFresh=false;

    private CollectNumBean collectNumBean;
    private EggBean egginfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_number);
        initView();
        getDate();
        getExperience();
        getEggInfo();
        getHotNotice();

    }

    //集数字完成领取奖励
    private void receiNumberPoints() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .receiveNumberPoints(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<DialogCoinsBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<DialogCoinsBean>() {
                    @Override
                    public void onSuccess(DialogCoinsBean demo) {
                        if(demo!=null){
                            UserEntity.getInstance().setPoints(demo.getTotal_points());
                            UserEntity.getInstance().setCash(demo.getCash());
                            ReceiveDialog dialog=new ReceiveDialog.Builder(SetNumberActivity.this)
                                    .setTitleContent(collectNumBean.getBefore_stages_number())
                                    .setDesContent(collectNumBean.getBefore_qualified_persons()+"")
                                    .setNumber(demo.getPoints())
                                    .setButtonClick(new ReceiveDialog.onButtonClick() {
                                        @Override
                                        public void onConfirmClick(Dialog dialog) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .build();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    //热门播报
    private void getHotNotice() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getHotNotice()
                .compose(CommonSchedulers.<BaseResponse<HotNoticeBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<HotNoticeBean>() {
                    @Override
                    public void onSuccess(final HotNoticeBean demo) {
                        if(demo!=null){
                            final List<HotNoticeBean.HotNotice> list = demo.getList();
                            for (int i=0;i<list.size();i++) {
                                View view = LayoutInflater.from(SetNumberActivity.this).inflate(R.layout.hot_notice_view,null);
                                SpannableString spannableString = AppUtil.matcherSearchText(getResources().getColor(R.color.c_FF492B),list.get(i).getTitle(), String.valueOf(list.get(i).getRed_sign()));
                                TextView tv1 =  view.findViewById(R.id.view1);
                                tv1.setText(spannableString);
//                                SpannableString spannableString2 = AppUtil.matcherSearchText(getResources().getColor(R.color.c_FF492B), list.get(i+1).getTitle(), String.valueOf(list.get(i+1).getRed_sign()));
//                                TextView tv2 =  view.findViewById(R.id.view2);
//                                tv2.setText(spannableString2);
                                notice_view.addView(view);
                            }
                            notice_view.startFlipping();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }



    //领取锤子
    private void receiHammer() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .receiveHammer(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<UploadNikeName>>observableIO2Main(this))
                .subscribe(new BaseObserver<UploadNikeName>() {
                    @Override
                    public void onSuccess(final UploadNikeName demo) {
                        ButtonStatistical.getHammer();
                        getEggInfo();
                        CompleteNumDialog dialog=new CompleteNumDialog.Builder(SetNumberActivity.this)
                                .setButtonClick(new CompleteNumDialog.onButtonClick() {
                                    @Override
                                    public void onConfirmClick(Dialog dialog) {
                                        dialog.dismiss();
                                    }
                                })
                                .build();
                        dialog.show();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    //金蛋列表
    private void getEggInfo() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getEggInfo(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<EggBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<EggBean>() {
                    @Override
                    public void onSuccess(EggBean demo) {
                        if(demo!=null){
                            egginfo = demo;
                            hammer_num.setText("x"+demo.getTotal_num());

                            copper_coins_interval.setText(egginfo.getEgg_list().get(0).getMin_points()+"-"+egginfo.getEgg_list().get(0).getMax_points());
                            need_copper.setText(getResources().getString(R.string.string_copperegg,String.valueOf(egginfo.getEgg_list().get(0).getNeed_num())));
                            copperhas_hammer.setText(Html.fromHtml(getResources().getString(R.string.string_hammer_has,String.valueOf(demo.getTotal_num()),String.valueOf(egginfo.getEgg_list().get(0).getNeed_num()))));

                            silver_coins_interval.setText(egginfo.getEgg_list().get(1).getMin_points()+"-"+egginfo.getEgg_list().get(1).getMax_points());
                            need_silver.setText(getResources().getString(R.string.string_silveregg,String.valueOf(egginfo.getEgg_list().get(1).getNeed_num())));
                            silverhas_hammer.setText(Html.fromHtml(getResources().getString(R.string.string_hammer_has,String.valueOf(demo.getTotal_num()),String.valueOf(egginfo.getEgg_list().get(1).getNeed_num()))));

                            gold_coins_interval.setText(egginfo.getEgg_list().get(2).getMin_points()+"-"+egginfo.getEgg_list().get(2).getMax_points());
                            need_gold.setText(getResources().getString(R.string.string_goldegg,String.valueOf(egginfo.getEgg_list().get(2).getNeed_num())));
                            goldhas_hammer.setText(Html.fromHtml(getResources().getString(R.string.string_hammer_has,String.valueOf(demo.getTotal_num()),String.valueOf(egginfo.getEgg_list().get(2).getNeed_num()))));
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isFresh){
            getDate();
            getExperience();
            isFresh=false;
        }

    }

    //体验活动
    private void getExperience() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getExperienceActivity(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<ExperienceActivityBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<ExperienceActivityBean>() {
                    @Override
                    public void onSuccess(ExperienceActivityBean demo) {
                        if(demo!=null){
                            experienceList=demo.getList();
                            experienceAdapter.replaceList(experienceList);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    private void getDate() {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .collectNumberDetail(UserEntity.getInstance().getUserId())
                .compose(CommonSchedulers.<BaseResponse<CollectNumBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<CollectNumBean>() {
                    @Override
                    public void onSuccess(CollectNumBean demo) {
                        if(demo!=null){
                            collectNumBean=demo;
                            tv_info.setText(Html.fromHtml(getResources().getString(R.string.string_num_info,String.valueOf(demo.getQualified_persons()))));
                            num_title.setText(getResources().getString(R.string.string_num_title,demo.getStages_number()));
                            numBerList=demo.getLucky_number();
                            numAdapter.replaceList(numBerList);
                            if(demo.isBefore_phase_is_winning()){
                                receiNumberPoints();
                            }
                            if(demo.isHammer_status()){
                                receiHammer();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    private void initView() {
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        activity_rules=findViewById(R.id.activity_rules);
        activity_rules.setOnClickListener(this);
        tv_info=findViewById(R.id.tv_info);
        num_title=findViewById(R.id.num_title);
        num_recycler=findViewById(R.id.num_recycler);
        notice_view=findViewById(R.id.notice_view);
        ty_recycler=findViewById(R.id.ty_recycler);
        hammer_num=findViewById(R.id.hammer_num);
        need_copper=findViewById(R.id.need_copper);
        need_silver=findViewById(R.id.need_silver);
        need_gold=findViewById(R.id.need_gold);
        copper_coins_interval=findViewById(R.id.copper_coins_interval);
        copperhas_hammer=findViewById(R.id.copperhas_hammer);
        silver_coins_interval=findViewById(R.id.silver_coins_interval);
        silverhas_hammer=findViewById(R.id.silverhas_hammer);
        gold_coins_interval=findViewById(R.id.gold_coins_interval);
        goldhas_hammer=findViewById(R.id.goldhas_hammer);
        egg_btn=findViewById(R.id.egg_btn);
        egg_btn.setOnClickListener(this);
        layout_copper=findViewById(R.id.layout_copper);
        layout_copper.setOnClickListener(this);
        layout_silver=findViewById(R.id.layout_silver);
        layout_silver.setOnClickListener(this);
        layout_gold=findViewById(R.id.layout_gold);
        layout_gold.setOnClickListener(this);
        numAdapter=new CommonAdapter<LuckyNumBer>(this,R.layout.num_layout_item,numBerList) {
            @Override
            public void convert(RecycleViewHolder holder, LuckyNumBer luckyNumBer) {
                TextView num=holder.getView(R.id.tv_num);
                TextView value=holder.getView(R.id.red_title);
                num.setText(luckyNumBer.getNumber());
                if(luckyNumBer.getNumbers()>0){
                    num.setBackgroundResource(R.mipmap.num_bg);
                    num.setTextColor(getResources().getColor(R.color.c_CA7A03));
                    if(luckyNumBer.getNumbers()>1){
                        value.setVisibility(View.VISIBLE);
                        value.setText(luckyNumBer.getNumbers()+"");
                    }else {
                        value.setVisibility(View.GONE);
                    }
                }else {
                    num.setBackgroundResource(R.mipmap.num_bg_un);
                    num.setTextColor(getResources().getColor(R.color.c_0073FF));
                    value.setVisibility(View.GONE);
                }

            }
        };
        LinearLayoutManager manager=new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        num_recycler.setLayoutManager(manager);
        num_recycler.setAdapter(numAdapter);


        experienceAdapter=new CommonAdapter<ExperienceActivityBean.ExperienceActivityItemBean>(this,R.layout.experience_layout_item,experienceList) {
            @Override
            public void convert(RecycleViewHolder holder, final ExperienceActivityBean.ExperienceActivityItemBean item) {
                LinearLayout layout_view=holder.getView(R.id.layout_view);
                ImageView task_icon=holder.getView(R.id.task_icon);
                TextView tv_content=holder.getView(R.id.tv_content);
                TextView time=holder.getView(R.id.time);
                TextView btn_content=holder.getView(R.id.btn_content);
                View line_view=holder.getView(R.id.line_view);
                if(holder.getAdapterPosition()==experienceList.size()-1){
                    line_view.setVisibility(View.GONE);
                }else {
                    line_view.setVisibility(View.VISIBLE);
                }
                ImageLoader.loadImage(SetNumberActivity.this,task_icon,item.getIcon());
                tv_content.setText(item.getTitle());
                time.setText(Html.fromHtml(getResources().getString(R.string.string_num_expe,String.valueOf(item.getCurr_times()),String.valueOf(item.getTotal_times()))));
                switch (item.getState()){
                    case 1:
                        btn_content.setText(getResources().getString(R.string.string_once_cy));
                        btn_content.setTextColor(getResources().getColor(R.color.white));
                        btn_content.setBackgroundResource(R.drawable.btn_2cff_17_shape);
                        break;
                    case 2:
                        btn_content.setText(getResources().getString(R.string.once_receive));
                        btn_content.setTextColor(getResources().getColor(R.color.white));
                        btn_content.setBackgroundResource(R.drawable.btn_ff3b_17_shape);
                        break;
                    case 3:
                        btn_content.setText(getResources().getString(R.string.finished_string));
                        btn_content.setTextColor(getResources().getColor(R.color.c_B1B1B1));
                        btn_content.setBackgroundResource(R.drawable.finishi_bg_shape);
                        break;
                }
                layout_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(item.getState()==2){
                            getNumber(item.getClass_id(),item.getActivity_id());
                        }else {
                            isFresh=true;
                            if (item.getJump_type() == 1) {//原生
                                AppUtil.startActivityFromAction(SetNumberActivity.this, item.getJump_url(), item.getParams());
                            } else  if (item.getJump_type() == 2) {//H5
                                String url = item.getJump_url()
                                        + "?token=" + UserEntity.getInstance().getToken()
                                        + "&user_id=" + UserEntity.getInstance().getUserId()
                                        +"&version="+AppUtil.packageName(MyApplication.getContext());
                                LoadWebViewActivity.IntoLoadWebView(SetNumberActivity.this, url);
                            } else if (item.getJump_type() == 3) {
                                EventBus.getDefault().post(new CheckTabEvent(item.getJump_url()));
                            }
                        }
                    }
                });

            }
        };
        LinearLayoutManager manage2r=new LinearLayoutManager(this);
        manage2r.setOrientation(RecyclerView.HORIZONTAL);
        ty_recycler.setLayoutManager(manage2r);
        ty_recycler.setAdapter(experienceAdapter);


    }

    //体验活动领取数字
    private void getNumber(int class_id,int activity_id) {
        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                .getNumber(UserEntity.getInstance().getUserId(),class_id,activity_id)
                .compose(CommonSchedulers.<BaseResponse<NemberBean>>observableIO2Main(this))
                .subscribe(new BaseObserver<NemberBean>() {
                    @Override
                    public void onSuccess(final NemberBean demo) {
                        if(demo!=null){
                            getDate();
                            NumberCardDialog dialog=new NumberCardDialog.Builder(SetNumberActivity.this)
                                    .setNumber(demo.getNumber())
                                    .setDesContent(demo.getNumber_tips())
                                    .setBtnText(demo.getButton_msg())
                                    .setNeedNumStr(demo.getLack_number_msg())
                                    .setIsComplete(demo.isIs_complete())
                                    .setButtonClick(new NumberCardDialog.onButtonClick() {
                                        @Override
                                        public void onConfirmClick(Dialog dialog) {
                                            dialog.dismiss();
                                            getDate();
                                            getExperience();
                                        }
                                    })
                                    .build();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg, String code) {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.activity_rules://活动规则
                NumberRulesDialog dialog=new NumberRulesDialog.Builder(this)
                        .setButtonClick(new NumberRulesDialog.onButtonClick() {
                            @Override
                            public void onConfirmClick(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build();
                dialog.show();
                break;
            case R.id.egg_btn:
                EventBus.getDefault().post(new CheckTabEvent("activity"));
                finish();
                break;
            case R.id.layout_copper://砸铜蛋
                if(egginfo.getEgg_list().get(0).getNeed_num()>egginfo.getTotal_num()){
                    ToastUtils.showShort(getResources().getString(R.string.string_clickegg_tip));
                }else {
                    clickEgg(egginfo.getEgg_list().get(0).getId());
                }
                break;
            case R.id.layout_silver://砸银蛋
                if(egginfo.getEgg_list().get(1).getNeed_num()>egginfo.getTotal_num()){
                    ToastUtils.showShort(getResources().getString(R.string.string_clickegg_tip));
                }else {
                    clickEgg(egginfo.getEgg_list().get(1).getId());
                }
                break;
            case R.id.layout_gold://砸金蛋
                if(egginfo.getEgg_list().get(2).getNeed_num()>egginfo.getTotal_num()){
                    ToastUtils.showShort(getResources().getString(R.string.string_clickegg_tip));
                }else {
                    clickEgg(egginfo.getEgg_list().get(2).getId());
                }
                break;
        }
    }


    //砸蛋
    private void clickEgg(final int id) {
        StepCeilingNewDialog dialog=new StepCeilingNewDialog.Builder(this)
                .setType(4)
                .setTitle("观看视频领取奖励")
                .setContent("福利金币海量派送")
                .setDialogClick(new StepCeilingNewDialog.StepDialogNewClick() {
                    @Override
                    public void moreBtnClick(Dialog dialog) {

                    }

                    @Override
                    public void watchVideo(Dialog dialog) {
                        dialog.dismiss();
                        new RewardVideoAd.Builder(SetNumberActivity.this)
                                .setSupportDeepLink(true)
                                .setScenario(ScenarioEnum.click_egg_video)
                                .setRewardVideoLoadListener(new RewardVideoLoadListener() {
                                    @Override
                                    public void onAdClick(String channel) {

                                    }

                                    @Override
                                    public void onVideoComplete(String channel) {

                                    }

                                    @Override
                                    public void onLoadFaild(String channel, int faildCode, String faildMsg) {

                                    }

                                    @Override
                                    public void onAdClose(String channel) {
                                        OkHttpClientManager.getInstance().getApiService(Const.getBaseUrl())
                                                .breadEgg(UserEntity.getInstance().getUserId(),id)
                                                .compose(CommonSchedulers.<BaseResponse<DialogCoinsBean>>observableIO2Main(SetNumberActivity.this))
                                                .subscribe(new BaseObserver<DialogCoinsBean>() {
                                                    @Override
                                                    public void onSuccess(DialogCoinsBean demo) {
                                                        if(demo!=null){
                                                            getEggInfo();
                                                            UserEntity.getInstance().setPoints(demo.getTotal_points());
                                                            UserEntity.getInstance().setCash(demo.getCash());
                                                            GetGoldNewDialog dialog = new GetGoldNewDialog.Builder(SetNumberActivity.this)
                                                                    .isVisible(false)
                                                                    .setShowAgain(false)
                                                                    .setlableIsVisible(false)
                                                                    .setFanbeiTitle(false)
                                                                    .setIsShowAd(true)
                                                                    .setGold(demo.getPoints())
                                                                    .setScenario(ScenarioEnum.click_egg_native)
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
                                                        ToastUtils.showShort(errorMsg);
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onAdShow(String channel) {

                                    }
                                }).build();
                    }

                    @Override
                    public void closeBtnClick(Dialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build();
        dialog.show();

    }
}
