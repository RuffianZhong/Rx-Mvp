package com.rx.mvp.cn.model.phone.biz;

import com.r.http.cn.RHttp;
import com.r.http.cn.callback.HttpCallback;
import com.rx.mvp.cn.base.BaseBiz;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.TreeMap;

/**
 * 其他业务类
 *
 * @author ZhongDaFeng
 */
public class PhoneBiz extends BaseBiz {

    /**
     * 号码归属地查询API
     */
    private final String API_PHONE_QUERY = "v1/mobile/address/query";

    public void phoneQuery(String phone, LifecycleProvider lifecycle, HttpCallback callback) {
        /**
         * 构建参数
         */
        TreeMap<String, Object> request = new TreeMap<>();
        request.put("phone", phone);
        request.putAll(getBaseRequest());

        /**
         * 发送请求
         */
        RHttp http = new RHttp.Builder()
                .post()
                .baseUrl("http://apicloud.mob.com/")
                .apiUrl(API_PHONE_QUERY)
                .addParameter(request)
                .lifecycle(lifecycle)
                .build();

        http.request(callback);

    }

}
