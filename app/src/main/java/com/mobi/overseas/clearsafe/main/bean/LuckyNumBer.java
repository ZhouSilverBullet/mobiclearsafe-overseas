package com.mobi.overseas.clearsafe.main.bean;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2020/2/11
 * desc: 开奖数字
 */
public class LuckyNumBer implements Serializable {
    private String number;//本期开奖数字
    private int numbers;//value收集到的个数

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getNumbers() {
        return numbers;
    }

    public void setNumbers(int numbers) {
        this.numbers = numbers;
    }
}
