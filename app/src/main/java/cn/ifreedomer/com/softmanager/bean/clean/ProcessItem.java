package cn.ifreedomer.com.softmanager.bean.clean;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

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
    private List<Integer> pids = new ArrayList<>();

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

    public List<Integer> getPids() {
        return pids;
    }

    public void setPids(List<Integer> pids) {
        this.pids = pids;
    }
}
