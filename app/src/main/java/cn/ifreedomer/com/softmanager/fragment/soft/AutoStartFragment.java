package cn.ifreedomer.com.softmanager.fragment.soft;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.adapter.AutoStartAdapter;
import cn.ifreedomer.com.softmanager.util.AutoStartInfo;
import cn.ifreedomer.com.softmanager.util.BootStartUtils;

/**
 * @author wuyihua
 * @Date 2017/2/22
 */

public class AutoStartFragment extends Fragment {

    @InjectView(R.id.rv_autostart)
    RecyclerView rvAutostart;
    private List<AutoStartInfo> autoStartList = new ArrayList<>();
    private AutoStartAdapter autoStartAdapter;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_autostart, container, false);
        ButterKnife.inject(this, view);
        initRv();
        initData();
        return view;
    }

    private void initRv() {
        rvAutostart.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.autoStartAdapter = new AutoStartAdapter(getActivity(), R.layout.item_autostart, autoStartList);
        rvAutostart.setAdapter(this.autoStartAdapter);
    }

    private void initData() {
        List<AutoStartInfo> autoStartInfos = BootStartUtils.fetchAutoApps(getActivity());
        autoStartList.clear();
        autoStartList.addAll(autoStartInfos);
        autoStartAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
