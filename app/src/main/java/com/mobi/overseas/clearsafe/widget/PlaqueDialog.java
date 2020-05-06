package com.mobi.overseas.clearsafe.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.eventbean.CheckTabEvent;
import com.mobi.overseas.clearsafe.main.BannerBean;
import com.mobi.overseas.clearsafe.ui.common.dialog.BaseDialog;
import com.mobi.overseas.clearsafe.utils.AppUtil;
import com.mobi.overseas.clearsafe.utils.imageloader.Utils;
import com.mobi.overseas.clearsafe.wxapi.bean.UserEntity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页自有活动推广弹窗
 * author : liangning
 * date : 2019-11-29  14:03
 */
public class PlaqueDialog extends BaseDialog {
    private Context mContext;
    private Banner banner_dialog;
    private ImageView iv_close;
    private List<BannerBean.InsertScreen> mList = new ArrayList<>();

    public PlaqueDialog(Builder builder) {
        super(builder.mContext);
        this.mContext = builder.mContext;
        this.mList = builder.mList;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plaque_layout);
        initWindow();
        initView();
        ArrayList<String> urlList = new ArrayList<>();
        for (BannerBean.InsertScreen item : mList) {
            urlList.add(item.url);
        }
        banner_dialog.setImages(urlList);
        banner_dialog.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        banner_dialog = findViewById(R.id.banner_dialog);
        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        banner_dialog.setImageLoader(new MyLoader());
        banner_dialog.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerBean.InsertScreen item = mList.get(position);
                int jump_type = item.judge_type;
                if (jump_type == 1) {//原生
                    AppUtil.startActivityFromAction(getContext(),item.jump_url,item.params);
                } else if (jump_type == 2) {//H5
                    String jump_url = item.jump_url
                            + "?token=" + UserEntity.getInstance().getToken()
                            + "&user_id=" + UserEntity.getInstance().getUserId()
                            +"&version="+AppUtil.packageName(MyApplication.getContext());
                    LoadWebViewActivity.IntoLoadWebView(getContext(), jump_url);
                } else if (jump_type == 3) {
                    EventBus.getDefault().post(new CheckTabEvent(item.jump_url));
                }
                dismiss();
//                LoadWebViewActivity.IntoLoadWebView(mContext,bannerList.get(position).getJump_url()+"?token="+ UserEntity.getInstance().getToken()+"&user_id="+UserEntity.getInstance().getUserId());
            }
        });
        banner_dialog.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), Utils.dp2px(mContext, 8));
            }
        });
        banner_dialog.setClipToOutline(true);
    }

    @Override
    protected Context getActivityContext() {
        return mContext;
    }

    private class MyLoader extends ImageLoader {


//        CornerTransform transformation = new CornerTransform(mContext, Utils.dp2px(mContext, 8));

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path)
                    .placeholder(R.color.white)
                    .error(R.color.white)
                    .fitCenter()
//                    .transform(transformation)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imageView);
        }
    }

    private void initWindow() {
        Window dialogWindow = this.getWindow();
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);
        WindowManager.LayoutParams lps = dialogWindow.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int w = dm.widthPixels > dm.heightPixels ? dm.heightPixels
                : dm.widthPixels;
        int h = dm.widthPixels > dm.heightPixels ? dm.widthPixels
                : dm.heightPixels;
        lps.width = w;
        lps.height = h;
//        lps.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lps);
        dialogWindow.setGravity(Gravity.CENTER);
    }

    public static class Builder {
        private Context mContext;
        private List<BannerBean.InsertScreen> mList;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setList(List<BannerBean.InsertScreen> list){
            this.mList = list;
            return this;
        }

        public PlaqueDialog build(){
            return new PlaqueDialog(this);
        }
    }

}
