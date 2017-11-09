package cn.ifreedomer.com.softmanager.bean.clean;

import android.graphics.drawable.Drawable;

import cn.ifreedomer.com.softmanager.bean.GarbageInfo;

/**
 * @author:eavawu
 * @since: 09/11/2017.
 * TODO:
 */

public class AppCacheInfo {
    private String name;
    private String path;
    private float size;
    private Drawable drawable;
    private String packageName;

    public AppCacheInfo(String name, String path, float size, Drawable drawable, String packageName) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.drawable = drawable;
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public static GarbageInfo<AppCacheInfo> create(AppCacheInfo appCacheInfo) {
        GarbageInfo<AppCacheInfo> garbageInfo = new GarbageInfo<>();
        garbageInfo.setData(appCacheInfo);
        garbageInfo.setType(GarbageInfo.TYPE_APP_CACHE);
        return garbageInfo;
    }
}
