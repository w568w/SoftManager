package cn.ifreedomer.com.softmanager.activity.operation;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.adapter.DisableComponentAdapter;
import cn.ifreedomer.com.softmanager.bean.ComponentEntity;
import cn.ifreedomer.com.softmanager.db.DBSoftUtil;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;

/**
 * 已经被禁止的组件
 *
 * @author eavawu
 * @since 27/12/2017.
 */

public class DisableActivity extends BaseActivity {

    private static final String TAG = DisableActivity.class.getSimpleName();
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.rv)
    RecyclerView rv;
    @InjectView(R.id.pb)
    ProgressBar pb;
    @InjectView(R.id.lin_loading)
    LinearLayout linLoading;
    @InjectView(R.id.tv_no_content)
    TextView tvNoContent;
    private List<ComponentEntity> mAllComponent = new ArrayList<>();
    private DisableComponentAdapter mDisableComponentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receycle);
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            mAllComponent.clear();
            mAllComponent.addAll(DBSoftUtil.getAll());
            runOnUiThread(() ->
                    {
                        initView();
                        if (mAllComponent == null || mAllComponent.size() == 0) {
                            tvNoContent.setVisibility(View.VISIBLE);
                        } else {
                            tvNoContent.setVisibility(View.GONE);
                        }
                        mDisableComponentAdapter.notifyDataSetChanged();
                    }
            );
        });
    }

    private void initView() {
        //support toolbar
        LogUtil.d(TAG, "thread = " + Thread.currentThread().getName() + "   size = " + mAllComponent.size());
        setSupportActionBar(toolbar);
        ToolbarUtil.setTitleBarWhiteBack(this, toolbar);
        toolbar.setTitle(R.string.has_disable);

        mDisableComponentAdapter = new DisableComponentAdapter(this, R.layout.item_component_detail, mAllComponent);
        rv.setAdapter(mDisableComponentAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
