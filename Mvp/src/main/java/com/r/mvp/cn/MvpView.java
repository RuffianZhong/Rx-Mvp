
package com.r.mvp.cn;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.r.mvp.cn.root.IMvpView;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * 基础View接口
 */
public interface MvpView extends IMvpView {

    /**
     * RxLifecycle用于绑定组件生命周期
     *
     * @return
     */
    LifecycleProvider getRxLifecycle();

    /**
     * 获取Activity实例
     *
     * @return
     */
    Activity getActivity();

    /**
     * 展示吐司
     *
     * @param msg 吐司文本
     */
    @UiThread
    void showToast(@NonNull String msg);

    /**
     * 显示进度View
     */
    @UiThread
    void showProgressView();

    /**
     * 隐藏进度View
     */
    @UiThread
    void dismissProgressView();

}
