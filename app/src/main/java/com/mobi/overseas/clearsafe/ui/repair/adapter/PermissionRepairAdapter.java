package com.mobi.overseas.clearsafe.ui.repair.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjq.permissions.XXPermissions;
import com.mobi.overseas.clearsafe.MainActivity;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.ui.appwiget.CleanAppWidgetGuideActivity;
import com.mobi.overseas.clearsafe.ui.common.util.PermissionPageUtils;
import com.mobi.overseas.clearsafe.ui.repair.data.PermissionRepairBean;

import java.util.ArrayList;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/17 10:58
 * @Dec 略
 */
public class PermissionRepairAdapter extends BaseMultiItemQuickAdapter<PermissionRepairBean, BaseViewHolder> {
    public PermissionRepairAdapter() {
        super(new ArrayList<>());

        addItemType(0, R.layout.permission_repair_recycler_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PermissionRepairBean item) {

        switch (helper.getItemViewType()) {
            case 0: {
                ImageView ivIcon = helper.getView(R.id.ivIcon);
                TextView tvTitle = helper.getView(R.id.tvTitle);
                TextView tvDec = helper.getView(R.id.tvDec);
                TextView tvGold = helper.getView(R.id.tvGold);

                ivIcon.setImageResource(item.getIcon());
                tvTitle.setText(item.getTitle());
                tvDec.setText(item.getDec());

                LinearLayout llGold = helper.getView(R.id.llGold);
                Button tvBtn = helper.getView(R.id.tvBtn);

                if (item.isLoaded()) {
                    tvBtn.setVisibility(View.VISIBLE);
                    llGold.setVisibility(View.VISIBLE);

                    //这里要进行判断，显示三种状态
                    //1. 有权限，未领取 [去领取，显示橙色，有金币]
                    if (item.isHasPermission() && item.hasGetPoints()) {
                        tvBtn.setText("去领取");
                        tvBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                        tvBtn.setBackgroundResource(R.drawable.permission_repair_oringe_bg);
                        tvBtn.setOnClickListener(v -> {
                            if (getOnItemChildClickListener() != null) {
                                getOnItemChildClickListener().onItemChildClick(this, v, helper.getAdapterPosition() - getHeaderLayoutCount());
                            }
                        });
                    }
                    //2. 有权限，已经领取 [灰色，无金币]
                    else if (item.isHasPermission() && !item.hasGetPoints()) {
                        //隐藏金币
                        llGold.setVisibility(View.GONE);

                        tvBtn.setText("已开启");
                        tvBtn.setTextColor(mContext.getResources().getColor(R.color.c_c4c4c4));
                        tvBtn.setBackgroundResource(R.drawable.permission_repair_gray_bg);

                        //啥也不干
                        tvBtn.setOnClickListener(v -> {

                        });
                    }
                    //没有权限 [蓝色，有金币]
                    else if (!item.isHasPermission() && item.hasGetPoints()) {
                        tvBtn.setText("领金币");
                        tvBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                        tvBtn.setBackgroundResource(R.drawable.permission_repair_blue_bg);

                        tvBtn.setOnClickListener(v -> {
                            skipActivityResult(v.getContext(), item);
                        });
                    }
                    //4. [显示蓝色，没有金币]
                    else if (!item.isHasPermission() && !item.hasGetPoints()) {
                        //隐藏金币
                        llGold.setVisibility(View.GONE);

                        tvBtn.setText("去开启");
                        tvBtn.setTextColor(mContext.getResources().getColor(R.color.c_c4c4c4));
                        tvBtn.setBackgroundResource(R.drawable.permission_repair_blue_bg);

                        tvBtn.setOnClickListener(v -> {
                            skipActivityResult(v.getContext(), item);
                        });
                    }

                    tvGold.setText("+" + item.getPoints());
                } else {
                    tvBtn.setVisibility(View.GONE);
                    llGold.setVisibility(View.GONE);
                }


            }
            break;
            case 1:
                break;
        }

    }

    private void skipActivityResult(Context context, PermissionRepairBean item) {
        int skipType = item.getSkipType();
        switch (skipType) {
            //开启桌面小工具
            case 5: {
                CleanAppWidgetGuideActivity.start(context, 0);
            }
            break;
            default: {
                Intent localIntent = new Intent();
                localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
                context.startActivity(localIntent);
            }
            break;
        }


    }

}
