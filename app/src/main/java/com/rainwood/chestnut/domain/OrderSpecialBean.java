package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/30 19:45
 * @Desc: 订单详情的规格参数
 */
public final class OrderSpecialBean implements Serializable {

    private String id;      // 订单明细ID
    private String skuName;         // 规格名称
    private String price;               // 单价
    private String num;                 // 数量
    private String money;               // 合计
    private String refundState;         // 退货状态

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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRefundState() {
        return refundState;
    }

    public void setRefundState(String refundState) {
        this.refundState = refundState;
    }
}
