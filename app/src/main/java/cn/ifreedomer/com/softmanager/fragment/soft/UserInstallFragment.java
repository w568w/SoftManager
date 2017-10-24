package cn.ifreedomer.com.softmanager.fragment.soft;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.ifreedomer.com.softmanager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.constant.ResultCodeConstant;
import cn.ifreedomer.com.softmanager.listener.OnUnInstallListener;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @date: 18/02/2017.
 * @todo:
 */

public class UserInstallFragment extends RecycleFragment {
    private static final String TAG =  UserInstallFragment.class.getSimpleName();
    private AppInfo curUninstallApp = null;
    private static final int REQUEST_INSTALL = 10;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_INSTALL) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getActivity(), getString(R.string.uninstall_success), Toast.LENGTH_SHORT).show();
                refreshUninstallData();
            }
        }
    }

    public void refreshUninstallData() {
        //刷新数据
        if (curUninstallApp == null) {
            Log.e(TAG, "refreshUninstallData: curUninstallApp is null" );
            return;
        }
        Log.e(TAG, "refreshUninstallData=: "+ PackageInfoManager.getInstance().getUserApps().size());
        PackageInfoManager.getInstance().getUserApps().remove(curUninstallApp);
        Log.e(TAG, "refreshUninstallData: "+ PackageInfoManager.getInstance().getUserApps().size());
        setData(PackageInfoManager.getInstance().getUserApps());
        curUninstallApp = null;
    }
}
