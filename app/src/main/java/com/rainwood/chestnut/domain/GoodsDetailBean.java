package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/4/4 18:54
 * @Desc: 商品详情
 */
public final class GoodsDetailBean implements Serializable {

    private String id;          // 商品id
    private String ico;         // 商品图片
    private String name;        // 商品名称
    private String model;       // 型号
    private String goodsPromotion;      // 是否是促销商品
    private String oldMinPrice;     // 最小原价格
    private String oldMaxPrice;     // 最大原价格
    private String minPrice;        // 最小促销价格
    private String maxPrice;        // 最大促销价格
    private String cartNum;         // 购物车数量
    private String startNum;        // 起订量

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public String getGoodsPromotion() {
        return goodsPromotion;
    }

    public void setGoodsPromotion(String goodsPromotion) {
        this.goodsPromotion = goodsPromotion;
    }

    public String getOldMinPrice() {
        return oldMinPrice;
    }

    public void setOldMinPrice(String oldMinPrice) {
        this.oldMinPrice = oldMinPrice;
    }

    public String getOldMaxPrice() {
        return oldMaxPrice;
    }

    public void setOldMaxPrice(String oldMaxPrice) {
        this.oldMaxPrice = oldMaxPrice;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getCartNum() {
        return cartNum;
    }

    public void setCartNum(String cartNum) {
        this.cartNum = cartNum;
    }

    public String getStartNum() {
        return startNum;
    }

    public void setStartNum(String startNum) {
        this.startNum = startNum;
    }
}
