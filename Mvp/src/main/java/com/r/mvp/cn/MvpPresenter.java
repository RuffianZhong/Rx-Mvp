
package com.r.mvp.cn;

import android.support.annotation.UiThread;

import com.r.mvp.cn.proxy.MvpViewProxy;
import com.r.mvp.cn.root.IMvpPresenter;
import com.r.mvp.cn.root.IMvpView;

/**
 * Presenter基础实现
 *
 * @param <V>
 */
public abstract class MvpPresenter<V extends IMvpView> implements IMvpPresenter<V> {

    protected V mView;

    //View代理对象
    protected MvpViewProxy<V> mMvpViewProxy;

    /**
     * 获取view
     *
     * @return
     */
    @UiThread
    public V getView() {
        return mView;
    }

    /**
     * 判断View是否已经添加
     *
     * @return
     */
    @UiThread
    public boolean isViewAttached() {
        return mView != null;
    }

    /**
     * 绑定View
     *
     * @param view
     */
    @UiThread
    @Override
    public void attachView(V view) {
        mMvpViewProxy = new MvpViewProxy<V>();
        mView = (V) mMvpViewProxy.newProxyInstance(view);
    }

    /**
     * 移除View
     */
    @Override
    public void detachView() {
        if (mMvpViewProxy != null) {
            mMvpViewProxy.detachView();
        }
    }

}
