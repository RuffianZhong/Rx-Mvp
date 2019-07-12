package com.r.mvp.cn;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.r.mvp.cn.delegate.ActivityMvpDelegate;
import com.r.mvp.cn.delegate.ActivityMvpDelegateImpl;
import com.r.mvp.cn.delegate.MvpDelegateCallback;
import com.r.mvp.cn.root.IMvpPresenter;
import com.r.mvp.cn.root.IMvpView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


/**
 * MvpAppCompatActivity
 * 备注:
 * 1.XXActivity 继承 MvpActivity,当页面存在 Presenter 时，具体 Activity 需要实现 createPresenter()
 * 2.由于此框架集合了 RxLifecycle 因此本 Activity 继承自 RxActivity (开发者也可以直接继承 Activity)
 * 3.支持一个 Activity 存在多个 Presenter 查看分支 {@link # https://github.com/RuffianZhong/Rx-Mvp/tree/master.release.mvp_view_1vN_presenter}
 *
 * @author ZhongDaFeng
 * {@link # https://github.com/RuffianZhong/Rx-Mvp}
 */
public abstract class MvpAppCompatActivity<V extends IMvpView, P extends IMvpPresenter<V>> extends RxAppCompatActivity implements IMvpView, MvpDelegateCallback<V, P> {

    private P mPresenter;
    protected ActivityMvpDelegate mvpDelegate;

    /**
     * 创建 Presenter
     */
    public abstract P createPresenter();

    @Override
    public void setPresenter(P mPresenter) {
        this.mPresenter = mPresenter;
    }

    @Override
    public P getPresenter() {
        if (mPresenter == null) {
            throw new NullPointerException("createPresenter() must return not null if use getPresenter()");
        }
        return mPresenter;
    }

    @Override
    public V getMvpView() {
        return (V) this;
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
