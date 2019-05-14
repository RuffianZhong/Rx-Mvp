package com.rx.mvp.cn.sample.mvp;

import com.r.mvp.cn.MvpPresenter;
import com.r.mvp.cn.model.ModelCallback;
import com.r.mvp.cn.model.ModelFactory;
import com.rx.mvp.cn.model.account.entity.UserBean;

/**
 * XX业务的Presenter
 *
 * @author ZhongDaFeng
 */
public class AccountPresenter extends MvpPresenter<AccountContract.ILoginView> {

    /**
     * 登录
     */
    void login(String userName, String password) {

        //1.展示loading框
        getView().showProgressView();

        //调用model中login方法
        ModelFactory.getModel(AccountModel.class).login(userName, password, getView().getRxLifecycle(), new ModelCallback.Http<UserBean>() {
            @Override
            public void onSuccess(UserBean object) {
                //model给回的数据

                //2.关闭loading框
                getView().dismissProgressView();

                //展示用户数据
                getView().showResult(object);

            }

            @Override
            public void onError(int code, String desc) {
                //model给回的数据

                //2.关闭loading框
                getView().dismissProgressView();

                //展示错误信息
                getView().showError(code, desc);

            }

            @Override
            public void onCancel() {
                //2.关闭loading框
                getView().dismissProgressView();
            }
        });
    }

    /**
     * 获取本地缓存数据
     */
    public void getLocalCache(String account) {

        //调用model中getLocalCache方法
        ModelFactory.getModel(AccountModel.class).getLocalCache(account, getView().getRxLifecycle(), new ModelCallback.Data<UserBean>() {
            @Override
            public void onSuccess(UserBean object) {
                //model给回的数据

                //展示用户数据
                getView().showResult(object);
            }
        });

    }

    @Override
    public void destroy() {

    }
}
