package cn.ifreedomer.com.softmanager.activity.operation;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.bean.ComponentEntity;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;

/**
 * 已经被禁止的组件
 *
 * @author eavawu
 * @since 27/12/2017.
 */

public class DisableActivity extends BaseActivity {

    @InjectView(R.id.rv)
    RecyclerView rv;
    @InjectView(R.id.pb)
    ProgressBar pb;
    @InjectView(R.id.lin_loading)
    LinearLayout linLoading;
    @InjectView(R.id.tv_no_content)
    TextView tvNoContent;
    private List<ComponentEntity> componentEntitieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receycle);
        ButterKnife.inject(this);
        initView();
        initData();
    }

    private void initData() {
        GlobalDataManager.getInstance().getThreadPool().execute(
                new Runnable() {
                    @Override
                    public void run() {

                    }
                }
        );
//        rv.setAdapter();
    }

    private void initView() {

    }


}
