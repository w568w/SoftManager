package cn.ifreedomer.com.softmanager.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:eavawu
 * @since: 01/12/2017.
 * TODO:
 */

public class ComponentEntity {
    private String name;
    private String exported;
    private String belongPkg;
    private String fullPathName;

    public String getFullPathName() {
        return fullPathName;
    }

    public String getBelongPkg() {
        return belongPkg;
    }

    public void setBelongPkg(String belongPkg) {
        this.belongPkg = belongPkg;
    }

    private List<String> actionList = new ArrayList<>();
    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public List<String> getActionList() {
        return actionList;
    }

    public void setActionList(List<String> actionList) {
        this.actionList = actionList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExported() {
        return exported;
    }

    public void setExported(String exported) {
        this.exported = exported;
    }

    @Override
    public String toString() {
        return "ComponentEntity{" +
                ", name='" + name + '\'' +
                ", exported='" + exported + '\'' +
                '}';
    }

    public void setFullPathName(String s) {
        this.fullPathName = s;
    }
}
