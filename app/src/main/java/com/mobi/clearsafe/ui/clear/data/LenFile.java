package com.mobi.clearsafe.ui.clear.data;

import java.io.File;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/18 11:10
 * @Dec 这个file提供给GarbageActivity删除文件夹没有长度的时候使用
 * 通知外部，减少长度
 */
public class LenFile extends File {
    private long len;

    public LenFile(String pathname) {
        super(pathname);
    }

    public void setLen(long len) {
        this.len = len;
    }

    public long getLen() {
        if (len <= 0L) {
            return length();
        }
        return len;
    }
}
