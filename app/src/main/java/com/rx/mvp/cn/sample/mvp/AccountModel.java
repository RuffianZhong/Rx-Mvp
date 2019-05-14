package com.rx.mvp.cn.sample.mvp;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.r.mvp.cn.model.ModelCallback;
import com.rx.mvp.cn.base.BizFactory;
import com.rx.mvp.cn.core.net.http.RHttpCallback;
import com.rx.mvp.cn.model.account.biz.UserBiz;
import com.rx.mvp.cn.model.account.entity.UserBean;
import com.trello.rxlifecycle2.LifecycleProvider;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * XX业务的Model
 *
 * @author ZhongDaFeng
 */
public class AccountModel implements AccountContract.LoginModel {

    @Override
    public void login(String userName, String password, LifecycleProvider lifecycle, final ModelCallback.Http<UserBean> modelCallback) {
        //Biz发起网络请求
        BizFactory.getBiz(UserBiz.class).login(userName, password, lifecycle, new RHttpCallback<UserBean>() {

            @Override
            public UserBean convert(JsonElement data) {
                return new Gson().fromJson(data, UserBean.class);
            }

            @Override
            public void onSuccess(UserBean value) {
                //回调给Presenter
                modelCallback.onSuccess(value);
            }

            @Override
            public void onError(int code, String desc) {
                //回调给Presenter
                modelCallback.onError(code, desc);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void getLocalCache(String account, LifecycleProvider lifecycle, final ModelCallback.Data<UserBean> modelCallback) {

        //RxJava异步解析本地数据
        Observable.create(new ObservableOnSubscribe<UserBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<UserBean> e) throws Exception {

                //模拟工作线程获取并解析数据

                e.onNext(new UserBean());

            }
        }).subscribeOn(Schedulers.io())//工作线程
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle.bindToLifecycle())//绑定生命周期
                .subscribe(new Observer<UserBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull UserBean userBean) {
                        modelCallback.onSuccess(userBean);  //回调给Presenter
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        modelCallback.onSuccess(new UserBean());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
