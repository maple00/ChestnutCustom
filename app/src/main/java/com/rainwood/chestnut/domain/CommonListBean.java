package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: shearson
 * @Time: 2020/2/29 17:13
 * @Desc: 常见列表UI
 */
public class CommonListBean implements Serializable {

    private int imgPath;             // 图片路径
    private String name;                // 名称
    private String note;                // 备注
    /**
     * 显示箭头的类型：
     * 0：不显示箭头
     * 1：显示箭头
     * 2：显示其他箭头
     */
    private int arrowType;

    public int getImgPath() {
        return imgPath;
    }

    public void setImgPath(int imgPath) {
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getArrowType() {
        return arrowType;
    }

    public void setArrowType(int arrowType) {
        this.arrowType = arrowType;
    }
}
