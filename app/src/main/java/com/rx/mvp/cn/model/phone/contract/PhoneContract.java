package com.rx.mvp.cn.model.phone.contract;

import android.support.annotation.UiThread;

import com.r.mvp.cn.MvpView;
import com.r.mvp.cn.model.ModelCallback;
import com.r.mvp.cn.root.IMvpModel;
import com.rx.mvp.cn.model.phone.entity.PhoneAddressBean;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * Phone业务的Contract(协议)
 * 目的：避免mvp架构 view/model 文件过多
 * 综合管理某业务的 view/model 接口
 *
 * @author ZhongDaFeng
 */
public interface PhoneContract {

    /*号码查询模块View接口*/
    interface IPhoneView extends MvpView {

        /**
         * 展示号码结果
         */
        @UiThread
        void showPhoneResult(PhoneAddressBean bean);
    }

    /*Phone模块model接口.此处根据具体项目决定是否需要此接口层*/
    interface PhoneModel extends IMvpModel {
        /**
         * 查询号码
         */
        void phoneQuery(String phone, LifecycleProvider lifecycle, ModelCallback.Http<PhoneAddressBean> modelCallback);
    }

}
