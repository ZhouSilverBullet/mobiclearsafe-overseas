package com.mobi.overseas.clearsafe.ui.powercontrol.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.powercontrol.data.BatteryPropertyBean;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/22 15:39
 * @Dec 略
 */
public class PowerControlPropertyAdapter extends BaseQuickAdapter<BatteryPropertyBean, BaseViewHolder> {
    public PowerControlPropertyAdapter() {
        super(R.layout.power_control_property_reycler_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BatteryPropertyBean item) {
        TextView tvTitle = helper.getView(R.id.tvTitle);
        TextView tvContent = helper.getView(R.id.tvContent);
        tvTitle.setText(item.getTitle());
        tvContent.setText(item.getContent());
    }
}
