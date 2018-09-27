package com.rx.mvp.cn.model.account.iface;

import com.r.mvp.cn.MvpView;

/**
 * 登录view
 * 备注: MvpView 未能满足需求时新增方法
 *
 * @author ZhongDaFeng
 */
public interface ILoginView extends MvpView {

    /**
     * 额外方法显示吐司
     *
     * @param msg
     */
    void showToast(String msg);

}
