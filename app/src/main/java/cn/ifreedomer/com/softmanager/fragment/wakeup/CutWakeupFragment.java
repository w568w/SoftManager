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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.ifreedomer.com.softmanager.R;
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
    private ConcurrentHashMap<String, WakeupPathInfo> wakupPathMap = new ConcurrentHashMap<>();

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
        PackageManager packageManager = this.getActivity().getPackageManager();

        List<AppInfo> allApp = PackageInfoManager.getInstance().getUserApps();
        for (int i = 0; i < allApp.size(); i++) {
            AppInfo appInfo = allApp.get(i);
            String packname = appInfo.getPackname();
            try {
                PackageInfo packageInfo = this.getActivity().getPackageManager().getPackageInfo(packname, PackageManager.GET_RECEIVERS | PackageManager.GET_SERVICES);
                ActivityInfo[] receivers = packageInfo.receivers;
                ServiceInfo[] services = packageInfo.services;
                if (receivers != null) {
                    for (int j = 0; j < receivers.length; j++) {
                        ActivityInfo receiver = receivers[j];

                        if (receiver == null) {
                            continue;
                        }
                        String receiverName = receiver.name;
                        LogUtil.d(TAG, receiverName);

                        if (wakupPathMap.containsKey(receiverName)) {
                            wakupPathMap.get(receiverName).getWakeupPath().add(appInfo);
                        } else {
                            WakeupPathInfo wakeupPathInfo = new WakeupPathInfo();
                            wakeupPathInfo.setWakeUpName(receiver.processName);
                            wakeupPathInfo.getWakeupPath().add(appInfo);
                            wakupPathMap.put(receiverName, wakeupPathInfo);
                        }
                    }
                }
                if (services != null) {


                    for (int j = 0; j < services.length; j++) {
                        ServiceInfo service = services[j];
                        if (service == null) {
                            continue;
                        }
                        if (!service.exported) {
                            continue;
                        }
                        String serviceName = services[j].name;
                        LogUtil.d(TAG, serviceName);
                        if (wakupPathMap.containsKey(serviceName)) {
                            wakupPathMap.get(serviceName).getWakeupPath().add(appInfo);
                        } else {
                            WakeupPathInfo wakeupPathInfo = new WakeupPathInfo();
                            wakeupPathInfo.setWakeUpName(service.processName);
                            wakeupPathInfo.getWakeupPath().add(appInfo);
                            wakupPathMap.put(serviceName, wakeupPathInfo);
                        }
                    }
                }


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }


        Set<Map.Entry<String, WakeupPathInfo>> entries = wakupPathMap.entrySet();
        for (Map.Entry<String, WakeupPathInfo> wakeupEntry : entries) {
            if (wakeupEntry.getValue().getWakeupPath().size() < 2) {
                wakupPathMap.remove(wakeupEntry.getKey());
            }
        }

        for (Map.Entry<String, WakeupPathInfo> wakeupEntry : entries) {
            LogUtil.d(TAG, "key=" + wakeupEntry.getKey() + "   value=" + wakeupEntry.getValue());
        }

    }
}
