package cn.ifreedomer.com.softmanager.fragment.soft;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.ifreedomer.com.softmanager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.R;
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
            if (Terminal.hasRootPermission()) {
                if (Terminal.uninstallSystemApp(appInfo)) {
                    appAdapter.getData().remove(appInfo);
                    appAdapter.notifyDataSetChanged();
                }
            } else {
                Terminal.grantRoot(getActivity());
                Toast.makeText(getActivity(), getString(R.string.no_permission),Toast.LENGTH_SHORT).show();;
            }
        });
    }
}
