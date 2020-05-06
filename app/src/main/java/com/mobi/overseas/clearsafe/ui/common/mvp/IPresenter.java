package com.mobi.overseas.clearsafe.ui.common.mvp;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/15 19:30
 * @Dec 略
 */
public interface IPresenter<T> {
    void attach(T view);

    void detach();
}
