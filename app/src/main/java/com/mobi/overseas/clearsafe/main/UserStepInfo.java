package com.mobi.overseas.clearsafe.main;

import java.io.Serializable;
import java.util.List;

/**
 * author:zhaijinlu
 * date: 2019/11/1
 * desc:
 */
public class UserStepInfo implements Serializable {

    private int step;
    private int minute;
    private double calories;
    private double kilometer;
    private int coins;
    private int target_step;
    private int max_steps;//首页转盘一圈的步数

    private int next_target;//下一阶段需要步数，如果为0，代表完成所有阶段
    private int next_coins;//下一阶段奖励
    private int button_state;//领取button状态 1-继续努力 4-可领取   5-今日已达标
    private int total_coins;
    private float cash;
    private int pop_type;//阶段奖励弹框样式
    private String pop_up_message;
    private int reward_points;//本次奖励金币数
    public int sign_points;
    private String phase_content;//阶段文案
    private boolean is_step_bubble;//是否有步数泡泡
    private List<GoldBubble> coins_bubble_list;//金币泡泡列表

    public String getPhase_content() {
        return phase_content;
    }

    public void setPhase_content(String phase_content) {
        this.phase_content = phase_content;
    }

    public boolean isIs_step_bubble() {
        return is_step_bubble;
    }

    public void setIs_step_bubble(boolean is_step_bubble) {
        this.is_step_bubble = is_step_bubble;
    }

    public List<GoldBubble> getCoins_bubble_list() {
        return coins_bubble_list;
    }

    public void setCoins_bubble_list(List<GoldBubble> coins_bubble_list) {
        this.coins_bubble_list = coins_bubble_list;
    }

    public int getSign_points() {
        return sign_points;
    }

    public void setSign_points(int sign_points) {
        this.sign_points = sign_points;
    }

    private StepBubble bubble;

    public int getPop_type() {
        return pop_type;
    }

    public void setPop_type(int pop_type) {
        this.pop_type = pop_type;
    }

    public String getPop_up_message() {
        return pop_up_message;
    }

    public void setPop_up_message(String pop_up_message) {
        this.pop_up_message = pop_up_message;
    }

    public int getReward_points() {
        return reward_points;
    }

    public void setReward_points(int reward_points) {
        this.reward_points = reward_points;
    }

    public int getMax_steps() {
        return max_steps;
    }

    public void setMax_steps(int max_steps) {
        this.max_steps = max_steps;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getKilometer() {
        return kilometer;
    }

    public void setKilometer(double kilometer) {
        this.kilometer = kilometer;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getTarget_step() {
        return target_step;
    }

    public void setTarget_step(int target_step) {
        this.target_step = target_step;
    }

    public int getNext_target() {
        return next_target;
    }

    public void setNext_target(int next_target) {
        this.next_target = next_target;
    }

    public int getNext_coins() {
        return next_coins;
    }

    public void setNext_coins(int next_coins) {
        this.next_coins = next_coins;
    }

    public int getButton_state() {
        return button_state;
    }

    public void setButton_state(int button_state) {
        this.button_state = button_state;
    }

    public int getTotal_coins() {
        return total_coins;
    }

    public void setTotal_coins(int total_coins) {
        this.total_coins = total_coins;
    }

    public float getCash() {
        return cash;
    }

    public void setCash(float cash) {
        this.cash = cash;
    }

    public StepBubble getBubble() {
        return bubble;
    }

    public void setBubble(StepBubble bubble) {
        this.bubble = bubble;
    }
}
