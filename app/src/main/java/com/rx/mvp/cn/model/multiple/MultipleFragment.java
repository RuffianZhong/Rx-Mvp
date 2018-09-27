package com.rx.mvp.cn.model.multiple;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.r.mvp.cn.root.IMvpPresenter;
import com.ruffian.library.widget.RTextView;
import com.rx.mvp.cn.R;
import com.rx.mvp.cn.base.BaseFragment;
import com.rx.mvp.cn.model.GlobalConstants;
import com.rx.mvp.cn.model.account.entity.UserBean;
import com.rx.mvp.cn.model.account.iface.ILoginView;
import com.rx.mvp.cn.model.account.presenter.LoginPresenter;
import com.rx.mvp.cn.model.phone.entity.PhoneAddressBean;
import com.rx.mvp.cn.model.phone.iface.IPhoneAddressView;
import com.rx.mvp.cn.model.phone.presenter.PhoneAddressPresenter;
import com.rx.mvp.cn.utils.ToastUtils;
import com.rx.mvp.cn.widget.RLoadingDialog;

import butterknife.BindView;

/**
 * 演示一个页面调用多个接口
 * 备注:使用多个 Presenter / 实现多个 View 接口
 *
 * @author ZhongDaFeng
 */
public class MultipleFragment extends BaseFragment implements ILoginView, IPhoneAddressView {

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

    private LoginPresenter mLoginPresenter = new LoginPresenter(this);
    private PhoneAddressPresenter mPhonePst = new PhoneAddressPresenter(this);
    private RLoadingDialog mLoadingDialog;

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

    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(mContext, msg);
    }

    @Override
    public void mvpLoading(String action, boolean show) {
        /**
         * 区分action  只有action是登录业务时才需要loading  （根据开发者具体业务需求实现）
         */
        if (GlobalConstants.ACTION_LOGIN.equals(action)) {
            if (show) {
                mLoadingDialog.show();
            } else {
                mLoadingDialog.dismiss();
            }
        }
    }

    @Override
    public <M> void mvpData(String action, M data) {
        if (GlobalConstants.ACTION_LOGIN.equals(action)) {//登录返回数据
            UserBean bean = (UserBean) data;
            setupUserInfo(bean);
        } else if (GlobalConstants.ACTION_QUERY_PHONE.equals(action)) {//号码查询返回数据
            PhoneAddressBean bean = (PhoneAddressBean) data;
            setupPhoneInfo(bean);
        }
    }

    @Override
    public void mvpError(String action, int code, String msg) {
        /**
         * 具体业务具体分析，这里不需要根据action或者code做特殊处理，因此一并吐司提示
         */
        showToast(msg);
    }

    /**
     * 设置用户UI
     *
     * @param bean
     */
    private void setupUserInfo(UserBean bean) {
        if (bean == null) return;
        tvUid.setText(bean.getUid());
    }

    /**
     * 设置号码相关UI
     *
     * @param bean
     */
    private void setupPhoneInfo(PhoneAddressBean bean) {
        if (bean == null) return;
        tvPhone.setText(bean.getMobileNumber());
        tvCity.setText(bean.getCity());
        tvProvince.setText(bean.getProvince());
        tvOperator.setText(bean.getOperator());
    }

}
