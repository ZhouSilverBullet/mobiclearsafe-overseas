package com.example.adtest.statistical;

import java.io.Serializable;

/**
 * 统计用到的数据基类
 * author : liangning
 * date : 2019-11-17  18:54
 */
public class StatisticalBean extends StatisticalBaseBean implements Serializable {

    public String network;//广告渠道 tt/gdt
    public String posid;//自由广告位id
    //pv和click取反值
    public int pv;// 1展示 0未展示
    public int click;// 1 点击 0未点击

}
