package com.r.mvp.cn.model;

/**
 * 模块数据回调接口
 *
 * @author ZhongDaFeng
 */
public interface ModelCallback {

    /**
     * 网络数据回调，泛指http
     *
     * @param <T>
     */
    interface Http<T> {

        public void onSuccess(T object);

        public void onError(int code, String desc);

        public void onCancel();
    }

    /**
     * 其他数据回调<本地数据，数据库等>
     *
     * @param <T>
     */
    interface Data<T> {

        public void onSuccess(T object);
    }

}
