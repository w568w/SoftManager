package cn.ifreedomer.com.softmanager.bean;

/**
 * @author:eavawu
 * @since: 25/10/2017.
 * TODO:
 */

public class Title {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static Title create(String titleStr) {
        Title title = new Title();
        title.title = titleStr;
        return title;
    }
}
