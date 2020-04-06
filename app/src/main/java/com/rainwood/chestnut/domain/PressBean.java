package com.rainwood.chestnut.domain;

/**
 * @Author: a797s
 * @Date: 2020/3/3 15:29
 * @Desc: 按压选中
 */
public class PressBean {

    private String id;          // 选中id
    private String name;        // 选中名称
    private boolean choose;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }
}
