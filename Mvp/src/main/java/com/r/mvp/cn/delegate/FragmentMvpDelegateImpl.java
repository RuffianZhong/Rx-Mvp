package com.r.mvp.cn.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.r.mvp.cn.root.IMvpPresenter;
import com.r.mvp.cn.root.IMvpView;

/**
 * Fragment 媒介
 * 备注:主要是连接 Fragment 的生命周期与 Presenter 实现特定生命周期绑定与解除 V
 *
 * @author ZhongDaFeng
 * {@link # https://github.com/RuffianZhong/Rx-Mvp}
 */
public class FragmentMvpDelegateImpl<V extends IMvpView, P extends IMvpPresenter<V>> implements FragmentMvpDelegate {

    /**
     * Fragment
     */
    protected Fragment fragment;

    /**
     * V & P
     */
    private MvpDelegateCallback<V, P> delegateCallback;

    public FragmentMvpDelegateImpl(Fragment fragment, MvpDelegateCallback<V, P> delegateCallback) {
        if (fragment == null) {
            throw new NullPointerException("Fragment is null!");
        }
        if (delegateCallback == null) {
            throw new NullPointerException("MvpDelegateCallback is null!");
        }
        this.fragment = fragment;
        this.delegateCallback = delegateCallback;
    }

    /**
     * 是否保留V&P实例
     *
     * @return
     */
    private static boolean retainVPInstance(Activity activity, Fragment fragment) {
        if (activity.isChangingConfigurations()) {
            return false;
        }
        if (activity.isFinishing()) {
            return false;
        }
        return !fragment.isRemoving();
    }


    /**
     * 获取Activity
     *
     * @return
     */
    private Activity getActivity() {
        Activity activity = fragment.getActivity();
        if (activity == null) {
            throw new NullPointerException("Activity returned by Fragment.getActivity() is null. Fragment is " + fragment);
        }
        return activity;
    }

    @Override
    public void onCreate(Bundle saved) {
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
            Activity activity = getActivity();
            P presenter = delegateCallback.getPresenter();
            if (presenter != null) {
                //解除View
                presenter.detachView();
                if (!retainVPInstance(activity, fragment)) {
                    //销毁 V & P 实例
                    presenter.destroy();
                }
            }
        } catch (NullPointerException e) {
            // e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onDestroyView() {
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
    public void onActivityCreated(Bundle savedInstanceState) {

    }

    @Override
    public void onAttach(Activity activity) {

    }

    @Override
    public void onDetach() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}
