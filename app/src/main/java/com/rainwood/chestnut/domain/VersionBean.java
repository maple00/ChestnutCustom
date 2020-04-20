package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/7 18:39
 * @Desc: 版本信息
 */
public final class VersionBean implements Serializable {

    private String url;             // 更新地址
    private String version;         // 版本号

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
