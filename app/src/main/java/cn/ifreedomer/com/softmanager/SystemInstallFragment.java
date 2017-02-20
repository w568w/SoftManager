package cn.ifreedomer.com.softmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ifreedomer.com.softmanager.listener.OnUnInstallListener;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @date: 18/02/2017.
 * @todo:
 */

public class SystemInstallFragment extends RecycleFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setData(PackageInfoManager.getInstance().getSystemApps());
        initUnstallLogic();
        return view;
    }

    private void initUnstallLogic() {
        appAdapter.setUnInstallListener(new OnUnInstallListener() {
            @Override
            public void onUninstall(AppInfo appInfo) {

            }
        });
    }
}
