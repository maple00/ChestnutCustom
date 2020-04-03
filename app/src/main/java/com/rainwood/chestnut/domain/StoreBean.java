package com.rainwood.chestnut.domain;

/**
 * @Author: a797s
 * @Date: 2020/3/3 9:58
 * @Desc: 门店
 */
public class StoreBean {

    private String name;            // 门店名称
    private String totalAmount;     // 总金额

    @Override
    public String toString() {
        return "StoreBean{" +
                "name='" + name + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
