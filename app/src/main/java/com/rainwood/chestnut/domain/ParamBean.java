package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/4/4 18:36
 * @Desc: 商品详情规格
 */
public final class ParamBean implements Serializable {

    private String id;              // 规格id
    private String name;            // 规格名称
    private String price;           // 价格
    private String num;             // 数量

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
