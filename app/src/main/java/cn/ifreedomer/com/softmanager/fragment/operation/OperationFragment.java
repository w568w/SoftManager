package cn.ifreedomer.com.softmanager.fragment.operation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.operation.DisableActivity;
import cn.ifreedomer.com.softmanager.adapter.OperationAdapter;
import cn.ifreedomer.com.softmanager.bean.SettingInfo;
import cn.ifreedomer.com.softmanager.util.LogUtil;

/**
 * @author eavawu
 * @since 27/12/2017.
 */

public class OperationFragment extends Fragment {
    private static final String TAG = OperationFragment.class.getSimpleName();
    @InjectView(R.id.rv)
    RecyclerView rv;
    private List<SettingInfo> settingInfoList = new ArrayList<>();
    private OperationAdapter mSettingAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyecle_fragment, container, false);
        ButterKnife.inject(this, view);
        initView();
        initData();
        LogUtil.d(TAG, "OperationFragment");
        return view;
    }

    private void initData() {

        SettingInfo settingInfo = new SettingInfo(getString(R.string.has_disable), DisableActivity.class);
        settingInfoList.add(settingInfo);
        mSettingAdapter.notifyDataSetChanged();
        mSettingAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(getActivity(), settingInfoList.get(position).getActivity()));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });






    }

    private void initView() {
        mSettingAdapter = new OperationAdapter(getActivity(), R.layout.item_operation_info, settingInfoList);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(mSettingAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
