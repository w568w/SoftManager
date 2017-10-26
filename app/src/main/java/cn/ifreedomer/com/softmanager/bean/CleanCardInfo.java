package cn.ifreedomer.com.softmanager.bean;

/**
 * @author:eavawu
 * @since: 26/10/2017.
 * TODO: clean界面卡片的icon和name
 */

public class CleanCardInfo {
    private int icon;
    private String title;

    public CleanCardInfo(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
