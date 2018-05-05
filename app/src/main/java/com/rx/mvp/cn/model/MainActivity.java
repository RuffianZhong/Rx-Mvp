package com.rx.mvp.cn.model;

import android.content.Intent;
import android.view.View;

import com.rx.mvp.cn.R;
import com.rx.mvp.cn.base.BaseActivity;
import com.rx.mvp.cn.model.account.activity.LoginActivity;
import com.rx.mvp.cn.model.other.activity.PhoneAddressActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initBundleData() {
    }

    @OnClick({R.id.login, R.id.phone_address})
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
        }

    }

}

