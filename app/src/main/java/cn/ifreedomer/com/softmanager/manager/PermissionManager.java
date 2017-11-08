package cn.ifreedomer.com.softmanager.manager;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.zzzmode.appopsx.common.OpEntry;
import com.zzzmode.appopsx.common.OpsResult;
import com.zzzmode.appopsx.common.PackageOps;
import com.zzzmode.appopsx.common.ReflectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.ifreedomer.com.softmanager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.bean.PermissionDetail;
import cn.ifreedomer.com.softmanager.bean.PermissionGroup;
import cn.ifreedomer.com.softmanager.util.AppOpsx;
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
    private static final int GRAINTED = 0;
    private Context mContext;
    private boolean mIsRequestedRoot;
    private boolean mHasRootPermission = false;
    private static PermissionManager mCleanManager = new PermissionManager();

    public static PermissionManager getInstance() {
        return mCleanManager;
    }

    private List<PermissionGroup> mPermissionGroups;
    //permission和PermissionDetail之间的映射关系
    private ConcurrentHashMap<String, PermissionDetail> mPermissionDetailMap = new ConcurrentHashMap<>();
    private int[] ALL_PERMISSION_OPS = {2, 11, 12, 15, 22, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 41, 42, 44, 45,
            46, 47, 48, 49, 50, 58, 61, 63, 65, 69};


    //对应包名包含的所有权限
    private ConcurrentHashMap<String, List<PermissionDetail>> mAllAppPermissionMap = new ConcurrentHashMap<>();


    public void loadPermissionConfig() {
        XmlResourceParser xmlResourceParser = mContext.getApplicationContext().getResources().getXml(R.xml.permission);
        mPermissionGroups = XmlUtil.parsePermissionGroup(xmlResourceParser);
        for (int i = 0; i < mPermissionGroups.size(); i++) {
            List<PermissionDetail> permissionDetailList = mPermissionGroups.get(i).getPermissionDetailList();
            for (int j = 0; j < permissionDetailList.size(); j++) {
                mPermissionDetailMap.put(permissionDetailList.get(j).getPermission(), permissionDetailList.get(j));
            }
        }

    }


    public ConcurrentHashMap<String, PermissionDetail> getPermissionDetailMap() {
        return mPermissionDetailMap;
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


    public void grantPermission(String packageName, String permission) {
        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                ShellUtils.CommandResult commandResult = ShellUtils.grantPermission(packageName, permission);
                LogUtil.e(TAG, commandResult.toString());
            }
        });

    }

    public void revokePermission(String packageName, String permission) {
        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                ShellUtils.CommandResult commandResult = ShellUtils.revokePermission(packageName, permission);
                LogUtil.e(TAG, commandResult.toString());

            }
        });

    }


    public OpsResult setMode(Context context, String pkgName, int op, int mode) {
        try {
            return AppOpsx.getInstance(context)
                    .setOpsMode(pkgName, op, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public OpsResult setMode(Context context, String pkgName, int op, boolean isAllowed) {
        int mode;
        if (isAllowed) {
            mode = AppOpsManager.MODE_ALLOWED;
        } else {
            mode = AppOpsManager.MODE_IGNORED;
        }
        try {
            return setMode(context, pkgName, op, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<PermissionDetail> getAppPermission(String pkgName) {

        return mAllAppPermissionMap.get(pkgName);
    }


    public void loadAllPermission() {
        try {
            OpsResult allOpsResult = AppOpsx.getInstance(mContext).getPackagesForOps(ALL_PERMISSION_OPS, false);
            for (PackageOps pkgOps : allOpsResult.getList()) {
                List<OpEntry> ops = pkgOps.getOps();
                List<PermissionDetail> appPermissionlist = new ArrayList<>();
                for (int i = 0; i < ops.size(); i++) {
                    if (getPermissionByOp(ops.get(i).getOp()) == null) {
                        LogUtil.e(TAG, "package name=" + pkgOps.getPackageName() + "  op=" + ops.get(i).getOp() + " is not support");
                        continue;
                    }
                    PermissionDetail permissionDetail = getPermissionByOp(ops.get(i).getOp());
                    permissionDetail.setGranted(ops.get(i).getMode() == GRAINTED);
                    appPermissionlist.add(permissionDetail);
                }
                mAllAppPermissionMap.put(pkgOps.getPackageName(), appPermissionlist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据op的数值来获取权限
     *
     * @param op appOps的一个常量值
     * @return op对应的权限信息
     */
    public PermissionDetail getPermissionByOp(int op) {
        String permissionName = (String) ReflectUtils.getArrayFieldValue(AppOpsManager.class, "sOpPerms", op);
//        LogUtil.e(TAG, "getPermissionByOp mPermissionDetailMap=" + mPermissionDetailMap + "  permissionName=" + permissionName);
        if (!TextUtils.isEmpty(permissionName) && mPermissionDetailMap.containsKey(permissionName)) {
            return PermissionManager.getInstance().getPermissionDetailMap().get(permissionName);
        }
        return null;
    }

    /**
     * 根据权限的名字获取op
     *
     * @param permission 权限的名字
     * @return
     */
    public int getOpByPermission(String permission) {
        Object[] sOpPerms = ReflectUtils.getArrayField(AppOpsManager.class, "sOpPerms");
        for (int i = 0; i < sOpPerms.length; i++) {
            if (!TextUtils.isEmpty(permission) && sOpPerms[i] != null) {
                if (sOpPerms[i].equals(permission)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 设置权限
     *
     * @param context    context
     * @param pkgName    包名
     * @param permission 权限名称
     * @param isAllowed  是否允许
     * @return
     */

    public OpsResult setPermission(Context context, String pkgName, String permission, boolean isAllowed) {
        int op = getOpByPermission(permission);
        return setMode(context, pkgName, op, isAllowed);
    }


}
