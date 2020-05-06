package com.mobi.clearsafe.ui.clear.adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.mobi.clearsafe.R;

import java.util.ArrayList;

public class WechatAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public WechatAdapter() {
        super(new ArrayList<>());
        addItemType(1, R.layout.item_clear_layout);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiItemEntity item) {
        switch (item.getItemType()) {
            case 1:
                View view = helper.getView(R.id.clRoot);
                TextView tvNum = helper.getView(R.id.tvNum);
                view.setBackgroundResource(R.drawable.clear_orange_shape);
//                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 100f);
//                valueAnimator.setDuration(3000);
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        float animatedValue = (float) animation.getAnimatedValue();
//                        tvNum.setText(animatedValue + "");
//                    }
//                });
//                valueAnimator.start();
                break;
        }
    }
}
