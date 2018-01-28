package cn.ifreedomer.com.softmanager.fragment.component;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.component.ComponentListActivity;
import cn.ifreedomer.com.softmanager.activity.component.ReceiverListActivity;
import cn.ifreedomer.com.softmanager.adapter.ReceiverAdapter;
import cn.ifreedomer.com.softmanager.adapter.ViewPagerFragmentAdapter;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @since: 02/12/2017.
 * TODO:
 */

public class ReceiverFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    @InjectView(R.id.tab)
    TabLayout tab;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    private int[] tabIds = new int[]{R.string.mine, R.string.system};
    private List<Fragment> fragmentList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_viewpager, container, false);
        ButterKnife.inject(this, view);
        initAllFragment();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    private void initAllFragment() {
        CommonRecycleFragment mineFragment = new CommonRecycleFragment();
        List<AppInfo> userApps = PackageInfoManager.getInstance().getUserApps();
        List<AppInfo> userAppClone = new ArrayList<>();
        userAppClone.addAll(userApps);

        Collections.sort(userAppClone, (lhs, rhs) -> {
            if (lhs.getReceiverList() != null && rhs.getReceiverList() == null) {
                return -1;
            }
            if (lhs.getReceiverList() == null && rhs.getReceiverList() != null) {
                return 1;
            }
            if (lhs.getReceiverList() == null && rhs.getReceiverList() == null) {
                return 0;
            }


            return rhs.getReceiverList().size() - lhs.getReceiverList().size();
        });

        ReceiverAdapter mineAdapter = new ReceiverAdapter(getActivity(), R.layout.item_common_recycle, userAppClone);
        mineFragment.setAdapter(mineAdapter);
        mineFragment.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentList.add(mineFragment);
        mineAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (userAppClone == null || userAppClone.size() == 0 || userAppClone.get(position).getReceiverList() == null || userAppClone.get(position).getReceiverList().size() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.no_content), Toast.LENGTH_SHORT).show();
                    return;
                }
                GlobalDataManager.getInstance().getTempMap().put(ComponentListActivity.APP_INFO, userAppClone.get(position));
                startActivity(new Intent(getActivity(), ReceiverListActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


        CommonRecycleFragment systemFragment = new CommonRecycleFragment();
        systemFragment.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentList.add(systemFragment);
        List<AppInfo> systemApps = PackageInfoManager.getInstance().getSystemApps();
        ReceiverAdapter systemAdapter = new ReceiverAdapter(getActivity(), R.layout.item_common_recycle, systemApps);
        systemFragment.setAdapter(systemAdapter);
        systemAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (systemApps == null || systemApps.size() == 0 || systemApps.get(position).getReceiverList() == null || systemApps.get(position).getReceiverList().size() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.no_content), Toast.LENGTH_SHORT).show();
                    return;
                }
                GlobalDataManager.getInstance().getTempMap().put(ComponentListActivity.APP_INFO, systemApps.get(position));
                startActivity(new Intent(getActivity(), ReceiverListActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


        // init view pager
        ViewPagerFragmentAdapter pagerAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), fragmentList);
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
