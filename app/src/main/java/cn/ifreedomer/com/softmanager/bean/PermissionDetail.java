package cn.ifreedomer.com.softmanager.bean;

import android.graphics.drawable.Drawable;

/**
 * @author:eavawu
 * @since: 03/11/2017.
 * TODO: 权限详情
 */

public class PermissionDetail implements Cloneable {
    private String permission;
    private Drawable icon;
    private String permissionDes;
    private String title;
    private String protectLevel;
    private String group;
    private boolean isGranted = false;

    public String getGroup() {
        return group;
    }

    public boolean isGranted() {
        return isGranted;
    }

    public void setGranted(boolean granted) {
        isGranted = granted;
    }

    public String getTitle() {
        return title;
    }

    public String getProtectLevel() {
        return protectLevel;
    }

    public void setProtectLevel(String protectLevel) {
        this.protectLevel = protectLevel;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPermissionDes() {
        return permissionDes;
    }

    public void setPermissionDes(String permissionDes) {
        this.permissionDes = permissionDes;
    }

    @Override
    public String toString() {
        return "PermissionDetail{" +
                "permission='" + permission + '\'' +
                ", icon=" + icon +
                ", permissionDes='" + permissionDes + '\'' +
                ", name='" + title + '\'' +
                ", protectLevel='" + protectLevel + '\'' +
                ", group='" + group + '\'' +
                '}';
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
