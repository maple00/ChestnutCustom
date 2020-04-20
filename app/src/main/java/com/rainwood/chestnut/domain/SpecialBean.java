package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/2 17:39
 * @Desc: 商品规格
 */
public class SpecialBean implements Serializable {

    private String id;                  // 购物车id
    private String goodsSkuId;          // 规格id
    private String goodsSkuName;        // 规格名称
    private String skuName;              // 规格名称
    private String price;               // 单价
    private String num;              // 数量
    private String totalMoney;         // 合计金额
    private String money;                   // 合计金额
    private String refundState;         /// 退货状态 为空 可退货 applying：申请中 complete:已完成
    private String refundText;          // 退货提示

    public String getRefundState() {
        return refundState;
    }

    public void setRefundState(String refundState) {
        this.refundState = refundState;
    }

    public String getRefundText() {
        return refundText;
    }

    public void setRefundText(String refundText) {
        this.refundText = refundText;
    }

    public String getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(String goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public String getGoodsSkuName() {
        return goodsSkuName;
    }

    public void setGoodsSkuName(String goodsSkuName) {
        this.goodsSkuName = goodsSkuName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
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

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }
}
