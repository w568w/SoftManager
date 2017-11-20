package cn.ifreedomer.com.softmanager.bean.clean;

import android.graphics.drawable.Drawable;

/**
 * @author:eavawu
 * @since: 19/11/2017.
 * TODO:
 */

public class ProcessItem {

    private String pkgName;
    private int memorySize;
    private String pid;
    private Drawable icon;
    private CharSequence label;
    private boolean checked;
    private CharSequence des;

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }



    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public CharSequence getLabel() {
        return label;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setIcon(Drawable drawable) {
        this.icon = drawable;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setDes(CharSequence des) {
        this.des = des;
    }

    public CharSequence getDes() {
        return des;
    }
}
