package cn.ifreedomer.com.softmanager.bean.clean;

import android.graphics.drawable.Drawable;

/**
 * @author wuyihua
 * @since 2018/6/21
 */

public class AppCacheItem extends BaseGarbage {
    private String path;
    private String pkgName;
    private Drawable appIcon;
    private String appName;

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
