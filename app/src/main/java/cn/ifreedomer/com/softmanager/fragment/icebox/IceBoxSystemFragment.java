package cn.ifreedomer.com.softmanager.fragment.icebox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.adapter.IceBoxAdapter;
import cn.ifreedomer.com.softmanager.fragment.soft.UserInstallFragment;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;

/**
 * @author:eavawu
 * @date: 18/02/2017.
 * @todo:
 */

public class IceBoxSystemFragment extends Fragment {
    private static final String TAG = UserInstallFragment.class.getSimpleName();
    @InjectView(R.id.rv)
    RecyclerView mRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_icebox_user, container, false);
        ButterKnife.inject(this, view);
        initLogic();
        return view;
    }

    private void initLogic() {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<AppInfo> userApps = PackageInfoManager.getInstance().getSystemApps();
        IceBoxAdapter iceBoxAdapter = new IceBoxAdapter(getActivity(), R.layout.item_icebox, userApps);
        mRv.setAdapter(iceBoxAdapter);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
