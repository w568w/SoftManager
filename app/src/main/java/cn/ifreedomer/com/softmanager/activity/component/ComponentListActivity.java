package cn.ifreedomer.com.softmanager.activity.component;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.adapter.ComponentListAdapter;
import cn.ifreedomer.com.softmanager.bean.ComponentEntity;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;

public class ComponentListActivity extends BaseActivity {

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
    }

    private void initData() {
        AppInfo appInfo = (AppInfo) GlobalDataManager.getInstance().getTempMap().get(APP_INFO);
        int showType = (int) GlobalDataManager.getInstance().getTempMap().get(SHOW_TYPE);
        List<ComponentEntity> componentEntityList = null;
        switch (showType) {
            case ACTIVITY:
                componentEntityList = appInfo.getActivityList();
                getSupportActionBar().setTitle(R.string.activity);
                break;
            case RECEIVER:
                componentEntityList = appInfo.getReceiverList();
                getSupportActionBar().setTitle(R.string.receiver);
                break;
            case SERVICE:
                componentEntityList = appInfo.getServiceList();
                getSupportActionBar().setTitle(R.string.service);
                break;
            case CONTENT_PROVIDER:
                componentEntityList = appInfo.getContentProviderList();
                getSupportActionBar().setTitle(R.string.content_provider);
                break;
        }
        rv.setAdapter(new ComponentListAdapter(this, R.layout.item_component_detail, componentEntityList, appInfo));
        rv.setLayoutManager(new LinearLayoutManager(this));

    }


}
