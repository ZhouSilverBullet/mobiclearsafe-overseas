package com.mobi.clearsafe.ui.clear.control;

import android.os.Handler;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/21 14:39
 * @Dec 干拿数据的
 *
 * 通过handler来进行数据回调处理
 *
 */
public class ClearDataCenter {

    /**
     * 总的数据 [返回给按钮，title]
     */
    private long allSize;

    /**
     * 数据回传处理
     */
    private Handler handler;


    public ClearDataCenter() {
        init();
    }

    //处理话一些策略
    private void init() {

    }

}
