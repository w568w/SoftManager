package cn.ifreedomer.com.softmanager;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.adapter.ViewPagerFragmentAdapter;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {


    @InjectView(R.id.pb)
    ProgressBar pb;
    private List<RecycleFragment> fragmentList = new ArrayList<>();
    private int[] tabIds = new int[]{R.string.mine, R.string.system};
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
        initData();

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
                fragmentList.get(0).setData(PackageInfoManager.getInstance().getUserApps());
                fragmentList.get(1).setData(PackageInfoManager.getInstance().getSystemApps());
            }
        });
    }

    private void initAllFragment() {

        fragmentList.add(new UserInstallFragment());
        fragmentList.add(new SystemInstallFragment());

        // init view pager
        ViewPagerFragmentAdapter pagerAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(2);
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
}
