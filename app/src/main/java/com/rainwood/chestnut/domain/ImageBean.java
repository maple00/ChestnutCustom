package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/4/4 19:02
 * @Desc: 图片
 */
public final class ImageBean implements Serializable {

    private String id;          // 图片
    private String ico;         // 图片地址
    private String name;        // 图片名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
