package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/4/4 16:03
 * @Desc: 一级商品分类
 */
public final class GoodsParentBean implements Serializable {

    private String goodsTypeOneId;              // 一级分类id
    private String goodsTypeOne;                // 一级分类名称

    public String getGoodsTypeOneId() {
        return goodsTypeOneId;
    }

    public void setGoodsTypeOneId(String goodsTypeOneId) {
        this.goodsTypeOneId = goodsTypeOneId;
    }

    public String getGoodsTypeOne() {
        return goodsTypeOne;
    }

    public void setGoodsTypeOne(String goodsTypeOne) {
        this.goodsTypeOne = goodsTypeOne;
    }
}
