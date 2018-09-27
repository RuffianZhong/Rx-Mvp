package com.rx.mvp.cn.model.account.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.r.mvp.cn.root.IMvpPresenter;
import com.rx.mvp.cn.R;
import com.rx.mvp.cn.base.BaseFragment;
import com.rx.mvp.cn.model.account.entity.UserBean;
import com.rx.mvp.cn.model.account.iface.ILoginView;
import com.rx.mvp.cn.model.account.presenter.LoginPresenter;
import com.rx.mvp.cn.utils.ToastUtils;
import com.rx.mvp.cn.widget.RLoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录Fragment
 *
 * @author ZhongDaFeng
 */
public class LoginFragment extends BaseFragment implements ILoginView {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;

    private LoginPresenter mLoginPresenter = new LoginPresenter(this);

    private RLoadingDialog mLoadingDialog;

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
                mLoginPresenter.login(userName, password);
                break;
        }
    }

    @Override
    protected IMvpPresenter[] getPresenterArray() {
        return new IMvpPresenter[]{mLoginPresenter};
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(mContext, msg);
    }

    @Override
    public void mvpLoading(String action, boolean show) {
        if (show) {
            mLoadingDialog.show();
        } else {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public <M> void mvpData(String action, M data) {
        if (data == null) return;
        UserBean bean = (UserBean) data;
        showToast(bean.getUid());
    }

    @Override
    public void mvpError(String action, int code, String msg) {
        showToast(msg);
    }
}
