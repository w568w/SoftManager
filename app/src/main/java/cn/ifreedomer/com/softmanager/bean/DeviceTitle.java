package cn.ifreedomer.com.softmanager.bean;

/**
 * @author:eavawu
 * @since: 25/10/2017.
 * TODO:
 */

public class DeviceTitle {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static DeviceTitle create(String titleStr) {
        DeviceTitle title = new DeviceTitle();
        title.title = titleStr;
        return title;
    }
}
