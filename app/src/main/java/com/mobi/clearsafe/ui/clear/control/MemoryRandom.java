package com.mobi.clearsafe.ui.clear.control;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Random;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/29 13:26
 * @Dec 略
 */
public class MemoryRandom implements Handler.Callback {

    private final Handler handler;
    private final Random random;

    private boolean newTime = false;
    private long startTotalTime = 1000;

    public MemoryRandom() {
        handler = new Handler(Looper.getMainLooper(), this);
        random = new Random(100);
        handler.sendEmptyMessageDelayed(100, startTotalTime);
    }

    /**
     * 重新计算随机的周期
     *
     * @param newTime
     */
    public void setNewTime(boolean newTime) {
        this.newTime = newTime;
        productionRandom();
    }

    /**
     * 生产随机数
     */
    private void productionRandom() {
        int randomValue = random.nextInt(120) + 100;
        if (newTime) {
            newTime = false;
            startTotalTime = 2000;
        } else {
            if (startTotalTime <= 5 * 60 *60 * 1000) {
                startTotalTime += 2000;
            }
        }
        if (randomListener != null) {
            randomListener.onRandom(randomValue);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        productionRandom();

        handler.sendEmptyMessageDelayed(100, startTotalTime);
        return false;
    }

    private IRandomListener randomListener;

    public void setRandomListener(IRandomListener randomListener) {
        this.randomListener = randomListener;
    }

    public interface IRandomListener {
        void onRandom(int random);
    }
}
