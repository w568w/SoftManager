package cn.ifreedomer.com.softmanager.bean.clean;

/**
 * @author:eavawu
 * @since: 11/11/2017.
 * TODO: 清理界面的group title
 */

public class GarbageGroupTitle {
    private String title;
    private String totalSize;
    private boolean isChecked;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public GarbageGroupTitle(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "GarbageGroupTitle{" +
                "title='" + title + '\'' +
                ", totalSize='" + totalSize + '\'' +
                ", isEnable=" + isChecked +
                ", type=" + type +
                '}';
    }
}
