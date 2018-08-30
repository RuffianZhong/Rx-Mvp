package com.r.http.cn.load.download;


/**
 * 下载回调接口
 *
 * @author ZhongDaFeng
 */
public interface DownloadProgressCallback {

    /**
     * 下载进度回调
     *
     * @param currentSize 当前值
     * @param totalSize   总大小
     */
    void progress(long currentSize, long totalSize);

}
