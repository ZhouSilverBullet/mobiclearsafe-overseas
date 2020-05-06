package com.mobi.clearsafe.ui.powersaving.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.powersaving.data.PowerSavingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/15 16:33
 * @Dec 略
 */
public class PowerSavingAdapter extends BaseQuickAdapter<PowerSavingBean, BaseViewHolder> {

    public PowerSavingAdapter() {
        super(R.layout.power_saving_recycler_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PowerSavingBean item) {
        ImageView ivIcon = helper.getView(R.id.ivIcon);
        TextView tvName = helper.getView(R.id.tvName);
        CheckBox cbCheck = helper.getView(R.id.cbCheck);

        ivIcon.setImageDrawable(item.getImageDrawable());
        tvName.setText(item.getName());

        if (item.getMode() == 1) {
            //清除的状态下
            cbCheck.setVisibility(View.GONE);
        } else {
            cbCheck.setVisibility(View.VISIBLE);
            cbCheck.setOnCheckedChangeListener(null);
            cbCheck.setChecked(item.isCheck());
            cbCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {

            });
        }
    }

    /**
     * 把remove的设置上
     */
    public void replaceToRemoveData() {
        List<PowerSavingBean> list = new ArrayList<>();
        List<PowerSavingBean> data = getData();
        for (PowerSavingBean datum : data) {
            if (datum.isCheck()) {
                datum.setMode(PowerSavingBean.MODE_REMOVE);
                list.add(datum);
            }
        }
        replaceData(list);
    }
}
