package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/4 10:50
 * @Desc: 商品规格
 */
public class ParamsBean implements Serializable {

    private String name;            // 规格名称
    private String price = "0";           // 价格
    private String number = "0";          // 数量

    @Override
    public String toString() {
        return "ParamsBean{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", number='" + number + '\'' +
                '}';
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
