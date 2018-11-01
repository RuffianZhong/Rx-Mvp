package com.rx.mvp.cn.core.net.http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.r.http.cn.callback.UploadCallback;
import com.rx.mvp.cn.model.Response;

import java.io.File;

/**
 * 根据业务进一步封装
 *
 * @author ZhongDaFeng
 */
public abstract class RUploadCallback<T> extends UploadCallback<T> {

    private Response response;

    @Override
    public T onConvert(String data) {
        /**
         * 接口响应数据格式如@Response
         * 将result转化给success
         * 这里处理通过错误
         */
        T t = null;
        response = new Gson().fromJson(data, Response.class);
        int code = response.getCode();
        String msg = response.getMsg();
        switch (code) {
            case 101://token过期，跳转登录页面重新登录(示例)
                break;
            case 102://系统公告(示例)
                break;
            default:
                if (response.isSuccess()) {//与服务器约定成功逻辑
                    t = convert(response.getResult());
                } else {//统一为错误处理
                    onError(code, msg);
                }
                break;
        }
        return t;
    }

    /**
     * 数据转换/解析
     *
     * @param data
     * @return
     */
    public abstract T convert(JsonElement data);

    /**
     * 上传回调
     *
     * @param file
     * @param currentSize
     * @param totalSize
     * @param progress
     * @param currentIndex
     * @param totalFile
     */
    public abstract void onProgress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile);


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

    /**
     * 业务逻辑是否成功
     *
     * @return
     */
    @Override
    public boolean isBusinessOk() {
        return response.isSuccess();
    }
}
