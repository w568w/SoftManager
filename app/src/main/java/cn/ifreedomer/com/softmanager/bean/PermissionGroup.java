package cn.ifreedomer.com.softmanager.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:eavawu
 * @since: 04/11/2017.
 * TODO:
 */

public class PermissionGroup {
    private String name;
    private List<PermissionDetail> permissionDetailList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PermissionDetail> getPermissionDetailList() {
        return permissionDetailList;
    }

    public void setPermissionDetailList(List<PermissionDetail> permissionDetailList) {
        this.permissionDetailList = permissionDetailList;
    }

    @Override
    public String toString() {
        return "PermissionGroup{" +
                "name='" + name + '\'' +
                ", permissionDetailList=" + permissionDetailList +
                '}';
    }
}
