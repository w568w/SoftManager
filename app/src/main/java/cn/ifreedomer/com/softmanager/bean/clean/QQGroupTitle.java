package cn.ifreedomer.com.softmanager.bean.clean;

/**
 * @author:eavawu
 * @since: 11/11/2017.
 * TODO: qq的group title
 */

public class QQGroupTitle {
    public static final int TYPE_USER_HEAD = 1;
    public static final int TYPE_THUMBNAILS = 3;
    public static final int TYPE_CHAT_PHOTO = 4;
    private String title;
    private String totalSize;
    private boolean isChecked;
    private int type;
    public static final int TYPE_RECEIVE_FILES = 0;
    public static final int TYPE_SHORT_VIDEOS = 2;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public QQGroupTitle(String title, int type) {
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
