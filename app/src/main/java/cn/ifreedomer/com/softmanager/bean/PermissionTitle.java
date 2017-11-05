package cn.ifreedomer.com.softmanager.bean;

/**
 * @author:eavawu
 * @since: 05/11/2017.
 * TODO:
 */

public class PermissionTitle {

    private String title;
    private boolean showPadding;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isShowPadding() {
        return showPadding;
    }

    public void setShowPadding(boolean showPadding) {
        this.showPadding = showPadding;
    }


    public PermissionTitle(String title) {
        this.title = title;
    }
}
