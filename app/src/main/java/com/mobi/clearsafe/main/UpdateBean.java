package com.mobi.clearsafe.main;

import java.io.Serializable;

/**
 * author:zhaijinlu
 * date: 2019/11/12
 * desc: 版本更新
 */
public class UpdateBean implements Serializable {

    private String version;
    private boolean need_update;
    private boolean must_update;
    private String download_url;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isNeed_update() {
        return need_update;
    }

    public void setNeed_update(boolean need_update) {
        this.need_update = need_update;
    }

    public boolean isMust_update() {
        return must_update;
    }

    public void setMust_update(boolean must_update) {
        this.must_update = must_update;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }
}
