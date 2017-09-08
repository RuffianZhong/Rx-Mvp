package com.rx.mvp.cn.view.iface;

import com.rx.mvp.cn.base.IBaseView;
import com.rx.mvp.cn.model.bean.AddressBean;

/**
 * 手机归属地页面view接口
 *
 * @author ZhongDaFeng
 */

public interface IPhoneAddressView extends IBaseView {

    //显示结果
    void showResult(AddressBean bean);

}
