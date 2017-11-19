package cn.ifreedomer.com.softmanager.fragment.icebox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.adapter.IceBoxAdapter;
import cn.ifreedomer.com.softmanager.fragment.soft.UserInstallFragment;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @date: 18/02/2017.
 * @todo: 所有冰冻的app
 */

public class IceBoxFreezeFragment extends Fragment {
    private static final String TAG = UserInstallFragment.class.getSimpleName();
    @InjectView(R.id.rv)
    RecyclerView mRv;
    private List<AppInfo> appInfoList = new ArrayList<>();
    private IceBoxAdapter mIceBoxAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_icebox_user, container, false);
        ButterKnife.inject(this, view);
        initLogic();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            refreshData();
            //相当于Fragment的onResume
        }
    }


    private void initLogic() {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mIceBoxAdapter = new IceBoxAdapter(getActivity(), R.layout.item_icebox, appInfoList);
        mRv.setAdapter(mIceBoxAdapter);
    }

    private void refreshData() {
        appInfoList.clear();
        List<AppInfo> userApps = PackageInfoManager.getInstance().getUserApps();
        for (int i = 0; i < userApps.size(); i++) {
            AppInfo appInfo = userApps.get(i);
            boolean enable = appInfo.isEnable();
            if (!enable) {
                appInfoList.add(appInfo);
            }
        }

        List<AppInfo> systemApps = PackageInfoManager.getInstance().getSystemApps();

        for (int i = 0; i < systemApps.size(); i++) {
            AppInfo appInfo = systemApps.get(i);
            boolean enable = appInfo.isEnable();
            if (!enable) {
                appInfoList.add(appInfo);
            }
        }
        mIceBoxAdapter.notifyDataSetChanged();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
