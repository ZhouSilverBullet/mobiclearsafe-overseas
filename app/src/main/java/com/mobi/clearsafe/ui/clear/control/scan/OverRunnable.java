package com.mobi.clearsafe.ui.clear.control.scan;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/8 16:15
 * @Dec 判断任务是否结束
 */
public abstract class OverRunnable implements Runnable {
    private boolean isFinish;

    public boolean isFinish() {
        return isFinish;
    }

    @Override
    public final void run() {
        isFinish = false;
        execTask();
        isFinish = true;
    }

    public abstract void execTask();
}
