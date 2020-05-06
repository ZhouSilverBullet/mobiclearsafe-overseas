package com.mobi.overseas.clearsafe.ui.clear.control.scan;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/27 15:44
 * @Dec 略
 */
public interface IScanCallback<T> {
    void onBegin();

    void onProgress(T info);

    void onFinish();

    /**
     * 用于显示扫描的路径
     * @param path
     */
    default void onPath(String path) {

    }
}
