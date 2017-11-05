package cn.ifreedomer.com.softmanager.bean;

/**
 * @author:eavawu
 * @since: 05/11/2017.
 * TODO:
 */

public class PermissionWrap<T> {
    public static final int PERMISSION_TYPE = 1;
    public static final int TITLE_TYPE = 2;
    private T data;
    private int type;


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
