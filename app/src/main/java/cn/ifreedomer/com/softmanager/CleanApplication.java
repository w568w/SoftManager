package cn.ifreedomer.com.softmanager;

import android.app.Application;

/**
 * @author:eavawu
 * @since: 01/11/2017.
 * TODO:
 */

public class CleanApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PackageInfoManager.getInstance().init(this);
    }
}
