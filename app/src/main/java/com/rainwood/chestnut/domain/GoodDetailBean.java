package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/7 10:10
 * @Desc: 商品详情
 */
public final class GoodDetailBean implements Serializable {

    private String storeId;         // 门店id
    private String goodsId;         // 商品id
    private String ico;             // 图片
    private String model;           // 型号
    private String goodsName;       // 商品名称
    private String totalMoney;      // 商品总价
    private String discountMoney;   // 折扣金额
    private String realPayMoney;        // 实际支付金额
    private String tel;             // 客服电话

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(String discountMoney) {
        this.discountMoney = discountMoney;
    }

    public String getRealPayMoney() {
        return realPayMoney;
    }

    public void setRealPayMoney(String realPayMoney) {
        this.realPayMoney = realPayMoney;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
