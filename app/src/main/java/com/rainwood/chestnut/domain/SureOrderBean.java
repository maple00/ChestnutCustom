package com.rainwood.chestnut.domain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/3 11:35
 * @Desc: 确认订单
 */
public class SureOrderBean  {

    private String methodl;             // 配送方式
    private String expressName;         // 收货人姓名
    private String expressTel;          // 收货人电话
    private String addres;              // 收货地址
    private List<CartBean> goodsList;       // 商品
    private List<TitleAndHintBean> summaryList;    // 汇总
    private String serviceTel;          // 客服电话
    private String totalAmount;         // 需支付总金额

    @Override
    public String toString() {
        return "SureOrderBean{" +
                "methodl='" + methodl + '\'' +
                ", expressName='" + expressName + '\'' +
                ", expressTel='" + expressTel + '\'' +
                ", addres='" + addres + '\'' +
                ", goodsList=" + goodsList +
                ", summaryList=" + summaryList +
                ", serviceTel='" + serviceTel + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                '}';
    }

    public String getMethodl() {
        return methodl;
    }

    public void setMethodl(String methodl) {
        this.methodl = methodl;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getExpressTel() {
        return expressTel;
    }

    public void setExpressTel(String expressTel) {
        this.expressTel = expressTel;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public List<CartBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<CartBean> goodsList) {
        this.goodsList = goodsList;
    }

    public List<TitleAndHintBean> getSummaryList() {
        return summaryList;
    }

    public void setSummaryList(List<TitleAndHintBean> summaryList) {
        this.summaryList = summaryList;
    }

    public String getServiceTel() {
        return serviceTel;
    }

    public void setServiceTel(String serviceTel) {
        this.serviceTel = serviceTel;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
