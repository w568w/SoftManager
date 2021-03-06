package cn.ifreedomer.com.softmanager.fragment.soft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.DialogUtil;
import cn.ifreedomer.com.softmanager.util.Terminal;

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
        appAdapter.setUnInstallListener(appInfo -> {
            if (PermissionManager.getInstance().checkOrRequestedRootPermission()) {
                GlobalDataManager.getInstance().getThreadPool().execute(() -> {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), getString(R.string.uninstalling), Toast.LENGTH_SHORT).show();
                    });
                    boolean isBackupSusccess = Terminal.backupApp(appInfo);
                    if (!isBackupSusccess) {
                        getActivity().runOnUiThread(() -> DialogUtil.showDialog(getString(R.string.tip), getString(R.string.back_failed_continue_uninstall), getContext(), new DialogUtil.DialogCallback() {
                            @Override
                            public void onConfirm() {
                                removeSystemApp(appInfo);
                            }

                            @Override
                            public void onCancel() {

                            }
                        }));

//                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), R.string.backup_failed, Toast.LENGTH_SHORT).show());
                        return;
                        
                    } else {
                        PackageInfoManager.getInstance().getBackupList().add(appInfo);
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), R.string.back_success, Toast.LENGTH_SHORT).show());

                    }
                    removeSystemApp(appInfo);
                });

            } else {
                Terminal.grantRoot(getActivity());
                Toast.makeText(getActivity(), getString(R.string.no_permission), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeSystemApp(AppInfo appInfo) {
        boolean b = Terminal.uninstallSystemApp(appInfo);
        if (b) {
            PackageInfoManager.getInstance().getSystemApps().remove(appInfo);
            appAdapter.getDatas().remove(appInfo);
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), getString(R.string.uninstall_success), Toast.LENGTH_SHORT).show();
                appAdapter.notifyDataSetChanged();
            });
        } else {
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), R.string.uninstall_failed, Toast.LENGTH_SHORT).show();
            });
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getData().clear();
            getData().addAll(PackageInfoManager.getInstance().getSystemApps());
            appAdapter.notifyDataSetChanged();
        }
    }
}
