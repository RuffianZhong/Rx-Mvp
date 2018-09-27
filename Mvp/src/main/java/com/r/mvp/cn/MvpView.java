
package com.r.mvp.cn;

import android.support.annotation.UiThread;

import com.r.mvp.cn.root.IMvpView;

/**
 * 基础View接口
 * 备注:loading/data/error
 * 1. lde 思想: 页面通用  加载中/展示数据/错误处理
 * 2. action 方式: 考虑多个请求时 根据 action 区分处理
 */
public interface MvpView extends IMvpView {

    /**
     * mvp 加载中
     *
     * @param action 区分不同事件
     * @param show   开启/关闭 true:开启
     */
    @UiThread
    void mvpLoading(String action, boolean show);

    /**
     * mvp 展示数据
     *
     * @param action 区分不同事件
     * @param data   数据
     * @param <M>
     */
    @UiThread
    <M> void mvpData(String action, M data);

    /**
     * mvp 错误处理
     *
     * @param action 区分不同事件
     * @param code   错误码
     * @param msg    错误信息
     */
    @UiThread
    void mvpError(String action, int code, String msg);

}
