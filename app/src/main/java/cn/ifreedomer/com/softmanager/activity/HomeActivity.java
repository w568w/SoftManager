package cn.ifreedomer.com.softmanager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.LoadStateCallback;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.setting.SettingActivity;
import cn.ifreedomer.com.softmanager.bean.RespResult;
import cn.ifreedomer.com.softmanager.bean.json.Authority;
import cn.ifreedomer.com.softmanager.fragment.clean.CleanFragment;
import cn.ifreedomer.com.softmanager.fragment.device.DeviceInfoFragment;
import cn.ifreedomer.com.softmanager.fragment.icebox.IceBoxFragment;
import cn.ifreedomer.com.softmanager.fragment.permission.PermissionFragment;
import cn.ifreedomer.com.softmanager.fragment.soft.SoftFragment;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.fragment.wakeup.CutWakeupFragment;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.network.requestservice.ServiceManager;
import cn.ifreedomer.com.softmanager.util.HardwareUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.widget.PayDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    @InjectView(R.id.pb)
    ProgressBar pb;
    @InjectView(R.id.lin_loading)
    LinearLayout linLoading;
    private Fragment softFragment;
    private Fragment hardwareFragment;
    private Fragment cleanFragment;
    private Fragment permissionFragment;
    private static final String SOFT_TAG = "soft";
    public static final String HARDWARE_TAG = "hardware";
    public static final String CLEAN_TAG = "clean";
    private ImageView mSettingIv;
    private Fragment iceboxFragment;
    private ImageView mBuyId;
    private Fragment lastShowFragment;
    private RxPermissions mRxPermissions;
    private Fragment cutWakeUpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        initView();

        mRxPermissions = new RxPermissions(this);
        mRxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        loadData();
                    } else {
                        loadData();
                        LogUtil.e(TAG, "没有获取权限");
                    }
                });

    }


    private void refreView() {
        initFragments();
        checkAuthority();

    }


    private void loadData() {

        if (PackageInfoManager.getInstance().isLoadFinish()) {
            refreView();
            return;
        }
        linLoading.setVisibility(View.VISIBLE);
        LogUtil.e(TAG, "loadFinish before");

        PackageInfoManager.getInstance().addLoadStateCallback(new LoadStateCallback() {
            @Override
            public void loadBegin() {
            }

            @Override
            public void loadProgress(int current, int max) {
            }

            @Override
            public void loadFinish() {
                LogUtil.e(TAG, "loadFinish");
                linLoading.setVisibility(View.GONE);
                refreView();
            }
        });
    }


    public void checkAuthority() {

//        if (authorityRespResult.getResultCode() == RespResult.SUCCESS) {
//            PayDialog payDialog = new PayDialog(HomeActivity.this);
//            payDialog.show();
//        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            mRxPermissions
                    .request(Manifest.permission.READ_PHONE_STATE)
                    .subscribe(granted -> {
                        if (!granted) {
                            Toast.makeText(HomeActivity.this, R.string.device_id_request, Toast.LENGTH_SHORT).show();
                        }
                    });
            return;
        }
        ServiceManager.getTime(HardwareUtil.getImei()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(authorityRespResult -> {
            if (authorityRespResult.getResultCode() == RespResult.SUCCESS) {
                Authority data = authorityRespResult.getData();
                long time = data.getExpirdTime() - System.currentTimeMillis();
                LogUtil.e(TAG, "checkAuthority: time=" + time);
                if (data.getExpirdTime() < System.currentTimeMillis()) {
                    mBuyId.setVisibility(View.VISIBLE);
                    showPayDialog();
                }
            }
        }, throwable -> {
            Log.e(TAG, "checkAuthority error: " + throwable);
            throwable.printStackTrace();
        });
    }

    private void showPayDialog() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                            if (!granted) {
                                Toast.makeText(this, "充值需要以设备Id作为凭证.请授权避免充值无效哦", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            PayDialog payDialog = new PayDialog(HomeActivity.this);
                            payDialog.showPay();
                        }
                );

    }

    private void initFragments() {
        softFragment = new SoftFragment();
        hardwareFragment = new DeviceInfoFragment();
        cleanFragment = new CleanFragment();
        permissionFragment = new PermissionFragment();
        iceboxFragment = new IceBoxFragment();
        cutWakeUpFragment = new CutWakeupFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.frame_content, softFragment, SOFT_TAG).
                add(R.id.frame_content, hardwareFragment, HARDWARE_TAG).
                add(R.id.frame_content, cleanFragment, HARDWARE_TAG).
                add(R.id.frame_content, permissionFragment, PERMISSION_TAG).
                add(R.id.frame_content, iceboxFragment).
                add(R.id.frame_content, cutWakeUpFragment).
                hide(softFragment).hide(hardwareFragment).hide(permissionFragment).hide(iceboxFragment).hide(cutWakeUpFragment).
                show(cleanFragment).commit();
        lastShowFragment = cleanFragment;

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
            getSupportActionBar().setTitle(getString(R.string.clean_garbage));
        }
    }

    private void initNavView() {
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
        mSettingIv = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.setting_iv);
        mBuyId = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.buy_iv);
        mSettingIv.setOnClickListener(this);
        mBuyId.setOnClickListener(this);

    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            Log.e(TAG, "onNavigationItemSelected: 0000");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(lastShowFragment);
            switch (menuItem.getItemId()) {
                case R.id.soft:
                    fragmentTransaction.show(softFragment);
                    lastShowFragment = softFragment;
                    MobclickAgent.onEvent(HomeActivity.this, "soft");
                    getSupportActionBar().setTitle(getString(R.string.soft_manager));
                    break;
                case R.id.hardware:
                    getSupportActionBar().setTitle(getString(R.string.hardware_info));
                    MobclickAgent.onEvent(HomeActivity.this, "device");
                    fragmentTransaction.show(hardwareFragment);
                    lastShowFragment = hardwareFragment;
                    break;
                case R.id.clean:
                    MobclickAgent.onEvent(HomeActivity.this, "clean");

                    getSupportActionBar().setTitle(getString(R.string.clean_garbage));
                    fragmentTransaction.show(cleanFragment);
                    lastShowFragment = cleanFragment;
                    break;
                case R.id.permission:
                    MobclickAgent.onEvent(HomeActivity.this, "permission");
                    if (!PermissionManager.getInstance().checkOrRequestedRootPermission()) {
                        Toast.makeText(HomeActivity.this, R.string.need_root, Toast.LENGTH_SHORT).show();
                    }
                    getSupportActionBar().setTitle(getString(R.string.permission_manager));
                    fragmentTransaction.show(permissionFragment);
                    lastShowFragment = permissionFragment;

                    break;
                case R.id.icebox:
                    MobclickAgent.onEvent(HomeActivity.this, "freeze");
                    getSupportActionBar().setTitle(getString(R.string.icebox));
                    fragmentTransaction.show(iceboxFragment);
                    lastShowFragment = iceboxFragment;
                    break;

                case R.id.cut_wakeup:
                    MobclickAgent.onEvent(HomeActivity.this, "cut wakeup");
                    getSupportActionBar().setTitle(getString(R.string.cut_wakeup));
                    fragmentTransaction.show(cutWakeUpFragment);
                    lastShowFragment = cutWakeUpFragment;
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
            case R.id.buy_iv:
                showPayDialog();
                break;
        }
    }


    boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.e(TAG + "======", "keyback====");
            if (!isExit) {
                isExit = true;
                Toast.makeText(this, R.string.exit_str, Toast.LENGTH_SHORT)
                        .show();
                new Handler().postDelayed(() -> isExit = false, 2000);
                return false;

            } else {
                Log.e(TAG + "======", "退出应用");
                Process.killProcess(Process.myPid());
                System.exit(0);
                //
            }
        }

        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
