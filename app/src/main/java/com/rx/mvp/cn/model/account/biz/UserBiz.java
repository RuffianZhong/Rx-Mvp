package com.rx.mvp.cn.model.account.biz;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.rx.mvp.cn.base.BaseBiz;
import com.rx.mvp.cn.core.net.http.helper.ParseHelper;
import com.rx.mvp.cn.core.net.http.observer.HttpRxCallback;
import com.rx.mvp.cn.core.net.http.retrofit.HttpRequest;
import com.rx.mvp.cn.model.account.entity.UserBean;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.TreeMap;

/**
 * 用户相关业务
 *
 * @author ZhongDaFeng
 */
public class UserBiz extends BaseBiz {

    /**
     * 登录API
     */
    private final String API_LOGIN = "user/login";

    /**
     * 用户登录
     *
     * @param userName
     * @param password
     * @param lifecycle
     * @param callback
     */
    public void login(String userName, String password, LifecycleProvider lifecycle, HttpRxCallback callback) {
        /**
         * 构建请求参数
         */
        TreeMap<String, Object> request = new TreeMap<>();
        request.put("username", userName);
        request.put("password", password);
        request.put(HttpRequest.API_URL, API_LOGIN);

        /**
         * 解析数据
         */
        callback.setParseHelper(new ParseHelper() {
            @Override
            public Object[] parse(JsonElement jsonElement) {
                UserBean bean = new Gson().fromJson(jsonElement, UserBean.class);
                Object[] obj = new Object[1];
                obj[0] = bean;
                return obj;
            }
        });

        /**
         * 发送请求
         */
        getRequest().request(HttpRequest.Method.POST, request, lifecycle, callback);

    }

}
