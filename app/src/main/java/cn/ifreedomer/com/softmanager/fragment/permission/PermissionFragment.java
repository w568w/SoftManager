package cn.ifreedomer.com.softmanager.fragment.permission;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.PermissionSetActivity;
import cn.ifreedomer.com.softmanager.adapter.PermissionAdater;
import cn.ifreedomer.com.softmanager.bean.PermissionDetail;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @since: 03/11/2017.
 * TODO:
 */

public class PermissionFragment extends Fragment {
    @InjectView(R.id.rv)
    RecyclerView rv;
    private PermissionAdater mPermissionAdater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permission, container, false);
        ButterKnife.inject(this, view);
        initRv();
        return view;
    }

    private void initRv() {
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
