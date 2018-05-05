package com.rx.mvp.cn.model.account.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author ZhongDaFeng
 */

public class UserBean implements Serializable {
    //token	string	用户登录生成的token
    //uid	string	用户Id
    @SerializedName("token")
    private String token;
    @SerializedName("uid")
    private String uid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
