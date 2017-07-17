package com.rx.mvp.cn.test;

import com.rx.mvp.cn.http.Api.ApiUtils;
import com.rx.mvp.cn.http.exception.ApiException;
import com.rx.mvp.cn.http.observer.HttpRxObservable;
import com.rx.mvp.cn.http.observer.HttpRxObserver;
import com.rx.mvp.cn.http.retrofit.HttpRequest;
import com.rx.mvp.cn.utils.LogUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Retrofit使用demo/测试类
 *
 * @author ZhongDaFeng
 */
public class RetrofitTest {
    public final String TAG = RetrofitTest.class.getSimpleName();//每个网络请求唯一TAG，用于取消网络请求使用
    /**
     * 模拟在activity中调用
     *
     * @author ZhongDaFeng
     */
    public void test(RxActivity activity, String account, String psw) {
        //设置唯一TAG
        HttpRxObserver httpRxObserver = new HttpRxObserver(TAG + "login") {
            @Override
            protected void onStart(Disposable d) {
                /**
                 * 开启loading等
                 */
            }
            @Override
            protected void onError(ApiException e) {
                /**
                 * 错误信息
                 */
                LogUtils.w("onError code:" + e.getCode() + " msg:" + e.getMsg());
            }
            @Override
            protected void onSuccess(Object response) {
                /**
                 * 成功处理
                 */
                LogUtils.w("onSuccess response:" + response.toString());
            }
        };

        new RetrofitTest().login(activity, account, psw).subscribe(httpRxObserver);

        //取消请求
        /*if(!httpRxObserver.isDisposed()){
            httpRxObserver.cancel();
        }*/

    }

    /**
     * 登录demo
     *
     * @author ZhongDaFeng
     */
    public Observable login(RxActivity activity, String phone, String psw) {
        //构建请求数据
        Map<String, Object> request = HttpRequest.getRequest();
        request.put("phone", phone);
        request.put("psw", psw);
        /**
         * 获取请求Observable
         * 1.RxActivity,RxFragment...所在页面继承RxLifecycle支持的组件
         * 2.ActivityEvent指定监听函数解绑的生命周期（手动管理,未设置则自动管理）
         * 以上两点作用防止RxJava监听没解除导致内存泄漏,ActivityEvent若未指定则按照activity/fragment的生命周期
         */
        // return HttpRxObservable.getObservable(ApiUtils.getPhoneApi().phoneQuery(request), activity);
        return HttpRxObservable.getObservable(ApiUtils.getUserApi().login(request), activity, ActivityEvent.PAUSE);
    }

}
