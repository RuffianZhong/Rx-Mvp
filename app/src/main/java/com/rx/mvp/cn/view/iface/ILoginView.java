package com.rx.mvp.cn.view.iface;

import com.rx.mvp.cn.base.IBaseView;
import com.rx.mvp.cn.model.bean.UserBean;

/**
 * 登录view
 *
 * @author ZhongDaFeng
 */

public interface ILoginView extends IBaseView {

    //显示结果
    void showResult(UserBean bean);

}
