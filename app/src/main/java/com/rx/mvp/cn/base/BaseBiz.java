package com.rx.mvp.cn.base;

import com.rx.mvp.cn.core.net.http.retrofit.HttpRequest;

/**
 * 基础业务类
 *
 * @author ZhongDaFeng
 */
public class BaseBiz {


    protected HttpRequest mHttpRequest;

    public BaseBiz() {
        mHttpRequest = new HttpRequest();
    }

    protected HttpRequest getRequest() {
        if (mHttpRequest == null) {
            mHttpRequest = new HttpRequest();
        }
        return mHttpRequest;
    }


}
