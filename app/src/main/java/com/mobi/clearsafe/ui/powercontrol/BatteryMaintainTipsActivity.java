package com.mobi.clearsafe.ui.powercontrol;

import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobi.clearsafe.R;
import com.mobi.clearsafe.ui.common.base.BaseActivity;
import com.mobi.clearsafe.utils.UiUtils;

import butterknife.BindView;

public class BatteryMaintainTipsActivity extends BaseActivity {


    @BindView(R.id.toolBar)
    Toolbar toolBar;
    @BindView(R.id.llContent)
    LinearLayout llContent;
    private String[] title = {"1. 手机电量不要用尽后再充电", "2. 手机电量充满后不要继续长时间充电", "3. 不要一边充电一边玩手机", "4. 不要将手机放在沙发/床上充电", "5. 不要使用非原装的充电器或充电线", "6. 不要用电脑USB接口为手机充电", "7. 不要长期用充电宝为手机充电", "8. 不要在过冷的情况下长时间使用手机"};
    private String[] content = {"现在手机使用的都是锂离子聚合物电池，这类电池已经没有过去镍镉电池所谓的“记忆效应”，所以没有必要将手机的电量用完再充电。如果锂电池长时间处于低电压的状态，反而会导致无法充电的现象发生。", "很多小伙伴喜欢在睡觉的时候充电，第二天再拔掉充电器。这样做理论上电量充满后手机会自动停止充电，但充电过程中电池过热，甚至燃烧都是在充电期间发生的，我们应该尽量避免让手机在长时间无人看守的情况下充电。", "我们在使用智能手机的过程中，应当尽量避免电池过热的情况发生。边充电边玩手机会导致电池和处理器发热量增加，影响电池寿命。", "发热是导致手机电池寿命缩短的元凶，而手机只能自散热。如果在沙发或者床上充电，会导致机身热量堆积，造成局部过热的情况，再加上沙发、床单、棉絮均为易燃物，这个时候如果发生意外，火灾的可能性将很大", "原装的充电器都内置了保护芯片，如果使用第三方劣质充电器，一些小的配件厂商为了节省成本，生产了一些没有保护芯片的三无充电器使用时，就有可能会出现功率不稳定甚至电流过大的危险，导致智能手机损坏。", "很多电脑USB接口电流不到500mA，为手机充电的过程会非常漫长。更严重的是，一些电脑主板的电源机制并不完善，如果同时连接或者插拔多款USB设备，就会导致USB接口输出电流不稳定，这也不利于手机电池的长期使用。", "虽然现在很多充电宝都能提供1A以上，甚至2.1A的高电流充电，但充电宝毕竟不是电源，无法保证电压/电流的持续稳定输出。再加上充电宝在为手机充电时本身也会产生一定的热量，所以不推荐长期使用充电宝为手机充电，只能作为应急时使用。", "锂电池不仅怕热，而且怕冷，在寒冷的环境中，锂电活性、及负极活性物质的嵌锂活性都将受到影响，电池容量减少，倍率性能下降。在较低温条件下使用，严重时会在负极表面形成锂枝晶，刺破隔膜，而引起安全事故。"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_battery_maintain_tips;
    }

    @Override
    protected boolean isFitWindow() {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    @Override
    public void initView() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int i = 0;
        while (i < this.title.length) {
            String localObject = this.title[i];
            String paramBundle = this.content[i];
            TextView localTextView = new TextView(this);
            localTextView.setTextSize(1, 16.0F);
            localTextView.setTextColor(getResources().getColor(R.color.black_33));
            localTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            localTextView.setText((CharSequence) localObject);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
            lp.setMargins(0, UiUtils.dp2px(this, 30), 0, UiUtils.dp2px(this, 16));
            this.llContent.addView(localTextView, lp);
            localTextView = new TextView(this);
            localTextView.setTextSize(1, 14.0F);
            localTextView.setTextColor(getResources().getColor(R.color.black_66));
            localTextView.setLineSpacing(0.0F, 1.5F);
            localTextView.setText(paramBundle);
            this.llContent.addView(localTextView);
            i += 1;
        }
    }

}
