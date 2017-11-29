package cn.ifreedomer.com.softmanager.fragment.wakeup;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.LogUtil;

/**
 * @author:eavawu
 * @since: 26/11/2017.
 * TODO:
 */

public class CutWakeupFragment extends Fragment {
    private static final String TAG = CutWakeupFragment.class.getSimpleName();
    private ConcurrentHashMap<String, List<AppInfo>> wakupPathMap = new ConcurrentHashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wakeup, container, false);
        //scan wake up path
        return view;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            scanWakeupPath();
        }
    }

    private void scanWakeupPath() {
        List<AppInfo> allApp = PackageInfoManager.getInstance().getUserApps();
        for (int i = 0; i < allApp.size(); i++) {
            AppInfo appInfo = allApp.get(i);
            String packname = appInfo.getPackname();
            try {
                PackageInfo packageInfo = this.getActivity().getPackageManager().getPackageInfo(packname, 0);
                ActivityInfo[] receivers = packageInfo.receivers;
                ServiceInfo[] services = packageInfo.services;
                if (receivers != null) {
                    for (int j = 0; j < receivers.length; j++) {
                        String receiverName = receivers[i].getClass().getSimpleName();
                        LogUtil.d(TAG, receiverName);
                        if (wakupPathMap.containsKey(receiverName)) {
                            wakupPathMap.get(receiverName).add(appInfo);
                        } else {
                            List<AppInfo> appInfoList = new ArrayList<>(5);
                            wakupPathMap.put(receiverName, appInfoList);
                        }
                    }
                }
                if (services != null) {


                    for (int j = 0; j < services.length; j++) {
                        String serviceName = services[i].getClass().getSimpleName();
                        LogUtil.d(TAG, serviceName);
                        if (wakupPathMap.containsKey(serviceName)) {
                            wakupPathMap.get(serviceName).add(appInfo);
                        } else {
                            List<AppInfo> appInfoList = new ArrayList<>(5);
                            wakupPathMap.put(serviceName, appInfoList);
                        }
                    }
                }


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }


        Set<Map.Entry<String, List<AppInfo>>> entries = wakupPathMap.entrySet();
        for (Map.Entry<String, List<AppInfo>> wakeupEntry : entries) {
            if (wakeupEntry.getValue().size() < 2) {
                wakupPathMap.remove(wakeupEntry.getKey());
            }
        }

        for (Map.Entry<String, List<AppInfo>> wakeupEntry : entries) {
            LogUtil.d(TAG, "key=" + wakeupEntry.getKey() + "   value=" + wakeupEntry.getValue());
        }

    }
}
