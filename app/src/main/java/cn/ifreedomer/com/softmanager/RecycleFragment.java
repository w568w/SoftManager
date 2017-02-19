package cn.ifreedomer.com.softmanager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author:eavawu
 * @date: 19/02/2017.
 * @todo:
 */

public class RecycleFragment extends Fragment {
    @InjectView(R.id.rv)
    RecyclerView rv;
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
        rv.setAdapter(new AppAdapter(getActivity(),R.layout.item_appinfo,appInfoList));
    }

    public void setData(List<AppInfo> appInfos) {
        appInfoList.clear();
        appInfoList.addAll(appInfos);
        if (rv!=null&&rv.getAdapter()!=null){
            rv.getAdapter().notifyDataSetChanged();
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
