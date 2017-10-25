package cn.ifreedomer.com.softmanager.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.fragment.device.DeviceInfoFragment;
import cn.ifreedomer.com.softmanager.fragment.soft.SoftFragment;

/**
 * @author HomorSmith
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.frame_content)
    FrameLayout frameContent;
    @InjectView(R.id.navigation_view)
    NavigationView navigationView;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private Fragment softFragment;
    private Fragment hardwareFragment;
    private static final String SOFT_TAG = "soft";
    public static final String HARDWARE_TAG = "hardware";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        initFragments();
        initView();

    }

    private void initFragments() {
        softFragment = new SoftFragment();
        hardwareFragment = new DeviceInfoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_content,softFragment,SOFT_TAG).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_content,hardwareFragment,HARDWARE_TAG).commit();

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
        toolbar.setTitleTextColor(getResources().getColor(R.color.whiteColor));
//        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.menu);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void initNavView() {
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().show(softFragment).hide(hardwareFragment).commit();
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            Log.e(TAG, "onNavigationItemSelected: 0000" );
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.soft:
                    fragmentTransaction.hide(hardwareFragment);
                    fragmentTransaction.show(softFragment);
                    break;
                case R.id.hardware:
                    Log.e(TAG, "onNavigationItemSelected: 1111");
                    fragmentTransaction.hide(softFragment);
                    fragmentTransaction.show(hardwareFragment);
                    break;
                default:
                    break;
            }
            fragmentTransaction.commit();
            drawerLayout.closeDrawers();
            return false;
        }
    };


    private void initDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}
