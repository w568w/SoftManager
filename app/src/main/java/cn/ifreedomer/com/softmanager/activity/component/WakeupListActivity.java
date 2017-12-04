package cn.ifreedomer.com.softmanager.activity.component;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.adapter.WakeupListAdapter;
import cn.ifreedomer.com.softmanager.fragment.wakeup.CutWakeupFragment;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.model.WakeupPathInfo;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;

public class WakeupListActivity extends BaseActivity {

    @InjectView(R.id.rv)
    RecyclerView rv;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.inject(this);
        initTitleBar();
        initData();
    }

    private void initTitleBar() {
        ToolbarUtil.setTitleBarWhiteBack(this, toolbar);
        getSupportActionBar().setTitle((String)GlobalDataManager.getInstance().getTempMap().get(CutWakeupFragment.WAKEUP_ACTION));
    }

    private void initData() {
        WakeupPathInfo wakeupPathInfo = (WakeupPathInfo) GlobalDataManager.getInstance().getTempMap().get(CutWakeupFragment.WAKEUP_PATH);
        rv.setAdapter(new WakeupListAdapter(this, R.layout.item_wakeup, wakeupPathInfo.getWakeupPath(), wakeupPathInfo.getComponentEntityList()));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }


}
