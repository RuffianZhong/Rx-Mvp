package com.r.http.cn.callback;

import com.r.http.cn.load.upload.UploadProgressCallback;

import java.io.File;

/**
 * 上传回调接口
 *
 * @author ZhongDaFeng
 */
public abstract class UploadCallback<T> extends HttpCallback<T> implements UploadProgressCallback {


    @Override
    public void progress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile) {
        onProgress(file, currentSize, totalSize, progress, currentIndex, totalFile);
    }

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
