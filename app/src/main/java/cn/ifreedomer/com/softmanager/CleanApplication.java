package cn.ifreedomer.com.softmanager;

import android.app.Application;

import cn.ifreedomer.com.softmanager.manager.PermissionManager;

/**
 * @author:eavawu
 * @since: 01/11/2017.
 * TODO:
 */

public class CleanApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //需要先初始化permission
        PermissionManager.getInstance().init(this);
        PackageInfoManager.getInstance().init(this);

    }
}
