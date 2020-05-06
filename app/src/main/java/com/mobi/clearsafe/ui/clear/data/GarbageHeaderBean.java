package com.mobi.clearsafe.ui.clear.data;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/3/20 17:52
 * @Dec 略
 */
public class GarbageHeaderBean extends AbstractExpandableItem<GarbageBean> implements MultiItemEntity {
    public static final int TYPE_CACHE_GARBAGE = 0;
    public static final int TYPE_SYSTEM_GARBAGE = 1;
    public static final int TYPE_AD_GARBAGE = 2;
    public static final int TYPE_UNINSTALL_GARBAGE = 3;
    //无用安装包
    public static final int TYPE_INVALID_PACKAGE = 4;

    public String name;
    public int level;
    public boolean isCheck = true;
    public long allSize;
    public int headerType;

    //正在加载中一个状态
    public boolean isLoading = true;

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getItemType() {
        return 1;
    }


    @NonNull
    public static GarbageHeaderBean createGarbageHeader(String name, int headerType) {
        GarbageHeaderBean bean = new GarbageHeaderBean();
        bean.name = name;
        bean.allSize = 0L;
        bean.headerType = headerType;
        bean.setSubItems(new ArrayList<GarbageBean>());
        return bean;
    }

    public static List<MultiItemEntity> createGarbageHeaders() {
        List<MultiItemEntity> list = new ArrayList<>();

        list.add(GarbageHeaderBean.createGarbageHeader("缓存垃圾", TYPE_CACHE_GARBAGE));
        list.add(GarbageHeaderBean.createGarbageHeader("系统垃圾", TYPE_SYSTEM_GARBAGE));
        list.add(GarbageHeaderBean.createGarbageHeader("广告垃圾", TYPE_AD_GARBAGE));
//        list.add(createGarbageHeader("卸载残留", TYPE_UNINSTALL_GARBAGE));
        list.add(GarbageHeaderBean.createGarbageHeader("无用安装包", TYPE_INVALID_PACKAGE));

        return list;
    }
}
