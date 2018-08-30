package com.rx.mvp.cn.model.other.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.rx.mvp.cn.base.BasePresenter;
import com.rx.mvp.cn.core.net.http.RHttpCallback;
import com.rx.mvp.cn.model.other.biz.OtherBiz;
import com.rx.mvp.cn.model.other.entity.AddressBean;
import com.rx.mvp.cn.model.other.iface.IPhoneAddressView;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * 手机号归属地Presenter
 *
 * @author ZhongDaFeng
 */

public class PhoneAddressPresenter extends BasePresenter<IPhoneAddressView, LifecycleProvider> {

    private final String TAG = PhoneAddressPresenter.class.getSimpleName();

    public PhoneAddressPresenter(IPhoneAddressView view, LifecycleProvider activity) {
        super(view, activity);
    }


    /**
     * 获取信息
     *
     * @author ZhongDaFeng
     */
    public void phoneQuery(String phone) {

        //loading
        if (getView() != null)
            getView().showLoading();

        RHttpCallback httpCallback = new RHttpCallback<AddressBean>() {
            @Override
            public AddressBean convert(JsonElement data) {
                return new Gson().fromJson(data, AddressBean.class);
            }

            @Override
            public void onSuccess(AddressBean value) {
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showResult(value);
                }
            }

            @Override
            public void onError(int code, String desc) {
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showToast(desc);
                }
            }

            @Override
            public void onCancel() {
                if (getView() != null) {
                    getView().closeLoading();
                }
            }
        };

        //请求
        new OtherBiz().phoneQuery(phone, getActivity(), httpCallback);


    }


}
