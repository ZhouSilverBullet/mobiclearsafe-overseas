package com.mobi.clearsafe.main.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.main.adapter.data.ClearBean;
import com.mobi.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.clearsafe.ui.cleannotice.CleanNoticeActivity;
import com.mobi.clearsafe.ui.clear.CleanApkActivity;
import com.mobi.clearsafe.ui.clear.CleanBigFileActivity;
import com.mobi.clearsafe.ui.clear.CleanQReportActivity;
import com.mobi.clearsafe.ui.clear.CleanReportActivity;
import com.mobi.clearsafe.ui.clear.PowerCoolActivity;
import com.mobi.clearsafe.ui.clear.SpeedPhoneActivity;
import com.mobi.clearsafe.ui.powercontrol.PowerControlActivity;

import java.util.ArrayList;

public class HomeAdapter extends BaseMultiItemQuickAdapter<ClearBean, BaseViewHolder> {

    public HomeAdapter() {
        super(new ArrayList<>());

        addItemType(1, R.layout.item_home_grid_recycler);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ClearBean item) {
        switch (item.getItemType()) {
            case 1:
                ImageView ivIcon = helper.getView(R.id.ivIcon);
                TextView tvName = helper.getView(R.id.tvName);
//                TextView tvDec = helper.getView(R.id.tvDec);
//                TextView tvReward = helper.getView(R.id.tvReward);
                if (item.isClear) {
                    tvName.setText(item.clearName);
                    tvName.setTextColor(mContext.getResources().getColor(item.color));
                } else {
                    tvName.setText(item.dec);
                    tvName.setTextColor(mContext.getResources().getColor(item.color));
                }

//                tvDec.setTextColor(mContext.getResources().getColor(item.color));
//                tvDec.setText(item.dec);

                ivIcon.setImageResource(item.icon);

                //点击事件处理
                helper.itemView.setOnClickListener(v -> {
                    skipHandler(v, item);
                });
//                int points = item.points;
//                if (points <= 0) {
//                    tvReward.setVisibility(View.INVISIBLE);
//                } else {
//                    tvReward.setVisibility(View.VISIBLE);
//                    tvReward.setText("+" + points);
//                }
                break;
        }
    }

    private void skipHandler(View v, ClearBean item) {
        switch (item.clearType) {
            //微信
            case 2: {
                ButtonStatistical.homeFeatureClick("CleanReport");
                CleanReportActivity.start(v.getContext(), item.id);
            }
            break;
//            //手Q
//            case 3: {
//                ButtonStatistical.homeFeatureClick("CleanQReport");
//                CleanQReportActivity.start(v.getContext(), item.id);
//            }
            //手Q
            case 3: {
                ButtonStatistical.homeFeatureClick("PowerControl");
                PowerControlActivity.start(v.getContext(), item.id);
            }
            break;
            //手机降温
            case 4: {
                ButtonStatistical.homeFeatureClick("PowerCool");
                PowerCoolActivity.start(v.getContext(), item.id);
            }
            break;
            case 5: {
                ButtonStatistical.homeFeatureClick("CleanApk");
                CleanApkActivity.start(v.getContext(), item.id);
            }
            break;
            //通知栏
            case 6: {
                ButtonStatistical.homeFeatureClick("CleanNotice");
                CleanNoticeActivity.start(v.getContext(), item.id);
            }
            break;
            //手机加速
            default: {
                ButtonStatistical.homeFeatureClick("SpeedPhone");
                SpeedPhoneActivity.start(v.getContext(), item.id, item.memoryValue);
            }

            break;
        }
    }

    public int getSkipId(int position) {
        if (position < 0 || position > getData().size()) {
            return 0;
        }
        return getData().get(position).id;
    }


}
