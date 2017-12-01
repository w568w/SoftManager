package cn.ifreedomer.com.softmanager.bean;

/**
 * @author:eavawu
 * @since: 01/12/2017.
 * TODO:
 */

public class ComponentEntity {
    private String name;
    private String exported;

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
}
