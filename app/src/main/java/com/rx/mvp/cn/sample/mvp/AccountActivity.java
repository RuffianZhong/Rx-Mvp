package com.rx.mvp.cn.sample.mvp;

import android.support.annotation.NonNull;
import android.view.View;

import com.r.mvp.cn.root.IMvpPresenter;
import com.rx.mvp.cn.base.BaseActivity;
import com.rx.mvp.cn.model.account.entity.UserBean;
import com.rx.mvp.cn.utils.ToastUtils;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * XX业务 Activity
 *
 * @author ZhongDaFeng
 */
public class AccountActivity extends BaseActivity implements AccountContract.ILoginView {

    private AccountPresenter accountPresenter = new AccountPresenter();

    @Override
    protected int getContentViewId() {
        return 0;
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
        accountPresenter.getLocalCache("123456");

        new View(this).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //账号密码登录
                accountPresenter.login("acb", "123456");
            }
        });
    }


    /*---------mvp--------*/

    /**
     * 需要使用Presenter时 覆盖重写
     */
    @Override
    protected IMvpPresenter[] getPresenterArray() {
        return new IMvpPresenter[]{accountPresenter};
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
                // new AlertDialog(this).show();
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
        ToastUtils.showToast(this, msg);
    }

    @Override
    public void showProgressView() {
        //展示加载中相关的控件
        //showProgressDialog();
    }

    @Override
    public void dismissProgressView() {
        //关闭加载中相关的控件
        //dismissProgressDialog();
    }

}
