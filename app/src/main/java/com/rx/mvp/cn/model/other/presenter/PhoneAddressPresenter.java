package com.rx.mvp.cn.model.other.presenter;

import com.rx.mvp.cn.base.BasePresenter;
import com.rx.mvp.cn.core.net.http.observer.HttpRxCallback;
import com.rx.mvp.cn.model.other.activity.PhoneAddressActivity;
import com.rx.mvp.cn.model.other.biz.OtherBiz;
import com.rx.mvp.cn.model.other.entity.AddressBean;
import com.rx.mvp.cn.model.other.iface.IPhoneAddressView;

/**
 * 手机号归属地Presenter
 *
 * @author ZhongDaFeng
 */

public class PhoneAddressPresenter extends BasePresenter<IPhoneAddressView, PhoneAddressActivity> {

    private final String TAG = PhoneAddressPresenter.class.getSimpleName();

    public PhoneAddressPresenter(IPhoneAddressView view, PhoneAddressActivity activity) {
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

        //请求
        new OtherBiz().phoneQuery(phone, getActivity(), new HttpRxCallback() {
            @Override
            public void onSuccess(Object... object) {
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showResult((AddressBean) object[0]);
                }
            }

            @Override
            public void onError(int code, String desc) {
                if (getView() != null) {
                    getView().closeLoading();
                    getView().showToast(desc);
                }
            }
        });


    }


}
