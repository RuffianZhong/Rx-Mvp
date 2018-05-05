package com.rx.mvp.cn.core.net.http.api;

import com.rx.mvp.cn.core.net.http.retrofit.HttpResponse;

import java.util.TreeMap;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Api接口
 *
 * @author ZhongDaFeng
 */
public interface Api {


    /**
     * GET请求
     *
     * @param url     api接口url
     * @param request 请求参数map
     * @return
     */
    @GET
    Observable<HttpResponse> get(@Url String url, @QueryMap TreeMap<String, Object> request);

    /**
     * POST请求
     *
     * @param url     api接口url
     * @param request 请求参数map
     * @return
     */
    @FormUrlEncoded
    @POST
    Observable<HttpResponse> post(@Url String url, @FieldMap TreeMap<String, Object> request);
}
