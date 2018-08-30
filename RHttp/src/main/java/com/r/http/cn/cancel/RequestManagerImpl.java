package com.r.http.cn.cancel;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.util.ArrayMap;

import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * Http请求管理实现类
 *
 * @author ZhongDaFeng
 */
public class RequestManagerImpl implements RequestManager<Object> {

    private static volatile RequestManagerImpl mInstance;
    private ArrayMap<Object, Disposable> mMaps;//处理,请求列表

    public static RequestManagerImpl getInstance() {
        if (mInstance == null) {
            synchronized (RequestManagerImpl.class) {
                if (mInstance == null) {
                    mInstance = new RequestManagerImpl();
                }
            }
        }
        return mInstance;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private RequestManagerImpl() {
        mMaps = new ArrayMap<>();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void add(Object tag, Disposable disposable) {
        mMaps.put(tag, disposable);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void remove(Object tag) {
        if (!mMaps.isEmpty()) {
            mMaps.remove(tag);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void cancel(Object tag) {
        if (mMaps.isEmpty()) {
            return;
        }
        if (mMaps.get(tag) == null) {
            return;
        }
        if (!mMaps.get(tag).isDisposed()) {
            mMaps.get(tag).dispose();
        }
        mMaps.remove(tag);
    }

    @Override
    public void cancelAll() {
        if (mMaps.isEmpty()) {
            return;
        }
        //遍历取消请求
        Disposable disposable;
        Set<Object> keySet = mMaps.keySet();
        for (Object key : keySet) {
            disposable = mMaps.get(key);
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        }
        mMaps.clear();
    }


    /**
     * 判断是否取消了请求
     *
     * @param tag
     * @return
     */
    public boolean isDisposed(Object tag) {
        if (mMaps.isEmpty() || mMaps.get(tag) == null) return true;
        return mMaps.get(tag).isDisposed();
    }
}
