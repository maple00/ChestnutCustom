package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: sxs
 * @Time: 2020/4/4 16:05
 * @Desc: 二级分类
 */
public final class GoodsBean implements Serializable {

    private String goodsTypeTwoId;              // 二级分类id
    private String name;                        // 二级分类名称
    private String ico;                         // 二级分类图片

    public String getGoodsTypeTwoId() {
        return goodsTypeTwoId;
    }

    public void setGoodsTypeTwoId(String goodsTypeTwoId) {
        this.goodsTypeTwoId = goodsTypeTwoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIco() {
        return ico;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }
}
