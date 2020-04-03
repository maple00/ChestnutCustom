package com.rainwood.chestnut.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/3 16:48
 * @Desc: 商品
 */
public class ShopBean implements Serializable {

    private String img;         // 商品图片
    private String name;        // 名称
    private String num;         // 商家NO
    private String label;       // 标签
    private String price;       // 价格   --- 是具体价格还是价格范围 -- 看接口
    private String number;      // 数量
    // 详情
    private List<String> imgs;      // 轮播图片
    private String minReserve;  // 最小起订量
    private List<ParamsBean> paramsList;        // 规格参数

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getMinReserve() {
        return minReserve;
    }

    public void setMinReserve(String minReserve) {
        this.minReserve = minReserve;
    }

    public List<ParamsBean> getParamsList() {
        return paramsList;
    }

    public void setParamsList(List<ParamsBean> paramsList) {
        this.paramsList = paramsList;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
