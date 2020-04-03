package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/3/30 19:39
 * @Desc: 订单详情
 */
public final class OrderDetailBean implements Serializable {

    private String id;          // 订单ID
    private String ico;         // 商品图片
    private String goodsName;       // 名称
    private String workFlow;        // 订单状态
    private String model;           // 商品型号
    private String totalMoney;      // 商品总价
    private String freightMoney;        // 运费
    private String discountMoney;       // 折扣费用
    private String realPayMoney;        // 实际支付
    private String time;                // 下单时间
    private String companyName;         // 公司名称
    private String contactName;         // 联系人
    private String contactTel;          // 联系电话
    private String region;              // 国家、地区
    private String addressMx;           // 地址详情
    private String taxRate;             // 税率
    private String taxRdiscountate;     // 折扣
    private String tel;                 // 客服电话

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getWorkFlow() {
        return workFlow;
    }

    public void setWorkFlow(String workFlow) {
        this.workFlow = workFlow;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getFreightMoney() {
        return freightMoney;
    }

    public void setFreightMoney(String freightMoney) {
        this.freightMoney = freightMoney;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddressMx() {
        return addressMx;
    }

    public void setAddressMx(String addressMx) {
        this.addressMx = addressMx;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getTaxRdiscountate() {
        return taxRdiscountate;
    }

    public void setTaxRdiscountate(String taxRdiscountate) {
        this.taxRdiscountate = taxRdiscountate;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
