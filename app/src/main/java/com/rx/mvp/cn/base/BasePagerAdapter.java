package com.rx.mvp.cn.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


/**
 * FragmentPager适配器
 *
 * @author ZhongDaFeng
 */
public class BasePagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;

    public BasePagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
