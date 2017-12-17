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
                    boolean b = Terminal.uninstallSystemApp(appInfo);

                    if (b){
                        appAdapter.getDatas().remove(appInfo);
                        getActivity().runOnUiThread(() -> appAdapter.notifyDataSetChanged());
                    }else{
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getActivity(), R.string.uninstall_failed,Toast.LENGTH_SHORT).show();
                        });
                    }
                });

            } else {
                Terminal.grantRoot(getActivity());
                Toast.makeText(getActivity(), getString(R.string.no_permission),Toast.LENGTH_SHORT).show();;
            }
        });
    }
}
