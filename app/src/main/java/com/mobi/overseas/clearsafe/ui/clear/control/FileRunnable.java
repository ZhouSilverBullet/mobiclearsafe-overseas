package com.mobi.overseas.clearsafe.ui.clear.control;

import com.mobi.overseas.clearsafe.ui.clear.data.GarbageBean;

import java.util.List;
import java.util.logging.Handler;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/26 20:37
 * @Dec 略
 */
public class FileRunnable<T> implements Runnable {
    public List<T> list;

    public FileRunnable(List<T> list) {
        this.list = list;
    }

    @Override
    public void run() {

    }
}
