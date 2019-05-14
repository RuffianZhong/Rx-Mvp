package com.rx.mvp.cn.model.phone.presenter;

import com.r.mvp.cn.MvpPresenter;
import com.r.mvp.cn.model.ModelCallback;
import com.r.mvp.cn.model.ModelFactory;
import com.rx.mvp.cn.model.phone.contract.PhoneContract;
import com.rx.mvp.cn.model.phone.entity.PhoneAddressBean;
import com.rx.mvp.cn.model.phone.model.PhoneModel;

/**
 * 手机号归属地Presenter
 *
 * @author ZhongDaFeng
 */
public class PhoneAddressPresenter extends MvpPresenter<PhoneContract.IPhoneView> {


    /**
     * 获取信息
     *
     * @author ZhongDaFeng
     */
    public void phoneQuery(String phone) {

        getView().showProgressView();
        ModelFactory.getModel(PhoneModel.class).phoneQuery(phone, getView().getRxLifecycle(), new ModelCallback.Http<PhoneAddressBean>() {
            @Override
            public void onSuccess(PhoneAddressBean object) {
                getView().dismissProgressView();
                getView().showPhoneResult(object);
            }

            @Override
            public void onError(int code, String desc) {
                getView().dismissProgressView();
                getView().showToast(desc);
            }

            @Override
            public void onCancel() {
                getView().dismissProgressView();
            }
        });

    }


    @Override
    public void destroy() {

    }
}
