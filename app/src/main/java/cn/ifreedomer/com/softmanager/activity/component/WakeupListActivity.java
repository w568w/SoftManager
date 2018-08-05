package cn.ifreedomer.com.softmanager.activity.component;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private boolean showDisabled = true;
    private int curIndex = 0;
    private static final int MINE_INDEX = 0;
    private static final int SYSTEM_INDEX = 1;

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
    private List<ComponentEntity> userComponentEntityList;
    private List<ComponentEntity> systemComPonentEntityList;
    private CommonRecycleFragment mineFragment;
    private CommonRecycleFragment systemFragment;

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

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        toolbar.inflateMenu(R.menu.cut_wakeup_disable);
        toolbar.getMenu().findItem(R.id.all_operation).setOnMenuItemClickListener((MenuItem item) -> {
            showDisabled = !showDisabled;
            linLoading.setVisibility(View.VISIBLE);
            if (showDisabled) {
                item.setTitle(R.string.all_disable);
                GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        enableAllComponent();
                        runOnUiThread(() ->
                        {
                            refreshList();
                            linLoading.setVisibility(View.GONE);

                        });
                    }
                });


            } else {
                item.setTitle(R.string.all_enable);
                GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        disableComponent();
                        runOnUiThread(() -> {
                            refreshList();
                            linLoading.setVisibility(View.GONE);
                        });
                    }
                });
            }
            return true;
        });
        return super.onPrepareOptionsMenu(menu);
    }

    private void refreshList() {
        if (curIndex == MINE_INDEX){
            mineFragment.getAdapter().notifyDataSetChanged();
        }else{
            systemFragment.getAdapter().notifyDataSetChanged();
        }
    }

    private void disableComponent() {
        if (curIndex == MINE_INDEX) {
            for (int i = 0; i < userComponentEntityList.size(); i++) {
                userComponentEntityList.get(i).setEnable(false);
                PackageInfoManager.getInstance().disableAndSaveComponent(userComponentEntityList.get(i));
            }
        } else {
            for (int i = 0; i < systemComPonentEntityList.size(); i++) {
                systemComPonentEntityList.get(i).setEnable(false);
                PackageInfoManager.getInstance().disableAndSaveComponent(systemComPonentEntityList.get(i));
            }
        }
    }

    private void enableAllComponent() {
        if (curIndex == MINE_INDEX) {
            for (int i = 0; i < userComponentEntityList.size(); i++) {
                userComponentEntityList.get(i).setEnable(true);
                PackageInfoManager.getInstance().enableAndRemoveComponent(userComponentEntityList.get(i));
            }
        } else {
            for (int i = 0; i < systemComPonentEntityList.size(); i++) {
                userComponentEntityList.get(i).setEnable(true);
                PackageInfoManager.getInstance().enableAndRemoveComponent(systemComPonentEntityList.get(i));
            }
        }
    }

    private void initData() {
        WakeupPathInfo wakeupPathInfo = (WakeupPathInfo) GlobalDataManager.getInstance().getTempMap().get(CutWakeupFragment.WAKEUP_PATH);
        List<AppInfo> userAppInfo = new ArrayList<>();
        userComponentEntityList = new ArrayList<>();
        List<AppInfo> systemAppInfo = new ArrayList<>();
        systemComPonentEntityList = new ArrayList<>();
        if (wakeupPathInfo==null){
            Toast.makeText(WakeupListActivity.this, R.string.wakeup_path_null,Toast.LENGTH_SHORT).show();;
            return;
        }
        List<ComponentEntity> componentEntityList = wakeupPathInfo.getComponentEntityList();
        for (int i = 0; i < wakeupPathInfo.getWakeupPath().size(); i++) {
            AppInfo appInfo = wakeupPathInfo.getWakeupPath().get(i);
            boolean userApp = appInfo.isUserApp();
            ComponentEntity componentEntity = componentEntityList.get(i);
            componentEntity.setEnable(PackageInfoManager.getInstance().isComponentEnable(appInfo.getPackname(), componentEntityList.get(i).getName()));
            if (userApp) {
                userAppInfo.add(appInfo);
                userComponentEntityList.add(componentEntityList.get(i));
            } else {
                systemAppInfo.add(appInfo);
                systemComPonentEntityList.add(componentEntityList.get(i));
            }
        }


        mineFragment = new CommonRecycleFragment();
        mineFragment.setLayoutManager(new LinearLayoutManager(this));
        mineFragment.setAdapter(new WakeupListAdapter(this, R.layout.item_wakeup, userAppInfo, userComponentEntityList));
        fragmentList.add(mineFragment);


        systemFragment = new CommonRecycleFragment();
        systemFragment.setLayoutManager(new LinearLayoutManager(this));
        systemFragment.setAdapter(new WakeupListAdapter(this, R.layout.item_wakeup, systemAppInfo, systemComPonentEntityList));
        fragmentList.add(systemFragment);

        // init view pager
        ViewPagerFragmentAdapter pagerAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(2);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab.setupWithViewPager(viewpager);
        for (int i = 0; i < tab.getTabCount(); i++) {
            tab.getTabAt(i).setText(getString(tabIds[i]));
        }


//        rv.setAdapter();
//        rv.setLayoutManager(new LinearLayoutManager(this));
    }


}
