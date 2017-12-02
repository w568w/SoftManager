package cn.ifreedomer.com.softmanager.activity.component;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.adapter.ReceiverListAdapter;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;

public class ReceiverListActivity extends BaseActivity {

    @InjectView(R.id.rv)
    RecyclerView rv;
    public static final String APP_INFO = "appInfo";
    public static final String SHOW_TYPE = "showType";
    public static final int ACTIVITY = 1;
    public static final int RECEIVER = 2;

    public static final int SERVICE = 3;

    public static final int CONTENT_PROVIDER = 4;
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
        getSupportActionBar().setTitle(R.string.receiver);
    }

    private void initData() {
        AppInfo appInfo = (AppInfo) GlobalDataManager.getInstance().getTempMap().get(APP_INFO);
        rv.setAdapter(new ReceiverListAdapter(this, R.layout.item_receiver_detail, appInfo.getReceiverList(), appInfo));
        rv.setLayoutManager(new LinearLayoutManager(this));

    }


}
