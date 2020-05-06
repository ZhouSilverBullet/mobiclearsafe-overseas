package com.mobi.overseas.clearsafe.main.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.main.adapter.data.ClearBean;
import com.mobi.overseas.clearsafe.statistical.umeng.ButtonStatistical;
import com.mobi.overseas.clearsafe.ui.appwiget.CleanAppWidgetGuideActivity;
import com.mobi.overseas.clearsafe.ui.cleannotice.CleanNoticeActivity;
import com.mobi.overseas.clearsafe.ui.clear.CleanApkActivity;
import com.mobi.overseas.clearsafe.ui.clear.CleanBigFileActivity;
import com.mobi.overseas.clearsafe.ui.powercontrol.PowerControlActivity;
import com.mobi.overseas.clearsafe.ui.powersaving.PowerSavingActivity;

import java.util.ArrayList;

public class ToolBoxFileAdapter extends BaseMultiItemQuickAdapter<ClearBean, BaseViewHolder> {

    public ToolBoxFileAdapter() {
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
                tvName.setText(item.clearName);

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

//
        switch (item.clearType) {
            //大文件
            case 2: {
                ButtonStatistical.toolboxFeatureClick("CleanBigFile");
                CleanBigFileActivity.start(v.getContext(), item.id);
            }
            break;
            //手Q
            case 3: {
                ButtonStatistical.toolboxFeatureClick("CleanQReport");
                PowerSavingActivity.start(v.getContext(), item.id);
            }
            break;
            //手机降温
            case 4: {
                ButtonStatistical.toolboxFeatureClick("CleanNotice");
                CleanNoticeActivity.start(v.getContext(), item.id);
            }
            break;
            //
            case 5: {
                ButtonStatistical.toolboxFeatureClick("PowerControl");
                PowerControlActivity.start(v.getContext(), item.id);
            }
            break;
            //
            case 6: {
                ButtonStatistical.toolboxFeatureClick("PowerControl");
                CleanAppWidgetGuideActivity.start(v.getContext(), item.id);
            }
            break;
            //安装包
            default: {
                ButtonStatistical.toolboxFeatureClick("CleanApk");
                CleanApkActivity.start(v.getContext(), item.id);

            }

            break;
        }
    }


}
