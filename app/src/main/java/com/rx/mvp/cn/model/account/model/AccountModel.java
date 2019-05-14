package com.rx.mvp.cn.model.account.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.r.mvp.cn.model.ModelCallback;
import com.rx.mvp.cn.base.BizFactory;
import com.rx.mvp.cn.core.net.http.RHttpCallback;
import com.rx.mvp.cn.model.account.biz.UserBiz;
import com.rx.mvp.cn.model.account.contract.AccountContract;
import com.rx.mvp.cn.model.account.entity.UserBean;
import com.rx.mvp.cn.utils.SpUtils;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.text.SimpleDateFormat;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * AccountModel
 *
 * @author ZhongDaFeng
 */
public class AccountModel implements AccountContract.LoginModel {

    private final String key_user_cache = "key_user_info";

    @Override
    public void login(final Context context, String userName, String password, LifecycleProvider lifecycle, final ModelCallback.Http<UserBean> modelCallback) {

        //Biz发起网络请求
        BizFactory.getBiz(UserBiz.class).login(userName, password, lifecycle, new RHttpCallback<UserBean>() {

            @Override
            public UserBean convert(JsonElement data) {
                return new Gson().fromJson(data, UserBean.class);
            }

            @Override
            public void onSuccess(UserBean value) {

                String time = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss").format(System.currentTimeMillis());
                value.setTime(time);
                //回调给Presenter
                modelCallback.onSuccess(value);
                //保存到本地数据
                saveLocalCache(context, value);
            }

            @Override
            public void onError(int code, String desc) {
                //回调给Presenter
                modelCallback.onError(code, desc);
            }

            @Override
            public void onCancel() {
                //回调给Presenter
                modelCallback.onCancel();
            }
        });

    }

    @Override
    public void getLocalCache(final Context context, LifecycleProvider lifecycle, final ModelCallback.Data<String> modelCallback) {
        //RxJava异步解析本地数据
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                //模拟工作线程获取并解析数据
                String userInfo = SpUtils.getSpUtils(context).getSPValue(key_user_cache, "");

                e.onNext(userInfo);
                e.onComplete();

            }
        }).subscribeOn(Schedulers.io())//工作线程
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle.<String>bindToLifecycle())//绑定生命周期
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String userBean) {
                        modelCallback.onSuccess(userBean);  //回调给Presenter
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        modelCallback.onSuccess("");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void saveLocalCache(Context context, UserBean data) {
        StringBuffer sb = new StringBuffer();
        sb.append("用户ID:")
                .append(data.getUid())
                .append("\n")
                .append("Token:")
                .append("\n")
                .append(data.getToken())
                .append("\n")
                .append("最后登录:")
                .append("\n")
                .append(data.getTime());

        SpUtils.getSpUtils(context).putSPValue(key_user_cache, sb.toString());
    }
}
