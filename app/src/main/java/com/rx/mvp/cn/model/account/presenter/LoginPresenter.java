package com.rx.mvp.cn.model.account.presenter;

import com.rx.mvp.cn.base.BasePresenter;
import com.rx.mvp.cn.core.net.http.observer.HttpRxCallback;
import com.rx.mvp.cn.model.account.activity.LoginActivity;
import com.rx.mvp.cn.model.account.biz.UserBiz;
import com.rx.mvp.cn.model.account.entity.UserBean;
import com.rx.mvp.cn.model.other.presenter.PhoneAddressPresenter;
import com.rx.mvp.cn.model.account.iface.ILoginView;

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

        if (getView() != null)
            getView().showLoading();


        HttpRxCallback httpRxCallback = new HttpRxCallback(TAG + "login") {
            @Override
            public void onSuccess(Object... object) {
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showResult((UserBean) object[0]);
                }
            }

            @Override
            public void onError(int code, String desc) {
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showToast(desc);
                }
            }
        };

        new UserBiz().login(userName, password, getActivity(), httpRxCallback);

        /**
         * ******此处代码为了测试取消请求,不是规范代码*****
         */
        /*try {
            Thread.sleep(50);
            //取消请求
            if (!httpRxCallback.isDisposed()) {
                httpRxCallback.cancel();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


    }

}
