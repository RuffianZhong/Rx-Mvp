package com.r.mvp.cn.delegate;

import android.app.Activity;
import android.os.Bundle;

import com.r.mvp.cn.root.IMvpPresenter;
import com.r.mvp.cn.root.IMvpView;

/**
 * Activity媒介
 * 备注:主要是连接 Activity 的生命周期与 Presenter 实现特定生命周期绑定与解除 V
 *
 * @author ZhongDaFeng
 * {@link # https://github.com/RuffianZhong/Rx-Mvp}
 */
public class ActivityMvpDelegateImpl<V extends IMvpView, P extends IMvpPresenter<V>> implements ActivityMvpDelegate {

    /**
     * Activity
     */
    protected Activity activity;

    /**
     * V & P
     */
    private MvpDelegateCallback<V, P> delegateCallback;

    public ActivityMvpDelegateImpl(Activity activity, MvpDelegateCallback<V, P> delegateCallback) {
        if (activity == null) {
            throw new NullPointerException("Activity is null!");
        }
        if (delegateCallback == null) {
            throw new NullPointerException("MvpDelegateCallback is null!");
        }
        this.activity = activity;
        this.delegateCallback = delegateCallback;
    }

    /**
     * 是否保留V&P实例
     *
     * @return
     */
    private static boolean retainVPInstance(Activity activity) {
        return activity.isChangingConfigurations() || !activity.isFinishing();
    }

    @Override
    public void onCreate(Bundle bundle) {

        /**
         * 调用创建 Presenter 函数
         */
        P presenter = delegateCallback.createPresenter();

        /**
         * 设置 Presenter
         */
        delegateCallback.setPresenter(presenter);

        /**
         * 获取 View
         */
        V view = delegateCallback.getMvpView();

        if (presenter != null && view != null) {
            //关联view
            presenter.attachView(view);
        }

    }


    @Override
    public void onDestroy() {
        /**
         * 移除绑定
         */
        try {
            P presenter = delegateCallback.getPresenter();
            if (presenter != null) {
                //解除View
                presenter.detachView();
                if (!retainVPInstance(activity)) {
                    //销毁 V & P 实例
                    presenter.destroy();
                }
            }
        } catch (NullPointerException e) {
            // e.printStackTrace();
        }
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onContentChanged() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {

    }
}
