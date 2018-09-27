package com.r.mvp.cn.root;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

/**
 * MVP  根Presenter
 *
 * @author ZhongDaFeng
 */
public interface IMvpPresenter<V extends IMvpView> {

    /**
     * 将 View 添加到当前 Presenter
     */
    @UiThread
    void attachView(@NonNull V view);

    /**
     * 将 View 从 Presenter 移除
     */
    @UiThread
    void detachView();

    /**
     * 销毁 V 实例
     */
    @UiThread
    void destroy();

}
