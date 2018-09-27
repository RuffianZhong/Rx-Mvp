package com.rx.mvp.cn.base;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.r.mvp.cn.MvpFragment;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * 基类Fragment
 * 备注:所有的Fragment都继承自此Fragment
 * 1.规范团队开发
 * 2.统一处理Fragment所需配置,初始化
 *
 * @author ZhongDaFeng
 */
public abstract class BaseFragment extends MvpFragment implements EasyPermissions.PermissionCallbacks {

    protected Context mContext;
    protected Unbinder unBinder;
    protected View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        if (mContext == null) return;
        mView = getContentView();
        unBinder = ButterKnife.bind(this, mView);
        initBundleData();
        initView();
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView.getParent() != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
        }
        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //移除view绑定
        if (unBinder != null) {
            unBinder.unbind();
        }
    }

    /**
     * 是否已经创建
     *
     * @return
     */
    public boolean isCreated() {
        return mView != null;
    }

    /**
     * 获取显示view
     */
    protected abstract View getContentView();

    /**
     * 获取上一个界面传送过来的数据
     */
    protected abstract void initBundleData();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化Data
     */
    protected abstract void initData();

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
    }

}
