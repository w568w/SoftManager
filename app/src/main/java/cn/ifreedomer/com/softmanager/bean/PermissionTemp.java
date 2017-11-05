package cn.ifreedomer.com.softmanager.bean;

/**
 * @author:eavawu
 * @since: 05/11/2017.
 * TODO:
 */

public class PermissionTemp {
    private String permission;
    private boolean isGranted;

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isGranted() {
        return isGranted;
    }

    public void setGranted(boolean granted) {
        isGranted = granted;
    }


    @Override
    public String toString() {
        return "PermissionTemp{" +
                "permission='" + permission + '\'' +
                ", isGranted=" + isGranted +
                '}';
    }
}
