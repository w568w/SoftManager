package cn.ifreedomer.com.softmanager.bean;

/**
 * @author:eavawu
 * @since: 31/10/2017.
 * TODO:
 */

public class GarbageInfo<T> {
    public static final int TYPE_APP_CACHE = 0;
    public static final int TYPE_SYSTEM_GARBAGE = 1;
    public static final int TYPE_AD_GARBAGE = 2;
    public static final int TYPE_EMPTY_FILE = 3;

    private boolean isChecked;
    private int type;
    private T data;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
