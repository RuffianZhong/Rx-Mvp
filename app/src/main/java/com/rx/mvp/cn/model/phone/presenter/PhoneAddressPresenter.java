package com.rx.mvp.cn.model.phone.presenter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.r.mvp.cn.MvpPresenter;
import com.r.mvp.cn.MvpView;
import com.rx.mvp.cn.core.net.http.RHttpCallback;
import com.rx.mvp.cn.model.GlobalConstants;
import com.rx.mvp.cn.model.phone.biz.PhoneBiz;
import com.rx.mvp.cn.model.phone.entity.PhoneAddressBean;
import com.rx.mvp.cn.utils.LogUtils;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * 手机号归属地Presenter
 *
 * @author ZhongDaFeng
 */
public class PhoneAddressPresenter extends MvpPresenter<MvpView> {

    private String ACTION_QUERY_PHONE = GlobalConstants.ACTION_QUERY_PHONE;
    private LifecycleProvider lifecycle;

    public PhoneAddressPresenter(LifecycleProvider activity) {
        lifecycle = activity;
    }

    /**
     * 获取信息
     *
     * @author ZhongDaFeng
     */
    public void phoneQuery(String phone) {

        //loading
        if (isViewAttached())
            getView().mvpLoading(ACTION_QUERY_PHONE, true);

        RHttpCallback httpCallback = new RHttpCallback<PhoneAddressBean>() {
            @Override
            public PhoneAddressBean convert(JsonElement data) {
                return new Gson().fromJson(data, PhoneAddressBean.class);
            }

            @Override
            public void onSuccess(PhoneAddressBean object) {
                if (isViewAttached()) {
                    getView().mvpLoading(ACTION_QUERY_PHONE, false);
                    getView().mvpData(ACTION_QUERY_PHONE, object);
                }
            }

            @Override
            public void onError(int code, String desc) {
                if (isViewAttached()) {
                    getView().mvpLoading(ACTION_QUERY_PHONE, false);
                    getView().mvpError(ACTION_QUERY_PHONE, code, desc);
                }
            }

            @Override
            public void onCancel() {
                LogUtils.e("请求取消了");
                if (isViewAttached()) {
                    getView().mvpLoading(ACTION_QUERY_PHONE, false);
                }
            }
        };

        //请求
        new PhoneBiz().phoneQuery(phone, lifecycle, httpCallback);

    }


}
