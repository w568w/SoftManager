package cn.ifreedomer.com.softmanager.bean;

/**
 * Created by eavawu on 4/28/16.
 */
public class RespResult<T> {
    public static final int FAILED = 1;
    public static final int SUCCESS = 0;
    private int resultCode;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }


    @Override
    public String toString() {
        return "HttpResult{" +
                "resultCode=" + resultCode +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
