package com.mobi.overseas.clearsafe.ui.powercontrol.adapter;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mobi.overseas.clearsafe.R;
import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.ui.powercontrol.data.BatteryBean;
import com.mobi.overseas.clearsafe.ui.powercontrol.util.SystemUtil;

import static com.mobi.overseas.clearsafe.ui.powercontrol.PowerControlActivity.POWER_CONTROL_NOTIFY;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/22 15:59
 * @Dec 略
 */
public class PowerControlAdapter extends BaseQuickAdapter<BatteryBean, BaseViewHolder> {
    public PowerControlAdapter() {
        super(R.layout.power_control_recycler_item);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BatteryBean item) {
        ImageView ivIcon = helper.getView(R.id.ivIcon);
        TextView tvText = helper.getView(R.id.tvText);

        ivIcon.setImageResource(item.getIcon());
        tvText.setText(item.getDec());
        tvText.setTextColor(mContext.getResources().getColor(item.getColor()));

        switch (item.getType()) {
            case 0:
            case 1:
            case 2:
            case 5:
            case 6:
            case 7:
                ivIcon.setSelected(item.isSelected());
                break;
            case 4:
                ivIcon.setImageLevel(item.getLevel());
                break;
            case 3:
                int level = item.getLevel();
                ivIcon.setImageLevel(level);
                break;

        }

        helper.itemView.setOnClickListener(v -> {
            handleClick(helper, item);
        });
    }

    private void handleClick(BaseViewHolder helper, BatteryBean item) {
        int type = item.getType();

        if (type > 2 && type < 7 && !SystemUtil.checkPermission()) {
            return;
        }

        switch (type) {
            //定位
            case 7:
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
                break;
            case 6: {
                boolean selected = item.isSelected();
                //旋转屏幕
                SystemUtil.setAccelerometerRotation(MyApplication.getContext(), !selected);
            }
            break;
            case 5:
                boolean selected = item.isSelected();
                if (selected) {
                    SystemUtil.stopAutoBrightness(MyApplication.getContext());
                } else {
                    SystemUtil.startAutoBrightness(MyApplication.getContext());
                }
                break;
            case 4:
                SystemUtil.setAudioMode();
                break;
            case 3:
                SystemUtil.setScreenTimeOut(MyApplication.getContext(), item.getLevel());
                break;
            case 2:
                SystemUtil.bluetooth();
                break;
            case 1:
                SystemUtil.setMobileDataEnabled();
                break;
            //开启wifi
            default:
                SystemUtil.openWifi();

                break;
        }

        //刷新界面
        Intent intent = new Intent(POWER_CONTROL_NOTIFY);
        mContext.sendBroadcast(intent);
    }


}
