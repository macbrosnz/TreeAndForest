package com.safe.macbros.treeandforest.Activities.tailgate.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class SectionsTailgateAdaptor extends FragmentStatePagerAdapter {
    private static final String TAG = "SectionsTailgateAdaptor";

    //vars
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitles = new ArrayList<>();

    public SectionsTailgateAdaptor(FragmentManager fm) {
        super(fm);
        fm.popBackStack();
    }


    public void addFragmentList(List<Fragment> fragmentList, List<String> titles){
        mFragmentList.clear();
        mFragmentList=fragmentList;
        mTitles = titles;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
