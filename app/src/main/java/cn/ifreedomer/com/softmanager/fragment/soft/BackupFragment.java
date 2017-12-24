package cn.ifreedomer.com.softmanager.fragment.soft;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.adapter.BackupAdapter;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author wuyihua
 * @Date 2017/2/22
 */

public class BackupFragment extends Fragment {

    RecyclerView rv;
    TextView tvNoContent;
    ProgressBar pb;
    LinearLayout linLoading;
    private List<AppInfo> backupList = new ArrayList<>();
    private BackupAdapter backApdater;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receycle, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        tvNoContent = (TextView) view.findViewById(R.id.tv_no_content);
//        linLoading = (LinearLayout) view.findViewById(R.id.lin_loading);
        initRv();
        initData();
        return view;
    }

    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.backApdater = new BackupAdapter(getActivity(), R.layout.item_backup, backupList);
        rv.setAdapter(this.backApdater);
    }

    private void initData() {

//        linLoading.setVisibility(View.VISIBLE);
        GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            GlobalDataManager.getInstance().getThreadPool().execute(() -> {
                PackageInfoManager.getInstance().loadBackup();
                getActivity().runOnUiThread(() -> {
//                linLoading.setVisibility(View.GONE);
                    if (backupList.size() == 0) {
                        tvNoContent.setVisibility(View.VISIBLE);
                    }
                    backApdater.notifyDataSetChanged();
                });
            });


        });
//        List<AutoStartInfo> autoStartInfos = BootStartUtils.fetchAutoApps(getActivity());
//        backupList.clear();
//        backupList.addAll(autoStartInfos);
//        backApdater.notifyDataSetChanged();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (PackageInfoManager.getInstance().getBackupList().size() > 0) {
                backupList.clear();
                backupList.addAll(PackageInfoManager.getInstance().getBackupList());
                tvNoContent.setVisibility(View.GONE);
                backApdater.notifyDataSetChanged();
            } else {
                tvNoContent.setVisibility(View.VISIBLE);

            }
            //相当于Fragment的onResume
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
