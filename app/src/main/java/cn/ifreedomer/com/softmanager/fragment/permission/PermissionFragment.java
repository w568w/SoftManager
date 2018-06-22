package cn.ifreedomer.com.softmanager.fragment.permission;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.PermissionSetActivity;
import cn.ifreedomer.com.softmanager.adapter.PermissionAdater;
import cn.ifreedomer.com.softmanager.bean.PermissionDetail;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @since: 03/11/2017.
 * TODO:
 */

public class PermissionFragment extends Fragment {
    @InjectView(R.id.rv)
    RecyclerView rv;
    @InjectView(R.id.pb)
    ProgressBar pb;
    @InjectView(R.id.tv_progress)
    TextView tvProgress;
    @InjectView(R.id.lin_loading)
    LinearLayout linLoading;
    private PermissionAdater mPermissionAdater;
    private static final int LOAD_SUCCESS = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_SUCCESS:
                    initRv();
                    linLoading.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permission, container, false);
        ButterKnife.inject(this, view);
        loadDataAsync();
        return view;
    }

    private void loadDataAsync() {
        //加载权限
        linLoading.setVisibility(View.VISIBLE);
        GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            PermissionManager.getInstance().loadPermissionConfig();
            PermissionManager.getInstance().loadAllPermission();
            for (int i = 0; i < PackageInfoManager.getInstance().getAllApp().size(); i++) {
                AppInfo appInfo = PackageInfoManager.getInstance().getAllApp().get(i);
                appInfo.setPermissionDetailList(PermissionManager.getInstance().getAppPermission(appInfo.getPackname()));
            }
            mHandler.sendEmptyMessage(LOAD_SUCCESS);
        });

    }

    private void initRv() {
        if (rv == null) {
            return;
        }
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<AppInfo> userApps = PackageInfoManager.getInstance().getUserApps();
        mPermissionAdater = new PermissionAdater(getActivity(), R.layout.item_user_app, PackageInfoManager.getInstance().getUserApps());
        rv.setAdapter(mPermissionAdater);
        mPermissionAdater.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                if (PermissionManager.getInstance().checkOrRequestedRootPermission()) {
                List<PermissionDetail> permissionDetailList = userApps.get(position).getPermissionDetailList();
                if (permissionDetailList == null || permissionDetailList.size() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.no_permission_see), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), PermissionSetActivity.class);
                intent.putExtra("packageName", userApps.get(position).getPackname());
                startActivity(intent);
//                } else {
//                    PermissionManager.getInstance().startSetting(getActivity(),userApps.get(position).getPackname());
//                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
