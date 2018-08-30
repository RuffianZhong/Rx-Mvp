package com.r.http.cn.function;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.r.http.cn.utils.LogUtils;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 服务器结果处理函数
 *
 * @author ZhongDaFeng
 */
public class ServerResultFunction implements Function<JsonElement, Object> {
    @Override
    public Object apply(@NonNull JsonElement response) throws Exception {
        //打印服务器回传结果
        LogUtils.e("HttpResponse:" + response.toString());
        /*此处不再处理业务相关逻辑交由开发者重写httpCallback*/
        return new Gson().toJson(response);
    }
}