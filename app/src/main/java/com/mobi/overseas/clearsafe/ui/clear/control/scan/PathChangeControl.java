package com.mobi.overseas.clearsafe.ui.clear.control.scan;

import android.os.AsyncTask;

import com.mobi.overseas.clearsafe.app.MyApplication;
import com.mobi.overseas.clearsafe.ui.common.global.AppGlobalConfig;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/8 16:32
 * @Dec 略
 */
public class PathChangeControl {
    private BlockingQueue<String> pathQueue = new LinkedBlockingDeque<>();
    private volatile boolean isOffer = false;

    public void runTask() {
        //执行队列的逻辑
        AppGlobalConfig.APP_THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String take = pathQueue.poll();
                        if (isOffer && take == null) {
                            break;
                        }
                        if (pathCallback != null && isOffer) {
                            pathCallback.onPath(take);
                        }
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setOffer(boolean offer) {
        isOffer = offer;
    }

    public void offer(String path) {
        if (pathQueue != null) {
            pathQueue.offer(path);
        }
        setOffer(true);
    }

    public int size() {
        return pathQueue.size();
    }

    private IPathCallback pathCallback;

    public void setPathCallback(IPathCallback pathCallback) {
        this.pathCallback = pathCallback;
    }

    public interface IPathCallback {
        void onPath(String path);
    }
}
