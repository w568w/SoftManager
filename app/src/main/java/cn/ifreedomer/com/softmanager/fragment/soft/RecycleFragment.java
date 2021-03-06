package cn.ifreedomer.com.softmanager.fragment.soft;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.adapter.AppAdapter;
import cn.ifreedomer.com.softmanager.model.AppInfo;

import static cn.ifreedomer.com.softmanager.fragment.soft.SoftFragment.TAG;

/**
 * @author:eavawu
 * @date: 19/02/2017.
 * @todo:
 */

public class RecycleFragment extends Fragment {
    @InjectView(R.id.rv)
    RecyclerView rv;
    public AppAdapter appAdapter;
    private List<AppInfo> appInfoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyecle_fragment, container, false);
        ButterKnife.inject(this, view);
        initRv();
        return view;
    }

    private void initRv() {
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        appAdapter = new AppAdapter(getActivity(), R.layout.item_appinfo, appInfoList);
        rv.setAdapter(appAdapter);
    }

    public void setData(List<AppInfo> appInfos) {
        appInfoList.clear();
        appInfoList.addAll(appInfos);
        Log.e(TAG, "setData: " + appInfoList.size());
        if (rv == null || rv.getAdapter() == null) {
            return;
        }
        rv.getAdapter().notifyDataSetChanged();

    }


    public List<AppInfo> getData() {
        return appInfoList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
