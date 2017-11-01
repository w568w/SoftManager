package cn.ifreedomer.com.softmanager.bean;

import android.graphics.drawable.Drawable;

/**
 * @author:eavawu
 * @since: 31/10/2017.
 * TODO:
 */

public class GarbageInfo {
    public static final int TYPE_APP_CACHE = 1;
    public static final int TYPE_SYSTEM_GARBAGE = 2;
    public static final int TYPE_AD_GARBAGE = 3;
    private String name;
    private String path;
    private float size;
    private Drawable drawable;
    private boolean isChecked;
    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public GarbageInfo(String name, String path, float size, Drawable drawable) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.drawable = drawable;
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
}
