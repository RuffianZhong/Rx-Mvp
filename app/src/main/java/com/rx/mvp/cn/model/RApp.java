package com.rx.mvp.cn.model;

import android.app.Application;

import com.r.http.cn.RHttp;

/**
 * @author ZhongDaFeng
 * @date 2018/8/20
 */

public class RApp extends Application {

    /*http请求基础路径*/
    public static final String BASE_API = "http://apicloud.mob.com/";

    @Override
    public void onCreate() {
        super.onCreate();
        RHttp.Configure.get()
                .baseUrl(BASE_API)                   //基础URL
                .init(this);                        //初始化
    }

}
