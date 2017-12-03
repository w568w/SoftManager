package cn.ifreedomer.com.softmanager.fragment.wakeup;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.adapter.ActionAdapter;
import cn.ifreedomer.com.softmanager.adapter.ViewPagerFragmentAdapter;
import cn.ifreedomer.com.softmanager.bean.ComponentEntity;
import cn.ifreedomer.com.softmanager.fragment.component.CommonRecycleFragment;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.model.WakeupPathInfo;
import cn.ifreedomer.com.softmanager.util.LogUtil;

/**
 * @author:eavawu
 * @since: 26/11/2017.
 * TODO:
 */

public class CutWakeupFragment extends Fragment {
    private static final String TAG = CutWakeupFragment.class.getSimpleName();
    @InjectView(R.id.tab)
    TabLayout tab;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.pb)
    ProgressBar pb;
    @InjectView(R.id.lin_loading)
    LinearLayout linLoading;
    @InjectView(R.id.tv_progress)
    TextView tvProgress;
    private int[] tabIds = new int[]{R.string.action_listen, R.string.push_receiver};

    private ConcurrentHashMap<String, WakeupPathInfo> wakupPathMap = new ConcurrentHashMap<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<WakeupPathInfo> wakeupPathInfoList = new ArrayList<>();
    private static final int LOAD_SUCCESS = 1;
    private static final int SCAN_SUCCESS = 2;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_SUCCESS:
                    tvProgress.setText(R.string.scan_wakeup_path);
                    break;
                case SCAN_SUCCESS:
                    tvProgress.setText(R.string.scan_finish);
                    linLoading.setVisibility(View.GONE);
                    initFragments();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_viewpager, container, false);
        ButterKnife.inject(this, view);
        return view;
    }


    private void initFragments() {

        Set<Map.Entry<String, WakeupPathInfo>> entries = wakupPathMap.entrySet();
        for (Map.Entry<String, WakeupPathInfo> entry : entries) {
            wakeupPathInfoList.add(entry.getValue());
        }

        CommonRecycleFragment actionFragment = new CommonRecycleFragment();
        ActionAdapter mineAdapter = new ActionAdapter(getActivity(), R.layout.item_action_listen, wakeupPathInfoList);
        actionFragment.setAdapter(mineAdapter);
        actionFragment.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentList.add(actionFragment);


        CommonRecycleFragment interFragment = new CommonRecycleFragment();
        mineAdapter = new ActionAdapter(getActivity(), R.layout.item_action_listen, wakeupPathInfoList);
        actionFragment.setLayoutManager(new LinearLayoutManager(getActivity()));

        actionFragment.setAdapter(mineAdapter);
//        fragmentList.add(interFragment);


        // init view pager
        ViewPagerFragmentAdapter pagerAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), fragmentList);
        viewpager.setAdapter(pagerAdapter);
        viewpager.setOffscreenPageLimit(1);
        tab.setupWithViewPager(viewpager);
        tab.setVisibility(View.GONE);
        for (int i = 0; i < tab.getTabCount(); i++) {
            tab.getTabAt(i).setText(getString(tabIds[i]));
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!PackageInfoManager.getInstance().isComponentLoaded()) {
                linLoading.setVisibility(View.VISIBLE);
                GlobalDataManager.getInstance().getThreadPool().execute(() -> {
                    PackageInfoManager.getInstance().loadAllComponent();
                    mHandler.sendEmptyMessage(LOAD_SUCCESS);
                    scanWakeupPath();
                    mHandler.sendEmptyMessage(SCAN_SUCCESS);
                });
            }
        }
    }


    private void scanWakeupPath() {
        LogUtil.d(TAG, "scanWakeupPath");
        List<AppInfo> allApp = PackageInfoManager.getInstance().getAllApp();
        for (int i = 0; i < allApp.size(); i++) {

            AppInfo appInfo = allApp.get(i);
            LogUtil.d(TAG, "scanWakeupPath pkgName = " + appInfo.getPackname());

            List<ComponentEntity> receiverList = appInfo.getReceiverList();
            if (receiverList == null || receiverList.size() == 0) {
                continue;
            }
            for (int j = 0; j < receiverList.size(); j++) {

                ComponentEntity componentEntity = receiverList.get(j);
                LogUtil.d(TAG, "scanWakeupPath pkgName  receiver= " + componentEntity.getName());

                List<String> actionList = componentEntity.getActionList();
                if (actionList == null || actionList.size() == 0) {
                    continue;
                }
                for (int k = 0; k < actionList.size(); k++) {

                    String action = actionList.get(k);
                    LogUtil.d(TAG, "scanWakeupPath pkgName  receiver action= " + action);

                    if (TextUtils.isEmpty(action)) {
                        continue;
                    }


                    if (wakupPathMap.containsKey(action)) {
                        if (wakupPathMap.get(action).getWakeupPath().contains(appInfo)) {
                            wakupPathMap.get(action).getComponentEntityList().add(componentEntity);
                            continue;
                        }
                        wakupPathMap.get(action).getWakeupPath().add(appInfo);
                    } else {
                        WakeupPathInfo wakeupPathInfo = new WakeupPathInfo();
                        //存组件
                        wakeupPathInfo.getComponentEntityList().add(componentEntity);
                        wakeupPathInfo.setWakeUpName(action);
                        //存储被启动的app
                        wakeupPathInfo.getWakeupPath().add(appInfo);

                        wakupPathMap.put(action, wakeupPathInfo);
                    }
                }
            }
        }


        Set<Map.Entry<String, WakeupPathInfo>> entries = wakupPathMap.entrySet();
        for (Map.Entry<String, WakeupPathInfo> wakeupEntry : entries) {
            if (wakeupEntry.getValue().getWakeupPath().size() < 2) {
                wakupPathMap.remove(wakeupEntry.getKey());
            }
        }

        LogUtil.d(TAG, "wakepath size = " + entries.size());
        for (Map.Entry<String, WakeupPathInfo> wakeupEntry : entries) {
            LogUtil.d(TAG, "key=" + wakeupEntry.getKey() + "   value size=" + wakeupEntry.getValue().getWakeupPath().size());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


//        PackageManager packageManager = this.getActivity().getPackageManager();
//
//        List<AppInfo> allApp = PackageInfoManager.getInstance().getUserApps();
//        for (int i = 0; i < allApp.size(); i++) {
//            AppInfo appInfo = allApp.get(i);
//            String packname = appInfo.getPackname();
//            try {
//                PackageInfo packageInfo = this.getActivity().getPackageManager().getPackageInfo(packname, PackageManager.GET_RECEIVERS | PackageManager.GET_SERVICES);
//                ActivityInfo[] receivers = packageInfo.receivers;
//                ServiceInfo[] services = packageInfo.services;
//                if (receivers != null) {
//                    for (int j = 0; j < receivers.length; j++) {
//                        ActivityInfo receiver = receivers[j];
//
//                        if (receiver == null) {
//                            continue;
//                        }
//                        String receiverName = receiver.name;
//                        LogUtil.d(TAG, receiverName);
//
//                        if (wakupPathMap.containsKey(receiverName)) {
//                            wakupPathMap.get(receiverName).getWakeupPath().add(appInfo);
//                        } else {
//                            WakeupPathInfo wakeupPathInfo = new WakeupPathInfo();
//                            wakeupPathInfo.setWakeUpName(receiver.processName);
//                            wakeupPathInfo.getWakeupPath().add(appInfo);
//                            wakupPathMap.put(receiverName, wakeupPathInfo);
//                        }
//                    }
//                }
//                if (services != null) {
//
//
//                    for (int j = 0; j < services.length; j++) {
//                        ServiceInfo service = services[j];
//                        if (service == null) {
//                            continue;
//                        }
//                        if (!service.exported) {
//                            continue;
//                        }
//                        String serviceName = services[j].name;
//                        LogUtil.d(TAG, serviceName);
//                        if (wakupPathMap.containsKey(serviceName)) {
//                            wakupPathMap.get(serviceName).getWakeupPath().add(appInfo);
//                        } else {
//                            WakeupPathInfo wakeupPathInfo = new WakeupPathInfo();
//                            wakeupPathInfo.setWakeUpName(service.processName);
//                            wakeupPathInfo.getWakeupPath().add(appInfo);
//                            wakupPathMap.put(serviceName, wakeupPathInfo);
//                        }
//                    }
//                }
//
//
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        Set<Map.Entry<String, WakeupPathInfo>> entries = wakupPathMap.entrySet();
//        for (Map.Entry<String, WakeupPathInfo> wakeupEntry : entries) {
//            if (wakeupEntry.getValue().getWakeupPath().size() < 2) {
//                wakupPathMap.remove(wakeupEntry.getKey());
//            }
//        }
//
//        for (Map.Entry<String, WakeupPathInfo> wakeupEntry : entries) {
//            LogUtil.d(TAG, "key=" + wakeupEntry.getKey() + "   value=" + wakeupEntry.getValue());
//        }
//
//    }
}
