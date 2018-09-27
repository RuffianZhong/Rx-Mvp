package com.rx.mvp.cn.model;

import android.Manifest;
import android.content.Intent;
import android.view.View;

import com.r.mvp.cn.root.IMvpPresenter;
import com.rx.mvp.cn.R;
import com.rx.mvp.cn.base.BaseActivity;
import com.rx.mvp.cn.model.account.activity.LoginActivity;
import com.rx.mvp.cn.model.load.download.DownloadActivity;
import com.rx.mvp.cn.model.load.upload.UploadActivity;
import com.rx.mvp.cn.model.multiple.MultipleActivity;
import com.rx.mvp.cn.model.phone.activity.PhoneAddressActivity;
import com.rx.mvp.cn.utils.LogUtils;

import java.util.List;

import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initBundleData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        String[] per = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, per)) {
            EasyPermissions.requestPermissions(this, "申请读写权限", 101, per);
        }
    }

    @OnClick({R.id.login, R.id.phone_address, R.id.multiple, R.id.upload, R.id.download})
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.phone_address:
                intent = new Intent(this, PhoneAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.multiple:
                intent = new Intent(this, MultipleActivity.class);
                startActivity(intent);
                break;
            case R.id.upload:
                intent = new Intent(this, UploadActivity.class);
                startActivity(intent);
                break;
            case R.id.download:
                intent = new Intent(this, DownloadActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        LogUtils.e("申请权限成功");
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        LogUtils.e("申请权限失败");
    }

    @Override
    protected IMvpPresenter[] getPresenterArray() {
        return new IMvpPresenter[0];
    }
}

