package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: shearson
 * @Time: 2020/3/1 19:18
 * @Desc: 商家
 */
public class MerchantsBean implements Serializable {

    private String id;                  // 客户门店关联ID
    private String ico;                 // 门店图片
    private String name;                // 商家名字
    private String storeId;             // 门店id、
    private String tel;                 // 电话
    private String text;                // 门店描述

    @Override
    public String toString() {
        return "MerchantsBean{" +
                "id='" + id + '\'' +
                ", ico='" + ico + '\'' +
                ", name='" + name + '\'' +
                ", storeId='" + storeId + '\'' +
                ", tel='" + tel + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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
