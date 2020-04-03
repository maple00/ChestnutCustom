package com.rainwood.chestnut.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/2 17:35
 * @Desc: 购物车
 */
public class CartBean implements Serializable {

    private boolean checked;            // 被选择
    private String id;                  // 购物车ID
    private String goodsId;             // 商品ID
    private String ico;             // 商品图片地址
    private String goodsName;                // 商品名称
    private String model;              // 商品型号
    private List<SpecialBean> skulist;       // 商品规格

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<SpecialBean> getSkulist() {
        return skulist;
    }

    public void setSkulist(List<SpecialBean> skulist) {
        this.skulist = skulist;
    }
}
