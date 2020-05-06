package com.mobi.overseas.clearsafe.ui.common.mvp;

import android.support.annotation.Nullable;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/15 19:04
 * @Dec 略
 */
public abstract class BasePresenter<T extends IMvpView> implements IPresenter<T> {

    @Nullable
    protected T mView;

    @Override
    public void attach(T view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }
}
