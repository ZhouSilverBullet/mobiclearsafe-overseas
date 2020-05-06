package com.mobi.clearsafe.main.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2020/2/11
 * desc:集数字详情
 */
public class CollectNumBean implements Serializable {

    private String before_stages_number;//上次参赛期号
    private boolean before_phase_is_winning;//是否可领奖
    private int before_points;//上次参赛获得积分
    private int before_qualified_persons;//上次参赛达标总人数
    private String stages_number;//本期期号
    private int qualified_persons;//达标人数(集齐人数)
    private boolean hammer_status;//合成锤子开大奖状态 false不可合成  true可合成
    private List<LuckyNumBer> lucky_number;//开奖数字

    public String getBefore_stages_number() {
        return before_stages_number;
    }

    public void setBefore_stages_number(String before_stages_number) {
        this.before_stages_number = before_stages_number;
    }

    public boolean isBefore_phase_is_winning() {
        return before_phase_is_winning;
    }

    public void setBefore_phase_is_winning(boolean before_phase_is_winning) {
        this.before_phase_is_winning = before_phase_is_winning;
    }

    public int getBefore_points() {
        return before_points;
    }

    public void setBefore_points(int before_points) {
        this.before_points = before_points;
    }

    public int getBefore_qualified_persons() {
        return before_qualified_persons;
    }

    public void setBefore_qualified_persons(int before_qualified_persons) {
        this.before_qualified_persons = before_qualified_persons;
    }

    public String getStages_number() {
        return stages_number;
    }

    public void setStages_number(String stages_number) {
        this.stages_number = stages_number;
    }

    public int getQualified_persons() {
        return qualified_persons;
    }

    public void setQualified_persons(int qualified_persons) {
        this.qualified_persons = qualified_persons;
    }

    public boolean isHammer_status() {
        return hammer_status;
    }

    public void setHammer_status(boolean hammer_status) {
        this.hammer_status = hammer_status;
    }

    public List<LuckyNumBer> getLucky_number() {
        return lucky_number;
    }

    public void setLucky_number(List<LuckyNumBer> lucky_number) {
        this.lucky_number = lucky_number;
    }
}
