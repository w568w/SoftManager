package cn.ifreedomer.com.softmanager.bean;

/**
 * @author:eavawu
 * @since: 25/10/2017.
 * TODO:
 */

public class TwoValue {
    private String title;
    private String value;


    public TwoValue(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
