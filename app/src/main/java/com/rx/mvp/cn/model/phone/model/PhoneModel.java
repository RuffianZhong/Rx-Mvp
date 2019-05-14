package com.rx.mvp.cn.model.phone.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.r.mvp.cn.model.ModelCallback;
import com.rx.mvp.cn.base.BizFactory;
import com.rx.mvp.cn.core.net.http.RHttpCallback;
import com.rx.mvp.cn.model.phone.biz.PhoneBiz;
import com.rx.mvp.cn.model.phone.contract.PhoneContract;
import com.rx.mvp.cn.model.phone.entity.PhoneAddressBean;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * PhoneModel
 *
 * @author ZhongDaFeng
 */
public class PhoneModel implements PhoneContract.PhoneModel {

    @Override
    public void phoneQuery(String phone, LifecycleProvider lifecycle, final ModelCallback.Http<PhoneAddressBean> modelCallback) {
        //Biz发起网络请求
        BizFactory.getBiz(PhoneBiz.class).phoneQuery(phone, lifecycle, new RHttpCallback<PhoneAddressBean>() {
            @Override
            public PhoneAddressBean convert(JsonElement data) {
                return new Gson().fromJson(data, PhoneAddressBean.class);
            }

            @Override
            public void onSuccess(PhoneAddressBean object) {
                modelCallback.onSuccess(object);
            }

            @Override
            public void onError(int code, String desc) {
                modelCallback.onError(code, desc);
            }

            @Override
            public void onCancel() {
                modelCallback.onCancel();
            }
        });
    }
}
