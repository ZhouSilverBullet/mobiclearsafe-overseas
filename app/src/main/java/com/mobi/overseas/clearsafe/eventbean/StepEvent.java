package com.mobi.overseas.clearsafe.eventbean;

import android.se.omapi.SEService;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/10/31
 * desc:
 */
public class StepEvent implements Serializable {
    private int step;

    public StepEvent(int step) {
        this.step = step;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
