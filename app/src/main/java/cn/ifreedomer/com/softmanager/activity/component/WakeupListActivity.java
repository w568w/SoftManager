package cn.ifreedomer.com.softmanager.activity.component;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.adapter.ViewPagerFragmentAdapter;
import cn.ifreedomer.com.softmanager.adapter.WakeupListAdapter;
import cn.ifreedomer.com.softmanager.bean.ComponentEntity;
import cn.ifreedomer.com.softmanager.fragment.component.CommonRecycleFragment;
import cn.ifreedomer.com.softmanager.fragment.wakeup.CutWakeupFragment;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.model.WakeupPathInfo;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;

public class WakeupListActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tab)
    TabLayout tab;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.pb)
    ProgressBar pb;
    @InjectView(R.id.tv_progress)
    TextView tvProgress;
    @InjectView(R.id.lin_loading)
    LinearLayout linLoading;
    private int[] tabIds = new int[]{R.string.mine, R.string.system};
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_viewpager);
        ButterKnife.inject(this);
        initTitleBar();
        initData();
    }

    private void initTitleBar() {
        ToolbarUtil.setTitleBarWhiteBack(this, toolbar);
        getSupportActionBar().setTitle((String) GlobalDataManager.getInstance().getTempMap().get(CutWakeupFragment.WAKEUP_ACTION));
    }

    private void initData() {
        WakeupPathInfo wakeupPathInfo = (WakeupPathInfo) GlobalDataManager.getInstance().getTempMap().get(CutWakeupFragment.WAKEUP_PATH);
        List<AppInfo> userAppInfo = new ArrayList<>();
        List<ComponentEntity> userComponentEntityList = new ArrayList<>();
        List<AppInfo> systemAppInfo = new ArrayList<>();
        List<ComponentEntity> systemComPonentEntityList = new ArrayList<>();
        List<ComponentEntity> componentEntityList = wakeupPathInfo.getComponentEntityList();
        for (int i = 0; i < wakeupPathInfo.getWakeupPath().size(); i++) {
            AppInfo appInfo = wakeupPathInfo.getWakeupPath().get(i);
            boolean userApp = appInfo.isUserApp();
            ComponentEntity componentEntity = componentEntityList.get(i);
            componentEntity.setChecked(PackageInfoManager.getInstance().isComponentEnable(appInfo.getPackname(),componentEntityList.get(i).getName()));
            if (userApp) {
                userAppInfo.add(appInfo);
                userComponentEntityList.add(componentEntityList.get(i));
            } else {
                systemAppInfo.add(appInfo);
                systemComPonentEntityList.add(componentEntityList.get(i));
            }
        }


        CommonRecycleFragment mineFragment = new CommonRecycleFragment();
        mineFragment.setLayoutManager(new LinearLayoutManager(this));
        mineFragment.setAdapter(new WakeupListAdapter(this, R.layout.item_wakeup, userAppInfo, userComponentEntityList));
        fragmentList.add(mineFragment);


        CommonRecycleFragment systemFragment = new CommonRecycleFragment();
        systemFragment.setLayoutManager(new LinearLayoutManager(this));
        systemFragment.setAdapter(new WakeupListAdapter(this, R.layout.item_wakeup, systemAppInfo, systemComPonentEntityList));
        fragmentList.add(systemFragment);

        // init view pager
        ViewPagerFragmentAdapter pagerAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(2);
        tab.setupWithViewPager(viewpager);
        for (int i = 0; i < tab.getTabCount(); i++) {
            tab.getTabAt(i).setText(getString(tabIds[i]));
        }


//        rv.setAdapter();
//        rv.setLayoutManager(new LinearLayoutManager(this));
    }


}
