package com.rx.mvp.cn.model.multiple;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.r.mvp.cn.root.IMvpPresenter;
import com.ruffian.library.RVPIndicator;
import com.rx.mvp.cn.R;
import com.rx.mvp.cn.base.BaseActivity;
import com.rx.mvp.cn.base.BaseFragmentActivity;
import com.rx.mvp.cn.base.BasePagerAdapter;
import com.rx.mvp.cn.model.account.fragment.LoginFragment;
import com.rx.mvp.cn.model.phone.fragment.PhoneAddressFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * Fragment使用示例
 *
 * @author ZhongDaFeng
 */
public class MultipleActivity extends BaseActivity {

    @BindView(R.id.vp_indicator)
    RVPIndicator vpIndicator;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private BasePagerAdapter mPagerAdapter;
    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mList = Arrays.asList("综合使用", "用户登录", "号码查询");

    @Override
    protected int getContentViewId() {
        return R.layout.multiple_activity;
    }

    @Override
    protected void initBundleData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

        mFragmentList.add(new MultipleFragment());
        mFragmentList.add(new LoginFragment());
        mFragmentList.add(new PhoneAddressFragment());
        mPagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), mFragmentList);

        //设置指示器title
        vpIndicator.setTitleList(mList);

        // 设置关联的ViewPager
        vpIndicator.setViewPager(viewPager, 0);

        //设置Adapter
        viewPager.setAdapter(mPagerAdapter);
    }

    @Override
    protected IMvpPresenter[] getPresenterArray() {
        return new IMvpPresenter[0];
    }
}
