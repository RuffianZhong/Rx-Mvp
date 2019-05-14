package com.rx.mvp.cn.sample.mvp;

import android.support.annotation.UiThread;

import com.r.mvp.cn.MvpView;
import com.r.mvp.cn.model.ModelCallback;
import com.r.mvp.cn.root.IMvpModel;
import com.rx.mvp.cn.model.account.entity.UserBean;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * XX业务的Contract(协议)
 * 目的：避免mvp架构 view/model 文件过多
 * 综合管理某业务的 view/model 接口
 *
 * @author ZhongDaFeng
 */
public interface AccountContract {

    /*登录模块View接口*/
    interface ILoginView extends MvpView {

        /*登录成功展示结果*/
        @UiThread
        void showResult(UserBean data);

        /*登录错误处理逻辑*/
        @UiThread
        void showError(int code, String msg);
    }

    /*登录模块model接口.此处根据具体项目决定是否需要此接口层*/
    interface LoginModel extends IMvpModel {
        /**
         * 用户密码登录
         *
         * @param lifecycle     组件生命周期
         * @param modelCallback model回调接口(网络)
         */
        void login(String userName, String password, LifecycleProvider lifecycle, ModelCallback.Http<UserBean> modelCallback);

        /**
         * 获取本地缓存数据
         *
         * @param modelCallback model回调接口(普通数据)
         */
        void getLocalCache(String account, LifecycleProvider lifecycle, ModelCallback.Data<UserBean> modelCallback);
    }


}
