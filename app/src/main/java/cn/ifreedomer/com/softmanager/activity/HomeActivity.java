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
import cn.ifreedomer.com.softmanager.bean.Channel;
import cn.ifreedomer.com.softmanager.bean.RespResult;
import cn.ifreedomer.com.softmanager.bean.json.Authority;
import cn.ifreedomer.com.softmanager.db.DBActionUtils;
import cn.ifreedomer.com.softmanager.fragment.clean.CleanFragment;
import cn.ifreedomer.com.softmanager.fragment.component.ComponentFragment;
import cn.ifreedomer.com.softmanager.fragment.device.DeviceInfoFragment;
import cn.ifreedomer.com.softmanager.fragment.icebox.IceBoxFragment;
import cn.ifreedomer.com.softmanager.fragment.permission.PermissionFragment;
import cn.ifreedomer.com.softmanager.fragment.soft.SoftFragment;
import cn.ifreedomer.com.softmanager.fragment.wakeup.CutWakeupFragment;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.network.requestservice.ServiceManager;
import cn.ifreedomer.com.softmanager.util.DBUtil;
import cn.ifreedomer.com.softmanager.util.HardwareUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import io.reactivex.Observable;
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
    private ComponentFragment componentFragment;
    private int mChannelState = 0;

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
        checkChannelState();

    }


    private void loadData() {

        //拷贝数据库
        GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            DBUtil.copyDB(HomeActivity.this);
            GlobalDataManager.getInstance().setActionMap(DBActionUtils.loadActionMap(HomeActivity.this));
        });

        if (PackageInfoManager.getInstance().isLoadFinish()) {
            LogUtil.e(TAG, "if isLoadFinish");
            linLoading.setVisibility(View.GONE);
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


    public void checkChannelState() {

        Observable<RespResult<Integer>> channelObserver = ServiceManager.getChannelState(PackageInfoManager.getInstance().getVersionCode(this), PackageInfoManager.getInstance().getMetadata("UMENG_CHANNEL"));
//        channelObserver.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(integerRespResult -> {
//            LogUtil.e(TAG, integerRespResult.toString());
//            GlobalDataManager.getInstance().setChannelState(integerRespResult.getData());
//        }, throwable -> {
//            throwable.printStackTrace();
//        });

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
        Observable<RespResult<Authority>> timeObserver = ServiceManager.getTime(HardwareUtil.getImei());
        channelObserver.flatMap(integerRespResult -> {
            LogUtil.d(TAG, integerRespResult.toString());
            mChannelState = integerRespResult.getData();
            return timeObserver;
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(authorityRespResult -> {
            if (authorityRespResult.getResultCode() == RespResult.SUCCESS) {
                Authority data = authorityRespResult.getData();
                long time = data.getExpirdTime() - System.currentTimeMillis();
                LogUtil.e(TAG, "channelstate = " + mChannelState + "checkAuthority: time=" + time);
                if (mChannelState == Channel.OPEN && time < 0) {
                    GlobalDataManager.getInstance().setOpenRecharge(true);
                    mBuyId.setVisibility(View.VISIBLE);
                    showPayDialog();
                } else {
                    GlobalDataManager.getInstance().setOpenRecharge(false);
                }
            }
        }, throwable -> {
            Log.e(TAG, "checkAuthority error: " + throwable);
            throwable.printStackTrace();
        });
//        ;
//        ServiceManager.getTime(HardwareUtil.getImei()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(authorityRespResult -> {
//            if (authorityRespResult.getResultCode() == RespResult.SUCCESS) {
//                Authority data = authorityRespResult.getData();
//                long time = data.getExpirdTime() - System.currentTimeMillis();
//                LogUtil.e(TAG, "checkAuthority: time=" + time);
//                if (time < 0) {
//                    mBuyId.setVisibility(View.VISIBLE);
//                    showPayDialog();
//                }
//            }
//        }, throwable -> {
//            Log.e(TAG, "checkAuthority error: " + throwable);
//            throwable.printStackTrace();
//        });
    }


    private void initFragments() {
        softFragment = new SoftFragment();
        hardwareFragment = new DeviceInfoFragment();
        cleanFragment = new CleanFragment();
        permissionFragment = new PermissionFragment();
        iceboxFragment = new IceBoxFragment();
        cutWakeUpFragment = new CutWakeupFragment();
        componentFragment = new ComponentFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.frame_content, softFragment, SOFT_TAG).
                add(R.id.frame_content, hardwareFragment, HARDWARE_TAG).
                add(R.id.frame_content, cleanFragment, HARDWARE_TAG).
                add(R.id.frame_content, permissionFragment, PERMISSION_TAG).
                add(R.id.frame_content, iceboxFragment).
                add(R.id.frame_content, cutWakeUpFragment).
                add(R.id.frame_content, componentFragment).
                hide(componentFragment).hide(softFragment).hide(hardwareFragment).hide(permissionFragment).hide(iceboxFragment).hide(cutWakeUpFragment).

                show(cleanFragment).commitAllowingStateLoss();
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
            if (lastShowFragment != null) {
                fragmentTransaction.hide(lastShowFragment);
            }
            switch (menuItem.getItemId()) {
                case R.id.soft:
                    lastShowFragment = softFragment;
                    MobclickAgent.onEvent(HomeActivity.this, "soft");
                    getSupportActionBar().setTitle(getString(R.string.soft_manager));
                    break;
                case R.id.hardware:
                    getSupportActionBar().setTitle(getString(R.string.hardware_info));
                    MobclickAgent.onEvent(HomeActivity.this, "device");
                    lastShowFragment = hardwareFragment;
                    break;
                case R.id.clean:
                    MobclickAgent.onEvent(HomeActivity.this, "clean");

                    getSupportActionBar().setTitle(getString(R.string.clean_garbage));
                    lastShowFragment = cleanFragment;
                    break;
                case R.id.permission:
                    MobclickAgent.onEvent(HomeActivity.this, "permission");
                    if (!PermissionManager.getInstance().checkOrRequestedRootPermission()) {
                        Toast.makeText(HomeActivity.this, R.string.need_root, Toast.LENGTH_SHORT).show();
                    }
                    getSupportActionBar().setTitle(getString(R.string.permission_manager));
                    lastShowFragment = permissionFragment;

                    break;
                case R.id.icebox:
                    MobclickAgent.onEvent(HomeActivity.this, "freeze");
                    getSupportActionBar().setTitle(getString(R.string.icebox));
                    lastShowFragment = iceboxFragment;
                    break;

                case R.id.cut_wakeup:
                    MobclickAgent.onEvent(HomeActivity.this, "cut_wakeup");
                    getSupportActionBar().setTitle(getString(R.string.cut_wakeup));
                    lastShowFragment = cutWakeUpFragment;
                    break;
                case R.id.component_manager:
                    MobclickAgent.onEvent(HomeActivity.this, "component_manager");
                    getSupportActionBar().setTitle(getString(R.string.component_manager));
                    fragmentTransaction.show(componentFragment);
                    lastShowFragment = componentFragment;
                    break;
                default:
                    break;
            }
            if (lastShowFragment != null) {
                fragmentTransaction.show(lastShowFragment);
                fragmentTransaction.commit();
            }
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
