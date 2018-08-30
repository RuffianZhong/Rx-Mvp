package com.r.http.cn;

import android.os.Handler;
import android.os.Looper;

import com.r.http.cn.api.Api;
import com.r.http.cn.load.download.DownloadInterceptor;
import com.r.http.cn.load.download.DownloadObserver;
import com.r.http.cn.model.Download;
import com.r.http.cn.retrofit.RetrofitUtils;
import com.r.http.cn.utils.ComputeUtils;
import com.r.http.cn.utils.DBHelper;
import com.r.http.cn.utils.LogUtils;
import com.r.http.cn.utils.RequestUtils;
import com.r.http.cn.utils.ResponseUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;


/**
 * 下载管理类
 * 备注：单例模式 开始下载，暂停下载，暂停全部，移除下载，获取下载列表
 *
 * @author ZhongDaFeng
 */
public class RDownLoad {
    /*单例模式*/
    private volatile static RDownLoad instance;
    /*下载集合*/
    private Set<Download> downloadSet;
    /*下载集合对应回调map*/
    private HashMap<String, DownloadObserver> callbackMap;
    /*Handler 回调下载进度到主线程*/
    private Handler handler;
    /*计数器*/
    private long counter = 0;

    private RDownLoad() {
        downloadSet = new HashSet<>();
        callbackMap = new HashMap<>();
        handler = new Handler(Looper.getMainLooper());
    }

    public static RDownLoad get() {
        if (instance == null) {
            synchronized (RDownLoad.class) {
                if (instance == null) {
                    instance = new RDownLoad();
                }
            }
        }
        return instance;
    }

    /**
     * 开始下载
     *
     * @param download
     */
    public void startDownload(final Download download) {
        if (download == null) return;

        /*正在下载不处理*/
        if (callbackMap.get(download.getServerUrl()) != null) {
            callbackMap.get(download.getServerUrl()).setDownload(download);
            return;
        }

        /*已完成下载*/
        if (download.getCurrentSize() == download.getTotalSize() && (download.getTotalSize() != 0)) {
            return;
        }
        LogUtils.d("RHttp startDownload:" + download.getServerUrl());
        /*判断本地文件是否存在*/
        boolean isFileExists = ComputeUtils.isFileExists(download.getLocalUrl());
        if (!isFileExists && download.getCurrentSize() > 0) {
            download.setCurrentSize(0);
        }

        DownloadObserver observer = new DownloadObserver(download, handler);
        callbackMap.put(download.getServerUrl(), observer);
        Api api;
        if (downloadSet.contains(download)) {
            api = download.getApi();
        } else {

            //下载拦截器
            DownloadInterceptor downloadInterceptor = new DownloadInterceptor(observer);
            //OkHttpClient
            OkHttpClient okHttpClient = RetrofitUtils.get().getOkHttpClientDownload(downloadInterceptor);
            //Retrofit
            Retrofit retrofit = RetrofitUtils.get().getRetrofit(RequestUtils.getBasUrl(download.getServerUrl()), okHttpClient);
            api = retrofit.create(Api.class);
            download.setApi(api);
            downloadSet.add(download);

        }
        /* RANGE 断点续传下载 */
        api.download("bytes=" + download.getCurrentSize() + "-", download.getServerUrl())
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, Object>() {//数据变换
                         @Override
                         public Object apply(@NonNull ResponseBody responseBody) throws Exception {
                             download.setState(Download.State.LOADING);//下载中状态
                             DBHelper.get().insertOrUpdate(download);//更新数据库状态(后期考虑下性能问题)
                             //写入文件
                             ResponseUtils.get().download2LocalFile(responseBody, new File(download.getLocalUrl()), download);
                             return download;
                         }
                     }
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 暂停/停止下载数据
     *
     * @param download
     */
    public void stopDownload(Download download) {

        if (download == null) return;
        LogUtils.d("RHttp stopDownload:" + download.getServerUrl());
        /**
         * 1.暂停网络数据
         * 2.设置数据状态
         * 3.更新数据库
         */

         /*1.暂停网络数据*/
        if (callbackMap.containsKey(download.getServerUrl())) {
            DownloadObserver observer = callbackMap.get(download.getServerUrl());
            observer.dispose();//取消
            callbackMap.remove(download.getServerUrl());
        }

        /*2.设置数据状态*/
        download.setState(Download.State.PAUSE);//暂停状态
        float progress = ComputeUtils.getProgress(download.getCurrentSize(), download.getTotalSize());//计算进度
        download.getCallback().onProgress(download.getState(), download.getCurrentSize(), download.getTotalSize(), progress);//回调

        /*3.更新数据库*/
        DBHelper.get().insertOrUpdate(download);

    }

    /**
     * 移除下载数据
     *
     * @param download
     * @param removeFile 是否移出本地文件
     */
    public void removeDownload(Download download, boolean removeFile) {

        if (download == null) return;
        LogUtils.d("RHttp removeDownload:" + download.getServerUrl());
        //未完成下载时,暂停再移除
        if (download.getState() != Download.State.FINISH) {
            stopDownload(download);
        }
        //移除本地保存数据
        if (removeFile) {
            ComputeUtils.deleteFile(download.getLocalUrl());
        }

        callbackMap.remove(download.getServerUrl());
        downloadSet.remove(download);

        //移除数据
        DBHelper.get().delete(download);
    }

    /**
     * 暂停/停止全部下载数据
     */
    public void stopAllDownload() {

        for (Download download : downloadSet) {
            stopDownload(download);
        }
        callbackMap.clear();
        downloadSet.clear();
        LogUtils.d("RHttp stopAllDownload");
    }

    /**
     * 获取下载列表
     * tClass extends Download
     *
     * @return
     */
    public <T> List<T> getDownloadList(Class<T> tClass) {
        List<T> list = DBHelper.get().query(tClass);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }


}
