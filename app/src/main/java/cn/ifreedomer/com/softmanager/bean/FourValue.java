package cn.ifreedomer.com.softmanager.bean;

/**
 * @author:eavawu
 * @since: 25/10/2017.
 * TODO:
 */

public class FourValue {
    private String title;
    private String subTitle;
    private String value;
    private String subValue;

    public FourValue(String title, String value, String subTitle, String subValue) {
        this.title = title;
        this.subTitle = subTitle;
        this.value = value;
        this.subValue = subValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSubValue() {
        return subValue;
    }

    public void setSubValue(String subValue) {
        this.subValue = subValue;
    }
}
