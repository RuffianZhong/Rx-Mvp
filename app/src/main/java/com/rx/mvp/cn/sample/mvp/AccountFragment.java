package com.rx.mvp.cn.sample.mvp;

import android.support.annotation.NonNull;
import android.view.View;

import com.rx.mvp.cn.base.BaseFragment;
import com.rx.mvp.cn.model.account.entity.UserBean;
import com.rx.mvp.cn.utils.ToastUtils;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * XX业务 Fragment
 *
 * @author ZhongDaFeng
 */
public class AccountFragment extends BaseFragment<AccountContract.ILoginView, AccountPresenter> implements AccountContract.ILoginView {

    @Override
    protected View getContentView() {
        return null;
    }

    @Override
    protected void initBundleData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        //默认加载本地缓存数据
        getPresenter().getLocalCache("123456");

        new View(getActivity()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //账号密码登录
                getPresenter().login("acb", "123456");
            }
        });
    }

    /*---------mvp--------*/

    @Override
    public AccountPresenter createPresenter() {
        return new AccountPresenter();
    }

    @Override
    public void showResult(UserBean data) {
        //展示用户数据
    }

    @Override
    public void showError(int code, String msg) {
        //错误处理UI
        switch (code) {
            case 0:
                showToast(msg);
                break;
            case 1:
                //  new AlertDialog(getActivity()).show();
                break;
        }
    }

    @Override
    public LifecycleProvider getRxLifecycle() {
        return this;
    }

    @Override
    public void showToast(@NonNull String msg) {
        //吐司提示信息
        ToastUtils.showToast(getActivity(), msg);
    }

    @Override
    public void showProgressView() {
        //展示加载中相关的控件
        //showProgressDialog();
    }

    @Override
    public void dismissProgressView() {
        //关闭加载中相关的控件
        // dismissProgressDialog();
    }

}
