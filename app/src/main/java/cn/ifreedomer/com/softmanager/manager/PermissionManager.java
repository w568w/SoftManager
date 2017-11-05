package cn.ifreedomer.com.softmanager.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.ifreedomer.com.softmanager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.PermissionDetail;
import cn.ifreedomer.com.softmanager.bean.PermissionGroup;
import cn.ifreedomer.com.softmanager.bean.PermissionTemp;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ShellUtils;
import cn.ifreedomer.com.softmanager.util.XmlUtil;

/**
 * @author:eavawu
 * @since: 27/10/2017.
 * TODO:
 */

public class PermissionManager {
    private static final String TAG = PermissionManager.class.getSimpleName();
    private Context mContext;
    private boolean mIsRequestedRoot;
    private boolean mHasRootPermission = false;
    private static PermissionManager mCleanManager = new PermissionManager();

    public static PermissionManager getInstance() {
        return mCleanManager;
    }

    List<PermissionGroup> mPermissionGroups;
    //permission和bean之间的映射关系
    private ConcurrentHashMap<String, PermissionDetail> mPermissionDetailMap = new ConcurrentHashMap<>();


    public void loadPermissionConfig() {
        XmlResourceParser xmlResourceParser = mContext.getApplicationContext().getResources().getXml(R.xml.permission);
        mPermissionGroups = XmlUtil.parsePermissionGroup(xmlResourceParser);
        for (int i = 0; i < mPermissionGroups.size(); i++) {
            List<PermissionDetail> permissionDetailList = mPermissionGroups.get(i).getPermissionDetailList();
            for (int j = 0; j < permissionDetailList.size(); j++) {
                mPermissionDetailMap.put(permissionDetailList.get(j).getPermission(), permissionDetailList.get(j));
            }
        }

        //加载所有app的权限
        List<AppInfo> userApps = PackageInfoManager.getInstance().getUserApps();
        for (int i = 0; i < userApps.size(); i++) {
            AppInfo appInfo = userApps.get(i);
        }

    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();

    }


    private static final Map<String, List<String>> PERMS_GROUPS = new HashMap<>();


    public boolean isRequestedRoot() {
        return mIsRequestedRoot;
    }

    public void setRequestedRoot(boolean isRequestedRoot) {
        this.mIsRequestedRoot = isRequestedRoot;
    }

    /**
     * 检查或者请求root权限
     *
     * @return 是否获取到了root
     */
    public boolean checkOrRequestedRootPermission() {
        if (!mIsRequestedRoot) {
            mIsRequestedRoot = true;
            //申请root
            mHasRootPermission = ShellUtils.checkRootPermission();
        }
        return mHasRootPermission;
    }

    public void startSetting(Activity activity, String packageName) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", packageName, null));
        activity.startActivity(localIntent);
    }


    @Nullable
    public static PermissionInfo getPermissionInfo(@NonNull Context context, @NonNull String permission) {
        PermissionInfo permissionInfo = new PermissionInfo();
        try {
            LogUtil.e(TAG, permission);
            return context.getPackageManager().getPermissionInfo(permission, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<PermissionDetail> getAllPermission(String packageName) {

        List<PermissionDetail> permissionDetailList = new ArrayList<>();
        try {
            List<PermissionTemp> permissions = getPermissions(packageName);
            if (permissions == null) {
                return null;
            }
            LogUtil.e(TAG, permissions.toString());
            for (int i = 0; i < permissions.size(); i++) {
                PermissionTemp permissionTemp = permissions.get(i);
                if (!permissionTemp.getPermission().startsWith("android") || permissionTemp.getPermission().equals("android.permission.RECEIVE_BOOT_COMPLETED")) {
//                    LogUtil.e(TAG, "permission=" + permission);
                    continue;
                }


                PermissionDetail permissionDetail = mPermissionDetailMap.get(permissionTemp.getPermission());
                if (permissionDetail == null) {
                    LogUtil.e(TAG, packageName + "   permission is not support=" + permissionTemp.getPermission());
                    continue;
                }
                permissionDetail = (PermissionDetail) mPermissionDetailMap.get(permissionTemp.getPermission()).clone();
                permissionDetail.setGranted(permissionTemp.isGranted());
                permissionDetailList.add(permissionDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return permissionDetailList;
    }


    public List<PermissionTemp> getPermissions(final String appPackage) {
        PermissionTemp permissionTemp = new PermissionTemp();
        PackageManager packageManager = mContext.getPackageManager();
        List<PermissionTemp> granted = new ArrayList<>();
        try {
            PackageInfo pi = packageManager.getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            if (pi.requestedPermissions == null) {
                return null;
            }
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if (!TextUtils.isEmpty(pi.requestedPermissions[i])) {
                    permissionTemp = new PermissionTemp();
                    permissionTemp.setPermission(pi.requestedPermissions[i]);
                    permissionTemp.setGranted(pi.requestedPermissionsFlags[i] != 0 && PackageInfo.REQUESTED_PERMISSION_GRANTED != 0);
                    granted.add(permissionTemp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return granted;
    }


    public void grantPermission(String packageName, String permission) {
        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                ShellUtils.CommandResult commandResult = ShellUtils.grantPermission(packageName, permission);
                LogUtil.e(TAG,commandResult.toString());
            }
        });

    }

    public void revokePermission(String packageName, String permission) {
        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                ShellUtils.CommandResult commandResult = ShellUtils.revokePermission(packageName, permission);
                LogUtil.e(TAG,commandResult.toString());

            }
        });

    }

}
