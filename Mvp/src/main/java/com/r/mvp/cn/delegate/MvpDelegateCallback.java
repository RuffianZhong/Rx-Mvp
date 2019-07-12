package com.r.mvp.cn.delegate;

import com.r.mvp.cn.root.IMvpPresenter;
import com.r.mvp.cn.root.IMvpView;

/**
 * V/P 媒介
 *
 * @param <V>
 * @param <P>
 */
public interface MvpDelegateCallback<V extends IMvpView, P extends IMvpPresenter<V>> {

    /**
     * Creates the presenter instance
     *
     * @return the created presenter instance
     */
    P createPresenter();

    /**
     * Gets the presenter. If null is returned, then a internally a new presenter instance gets
     * created by calling {@link #createPresenter()}
     *
     * @return the presenter instance. can be null.
     */
    P getPresenter();

    /**
     * Sets the presenter instance
     *
     * @param presenter The presenter instance
     */
    void setPresenter(P presenter);

    /**
     * Gets the MvpView for the presenter
     *
     * @return The view associated with the presenter
     */
    V getMvpView();

}

