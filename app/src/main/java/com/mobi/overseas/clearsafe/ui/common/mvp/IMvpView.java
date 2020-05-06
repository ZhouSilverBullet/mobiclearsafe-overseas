package com.mobi.overseas.clearsafe.ui.common.mvp;

import android.content.Context;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/15 19:05
 * @Dec 略
 */
public interface IMvpView {
    /**
     * activity 等生命周期的是否已经结束判断
     * @return
     */
    boolean isViewFinishing();

    /**
     * 用于rxjava生命周期监听
     *
     * @return
     */
    Context getContext();
}
