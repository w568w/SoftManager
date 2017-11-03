package cn.ifreedomer.com.softmanager.bean;

/**
 * @author:eavawu
 * @since: 03/11/2017.
 * TODO: 权限详情
 */

public class PermissionDetail {
    private String permission;
    private int icon;
    private String permissionDes;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getPermissionDes() {
        return permissionDes;
    }

    public void setPermissionDes(String permissionDes) {
        this.permissionDes = permissionDes;
    }
}
