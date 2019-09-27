package com.r.http.cn.retrofit;

import com.r.http.cn.utils.LogUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit工具类
 * 获取Retrofit 默认使用OkHttpClient
 *
 * @author ZhongDaFeng
 */
public class RetrofitUtils {

    private static RetrofitUtils instance = null;
    private static Retrofit.Builder retrofit;

    private RetrofitUtils() {
        retrofit = new Retrofit.Builder();
    }

    public static RetrofitUtils get() {
        if (instance == null) {
            synchronized (RetrofitUtils.class) {
                if (instance == null) {
                    instance = new RetrofitUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 获取Retrofit
     *
     * @param baseUrl 基础URL
     * @return
     */
    public Retrofit getRetrofit(String baseUrl, long timeout, TimeUnit timeUnit) {
        // Retrofit.Builder retrofit = new Retrofit.Builder();
        retrofit
                .client(getOkHttpClientBase(timeout, timeUnit))
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return retrofit.build();
    }

    /**
     * 获取Retrofit
     *
     * @param baseUrl
     * @param client
     * @return
     */
    public Retrofit getRetrofit(String baseUrl, OkHttpClient client) {
        // Retrofit.Builder retrofit = new Retrofit.Builder();
        retrofit
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return retrofit.build();
    }


    /**
     * 获取OkHttpClient
     * 备注:下载时不能使用OkHttpClient单例,在拦截器中处理进度会导致多任务下载混乱
     *
     * @param timeout
     * @param interceptorArray
     * @return
     */
    public OkHttpClient getOkHttpClient(long timeout, TimeUnit timeUnit, Interceptor... interceptorArray) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        //超时设置
        okHttpClient.connectTimeout(timeout, timeUnit)
                .writeTimeout(timeout, timeUnit)
                .readTimeout(timeout, timeUnit);

        /**
         * https设置
         * 备注:信任所有证书,不安全有风险
         */
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        okHttpClient.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        /**
         * 配置https的域名匹配规则，不需要就不要加入，使用不当会导致https握手失败
         * 备注:google平台不允许直接返回true
         */
        //okHttpClient.hostnameVerifier(new HostnameVerifier() {        });

        //Interceptor设置
        if (interceptorArray != null) {
            for (Interceptor interceptor : interceptorArray) {
                okHttpClient.addInterceptor(interceptor);
            }
        }
        return okHttpClient.build();
    }


    /**
     * 获取下载时使用 OkHttpClient
     *
     * @param interceptorArray
     * @return
     */
    public OkHttpClient getOkHttpClientDownload(Interceptor... interceptorArray) {
        final long timeout = 60;//超时时长
        final TimeUnit timeUnit = TimeUnit.SECONDS;//单位秒
        return getOkHttpClient(timeout, timeUnit, interceptorArray);
    }

    /**
     * 获取基础Http请求使用 OkHttpClient
     *
     * @param timeout  超时时间
     * @param timeUnit 超时时间单位
     * @return
     */
    public OkHttpClient getOkHttpClientBase(long timeout, TimeUnit timeUnit) {
        //日志拦截器
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtils.e("okHttp:" + message);
            }
        });
        //must
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        //网络请求拦截器
        Interceptor httpInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response;
                try {
                    response = chain.proceed(request);
                } catch (final Exception e) {
                    throw e;
                }
                return response;
            }
        };

        Interceptor[] interceptorArray = new Interceptor[]{logInterceptor, httpInterceptor};
        return getOkHttpClient(timeout, timeUnit, interceptorArray);
    }

}
