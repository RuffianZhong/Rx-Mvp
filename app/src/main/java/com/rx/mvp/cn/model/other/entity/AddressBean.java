package com.rx.mvp.cn.model.other.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 手机归属地实体类
 *
 * @author ZhongDaFeng
 */

public class AddressBean implements Serializable {


    //  "city":"南宁市",
    //  "cityCode":"0771",
    //  "mobileNumber":"1330000",
    //   "operator":"电信CDMA卡",
    //   "province":"广西",
    //   "zipCode":"530000"

    @SerializedName("city")
    private String city;
    @SerializedName("cityCode")
    private String cityCode;
    @SerializedName("mobileNumber")
    private String mobileNumber;
    @SerializedName("operator")
    private String operator;
    @SerializedName("province")
    private String province;
    @SerializedName("zipCode")
    private String zipCode;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
