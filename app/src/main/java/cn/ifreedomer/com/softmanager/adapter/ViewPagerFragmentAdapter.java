package cn.ifreedomer.com.softmanager.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.ifreedomer.com.softmanager.RecycleFragment;

public class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter {
    List<RecycleFragment> list;
    public ViewPagerFragmentAdapter(FragmentManager fm, List<RecycleFragment> list) {
        super(fm);
        this.list = list;

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }




}