package com.rx.mvp.cn.http.Api;

import com.rx.mvp.cn.http.retrofit.RetrofitUtils;
import com.rx.mvp.cn.test.RetrofitTest;

/**
 * 接口工具类
 *
 * @author ZhongDaFeng
 */

public class ApiUtils {


    private static PhoneApi phoneApi;
    private static UserApi userApi;

    public static PhoneApi getPhoneApi() {
        if (phoneApi == null) {
            phoneApi = RetrofitUtils.get().retrofit().create(PhoneApi.class);
        }
        return phoneApi;
    }

    public static UserApi getUserApi() {
        if (userApi == null) {
            userApi = RetrofitUtils.get().retrofit().create(UserApi.class);
        }
        return userApi;
    }

}
