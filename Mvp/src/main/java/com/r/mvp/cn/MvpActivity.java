package com.r.mvp.cn;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.r.mvp.cn.delegate.ActivityMvpDelegate;
import com.r.mvp.cn.delegate.ActivityMvpDelegateImpl;
import com.r.mvp.cn.delegate.MvpDelegateCallback;
import com.r.mvp.cn.root.IMvpPresenter;
import com.r.mvp.cn.root.IMvpView;
import com.trello.rxlifecycle2.components.RxActivity;


/**
 * MVPActivity
 * 备注:
 * 1.XXActivity 继承 MvpActivity,当页面存在 Presenter 时，具体 Activity 需要调用 setPresenter(P... presenter)
 * 2.由于此框架集合了 RxLifecycle 因此本 Activity 继承自 RxActivity (开发者也可以直接继承 Activity)
 * 3.支持一个 Activity 存在多个 Presenter
 *
 * @param <V>
 * @param <P>
 */
public abstract class MvpActivity<V extends IMvpView, P extends IMvpPresenter<V>> extends RxActivity implements IMvpView, MvpDelegateCallback<V, P> {

    protected ActivityMvpDelegate mvpDelegate;

    /**
     * 获取 Presenter 数组
     */
    protected abstract P[] getPresenterArray();

    @Override
    public P[] getPresenter() {
        return getPresenterArray();
    }

    @Override
    public V[] getMvpView() {
        V[] view = null;
        P[] pArray = getPresenter();
        if (pArray != null) {
            view = (V[]) new IMvpView[pArray.length];
            for (int i = 0; i < pArray.length; i++) {
                view[i] = (V) this;
            }
        }
        return view;
    }

    @NonNull
    protected ActivityMvpDelegate<V, P> getMvpDelegate() {
        if (mvpDelegate == null) {
            mvpDelegate = new ActivityMvpDelegateImpl(this, this);
        }
        return mvpDelegate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMvpDelegate().onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMvpDelegate().onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getMvpDelegate().onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getMvpDelegate().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMvpDelegate().onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getMvpDelegate().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getMvpDelegate().onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMvpDelegate().onRestart();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        getMvpDelegate().onContentChanged();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getMvpDelegate().onPostCreate(savedInstanceState);
    }

}
