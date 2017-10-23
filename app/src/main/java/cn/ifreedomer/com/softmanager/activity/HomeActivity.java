package cn.ifreedomer.com.softmanager.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;

/**
 * @author HomorSmith
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.recycleview)
    RecyclerView recycleview;
    @InjectView(R.id.ad_root_ll)
    LinearLayout adRootLl;
    @InjectView(R.id.frame_content)
    FrameLayout frameContent;
    @InjectView(R.id.navigationmenu)
    RecyclerView navigationmenu;
    @InjectView(R.id.navigation_view)
    NavigationView navigationView;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        //init drawerLayout
        initTitleBar();
        initDrawerLayout();
        initNavView();
    }

    private void initTitleBar() {
        //设置支持Toobar
        setSupportActionBar(toolbar);

    }

    private void initNavView() {

    }

    private void initDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }


//
//    private void initNavgationView() {
//        drawerLayout.set
////        View headerView = navigationView.getHeaderView(0);
////        ImageView buyIv = (ImageView) headerView.findViewById(R.id.buy_iv);
////        if (buyIv != null) {
////            buyIv.setOnClickListener(this);
////        }
////
//////        nameTv = (TextView) headerView.findViewById(R.id.login_tv);
////        ImageView settingIv = (ImageView) headerView.findViewById(R.id.setting_iv);
////        settingIv.setOnClickListener(this);
//////        nameTv.setOnClickListener(this);
////        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
////        if (navigationView != null) {
//////            setupDrawerContent(navigationView);
////        }
//    }

    @Override
    public void onClick(View v) {

    }
}
