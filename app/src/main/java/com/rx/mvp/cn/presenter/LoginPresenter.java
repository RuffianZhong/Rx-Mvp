package com.rx.mvp.cn.presenter;

import com.google.gson.Gson;
import com.rx.mvp.cn.base.BasePresenter;
import com.rx.mvp.cn.http.Api.ApiUtils;
import com.rx.mvp.cn.http.exception.ApiException;
import com.rx.mvp.cn.http.observer.HttpRxObservable;
import com.rx.mvp.cn.http.observer.HttpRxObserver;
import com.rx.mvp.cn.http.retrofit.HttpRequest;
import com.rx.mvp.cn.model.bean.UserBean;
import com.rx.mvp.cn.utils.LogUtils;
import com.rx.mvp.cn.view.activity.LoginActivity;
import com.rx.mvp.cn.view.iface.ILoginView;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * 登录Presenter
 *
 * @author ZhongDaFeng
 */

public class LoginPresenter extends BasePresenter<ILoginView, LoginActivity> {

    private final String TAG = PhoneAddressPresenter.class.getSimpleName();

    public LoginPresenter(ILoginView view, LoginActivity activity) {
        super(view, activity);
    }

    /**
     * 登录
     *
     * @author ZhongDaFeng
     */
    public void login(String userName, String password) {

        //构建请求数据
        Map<String, Object> request = HttpRequest.getRequest();
        request.put("username", userName);
        request.put("password", password);

        HttpRxObserver httpRxObserver = new HttpRxObserver(TAG + "getInfo") {

            @Override
            protected void onStart(Disposable d) {
                if (getView() != null)
                    getView().showLoading();
            }

            @Override
            protected void onError(ApiException e) {
                LogUtils.w("onError code:" + e.getCode() + " msg:" + e.getMsg());
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showToast(e.getMsg());
                }
            }

            @Override
            protected void onSuccess(Object response) {
                LogUtils.w("onSuccess response:" + response.toString());
                UserBean bean = new Gson().fromJson(response.toString(), UserBean.class);
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showResult(bean);
                }
            }
        };

        /**
         * 切入后台移除RxJava监听
         * ActivityEvent.PAUSE(FragmentEvent.PAUSE)
         * 手动管理移除RxJava监听,如果不设置此参数默认自动管理移除RxJava监听（onCrete创建,onDestroy移除）
         */
        HttpRxObservable.getObservable(ApiUtils.getUserApi().login(request), getActivity(), ActivityEvent.PAUSE).subscribe(httpRxObserver);


        /**
         * ******此处代码为了测试取消请求,不是规范代码*****
         */
        /*try {
            Thread.sleep(50);
            //取消请求
            if (!httpRxObserver.isDisposed()) {
                httpRxObserver.cancel();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


    }

}
