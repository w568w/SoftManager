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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wakeup, container, false);
        //scan wake up path
        scanWakeupPath();
        return view;
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
                    }
                }
                if (services != null) {


                    for (int j = 0; j < services.length; j++) {
                        String serviceName = services[i].getClass().getSimpleName();
                        LogUtil.d(TAG, serviceName);
                    }
                }


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
