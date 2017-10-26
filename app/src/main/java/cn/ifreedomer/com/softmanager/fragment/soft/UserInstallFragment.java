package cn.ifreedomer.com.softmanager.fragment.soft;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ifreedomer.com.softmanager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.constant.ResultCodeConstant;
import cn.ifreedomer.com.softmanager.listener.OnUnInstallListener;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @date: 18/02/2017.
 * @todo:
 */

public class UserInstallFragment extends RecycleFragment {
    private static final String TAG = UserInstallFragment.class.getSimpleName();
    private AppInfo curUninstallApp = null;
    private BroadcastReceiver uninstallReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setData(PackageInfoManager.getInstance().getUserApps());
        initUnstallLogic();
        return view;
    }

    private void initUnstallLogic() {
        appAdapter.setUnInstallListener(new OnUnInstallListener() {
            @Override
            public void onUninstall(AppInfo appInfo) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setAction("android.intent.action.DELETE");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse("package:" + appInfo.getPackname()));
                curUninstallApp = appInfo;
                startActivityForResult(intent, ResultCodeConstant.UNINSTALL_SUCCESS);
            }
        });
    }


    public void refreshUninstallData() {
        //刷新数据
        if (curUninstallApp == null) {
            Log.e(TAG, "refreshUninstallData: curUninstallApp is null");
            return;
        }
        Log.e(TAG, "refreshUninstallData=: " + PackageInfoManager.getInstance().getUserApps().size());
        PackageInfoManager.getInstance().getUserApps().remove(curUninstallApp);
        Log.e(TAG, "refreshUninstallData: " + PackageInfoManager.getInstance().getUserApps().size());
        setData(PackageInfoManager.getInstance().getUserApps());
        curUninstallApp = null;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerUninstallReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }

    private void unRegisterReceiver() {
        getContext().unregisterReceiver(uninstallReceiver);
    }


    private void registerUninstallReceiver() {
        uninstallReceiver = new UninstallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        getContext().registerReceiver(uninstallReceiver, filter);
    }

    private class UninstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (curUninstallApp == null) {
                return;
            }
            Log.e(TAG, "onReceive: " + intent.getDataString() + "     curUninstallApp=" + curUninstallApp.getPackname());

            if (!TextUtils.isEmpty(intent.getDataString()) && intent.getDataString().contains(curUninstallApp.getPackname())) {
                refreshUninstallData();
            }
        }
    }
}
