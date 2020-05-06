package com.mobi.clearsafe.ui.cleannotice.data;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/4/20 21:02
 * @Dec 略
 */
public class CleanNoticeBean {
    private Bitmap icon;
    private Bitmap bigIcon;
    private Drawable drawableIcon;
    private String title;
    private String content;
    private String time;

    public Drawable getDrawableIcon() {
        return drawableIcon;
    }

    public void setDrawableIcon(Drawable drawableIcon) {
        this.drawableIcon = drawableIcon;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public Bitmap getBigIcon() {
        return bigIcon;
    }

    public void setBigIcon(Bitmap bigIcon) {
        this.bigIcon = bigIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
