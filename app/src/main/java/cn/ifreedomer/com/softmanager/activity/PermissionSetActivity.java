package cn.ifreedomer.com.softmanager.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.PermissionDetail;
import cn.ifreedomer.com.softmanager.bean.PermissionTitle;
import cn.ifreedomer.com.softmanager.bean.PermissionWrap;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;
import cn.ifreedomer.com.softmanager.widget.permission.PermissionDetailItemDelegate;
import cn.ifreedomer.com.softmanager.widget.permission.PermissionTitleItemDelegate;

/**
 * @author:eavawu
 * @since: 03/11/2017.
 * TODO:设置权限的activity
 */

public class PermissionSetActivity extends BaseActivity {
    private static final String TAG = PermissionSetActivity.class.getSimpleName();
    @InjectView(R.id.rv)
    RecyclerView rv;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    private List<PermissionDetail> permissionDetailList = new ArrayList<>();
    private List<AppInfo> mUserApps;
    private AppInfo mCurAppInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions_set);
        ButterKnife.inject(this);
        initView();
    }


    private void initView() {
        rv.setLayoutManager(new LinearLayoutManager(this));
        String packageName = getIntent().getStringExtra("packageName");
        mUserApps = PackageInfoManager.getInstance().getUserApps();
        mCurAppInfo = null;
        for (int i = 0; i < mUserApps.size(); i++) {
            AppInfo appInfo = mUserApps.get(i);
            if (appInfo.getPackname().equals(packageName)) {
                mCurAppInfo = appInfo;
                break;
            }
        }


        List<PermissionWrap> allPermissionWrap = new ArrayList<>();

        List<PermissionWrap> feePermissionWrap = getPermissionListByGroup(getString(R.string.fee));
        if (feePermissionWrap != null) {
            allPermissionWrap.addAll(feePermissionWrap);
        }

        PermissionTitle permissionTitle;
        List<PermissionWrap> privacyPermissionWrap = getPermissionListByGroup(getString(R.string.privacy));
        if (privacyPermissionWrap != null && privacyPermissionWrap.size() > 0) {
            permissionTitle = (PermissionTitle) privacyPermissionWrap.get(0).getData();
            permissionTitle.setShowPadding(true);
            allPermissionWrap.addAll(privacyPermissionWrap);
        }


        List<PermissionWrap> mediaPermissionWrapList = getPermissionListByGroup(getString(R.string.media_about));
        if (mediaPermissionWrapList != null && mediaPermissionWrapList.size() > 0) {
            permissionTitle = (PermissionTitle) mediaPermissionWrapList.get(0).getData();
            permissionTitle.setShowPadding(true);
            allPermissionWrap.addAll(mediaPermissionWrapList);
        }

        List<PermissionWrap> settingPermissionWrap = getPermissionListByGroup(getString(R.string.setting_about));
        if (settingPermissionWrap != null && settingPermissionWrap.size() > 0) {
            permissionTitle = (PermissionTitle) settingPermissionWrap.get(0).getData();
            permissionTitle.setShowPadding(true);
            allPermissionWrap.addAll(settingPermissionWrap);
        }


//        LogUtil.e(TAG, "allPermision size=" + mCurAppInfo.getPermissionDetailList().size());


        MultiItemTypeAdapter mMultiAdapter = new MultiItemTypeAdapter<>(this, allPermissionWrap);
        mMultiAdapter.addItemViewDelegate(new PermissionDetailItemDelegate(this, mCurAppInfo));
        mMultiAdapter.addItemViewDelegate(new PermissionTitleItemDelegate());

        rv.setAdapter(mMultiAdapter);
        ToolbarUtil.setTitleBarWhiteBack(this, mToolbar);
        getSupportActionBar().setTitle(getString(R.string.permission_manager));
    }


    private List<PermissionWrap> getPermissionListByGroup(String group) {
        List<PermissionWrap> permissionWrapList = new ArrayList<>();
        List<PermissionDetail> permissionDetailList = mCurAppInfo.getPermissionDetailList();
        for (int i = 0; i < mCurAppInfo.getPermissionDetailList().size(); i++) {
            PermissionDetail permissionDetail = permissionDetailList.get(i);
            if (permissionDetail.getGroup().equals(group)) {
                PermissionWrap<PermissionDetail> permissionWrap = new PermissionWrap();
                permissionWrap.setData(permissionDetail);
                permissionWrap.setType(PermissionWrap.PERMISSION_TYPE);
                permissionWrapList.add(permissionWrap);
            }
        }
//        LogUtil.e(TAG, "group=" + group + "    size=" + permissionWrapList.size());

        if (permissionWrapList.size() > 0) {
            PermissionWrap<PermissionTitle> permissionTitlePermissionWrap = new PermissionWrap<>();
            permissionTitlePermissionWrap.setType(PermissionWrap.TITLE_TYPE);
            PermissionTitle permissionTitle = new PermissionTitle(group);
            permissionTitlePermissionWrap.setData(permissionTitle);
            permissionWrapList.add(0, permissionTitlePermissionWrap);
        }
        return permissionWrapList;
    }
}
