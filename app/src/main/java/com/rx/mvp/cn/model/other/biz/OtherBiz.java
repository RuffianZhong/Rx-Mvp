package com.rx.mvp.cn.model.other.biz;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.rx.mvp.cn.base.BaseBiz;
import com.rx.mvp.cn.core.net.http.helper.ParseHelper;
import com.rx.mvp.cn.core.net.http.observer.HttpRxCallback;
import com.rx.mvp.cn.core.net.http.retrofit.HttpRequest;
import com.rx.mvp.cn.model.account.entity.UserBean;
import com.rx.mvp.cn.model.other.entity.AddressBean;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.TreeMap;

/**
 * 其他业务类
 *
 * @author ZhongDaFeng
 */
public class OtherBiz extends BaseBiz {

    /**
     * 号码归属地查询API
     */
    private final String API_PHONE_QUERY = "v1/mobile/address/query";

    public void phoneQuery(String phone, LifecycleProvider lifecycle, HttpRxCallback callback) {
        /**
         * 构建参数
         */
        TreeMap<String, Object> request = new TreeMap<>();
        request.put("phone", phone);
        request.put(HttpRequest.API_URL, API_PHONE_QUERY);

        /**
         * 解析数据
         */
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                AddressBean bean = new Gson().fromJson(jsonElement, AddressBean.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });

        /**
         * 发送请求
         */
        getRequest().request(HttpRequest.Method.GET, request, lifecycle, callback);

    }

}
