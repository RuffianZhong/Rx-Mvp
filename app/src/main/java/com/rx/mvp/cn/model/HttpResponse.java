package com.rx.mvp.cn.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class HttpResponse implements Serializable {

    /**
     * 描述信息
     */
    @SerializedName("msg")
    private String msg;

    /**
     * 状态码
     */
    @SerializedName("code")
    private int code;

    /**
     * 数据对象
     */
    @SerializedName("result")
    private JsonElement result;

    /**
     * 是否成功(这里可以与服务器约定)
     * 备注：这里约定 code==200 认为这次请求逻辑成功
     *
     * @return
     */
    public boolean isSuccess() {
        return code == 200;
    }

    public String toString() {
        String response = "[http response]" + "{\"code\": " + code + ",\"msg\":" + msg + ",\"result\":" + new Gson().toJson(result) + "}";
        return response;
    }

    //get/set

}
