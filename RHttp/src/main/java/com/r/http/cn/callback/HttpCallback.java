package com.r.http.cn.callback;

import com.r.http.cn.exception.ExceptionEngine;
import com.r.http.cn.helper.ParseHelper;

/**
 * Http请求回调
 *
 * @author ZhongDaFeng
 */
public abstract class HttpCallback<T> extends BaseCallback<T> implements ParseHelper<T> {


    @Override
    public T parse(String data) {
        T t = null;
        try {
            t = onConvert(data);
        } catch (Exception e) {
            onError(ExceptionEngine.ANALYTIC_CLIENT_DATA_ERROR, "解析数据出错");
        }
        return t;
    }

    @Override
    public void inSuccess(T value) {
        onSuccess(parse((String) value));
    }

    @Override
    public void inError(int code, String desc) {
        onError(code, desc);
    }

    @Override
    public void inCancel() {
        onCancel();
    }

    /**
     * 数据转换/解析数据
     *
     * @param data
     * @return
     */
    public abstract T onConvert(String data);

    /**
     * 成功回调
     *
     * @param value
     */
    public abstract void onSuccess(T value);

    /**
     * 失败回调
     *
     * @param code
     * @param desc
     */
    public abstract void onError(int code, String desc);

    /**
     * 取消回调
     */
    public abstract void onCancel();


}
