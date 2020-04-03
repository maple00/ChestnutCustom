package com.rainwood.chestnut.domain;

import java.io.Serializable;

/**
 * @Author: shearson
 * @Time: 2020/3/1 21:12
 * @Desc: 地址
 */
public class AddressBean implements Serializable {

    private String id;                  // 地址id
    private String khid;            // 客户id
    private String companyName;         // 公司名称
    private String contactName;         // 联系人
    private String contactTel;          // 联系人电话
    private String region;              // 国家地区
    private String addressMx;           // 详细地址
    private String isDefault;           // 是否是默认地址

    @Override
    public String toString() {
        return "AddressBean{" +
                "id='" + id + '\'' +
                ", khid='" + khid + '\'' +
                ", companyName='" + companyName + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactTel='" + contactTel + '\'' +
                ", region='" + region + '\'' +
                ", addressMx='" + addressMx + '\'' +
                ", isDefault='" + isDefault + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKhid() {
        return khid;
    }

    public void setKhid(String khid) {
        this.khid = khid;
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

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
