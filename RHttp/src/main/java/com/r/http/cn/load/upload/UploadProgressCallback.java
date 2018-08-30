package com.r.http.cn.load.upload;

import java.io.File;

/**
 * 进度回调接口
 *
 * @author ZhongDaFeng
 */
public interface UploadProgressCallback {

    /**
     * 上传进度回调
     *
     * @param currentSize  当前值
     * @param totalSize    总大小
     * @param progress     进度
     * @param currentIndex 当前下标
     * @param totalFile    总文件数
     */
    void progress(File file, long currentSize, long totalSize, float progress, int currentIndex, int totalFile);
}
