package com.r.http.cn.load.download;


import android.os.Handler;

import com.r.http.cn.RDownLoad;
import com.r.http.cn.model.Download;
import com.r.http.cn.utils.ComputeUtils;
import com.r.http.cn.utils.DBHelper;
import com.r.http.cn.utils.LogUtils;

import java.lang.ref.SoftReference;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * 下载观察者(监听)
 * 备注:在此处监听: 开始下载 、下载错误 、下载完成  等状态
 *
 * @author ZhongDaFeng
 */
public class DownloadObserver<T extends Download> implements DownloadProgressCallback, Observer<T> {

    private Download download;
    private Handler handler;
    private Disposable disposable;
    private SoftReference<DownloadCallback> downloadCallback;

    public void setDownload(Download download) {
        this.download = download;
        this.downloadCallback = new SoftReference<>(download.getCallback());
    }

    public DownloadObserver(Download download, Handler handler) {
        this.download = download;
        this.handler = handler;
        this.downloadCallback = new SoftReference<>(download.getCallback());
    }

    /**
     * 开始下载/继续下载
     * 备注：继续下载需要获取之前下载的数据
     */
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposable = d;
        download.setState(Download.State.WAITING);//等待状态
        DBHelper.get().insertOrUpdate(download);//更新数据库
        if (downloadCallback.get() != null) {//回调
            float progress = ComputeUtils.getProgress(download.getCurrentSize(), download.getTotalSize());
            downloadCallback.get().onProgress(download.getState(), download.getCurrentSize(), download.getTotalSize(), progress);
        }
    }

    /**
     * 下载出错
     * 备注：回调进度，回调onError
     */
    @Override
    public void onError(Throwable e) {
        download.setState(Download.State.ERROR);//错误状态
        RDownLoad.get().removeDownload(download, false);//移除下载
        DBHelper.get().insertOrUpdate(download);//更新数据
        if (downloadCallback.get() != null) {
            float progress = ComputeUtils.getProgress(download.getCurrentSize(), download.getTotalSize());
            downloadCallback.get().onProgress(download.getState(), download.getCurrentSize(), download.getTotalSize(), progress);
            downloadCallback.get().onError(e);
        }
    }

    /**
     * 下载完成
     * 备注：将开发者传入的Download子类回传
     */
    @Override
    public void onNext(T t) {
        download.setState(Download.State.FINISH);//下载完成
        RDownLoad.get().removeDownload(download, false);//移除下载
        DBHelper.get().insertOrUpdate(download);//更新数据
        if (downloadCallback.get() != null) {//回调
            downloadCallback.get().onSuccess(t);
        }
    }

    @Override
    public void onComplete() {
    }


    /**
     * 进度回调
     *
     * @param currentSize 当前值
     * @param totalSize   总大小
     */
    @Override
    public void progress(long currentSize, long totalSize) {
        if (download.getTotalSize() > totalSize) {
            currentSize = download.getTotalSize() - totalSize + currentSize;
        } else {
            download.setTotalSize(totalSize);
        }
        download.setCurrentSize(currentSize);
        handler.post(new Runnable() {
            @Override
            public void run() {
                    /*下载进度==总进度修改为完成状态*/
                if ((download.getCurrentSize() == download.getTotalSize()) && (download.getTotalSize() != 0)) {
                    download.setState(Download.State.FINISH);
                }
                    /*如果暂停或者停止状态延迟，不需要继续发送回调，影响显示*/
                if (download.getState() != Download.State.PAUSE) {
                    float progress = (float) download.getCurrentSize() / (float) download.getTotalSize();
                    if (downloadCallback.get() != null) {
                        downloadCallback.get().onProgress(download.getState(), download.getCurrentSize(), download.getTotalSize(), progress);
                    }
                }
            }
        });
    }

    /**
     * 取消请求
     * 备注：暂停下载时调用
     */
    public void dispose() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}