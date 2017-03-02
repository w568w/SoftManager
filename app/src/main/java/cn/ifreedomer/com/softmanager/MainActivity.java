package cn.ifreedomer.com.softmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.adapter.ViewPagerFragmentAdapter;
import cn.ifreedomer.com.softmanager.constant.ResultCodeConstant;
import cn.ifreedomer.com.softmanager.fragment.AutoStartFragment;
import cn.ifreedomer.com.softmanager.fragment.RecycleFragment;
import cn.ifreedomer.com.softmanager.fragment.SystemInstallFragment;
import cn.ifreedomer.com.softmanager.fragment.UserInstallFragment;
import cn.ifreedomer.com.softmanager.util.L;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener {
    private static final int MINE_INDEX = 0;
    private static final int SYSTEM_INDEX = 1;
    private static final int AUTOSTART_INDEX = 2;
    public static final String TAG = MainActivity.class.getSimpleName();
    @InjectView(R.id.pb)
    ProgressBar pb;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    private List<Fragment> fragmentList = new ArrayList<>();
    private int[] tabIds = new int[]{R.string.mine, R.string.system, R.string.auto_start};
    @InjectView(R.id.tab)
    TabLayout tab;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initAllFragment();
        initView();
        initData();

    }

    private void initView() {
        toolbar.inflateMenu(R.menu.action_menu);
        toolbar.setTitle(getString(R.string.uninstall_master));
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    private void initData() {
        PackageInfoManager.getInstance().loadData(this, new LoadStateCallback() {
            @Override
            public void loadBegin() {
                pb.setVisibility(View.VISIBLE);
            }

            @Override
            public void loadProgress(int current, int max) {

            }

            @Override
            public void loadFinish() {
                pb.setVisibility(View.GONE);
                RecycleFragment mineFragment = (RecycleFragment) fragmentList.get(MINE_INDEX);
                RecycleFragment systemFragment = (RecycleFragment) fragmentList.get(SYSTEM_INDEX);

                mineFragment.setData(PackageInfoManager.getInstance().getUserApps());
                systemFragment.setData(PackageInfoManager.getInstance().getSystemApps());
            }
        });
    }

    private void initAllFragment() {

        fragmentList.add(new UserInstallFragment());
        fragmentList.add(new SystemInstallFragment());
        fragmentList.add(new AutoStartFragment());
        // init view pager
        ViewPagerFragmentAdapter pagerAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(3);
        viewpager.addOnPageChangeListener(this);
        tab.setupWithViewPager(viewpager);
        for (int i = 0; i < tab.getTabCount(); i++) {
            tab.getTabAt(i).setText(getString(tabIds[i]));
        }

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        L.e(TAG, "onActivityResult");
        if (requestCode == ResultCodeConstant.UNINSTALL_SUCCESS) {
            UserInstallFragment userInstallFragment = (UserInstallFragment) fragmentList.get(MINE_INDEX);
            userInstallFragment.refreshUninstallData();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
