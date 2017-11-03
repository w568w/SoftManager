package cn.ifreedomer.com.softmanager.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import cn.ifreedomer.com.softmanager.bean.PermissionDetail;
import cn.ifreedomer.com.softmanager.util.ShellUtils;

/**
 * @author:eavawu
 * @since: 27/10/2017.
 * TODO:
 */

public class PermissionManager {
    private Context mContext;
    private boolean mIsRequestedRoot;
    private boolean mHasRootPermission = false;
    private static PermissionManager mCleanManager = new PermissionManager();

    public static PermissionManager getInstance() {
        return mCleanManager;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public List<PermissionDetail> getAppPermissions() {
        return new ArrayList<>();
    }


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
}
