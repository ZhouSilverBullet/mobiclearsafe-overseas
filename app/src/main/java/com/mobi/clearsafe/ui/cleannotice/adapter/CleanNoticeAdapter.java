package com.mobi.clearsafe.ui.cleannotice.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.cleannotice.data.CleanNoticeBean;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/16 14:56
 * @Dec 略
 */
public class CleanNoticeAdapter extends BaseQuickAdapter<CleanNoticeBean, BaseViewHolder> {
    public CleanNoticeAdapter() {
        super(R.layout.clean_notice_recycler_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CleanNoticeBean item) {
        ImageView ivIcon = helper.getView(R.id.ivIcon);
        TextView tvTitle = helper.getView(R.id.tvTitle);
        TextView tvDec = helper.getView(R.id.tvDec);
        TextView tvTime = helper.getView(R.id.tvTime);

        ivIcon.setImageDrawable(item.getDrawableIcon());
        tvTitle.setText(item.getTitle());
        tvDec.setText(item.getContent());
        tvTime.setText(item.getTime());
    }
}
