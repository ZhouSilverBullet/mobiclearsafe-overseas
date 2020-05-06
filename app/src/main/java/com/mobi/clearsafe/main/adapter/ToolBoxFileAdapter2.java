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
import com.mobi.clearsafe.ui.clear.CleanBigFileActivity;
import com.mobi.clearsafe.ui.clear.CleanQReportActivity;
import com.mobi.clearsafe.ui.clear.CleanReportActivity;
import com.mobi.clearsafe.ui.clear.PowerCoolActivity;
import com.mobi.clearsafe.ui.clear.SpeedPhoneActivity;

import java.util.ArrayList;

public class ToolBoxFileAdapter2 extends BaseMultiItemQuickAdapter<ClearBean, BaseViewHolder> {

    private int mMemoryValue;

    public ToolBoxFileAdapter2() {
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
//        CleanBigFileActivity.start(v.getContext(), item.id);
        switch (item.clearType) {
            //微信
            case 2: {
                ButtonStatistical.toolboxFeatureClick("CleanReport");
                CleanReportActivity.start(v.getContext(), item.id);
            }
            break;
            //手Q
            case 3: {
                ButtonStatistical.toolboxFeatureClick("CleanQReport");
                CleanQReportActivity.start(v.getContext(), item.id);
            }
            break;
            //手机降温
            case 4: {
                ButtonStatistical.toolboxFeatureClick("PowerCool");
                PowerCoolActivity.start(v.getContext(), item.id);
            }
            break;
            //手机加速
            default: {
                ButtonStatistical.toolboxFeatureClick("SpeedPhone");
                SpeedPhoneActivity.start(v.getContext(), item.id, mMemoryValue);
            }

            break;
        }
    }


    public void setMemoryValue(int memoryValue) {
        mMemoryValue = memoryValue;
    }
}
