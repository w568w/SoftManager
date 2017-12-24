package cn.ifreedomer.com.softmanager.listener;

/**
 * @author eavawu
 * @since 24/12/2017.
 */

public interface GenericListener {
    void onSuccess();

    void onFailed(int errorCode, String errorMsg);
}
