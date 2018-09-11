package com.r.http.cn;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.r.http.cn.api.Api;
import com.r.http.cn.callback.HttpCallback;
import com.r.http.cn.callback.UploadCallback;
import com.r.http.cn.cancel.RequestManagerImpl;
import com.r.http.cn.load.upload.UploadRequestBody;
import com.r.http.cn.observer.HttpObservable;
import com.r.http.cn.retrofit.Method;
import com.r.http.cn.retrofit.RetrofitUtils;
import com.r.http.cn.utils.RequestUtils;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Http请求类
 *
 * @author ZhongDaFeng
 */
public class RHttp {

    /*请求方式*/
    private Method method;
    /*请求参数*/
    private Map<String, Object> parameter;
    /*header*/
    private Map<String, Object> header;
    /*LifecycleProvider*/
    private LifecycleProvider lifecycle;
    /*ActivityEvent*/
    private ActivityEvent activityEvent;
    /*FragmentEvent*/
    private FragmentEvent fragmentEvent;
    /*HttpCallback*/
    private HttpCallback httpCallback;
    /*标识请求的TAG*/
    private String tag;
    /*文件map*/
    private Map<String, File> fileMap;
    /*上传文件回调*/
    private UploadCallback uploadCallback;
    /*基础URL*/
    private String baseUrl;
    /*apiUrl*/
    private String apiUrl;
    /*String参数*/
    String bodyString;
    /*是否强制JSON格式*/
    boolean isJson;


    /*构造函数*/
    private RHttp(Builder builder) {
        this.parameter = builder.parameter;
        this.header = builder.header;
        this.lifecycle = builder.lifecycle;
        this.activityEvent = builder.activityEvent;
        this.fragmentEvent = builder.fragmentEvent;
        this.tag = builder.tag;
        this.fileMap = builder.fileMap;
        this.baseUrl = builder.baseUrl;
        this.apiUrl = builder.apiUrl;
        this.isJson = builder.isJson;
        this.bodyString = builder.bodyString;
        this.method = builder.method;
    }

    /*普通Http请求*/
    public void request(HttpCallback httpCallback) {
        this.httpCallback = httpCallback;
        if (httpCallback == null) {
            throw new NullPointerException("HttpObserver must not null!");
        } else {
            doRequest();
        }
    }

    /*上传文件请求*/
    public void upload(UploadCallback uploadCallback) {
        this.uploadCallback = uploadCallback;
        if (uploadCallback == null) {
            throw new NullPointerException("UploadCallback must not null!");
        } else {
            doUpload();
        }
    }

    /*取消网络请求*/
    public void cancel() {
        if (httpCallback != null) {
            httpCallback.cancel();
        }
        if (uploadCallback != null) {
            uploadCallback.cancel();
        }
    }

    /*请求是否已经取消*/
    public boolean isCanceled() {
        boolean isCanceled = true;
        if (httpCallback != null) {
            isCanceled = httpCallback.isDisposed();
        }
        if (uploadCallback != null) {
            isCanceled = uploadCallback.isDisposed();
        }
        return isCanceled;
    }

    /**
     * 根据tag取消请求
     *
     * @param tag
     */
    public static void cancel(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        RequestManagerImpl.getInstance().cancel(tag);
    }

    /**
     * 取消全部请求
     */
    public static void cancelAll() {
        RequestManagerImpl.getInstance().cancelAll();
    }


    /*执行请求*/
    private void doRequest() {

        /*设置请求唯一标识*/
        httpCallback.setTag(TextUtils.isEmpty(tag) ? String.valueOf(System.currentTimeMillis()) : tag);

        /*header处理*/
        disposeHeader();

        /*Parameter处理*/
        disposeParameter();

        /*请求方式处理*/
        Observable apiObservable = disposeApiObservable();

        /* 被观察者 httpObservable */
        HttpObservable httpObservable = new HttpObservable.Builder(apiObservable)
                .httpObserver(httpCallback)
                .lifecycleProvider(lifecycle)
                .activityEvent(activityEvent)
                .fragmentEvent(fragmentEvent)
                .build();
        /* 观察者  httpObserver */
        /*设置监听*/
        httpObservable.observe().subscribe(httpCallback);

    }

