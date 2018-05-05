package com.rx.mvp.cn.base;

import android.os.Bundle;

import com.rx.mvp.cn.listener.LifeCycleListener;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * BasePresenter
 *
 * @author ZhongDaFeng
 */

public class BasePresenter<V, T> implements LifeCycleListener {

    protected Reference<V> mViewRef;
    protected V mView;
    protected Reference<T> mActivityRef;
    protected T mActivity;


    public BasePresenter(V view, T activity) {
        attachView(view);
        attachActivity(activity);
        setListener(activity);
    }

    /**
     * 设置生命周期监听
     *
     * @author ZhongDaFeng
     */
    private void setListener(T activity) {
        if (getActivity() != null) {
            if (activity instanceof BaseActivity) {
                ((BaseActivity) getActivity()).setOnLifeCycleListener(this);
            } else if (activity instanceof BaseFragmentActivity) {
                ((BaseFragmentActivity) getActivity()).setOnLifeCycleListener(this);
            }
        }
    }

    /**
     * 关联
     *
     * @param view
     */
    private void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
        mView = mViewRef.get();
    }

    /**
     * 关联
     *
     * @param activity
     */
    private void attachActivity(T activity) {
        mActivityRef = new WeakReference<T>(activity);
        mActivity = mActivityRef.get();
    }

    /**
     * 销毁
     */
    private void detachView() {
        if (isViewAttached()) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    /**
     * 销毁
     */
    private void detachActivity() {
        if (isActivityAttached()) {
            mActivityRef.clear();
            mActivityRef = null;
        }
    }

    /**
     * 获取
     *
     * @return
     */
    public V getView() {
        if (mViewRef == null) {
            return null;
        }
        return mViewRef.get();
    }

    /**
     * 获取
     *
     * @return
     */
    public T getActivity() {
        if (mActivityRef == null) {
            return null;
        }
        return mActivityRef.get();
    }

    /**
     * 是否已经关联
     *
     * @return
     */
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     * 是否已经关联
     *
     * @return
     */
    public boolean isActivityAttached() {
        return mActivityRef != null && mActivityRef.get() != null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        detachView();
        detachActivity();
    }
}
