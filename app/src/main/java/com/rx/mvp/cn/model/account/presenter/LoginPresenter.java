package com.rx.mvp.cn.model.account.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.r.mvp.cn.MvpPresenter;
import com.rx.mvp.cn.core.net.http.RHttpCallback;
import com.rx.mvp.cn.model.GlobalConstants;
import com.rx.mvp.cn.model.account.biz.UserBiz;
import com.rx.mvp.cn.model.account.entity.UserBean;
import com.rx.mvp.cn.model.account.iface.ILoginView;
import com.rx.mvp.cn.utils.LogUtils;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * 登录Presenter
 * 备注:继承 MvpPresenter 指定 View 类型
 *
 * @author ZhongDaFeng
 */
public class LoginPresenter extends MvpPresenter<ILoginView> {

    private String ACTION_LOGIN = GlobalConstants.ACTION_LOGIN;
    private LifecycleProvider lifecycle;

    public LoginPresenter(LifecycleProvider activity) {
        lifecycle = activity;
    }

    /**
     * 登录
     *
     * @author ZhongDaFeng
     */
    public void login(String userName, String password) {


        if (isViewAttached())
            getView().mvpLoading(ACTION_LOGIN, true);

        RHttpCallback httpCallback = new RHttpCallback<UserBean>() {

            @Override
            public UserBean convert(JsonElement data) {
                return new Gson().fromJson(data, UserBean.class);
            }

            @Override
            public void onSuccess(UserBean object) {
                if (isViewAttached()) {
                    getView().mvpLoading(ACTION_LOGIN, false);
                    getView().mvpData(ACTION_LOGIN, object);
                }
            }

            @Override
            public void onError(int code, String desc) {
                if (isViewAttached()) {
                    getView().mvpLoading(ACTION_LOGIN, false);
                    getView().mvpError(ACTION_LOGIN, code, desc);
                }
            }

            @Override
            public void onCancel() {
                LogUtils.e("请求取消了");
                if (isViewAttached()) {
                    getView().mvpLoading(ACTION_LOGIN, false);
                }
            }
        };

        new UserBiz().login(userName, password, lifecycle, httpCallback);

    }

}
