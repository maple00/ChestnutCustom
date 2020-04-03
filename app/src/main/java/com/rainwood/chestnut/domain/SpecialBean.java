package com.rainwood.chestnut.domain;

/**
 * @Author: a797s
 * @Date: 2020/3/2 17:39
 * @Desc: 商品规格
 */
public class SpecialBean {

    private String id;                  // 购物车id
    private String skuName;              // 规格名称
    private String price;               // 单价
    private String num;              // 数量
    private String totalMoney;         // 合计金额

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
