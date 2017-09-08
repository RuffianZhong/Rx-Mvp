package com.rx.mvp.cn.base;

/**
 * IBaseView
 *
 * @author ZhongDaFeng
 */

public interface IBaseView {

    //显示loading
    void showLoading();

    //关闭loading
    void closeLoading();

    //显示吐司
    void showToast(String msg);

}
