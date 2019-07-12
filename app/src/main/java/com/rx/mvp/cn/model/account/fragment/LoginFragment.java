package com.rx.mvp.cn.model.account.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.r.mvp.cn.root.IMvpPresenter;
import com.rx.mvp.cn.R;
import com.rx.mvp.cn.base.BaseFragment;
import com.rx.mvp.cn.model.account.contract.AccountContract;
import com.rx.mvp.cn.model.account.presenter.LoginPresenter;
import com.rx.mvp.cn.utils.ToastUtils;
import com.rx.mvp.cn.widget.RLoadingDialog;
import com.trello.rxlifecycle2.LifecycleProvider;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录Fragment
 *
 * @author ZhongDaFeng
 */
public class LoginFragment extends BaseFragment<AccountContract.ILoginView, LoginPresenter> implements AccountContract.ILoginView {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_result)
    TextView tvResult;


    @Override
    protected View getContentView() {
        return LayoutInflater.from(mContext).inflate(R.layout.activity_login, null);
    }

    @Override
    protected void initBundleData() {

    }

    @Override
    protected void initView() {
        mLoadingDialog = new RLoadingDialog(mContext, true);
    }

    @Override
    protected void initData() {
        //获取缓存数据
        getPresenter().getLocalCache();
    }

    @OnClick({R.id.login})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    return;
                }
                getPresenter().login(userName, password);
                break;
        }
    }

    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public LifecycleProvider getRxLifecycle() {
        return this;
    }

    @Override
    public void showResult(String data) {
        tvResult.setText(data);
    }

    @Override
    public void showError(int code, String msg) {
        showToast(msg);
    }
}
