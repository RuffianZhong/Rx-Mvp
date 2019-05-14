package com.rx.mvp.cn.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.r.mvp.cn.MvpAppCompatActivity;
import com.r.mvp.cn.root.IMvpPresenter;
import com.rx.mvp.cn.manager.ActivityStackManager;
import com.rx.mvp.cn.utils.ToastUtils;
import com.rx.mvp.cn.widget.RLoadingDialog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * 基类Activity
 * 备注:所有的Activity都继承自此Activity
 * 1.规范团队开发
 * 2.统一处理Activity所需配置,初始化
 *
 * @author ZhongDaFeng
 */
public abstract class BaseActivity extends MvpAppCompatActivity implements EasyPermissions.PermissionCallbacks {

    protected Context mContext;
    protected Unbinder unBinder;
    protected RLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getManager().push(this);
        setContentView(getContentViewId());
        mContext = this;
        unBinder = ButterKnife.bind(this);
        mLoadingDialog = new RLoadingDialog(this, true);
        initBundleData();
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除view绑定
        if (unBinder != null) {
            unBinder.unbind();
        }
        ActivityStackManager.getManager().remove(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
    }

    /**
     * 获取显示view的xml文件ID
     */
    protected abstract int getContentViewId();


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


    /**------------MVP通用方法避免每个组件都要实现--------------**/

    /**
     * Presenter绑定入口，组件使用Presenter时存入具体值
     */
    @Override
    protected IMvpPresenter[] getPresenterArray() {
        return new IMvpPresenter[0];
    }

    /**------------MVP->View层方法预实现{@link com.r.mvp.cn.MvpView}--------------**/

    /**
     * 展示吐司
     */
    public void showToast(@NonNull String msg) {
        ToastUtils.showToast(this, msg);
    }

    /**
     * 显示进度View
     */
    public void showProgressView() {
        mLoadingDialog.show();
    }

    /**
     * 隐藏进度View
     */
    public void dismissProgressView() {
        mLoadingDialog.dismiss();
    }

    /**
     * 获取Activity实例
     */
    public Activity getActivity() {
        return this;
    }

}
