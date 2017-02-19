package cn.ifreedomer.com.softmanager;

/**
 * @author:eavawu
 * @date: 19/02/2017.
 * @todo:
 */

public interface LoadStateCallback {
    void loadBegin();

    void loadProgress(int current, int max);

    void loadFinish();

}