    /*执行文件上传*/
    private void doUpload() {

        /*设置请求唯一标识*/
        uploadCallback.setTag(TextUtils.isEmpty(tag) ? String.valueOf(System.currentTimeMillis()) : tag);

        /*header处理*/
        disposeHeader();

        /*Parameter处理*/
        disposeParameter();

        /*处理文件集合*/
        List<MultipartBody.Part> fileList = new ArrayList<>();
        if (fileMap != null && fileMap.size() > 0) {
            int size = fileMap.size();
            int index = 1;
            File file;
            RequestBody requestBody;
            for (String key : fileMap.keySet()) {
                file = fileMap.get(key);
                requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), new UploadRequestBody(requestBody, file, index, size, uploadCallback));
                fileList.add(part);
                index++;
            }
        }

       /*请求处理*/
        Observable apiObservable = RetrofitUtils.get().getRetrofit(getBaseUrl(), header, httpCallback).create(Api.class).upload(disposeApiUrl(), parameter, header, fileList);

       /* 被观察者 httpObservable */
        HttpObservable httpObservable = new HttpObservable.Builder(apiObservable)
                .httpObserver(uploadCallback)
                .lifecycleProvider(lifecycle)
                .activityEvent(activityEvent)
                .fragmentEvent(fragmentEvent)
                .build();
        /* 观察者  uploadCallback */
        /*设置监听*/
        httpObservable.observe().subscribe(uploadCallback);

    }

    /*获取基础URL*/
    private String getBaseUrl() {
        //如果没有重新指定URL则是用默认配置
        return TextUtils.isEmpty(baseUrl) ? Configure.get().getBaseUrl() : baseUrl;
    }

    /*ApiUrl处理*/
    private String disposeApiUrl() {
        return TextUtils.isEmpty(apiUrl) ? "" : apiUrl;
    }

    /*处理Header*/
    private void disposeHeader() {

       /*header空处理*/
        if (header == null) {
            header = new TreeMap<>();
        }

        //添加基础 Header
        Map<String, Object> baseHeader = Configure.get().getBaseHeader();
        if (baseHeader != null && baseHeader.size() > 0) {
            header.putAll(baseHeader);
        }

        if (!header.isEmpty()) {
            //处理header中文或者换行符出错问题
            for (String key : header.keySet()) {
                header.put(key, RequestUtils.getHeaderValueEncoded(header.get(key)));
            }
        }

    }

    /*处理 Parameter*/
    private void disposeParameter() {

        /*空处理*/
        if (parameter == null) {
            parameter = new TreeMap<>();
        }

        //添加基础 Parameter
        Map<String, Object> baseParameter = Configure.get().getBaseParameter();
        if (baseParameter != null && baseParameter.size() > 0) {
            parameter.putAll(baseParameter);
        }
    }

    /*处理ApiObservable*/
    private Observable disposeApiObservable() {

        Observable apiObservable = null;

        /*是否JSON格式提交参数*/
        boolean hasBodyString = !TextUtils.isEmpty(bodyString);
        RequestBody requestBody = null;
        if (hasBodyString) {
            String mediaType = isJson ? "application/json; charset=utf-8" : "text/plain;charset=utf-8";
            requestBody = RequestBody.create(okhttp3.MediaType.parse(mediaType), bodyString);
        }

        /*Api接口*/
        Api apiService = RetrofitUtils.get().getRetrofit(getBaseUrl(), header, httpCallback).create(Api.class);
        /*未指定默认POST*/
        if (method == null) method = Method.POST;

        switch (method) {
            case GET:
                apiObservable = apiService.get(disposeApiUrl(), parameter, header);
                break;
            case POST:
                if (hasBodyString)
                    apiObservable = apiService.post(disposeApiUrl(), requestBody, header);
                else
                    apiObservable = apiService.post(disposeApiUrl(), parameter, header);
                break;
            case DELETE:
                apiObservable = apiService.delete(disposeApiUrl(), parameter, header);
                break;
            case PUT:
                apiObservable = apiService.put(disposeApiUrl(), parameter, header);
                break;
        }
        return apiObservable;
    }

    /**
     * Configure配置
     */
    public static final class Configure {

        /*请求基础路径*/
        String baseUrl;
        /*超时时长*/
        long timeout;
        /*时间单位*/
        TimeUnit timeUnit;
        /*全局上下文*/
        Context context;
        /*全局Handler*/
        Handler handler;
        /*请求参数*/
        Map<String, Object> parameter;
        /*header*/
        Map<String, Object> header;
        /*是否显示Log*/
        boolean showLog;


        public static Configure get() {
            return Configure.Holder.holder;
        }

        private static class Holder {
            private static Configure holder = new Configure();
        }

        private Configure() {
            timeout = 60;//默认60秒
            timeUnit = TimeUnit.SECONDS;//默认秒
            showLog = true;//默认打印LOG
        }

        /*请求基础路径*/
        public RHttp.Configure baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        /*基础参数*/
        public RHttp.Configure baseParameter(Map<String, Object> parameter) {
            this.parameter = parameter;
            return this;
        }

        public Map<String, Object> getBaseParameter() {
            return parameter;
        }

        /*基础Header*/
        public RHttp.Configure baseHeader(Map<String, Object> header) {
            this.header = header;
            return this;
        }

        public Map<String, Object> getBaseHeader() {
            return header;
        }

        /*超时时长*/
        public RHttp.Configure timeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public long getTimeout() {
            return timeout;
        }

        /*是否显示LOG*/
        public RHttp.Configure showLog(boolean showLog) {
            this.showLog = showLog;
            return this;
        }

        public boolean isShowLog() {
            return showLog;
        }

        /*时间单位*/
        public RHttp.Configure timeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        /*Handler*/
        public Handler getHandler() {
            return handler;
        }

        /*Context*/
        public Context getContext() {
            return context;
        }

        /*初始化全局上下文*/
        public RHttp.Configure init(Application app) {
            this.context = app.getApplicationContext();
            this.handler = new Handler(Looper.getMainLooper());
            return this;
        }

    }

    /**
     * Builder
     * 构造Request所需参数，按需设置
     */
    public static final class Builder {
        /*请求方式*/
        Method method;
        /*请求参数*/
        Map<String, Object> parameter;
        /*header*/
        Map<String, Object> header;
        /*LifecycleProvider*/
        LifecycleProvider lifecycle;
        /*ActivityEvent*/
        ActivityEvent activityEvent;
        /*FragmentEvent*/
        FragmentEvent fragmentEvent;
        /*标识请求的TAG*/
        String tag;
        /*文件map*/
        Map<String, File> fileMap;
        /*基础URL*/
        String baseUrl;
        /*apiUrl*/
        String apiUrl;
        /*String参数*/
        String bodyString;
        /*是否强制JSON格式*/
        boolean isJson;

        public Builder() {
        }

        /*GET*/
        public RHttp.Builder get() {
            this.method = Method.GET;
            return this;
        }

        /*POST*/
        public RHttp.Builder post() {
            this.method = Method.POST;
            return this;
        }

        /*DELETE*/
        public RHttp.Builder delete() {
            this.method = Method.DELETE;
            return this;
        }

        /*PUT*/
        public RHttp.Builder put() {
            this.method = Method.PUT;
            return this;
        }

        /*基础URL*/
        public RHttp.Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /*API URL*/
        public RHttp.Builder apiUrl(String apiUrl) {
            this.apiUrl = apiUrl;
            return this;
        }

        /* 增加 Parameter 不断叠加参数 包括基础参数 */
        public RHttp.Builder addParameter(Map<String, Object> parameter) {
            if (this.parameter == null) {
                this.parameter = new TreeMap<>();
            }
            this.parameter.putAll(parameter);
            return this;
        }

        /*设置 Parameter 会覆盖 Parameter 包括基础参数*/
        public RHttp.Builder setParameter(Map<String, Object> parameter) {
            this.parameter = parameter;
            return this;
        }

        /* 设置String 类型参数  覆盖之前设置  isJson:是否强制JSON格式    bodyString设置后Parameter则无效 */
        public RHttp.Builder setBodyString(String bodyString, boolean isJson) {
            this.isJson = isJson;
            this.bodyString = bodyString;
            return this;
        }

        /* 增加 Header 不断叠加 Header 包括基础 Header */
        public RHttp.Builder addHeader(Map<String, Object> header) {
            this.header = header;
            return this;
        }

        /*设置 Header 会覆盖 Header 包括基础参数*/
        public RHttp.Builder setHeader(Map<String, Object> header) {
            this.header = header;
            return this;
        }

        /*LifecycleProvider*/
        public RHttp.Builder lifecycle(LifecycleProvider lifecycle) {
            this.lifecycle = lifecycle;
            return this;
        }

        /*ActivityEvent*/
        public RHttp.Builder activityEvent(ActivityEvent activityEvent) {
            this.activityEvent = activityEvent;
            return this;
        }

        /*FragmentEvent*/
        public RHttp.Builder fragmentEvent(FragmentEvent fragmentEvent) {
            this.fragmentEvent = fragmentEvent;
            return this;
        }

        /*tag*/
        public RHttp.Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        /*文件集合*/
        public RHttp.Builder file(Map<String, File> file) {
            this.fileMap = file;
            return this;
        }

        public RHttp build() {
            return new RHttp(this);
        }
    }

}
