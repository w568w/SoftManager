package cn.ifreedomer.com.softmanager.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
    private AppInfo curUninstallApp = null;

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
                getActivity().startActivityForResult(intent, ResultCodeConstant.UNINSTALL_SUCCESS);



            }
        });
    }

    public void refreshUninstallData() {
        //刷新数据
        if (curUninstallApp == null) {
            return;
        }
        PackageInfoManager.getInstance().getUserApps().remove(curUninstallApp);
        setData(PackageInfoManager.getInstance().getUserApps());
        curUninstallApp = null;
    }
}
