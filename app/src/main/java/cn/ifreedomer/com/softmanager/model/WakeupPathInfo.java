package cn.ifreedomer.com.softmanager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:eavawu
 * @since: 30/11/2017.
 * TODO:
 */

public class WakeupPathInfo {
    private List<AppInfo> wakeupPath = new ArrayList<>(5);
    private String wakeUpName = "";

    public List<AppInfo> getWakeupPath() {
        return wakeupPath;
    }

    public void setWakeupPath(List<AppInfo> wakeupPath) {
        this.wakeupPath = wakeupPath;
    }

    public String getWakeUpName() {
        return wakeUpName;
    }

    public void setWakeUpName(String wakeUpName) {
        this.wakeUpName = wakeUpName;
    }


    public WakeupPathInfo(List<AppInfo> wakeupPath, String wakeUpName) {
        this.wakeupPath = wakeupPath;
        this.wakeUpName = wakeUpName;
    }

    public WakeupPathInfo(List<AppInfo> wakeupPath) {
        this.wakeupPath = wakeupPath;
    }

    public WakeupPathInfo() {
    }

    @Override
    public String toString() {
        return "WakeupPathInfo{" +
                "wakeupPath=" + wakeupPath +
                ", wakeUpName='" + wakeUpName + '\'' +
                '}';
    }
}
