package cn.ifreedomer.com.softmanager.model;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import cn.ifreedomer.com.softmanager.bean.ComponentEntity;
import cn.ifreedomer.com.softmanager.bean.PermissionDetail;

public class AppInfo {
    private Drawable appIcon;
    private String appName;
    private String packname;
    private String version;
    private float pkgSize;
    private int uid;
    private String codePath;
    private float cacheSize;
    private boolean isEnable = true;
    private List<ComponentEntity> activityList = null;
    private List<ComponentEntity> receiverList = null;
    private List<ComponentEntity> serviceList = null;
    private List<ComponentEntity> contentProviderList = null;
    private List<ComponentEntity> wakeupList = new ArrayList<>();

    public List<ComponentEntity> getWakeupList() {
        return wakeupList;
    }

    public void setWakeupList(List<ComponentEntity> wakeupList) {
        this.wakeupList = wakeupList;
    }

    public List<ComponentEntity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<ComponentEntity> activityList) {
        this.activityList = activityList;
    }

    public List<ComponentEntity> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<ComponentEntity> receiverList) {
        this.receiverList = receiverList;
    }

    public List<ComponentEntity> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<ComponentEntity> serviceList) {
        this.serviceList = serviceList;
    }

    public List<ComponentEntity> getContentProviderList() {
        return contentProviderList;
    }

    public void setContentProviderList(List<ComponentEntity> contentProviderList) {
        this.contentProviderList = contentProviderList;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    private List<PermissionDetail> permissionDetailList = new ArrayList<>();

    public List<PermissionDetail> getPermissionDetailList() {
        return permissionDetailList;
    }

    public void setPermissionDetailList(List<PermissionDetail> permissionDetailList) {
        this.permissionDetailList = permissionDetailList;
    }

    public float getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(float cacheSize) {
        this.cacheSize = cacheSize;
    }

    public String getCodePath() {
        return codePath;
    }

    public void setCodePath(String codePath) {
        this.codePath = codePath;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public float getPkgSize() {
        return pkgSize;
    }

    public void setPkgSize(float pkgSize) {
        this.pkgSize = pkgSize;
    }

    /**
     * 应用程序可以被安装到不同的位置 , 手机内存 外部存储sd卡
     */


    private boolean inRom;

    private boolean userApp;

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

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isInRom() {
        return inRom;
    }

    public void setInRom(boolean inRom) {
        this.inRom = inRom;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appIcon=" + appIcon +
                ", appName='" + appName + '\'' +
                ", packname='" + packname + '\'' +
                ", version='" + version + '\'' +
                ", pkgSize=" + pkgSize +
                ", uid=" + uid +
                ", codePath='" + codePath + '\'' +
                ", cacheSize=" + cacheSize +
                ", permissionDetailList=" + permissionDetailList +
                ", inRom=" + inRom +
                ", userApp=" + userApp +
                '}';
    }
}
