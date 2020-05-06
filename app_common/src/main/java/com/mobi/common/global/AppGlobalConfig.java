package com.mobi.common.global;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/13 15:29
 * @Dec 略
 */
public class AppGlobalConfig {
    //公用的线程池
    //原来想些 Executor 不过，okhttp 需要 ExecutorService
    public static final ExecutorService APP_THREAD_POOL_EXECUTOR;

    static {
        // 就是 Executor#newCachedThreadPool
        // OkHttp使用的也是这个
        APP_THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());
    }
}
