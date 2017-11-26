package cn.ifreedomer.com.softmanager;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;

/**
 * @author:eavawu
 * @since: 01/11/2017.
 * TODO:
 */

public class CleanApplication extends Application {
    private static final String TAG = CleanApplication.class.getSimpleName();
    public static Context INSTANCE = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //需要先初始化permission
        Log.e(TAG, "onCreate: ");
        INSTANCE = this;
        PermissionManager.getInstance().init(this);
        PackageInfoManager.getInstance().init(this);
        PackageInfoManager.getInstance().loadData(new LoadStateCallback() {
            @Override
            public void loadBegin() {

            }

            @Override
            public void loadProgress(int current, int max) {

            }

            @Override
            public void loadFinish() {

            }
        });

    }
}
