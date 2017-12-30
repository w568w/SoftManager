package cn.ifreedomer.com.softmanager.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:eavawu
 * @since: 01/12/2017.
 * TODO:
 */

public class ComponentEntity {
    private String id = System.currentTimeMillis() + "";
    private String name;
    private String exported;
    private String belongPkg;
    private String fullPathName;
    private boolean enable = true;

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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
       this.enable = enable;
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
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", exported='" + exported + '\'' +
                ", belongPkg='" + belongPkg + '\'' +
                ", fullPathName='" + fullPathName + '\'' +
                ", enable=" + enable +
                ", actionList=" + actionList +
                '}';
    }

    public void setFullPathName(String s) {
        this.fullPathName = s;
    }


}
