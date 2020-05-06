package com.mobi.clearsafe.ui.clear.control;

import android.os.Handler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/26 20:30
 * @Dec 略
 */
public class TimedRun {
    private static final ExecutorService taskExec = Executors.newCachedThreadPool();

    public static void timedRun(long timeout, TimeUnit unit, FileRunnable r, ICancelCallback callback)
            throws InterruptedException {
        Future<?> task = taskExec.submit(r);
        try {
            task.get(timeout, unit);
        } catch (TimeoutException e) {
            // 因超时而取消任务
            if (callback != null) {
                callback.onCancel();
            }
        } catch (ExecutionException e) {
            // 任务异常，重新抛出异常信息
        } finally {
            // 如果该任务已经完成，将没有影响
            // 如果任务正在运行，将因为中断而被取消
            task.cancel(true); // interrupt if running
        }
    }

    public interface ICancelCallback {
        void onCancel();
    }
}
