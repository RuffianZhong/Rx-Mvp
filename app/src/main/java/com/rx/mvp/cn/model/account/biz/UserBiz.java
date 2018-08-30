package com.rx.mvp.cn.model.account.biz;

import com.r.http.cn.RHttp;
import com.r.http.cn.callback.HttpCallback;
import com.rx.mvp.cn.base.BaseBiz;
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
    public void login(String userName, String password, LifecycleProvider lifecycle, HttpCallback callback) {
        /**
         * 构建请求参数
         */
        TreeMap<String, Object> request = new TreeMap<>();
        request.put("username", userName);
        request.put("password", password);
        request.putAll(getBaseRequest());

        /**
         * 发送请求
         */
        RHttp http = new RHttp.Builder()
                .post()
                .baseUrl("http://apicloud.mob.com/")
                .apiUrl(API_LOGIN)
                .addParameter(request)
                .lifecycle(lifecycle)
                .build();

        http.request(callback);

    }


}
