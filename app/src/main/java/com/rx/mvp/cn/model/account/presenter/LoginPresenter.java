package com.rx.mvp.cn.model.account.presenter;

import android.util.Log;

import com.r.mvp.cn.MvpPresenter;
import com.r.mvp.cn.model.ModelCallback;
import com.r.mvp.cn.model.ModelFactory;
import com.rx.mvp.cn.model.account.contract.AccountContract;
import com.rx.mvp.cn.model.account.entity.UserBean;
import com.rx.mvp.cn.model.account.model.AccountModel;

/**
 * 登录Presenter
 * 备注:继承 MvpPresenter 指定 View 类型
 *
 * @author ZhongDaFeng
 */
public class LoginPresenter extends MvpPresenter<AccountContract.ILoginView> {

    /**
     * 登录
     */
    public void login(String userName, String password) {

        Log.e("tag", "============" + (getView() == null));
        //显示loading框
        getView().showProgressView();

        //调用model获取网络数据
        ModelFactory.getModel(AccountModel.class).login(getView().getActivity(), userName, password, getView().getRxLifecycle(), new ModelCallback.Http<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                //model数据回传

                //关闭弹窗
                getView().dismissProgressView();

                StringBuffer sb = new StringBuffer();
                sb.append("登录成功")
                        .append("\n")
                        .append("用户ID:")
                        .append(data.getUid())
                        .append("\n")
                        .append("Token:")
                        .append("\n")
                        .append(data.getToken())
                        .append("\n")
                        .append("最后登录:")
                        .append("\n")
                        .append(data.getTime());

                //用户信息展示
                getView().showResult(sb.toString());

            }

            @Override
            public void onError(int code, String desc) {
                //model数据回传

                //关闭弹窗
                getView().dismissProgressView();

                //错误信息提示
                getView().showError(code, desc);
            }

            @Override
            public void onCancel() {
                //关闭弹窗
                getView().dismissProgressView();
            }
        });

    }

    /**
     * 获取本地缓存数据
     */
    public void getLocalCache() {

        //调用model获取本地数据
        ModelFactory.getModel(AccountModel.class).getLocalCache(getView().getActivity(), getView().getRxLifecycle(), new ModelCallback.Data<String>() {
            @Override
            public void onSuccess(String object) {
                //model数据回传

                //关闭弹窗
                getView().dismissProgressView();

                //用户信息展示
                getView().showResult(object);

            }
        });
    }

    @Override
    public void destroy() {

    }
}
