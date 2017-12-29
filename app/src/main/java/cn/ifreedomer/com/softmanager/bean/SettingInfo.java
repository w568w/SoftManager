package cn.ifreedomer.com.softmanager.bean;

/**
 * @author eavawu
 * @since 27/12/2017.
 */

public class SettingInfo {
    private String title;
    private Class activity;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Class getActivity() {
        return activity;
    }

    public void setActivity(Class activity) {
        this.activity = activity;
    }

    public SettingInfo(String title, Class activity) {
        this.title = title;
        this.activity = activity;
    }
}
