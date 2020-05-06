package com.mobi.overseas.clearsafe.ui.clear.data;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WechatBean implements MultiItemEntity {
    public int itemType;

    public int icon;
    public String name;
    public String dec;

    public long fileSize;
    /**
     * 大小和单位
     */
    public String[] sizeAndUnit;
    /**
     * 文件的所在路径
     * 有可能是多个
     */
    public List<File> fileList = new ArrayList<>();

    /**
     * 默认都是选中的
     */
    public boolean isCheck = true;


    public String getFileStrSize() {
        return sizeAndUnit[0] + sizeAndUnit[1];
    }

    /**
     * icon:int
     * name:String
     * dec:String
     * size:String[] //6 mb
     * isCheck:boolean
     * filePath:String //用于删除的时候使用
     *
     * @return
     */


    @Override
    public int getItemType() {
        return itemType;
    }
}
