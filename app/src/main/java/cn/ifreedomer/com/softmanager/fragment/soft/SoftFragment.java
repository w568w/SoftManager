package cn.ifreedomer.com.softmanager.fragment.soft;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.adapter.ViewPagerFragmentAdapter;

public class SoftFragment extends Fragment implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    private static final int MINE_INDEX = 0;
    private static final int SYSTEM_INDEX = 1;
    private static final int AUTOSTART_INDEX = 2;
    public static final String TAG = SoftFragment.class.getSimpleName();
    @InjectView(R.id.pb)
    ProgressBar pb;

    private List<Fragment> fragmentList = new ArrayList<>();
    private int[] tabIds = new int[]{R.string.mine, R.string.system, R.string.auto_start};
    @InjectView(R.id.tab)
    TabLayout tab;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_soft, container, false);
        ButterKnife.inject(this, view);
        initAllFragment();
        return view;
    }


    private void initAllFragment() {

        fragmentList.add(new UserInstallFragment());
        fragmentList.add(new SystemInstallFragment());
        fragmentList.add(new AutoStartFragment());
        // init view pager
        ViewPagerFragmentAdapter pagerAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), fragmentList);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(3);
        viewpager.addOnPageChangeListener(this);
        tab.setupWithViewPager(viewpager);
        for (int i = 0; i < tab.getTabCount(); i++) {
            tab.getTabAt(i).setText(getString(tabIds[i]));
        }

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == MINE_INDEX) {
            MobclickAgent.onPageStart("MinePage"); //统计页面，"MainScreen"为页面名称，可自定义
        } else if (position == SYSTEM_INDEX) {
            MobclickAgent.onPageStart("SystemPage"); //统计页面，"MainScreen"为页面名称，可自定义
        } else {
            MobclickAgent.onPageStart("AutoPage"); //统计页面，"MainScreen"为页面名称，可自定义
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
