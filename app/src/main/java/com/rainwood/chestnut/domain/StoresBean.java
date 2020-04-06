package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/4/4 16:07
 * @Desc: 商铺信息
 */
public final class StoresBean implements Serializable {

    private String id;              // 门店编号
    private String ico;             // 门店图片
    private String name;            // 门店名称
    private String tel;             // 门店电话
    private String text;            // 门店简介

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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
