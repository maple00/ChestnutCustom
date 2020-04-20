package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: a797s
 * @Date: 2020/4/7 9:21
 * @Desc: 门店列表bean
 */
public final class StoreListBean implements Serializable {

    private String id;              // 门店id
    private String name;            // 门店名称
    private String totalMoney;      // 总计
    private String tel;             // 门店电话
    private boolean hasChecked;     // 被选择

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public boolean isHasChecked() {
        return hasChecked;
    }

    public void setHasChecked(boolean hasChecked) {
        this.hasChecked = hasChecked;
    }

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

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }
}
