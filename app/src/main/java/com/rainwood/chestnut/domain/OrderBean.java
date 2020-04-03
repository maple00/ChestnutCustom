package com.rainwood.chestnut.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/4 14:30
 * @Desc: 订单
 */
public class OrderBean implements Serializable {

    private String orderId;             // 订单编号
    private String status;              // 订单状态
    private String img;                 // 商品图片
    private String name;                // 商品名字
    private String shopId;              // 商家编号
    private String totalNum;            // 总件数
    private String totalCost;           // 总金额
    private String time;                // 订单时间
    // detail
    private String address;         // 收货地址
    private String company;         // 收货公司
    private String consignee;       // 收货人
    private String tel;             // 收货人电话
    private String discount;        // 折扣
    private String rate;            // 税率
    private List<SpecialBean> specialList;        // 规格参数
    private List<TitleAndHintBean> summaryList;                    // 订单汇总信息
    private List<TitleAndHintBean> orderInfoList;       // 订单信息

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public List<SpecialBean> getSpecialList() {
        return specialList;
    }

    public void setSpecialList(List<SpecialBean> specialList) {
        this.specialList = specialList;
    }

    public List<TitleAndHintBean> getSummaryList() {
        return summaryList;
    }

    public void setSummaryList(List<TitleAndHintBean> summaryList) {
        this.summaryList = summaryList;
    }

    public List<TitleAndHintBean> getOrderInfoList() {
        return orderInfoList;
    }

    public void setOrderInfoList(List<TitleAndHintBean> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
