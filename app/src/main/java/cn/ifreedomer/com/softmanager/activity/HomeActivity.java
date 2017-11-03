package cn.ifreedomer.com.softmanager.activity;

import android.Manifest;
import android.content.Intent;
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
import android.widget.ImageView;

import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.setting.SettingActivity;
import cn.ifreedomer.com.softmanager.fragment.clean.CleanFragment;
import cn.ifreedomer.com.softmanager.fragment.device.DeviceInfoFragment;
import cn.ifreedomer.com.softmanager.fragment.permission.PermissionFragment;
import cn.ifreedomer.com.softmanager.fragment.soft.SoftFragment;
import cn.ifreedomer.com.softmanager.util.LogUtil;

/**
 * @author HomorSmith
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final String PERMISSION_TAG = "permission";
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
    private Fragment cleanFragment;
    private Fragment permissionFragment;
    private static final String SOFT_TAG = "soft";
    public static final String HARDWARE_TAG = "hardware";
    public static final String CLEAN_TAG = "clean";
    private ImageView mSettingIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        initApp(savedInstanceState);
                    } else {
                        initApp(savedInstanceState);
                        LogUtil.e(TAG, "没有获取权限");
                    }
                });

    }


    private void initApp(Bundle savedInstanceState) {

        ButterKnife.inject(this);
        initFragments();
        initView();
    }

    private void initFragments() {
        softFragment = new SoftFragment();
        hardwareFragment = new DeviceInfoFragment();
        cleanFragment = new CleanFragment();
        permissionFragment = new PermissionFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.frame_content, softFragment, SOFT_TAG).
                add(R.id.frame_content, hardwareFragment, HARDWARE_TAG).
                add(R.id.frame_content, cleanFragment, HARDWARE_TAG).
                add(R.id.frame_content, permissionFragment, PERMISSION_TAG).
                show(softFragment).hide(hardwareFragment).hide(permissionFragment).
                hide(cleanFragment).commit();

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.soft_manager));
        }
    }

    private void initNavView() {
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        mSettingIv = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.setting_iv);
        mSettingIv.setOnClickListener(this);
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            Log.e(TAG, "onNavigationItemSelected: 0000");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(hardwareFragment);
            fragmentTransaction.hide(softFragment);
            fragmentTransaction.hide(cleanFragment);
            fragmentTransaction.hide(permissionFragment);
            switch (menuItem.getItemId()) {
                case R.id.soft:
                    fragmentTransaction.show(softFragment);
                    getSupportActionBar().setTitle(getString(R.string.soft_manager));
                    break;
                case R.id.hardware:
                    getSupportActionBar().setTitle(getString(R.string.hardware_info));
                    fragmentTransaction.show(hardwareFragment);
                    break;
                case R.id.clean:
                    getSupportActionBar().setTitle(getString(R.string.clean_garbage));
                    fragmentTransaction.show(cleanFragment);
                    break;
                case R.id.permission:
                    getSupportActionBar().setTitle(getString(R.string.permission_manager));
                    fragmentTransaction.show(permissionFragment);
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
//        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_iv:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}
