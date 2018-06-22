package cn.ifreedomer.com.softmanager.fragment.soft;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.HomeActivity;
import cn.ifreedomer.com.softmanager.adapter.BackupAdapter;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.DialogUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;

/**
 * @author wuyihua
 * @Date 2017/2/22
 */

public class BackupFragment extends Fragment {
    private static final String TAG = BackupFragment.class.getSimpleName();
    RecyclerView rv;
    TextView tvNoContent;
    ProgressBar pb;
    public LinearLayout linLoading;
    private List<AppInfo> backupList = new ArrayList<>();
    private BackupAdapter backApdater;
    public TextView loadTv;
    private HomeActivity homeActivity;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receycle, container, false);
        rv = (RecyclerView) view.findViewById(R.id.rv);
        tvNoContent = (TextView) view.findViewById(R.id.tv_no_content);
        linLoading = (LinearLayout) view.findViewById(R.id.lin_loading);
        loadTv = (TextView) view.findViewById(R.id.load_tv);
        initRv();
        initData();
        return view;
    }

    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.backApdater = new BackupAdapter(getActivity(), R.layout.item_backup, backupList);
        this.backApdater.setBackupFragment(this);
        rv.setAdapter(this.backApdater);
        homeActivity = (HomeActivity) getActivity();

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
            homeActivity.toolbar.inflateMenu(R.menu.clear_backup);
            homeActivity.toolbar.getMenu().findItem(R.id.item_delete).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    DialogUtil.showConfirmDialog(getActivity(), getString(R.string.tip), getString(R.string.confirm_clean_tip), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            linLoading.setVisibility(View.VISIBLE);
                            loadTv.setText(R.string.cleaning);
                            LogUtil.d(TAG, "delete before");
                            if (backupList==null){
                                Toast.makeText(getActivity(), R.string.not_finish,Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for (int i = 0; i < backupList.size(); i++) {
                                if (backupList.get(i) == null) {
                                    continue;
                                }
                                File file = new File(backupList.get(i).getBackupPath());
                                boolean delete = file.delete();
                                LogUtil.d(TAG, file.getAbsolutePath() + "  delete state = " + delete);
                            }
                            LogUtil.d(TAG, "delete after");

                            getActivity().runOnUiThread(() -> {
                                backupList.clear();
                                PackageInfoManager.getInstance().getBackupList().clear();
                                backApdater.notifyDataSetChanged();
                                linLoading.setVisibility(View.GONE);
                            });
                        }
                    });
//                    new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("确定要清空回收站吗?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            linLoading.setVisibility(View.VISIBLE);
//                            loadTv.setText("正在清空回收站,请稍等...");
//                            GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
//                                @Override
//                                public void run() {
//                                    for (int i = 0; i < backupList.size(); i++) {
//                                        File file = new File(backupList.get(i).getBackupPath());
//                                        file.delete();
//                                    }
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            linLoading.setVisibility(View.GONE);
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    });
                    return false;
                }
            });
        } else {
            if (homeActivity != null && homeActivity.toolbar != null) {
                homeActivity.toolbar.getMenu().clear();
            }

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
