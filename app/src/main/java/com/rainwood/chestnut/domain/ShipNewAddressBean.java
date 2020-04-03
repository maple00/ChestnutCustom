package com.rainwood.chestnut.domain;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/2 9:54
 * @Desc: 新增收货地址
 */
public class ShipNewAddressBean {

    private boolean orDefault;          // 是否是默认地址
    private List<ComEditBean> mList;    // content

    public boolean isOrDefault() {
        return orDefault;
    }

    public void setOrDefault(boolean orDefault) {
        this.orDefault = orDefault;
    }

    public List<ComEditBean> getList() {
        return mList;
    }

    public void setList(List<ComEditBean> list) {
        mList = list;
    }
}
