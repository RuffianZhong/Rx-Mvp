package com.rx.mvp.cn.model.phone.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.r.mvp.cn.MvpView;
import com.r.mvp.cn.root.IMvpPresenter;
import com.rx.mvp.cn.R;
import com.rx.mvp.cn.base.BaseActivity;
import com.rx.mvp.cn.model.phone.entity.PhoneAddressBean;
import com.rx.mvp.cn.model.phone.presenter.PhoneAddressPresenter;
import com.rx.mvp.cn.utils.ToastUtils;
import com.rx.mvp.cn.widget.RLoadingDialog;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 手机号码归属地查询页面
 *
 * @author ZhongDaFeng
 */
public class PhoneAddressActivity extends BaseActivity implements MvpView {


    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_operator)
    TextView tvOperator;

    private PhoneAddressPresenter mPhonePst = new PhoneAddressPresenter(this);
    private RLoadingDialog mLoadingDialog;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_phone_address;
    }

    @Override
    protected void initBundleData() {

    }

    @Override
    protected void initView() {
        mLoadingDialog = new RLoadingDialog(this, true);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                attemptSubmit();
                break;
        }
    }

    /**
     * 尝试提交
     */
    private void attemptSubmit() {
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(mContext, getString(R.string.hint_phone), Toast.LENGTH_SHORT).show();
            return;
        }

        mPhonePst.phoneQuery(phone);

    }


    @Override
    protected IMvpPresenter[] getPresenterArray() {
        return new IMvpPresenter[]{mPhonePst};
    }

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
        PhoneAddressBean bean = (PhoneAddressBean) data;

        tvPhone.setText(bean.getMobileNumber());
        tvCity.setText(bean.getCity());
        tvProvince.setText(bean.getProvince());
        tvOperator.setText(bean.getOperator());
    }

    @Override
    public void mvpError(String action, int code, String msg) {
        showToast(msg);
    }

}
