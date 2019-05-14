package com.rx.mvp.cn.model.multiple;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.r.mvp.cn.root.IMvpPresenter;
import com.ruffian.library.widget.RTextView;
import com.rx.mvp.cn.R;
import com.rx.mvp.cn.base.BaseFragment;
import com.rx.mvp.cn.model.account.contract.AccountContract;
import com.rx.mvp.cn.model.account.presenter.LoginPresenter;
import com.rx.mvp.cn.model.phone.contract.PhoneContract;
import com.rx.mvp.cn.model.phone.entity.PhoneAddressBean;
import com.rx.mvp.cn.model.phone.presenter.PhoneAddressPresenter;
import com.rx.mvp.cn.widget.RLoadingDialog;
import com.trello.rxlifecycle2.LifecycleProvider;

import butterknife.BindView;

/**
 * 演示一个页面调用多个接口
 * 备注:使用多个 Presenter / 实现多个 View 接口
 *
 * @author ZhongDaFeng
 */
public class MultipleFragment extends BaseFragment implements AccountContract.ILoginView, PhoneContract.IPhoneView {

    @BindView(R.id.tv_uid)
    RTextView tvUid;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_operator)
    TextView tvOperator;

    private LoginPresenter mLoginPresenter = new LoginPresenter();
    private PhoneAddressPresenter mPhonePst = new PhoneAddressPresenter();

    //手机号码
    private String mPhoneNumber = "1351073";
    //账号密码
    private String mUserName = "ruffian";
    private String mPsw = "EA8A706C4C34A168";

    @Override
    protected View getContentView() {
        return LayoutInflater.from(mContext).inflate(R.layout.multiple_fragment, null);
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
        //登录
        mLoginPresenter.login(mUserName, mPsw);
        //查询
        mPhonePst.phoneQuery(mPhoneNumber);
    }

    @Override
    protected IMvpPresenter[] getPresenterArray() {
        return new IMvpPresenter[]{mLoginPresenter, mPhonePst};
    }

    /**
     * 用户信息展示
     */
    @Override
    public void showResult(String data) {
        tvUid.setText(data);
    }

    @Override
    public void showError(int code, String msg) {
        showToast(msg);
    }

    /**
     * 号码查询结果展示
     */
    @Override
    public void showPhoneResult(PhoneAddressBean bean) {
        if (bean == null) return;
        tvPhone.setText(bean.getMobileNumber());
        tvCity.setText(bean.getCity());
        tvProvince.setText(bean.getProvince());
        tvOperator.setText(bean.getOperator());
    }

    @Override
    public LifecycleProvider getRxLifecycle() {
        return this;
    }
}
