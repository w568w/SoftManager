package cn.ifreedomer.com.softmanager.fragment.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.widget.HardwareHeadView;
import preference.PreferenceFragment;

import static android.os.ParcelFileDescriptor.MODE_WORLD_READABLE;


/**
 * @author wuyihua
 * @Date 2017/10/23
 * @todo
 */

public class DeviceInfoFragment extends PreferenceFragment {
    public static final String TAG = DeviceInfoFragment.class.getSimpleName();
    RecyclerView hardwareRv;
    private HardwareHeadView headerView;

    private MultiItemTypeAdapter mMultiAdapter;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_device_info, container, false);
//        ButterKnife.inject(this, view);
//        view.setVisibility(View.VISIBLE);
//        initAdapter();
//        initHeadView();
//        return view;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
        addPreferencesFromResource(R.xml.device_preference);
    }


    private void initAdapter() {
        List list = new ArrayList();
        mMultiAdapter = new MultiItemTypeAdapter(getActivity(), list);
        hardwareRv.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    //
    private void initHeadView() {
        headerView = new HardwareHeadView(getActivity());
        HeaderAndFooterWrapper mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mMultiAdapter);
        mHeaderAndFooterWrapper.addHeaderView(headerView);
        hardwareRv.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
